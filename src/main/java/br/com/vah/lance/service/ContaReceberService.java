package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.ContaReceberItem;
import br.com.vah.lance.entity.dbamv.ContaReceberRateio;
import br.com.vah.lance.entity.dbamv.Recebimento;
import br.com.vah.lance.exception.LanceBusinessException;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jairoportela
 */
@Stateless
public class ContaReceberService extends DataAccessService<ContaReceber> {

  private
  @Inject
  RecebimentoService recebimentoService;

  public ContaReceberService() {
    super(ContaReceber.class);
  }

  public List<ContaReceber> createList(List<ContaReceber> list) {
    List<ContaReceber> persistedList = new ArrayList<>();
    for (ContaReceber conRec : list) {
      if (BigDecimal.ZERO.equals(conRec.getValorBruto())) {
        continue;
      }
      persistedList.add(create(conRec));
    }
    return persistedList;
  }

  public void registrarRecebimento(ContaReceber conta, Recebimento recebimento) throws LanceBusinessException {
    ContaReceber attachedConta = find(conta.getId());

    ContaReceberItem item = attachedConta.getItensConta().iterator().next();

    if (item != null) {
      for (Recebimento attReceb : item.getRecebimentos()) {
        if (attReceb.getTpRecebimento().equals("6")) {
          throw new LanceBusinessException("Baixa já realizada");
        }
      }
    }

    recebimento.setItem(item);

    // save recebimento;
    recebimentoService.create(recebimento);

  }

}
