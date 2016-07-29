package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.ContaReceberItem;
import br.com.vah.lance.entity.dbamv.ContaReceberRateio;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jairoportela
 */
@Stateless
public class ContaReceberService extends DataAccessService<ContaReceber> {

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
}
