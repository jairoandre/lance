package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.*;

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

  public List<Cobranca> recuperarCobrancas(Date[] vigencia, Integer vencimento) {

    List<Lancamento> lancamentos = lancamentoService.recuperarLancamentosValidados(vigencia, vencimento);

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
          cobrancasMap.put(chave, cobranca);
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
}
