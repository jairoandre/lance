package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.*;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.util.VahUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jairoportela on 20/07/2016.
 */
@Stateless
public class CobrancaService extends DataAccessService<Cobranca> {

  private
  @Inject
  LancamentoService lancamentoService;

  private
  @Inject
  ContaReceberService contaReceberService;



  public CobrancaService() {
    super(Cobranca.class);
  }

  /**
   * Busca as cobranças gravadas na base de dados pela vigência e/ou vencimento.
   *
   * @param vigencia
   * @param vencimento
   * @return
   */
  public List<Cobranca> buscarCobrancas(Date[] vigencia, Integer vencimento) {
    Criteria criteria = createCriteria();

    criteria.add(Restrictions.between("vigencia", vigencia[0], vigencia[1]));
    if (vencimento != null) {
      criteria.add(Restrictions.eq("vencimento", VahUtils.calcNextMonthDate(vigencia[0], vencimento)));
    }

    criteria.setFetchMode("contas", FetchMode.SELECT);
    criteria.setFetchMode("descritivo", FetchMode.SELECT);

    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    return criteria.list();
  }

  /**
   * Cria ou atualiza na base de dados as cobranças contidas em um array.
   *
   * @param cobrancas
   * @return
   */
  public List<Cobranca> gravarCobrancas(Cobranca[] cobrancas) throws LanceBusinessException {
    List<Cobranca> attachedCobrancas = new ArrayList<>();
    if (cobrancas != null && cobrancas.length > 0) {

      StringBuilder builder = new StringBuilder();
      for (Cobranca cobranca : cobrancas) {
        if (cobranca.getId() == null && checkCobranca(cobranca)) {
          if (cobranca.getSetor() != null) {
            builder.append(String.format("Setor: %s", cobranca.getSetor().getLabelForSelectItem()));
          }
          builder.append(String.format("Cliente: %s", cobranca.getCliente().getLabelForSelectItem()));
          builder.append("\r\n");
        }
      }

      if (builder.length() > 0) {
        throw new LanceBusinessException("Já existe cobranças para os seguintes items: \r\n" + builder.toString());
      }

      for (Cobranca cobranca : cobrancas) {
        if (cobranca.getId() == null) {
          attachedCobrancas.add(create(cobranca));
        } else {
          attachedCobrancas.add(update(cobranca));
        }
      }
    }
    return attachedCobrancas;
  }

