package br.com.vah.lance.dto;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.Cobranca;
import br.com.vah.lance.entity.usrdbvah.ItemCobranca;
import br.com.vah.lance.entity.usrdbvah.Servico;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jairoportela on 28/07/2016.
 */
public class Descritivo {

  public static final String RATEADO = "0";
  public static final String INDIVIDUAL = "1";
  public static final String SETOR = "2";

  // 9(01) - 0: rateado; 1: individual; 2: Setor
  private String rateado;
  // X(08)
  private String nossoNumero;
  // X(100)
  private String descricao;
  // 9(11)V9(02)
  private String valorTotal;
  // 9(11)V9(02)
  private String valorIndividual;
  // REGISTRO 2
  // X(100)
  private String nomeSetor;
  // X(6)
  private String numero;
  // X(20)
  private String complemento;
  private Descritivo descritivo;


  public Descritivo(Cobranca cobranca) {
    rateado = "2";
    nossoNumero = ArquivoUtils.rightSpace(cobranca.getId().toString(), 8);
    nomeSetor = ArquivoUtils.rightSpace(cobranca.getSetor() == null ? "" : cobranca.getSetor().getTitle(), 100);
    numero = ArquivoUtils.rightSpace(cobranca.getCliente().getNumero(), 6);
    complemento = ArquivoUtils.rightSpace(cobranca.getCliente().getComplemento(), 20);
  }

  public Descritivo(ItemCobranca item) {

    Servico servico = item.getServico();
    Long contaCusto = servico.getContaCusto().getId();

    boolean isIndividual = !servico.getAgrupavel()
        || TipoServicoEnum.ENERGIA_PRIVADA.equals(servico.getType())
        || TipoServicoEnum.COLETA_INFECTANTE.equals(servico.getType())
        || TipoServicoEnum.TAXA_REFRIGERACAO.equals(servico.getType())
        || contaCusto.equals(269l)
        || contaCusto.equals(272l);

    rateado = isIndividual ? INDIVIDUAL : RATEADO;
    nossoNumero = ArquivoUtils.rightSpace(item.getCobranca().getId().toString(), 8);
    String servicoTitle = item.getServico().getTitle();
    descricao = ArquivoUtils.rightSpace(servicoTitle, 100);
    if (servicoTitle.equals("SERVIÇOS DE INTERNET E COMUNICAÇÃO")) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
      Calendar cld = Calendar.getInstance();
      cld.setTime(item.getCobranca().getVencimento());
      cld.add(Calendar.MONTH, -1);
      String ref = sdf.format(cld.getTime());
      descricao = ArquivoUtils.rightSpace("SERV. DE INTERNET E COMUN. (REF. " + ref + ")", 100);
    }
    valorTotal = ArquivoUtils.formatNumber(item.getTotal(), 13);
    valorIndividual = ArquivoUtils.formatNumber(item.getValor(), 13);
    descritivo = new Descritivo(item.getCobranca());
  }

  public String print() {
    if (SETOR.equals(rateado)) {
      return rateado + nossoNumero + nomeSetor + numero + complemento + "\r\n";
    } else {
      return descritivo.print() + rateado + nossoNumero + descricao + valorTotal + valorIndividual + "\r\n";
    }
  }
}
