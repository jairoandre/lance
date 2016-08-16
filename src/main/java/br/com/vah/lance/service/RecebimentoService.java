package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceberItem;
import br.com.vah.lance.entity.dbamv.Recebimento;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Jairoportela on 08/08/2016.
 */
@Stateless
public class RecebimentoService extends DataAccessService<Recebimento> {

  public RecebimentoService() {
    super(Recebimento.class);
  }

  public List<Recebimento> recebimentosByItem(ContaReceberItem item) {
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("item", item));
    return criteria.list();
  }

}
