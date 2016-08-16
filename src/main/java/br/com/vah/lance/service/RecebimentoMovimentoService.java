package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceberItem;
import br.com.vah.lance.entity.dbamv.Recebimento;
import br.com.vah.lance.entity.dbamv.RecebimentoMovimentacao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Jairoportela on 08/08/2016.
 */
@Stateless
public class RecebimentoMovimentoService extends DataAccessService<RecebimentoMovimentacao> {

  public RecebimentoMovimentoService() {
    super(RecebimentoMovimentacao.class);
  }

  public List<RecebimentoMovimentacao> movimentacaoByRecebimento(Recebimento recebimento) {
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("recebimento", recebimento));
    return criteria.list();
  }

}