  /**
   * Realiza uma varredura nos lançamentos verificando
   *
   * @param vigencia
   * @param vencimento
   * @return
   */
  public List<Cobranca> gerarCobrancas(Date[] vigencia, Integer vencimento, Boolean previa) {

    List<Lancamento> lancamentos = lancamentoService.recuperarLancamentosValidados(vigencia, vencimento, previa);

    Calendar cld = Calendar.getInstance();
    cld.setTime(vigencia[0]);
    cld.add(Calendar.MONTH, 1);

    Map<String, Cobranca> cobrancasMap = new HashMap<>();

    for (Lancamento lancamento : lancamentos) {

      Map<String, ContaReceber> contas = new HashMap<>();

      for (ContaReceber contaReceber : lancamento.getContasReceber()) {
        Fornecedor cliente = contaReceber.getCliente();
        Long cdSetor = contaReceber.getItensRateio().iterator().next().getCdSetor();
        String chave = String.format("%d - %d", cliente.getId(), cdSetor);
        contas.put(chave, contaReceber);
      }

      Servico servico = lancamento.getServico();

      cld.set(Calendar.DAY_OF_MONTH, servico.getDiaVencimento());

      Date dataVencimento = cld.getTime();

      lancamento.getContasReceber();

      Boolean cobraPorSetor = lancamento.getServico().getAgrupavel();

      for (LancamentoValor valor : lancamento.getValues()) {
        if (BigDecimal.ZERO.equals(valor.getValue())) {
          continue;
        }
        ContratoSetor contratoSetor = valor.getContratoSetor();
        Fornecedor cliente = contratoSetor.getContrato().getContratante();
        Setor setor = contratoSetor.getSetor();
        String chaveConta = String.format("%d - %d", cliente.getId(), setor.getId());
        String chave = chaveConta;
        if (!cobraPorSetor) {
          chave = cliente.getId().toString();
        }
        Cobranca cobranca = cobrancasMap.get(chave);

        if (cobranca == null) {
          cobranca = new Cobranca();
          cobranca.setVigencia(vigencia[0]);
          cobranca.setVencimento(dataVencimento);
          if (cobraPorSetor) {
            cobranca.setSetor(setor);
          }
          cobranca.setCliente(cliente);
          /*
          Cobranca attached = recuperarCobranca(cobranca);
          if (attached != null) {
            cobranca = attached;
            cobranca.setValor(BigDecimal.ZERO);
          }*/
          cobrancasMap.put(chave, cobranca);
        } else if (cobranca.getId() != null) {
          continue;
        }

        if (contas.get(chaveConta) != null) {
          cobranca.getContas().add(contas.get(chaveConta));
        }

        ItemCobranca item = new ItemCobranca();
        item.setCobranca(cobranca);
        item.setServico(lancamento.getServico());
        item.setTotal(lancamento.getTotalValue());
        item.setValor(valor.getValue());
        cobranca.getDescritivo().add(item);

        cobranca.setValor(cobranca.getValor().add(valor.getValue()));

      }
    }

    List<Cobranca> cobrancas = new ArrayList<>();

    // Remover itens já persistidos
    for (Cobranca cobranca : cobrancasMap.values()) {
      if (!BigDecimal.ZERO.equals(cobranca.getValor())) {
        cobrancas.add(cobranca);
      }
    }

    Collections.sort(cobrancas, new Comparator<Cobranca>() {
      @Override
      public int compare(Cobranca o1, Cobranca o2) {
        return o1.getCliente().getTitle().compareTo(o2.getCliente().getTitle());
      }
    });

    return cobrancas;
  }

  /**
   * Verifica se já existe cobrança com o mesmo vencimento para o cliente/setor informado na nova cobrança.
   *
   * @param cobranca
   * @return {@true} se houver {@false} se não.
   */
  public Boolean checkCobranca(Cobranca cobranca) {
    return recuperarCobranca(cobranca) != null;

  }

  public Cobranca recuperarCobranca(Cobranca cobranca) {
    Criteria criteria = createCriteria();
    if (cobranca.getSetor() != null) {
      criteria.add(Restrictions.eq("setor", cobranca.getSetor()));
    }
    criteria.add(Restrictions.eq("cliente", cobranca.getCliente()));
    criteria.add(Restrictions.eq("vencimento", cobranca.getVencimento()));
    if (criteria.list().size() == 0) {
      return null;
    } else {
      return (Cobranca) criteria.list().iterator().next();
    }
  }

  public void salvarNotaFiscal(List<ContaReceber> contas) throws LanceBusinessException {
    for (ContaReceber contaReceber : contas) {
      if (contaReceber.getNumeroDocumento() == null) {
        throw new LanceBusinessException("Número de documento obrigatório");
      }
    }
    for (ContaReceber contaReceber : contas) {
      contaReceber.setDescricao(String.format("%s - %s", contaReceber.getNumeroDocumento(), contaReceber.getCliente().getTitle()));
      contaReceberService.update(contaReceber);
    }
  }

  public void salvarNotaFiscal(Lancamento lancamento) throws LanceBusinessException {
    salvarNotaFiscal(lancamento.getContasReceber());
  }

  public void salvarNotaFiscal(Cobranca cobranca) throws LanceBusinessException {
    salvarNotaFiscal(new ArrayList<ContaReceber>(cobranca.getContas()));
  }

}
