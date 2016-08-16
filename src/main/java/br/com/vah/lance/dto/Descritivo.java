package br.com.vah.lance.dto;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.Cobranca;
import br.com.vah.lance.entity.usrdbvah.ItemCobranca;
import br.com.vah.lance.entity.usrdbvah.Servico;

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
  // X(45)
  private String descricao;
  // 9(11)V9(02)
  private String valorTotal;
  // 9(11)V9(02)
  private String valorIndividual;
  // X(31)
  private String nomeSetor;

  public Descritivo(Cobranca cobranca) {
    rateado = "2";
    nossoNumero = ArquivoUtils.rightSpace(cobranca.getId().toString(), 8);
    nomeSetor = ArquivoUtils.rightSpace(cobranca.getSetor().getTitle(), 71);
  }

  public Descritivo(ItemCobranca item) {

    Servico servico = item.getServico();
    Long contaCusto = servico.getContaCusto().getId();

    boolean isIndividual = TipoServicoEnum.ENERGIA_PRIVADA.equals(servico.getType())
        || TipoServicoEnum.COLETA_INFECTANTE.equals(servico.getType())
        || TipoServicoEnum.TAXA_REFRIGERACAO.equals(servico.getType())
        || contaCusto.equals(269l)
        || contaCusto.equals(272l);

    rateado = isIndividual ? INDIVIDUAL : RATEADO;
    nossoNumero = ArquivoUtils.rightSpace(item.getCobranca().getId().toString(), 8);
    descricao = ArquivoUtils.rightSpace(item.getServico().getTitle(), 45);
    valorTotal = ArquivoUtils.formatNumber(item.getTotal(), 13);
    valorIndividual = ArquivoUtils.formatNumber(item.getValor(), 13);

  }

  public String print() {
    if (SETOR.equals(rateado)) {
      return rateado + nossoNumero + nomeSetor + "\r\n";
    } else {
      return rateado + nossoNumero + descricao + valorTotal + valorIndividual + "\r\n";
    }
  }

  public String getRateado() {
    return rateado;
  }

  public void setRateado(String rateado) {
    this.rateado = rateado;
  }

  public String getNossoNumero() {
    return nossoNumero;
  }

  public void setNossoNumero(String nossoNumero) {
    this.nossoNumero = nossoNumero;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(String valorTotal) {
    this.valorTotal = valorTotal;
  }

  public String getValorIndividual() {
    return valorIndividual;
  }

  public void setValorIndividual(String valorIndividual) {
    this.valorIndividual = valorIndividual;
  }
}
