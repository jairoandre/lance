package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.ContaReceberItem;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 09/08/2016.
 */
@Stateless
public class ContaReceberItemService extends DataAccessService<ContaReceberItem> {

  public ContaReceberItemService() {
    super(ContaReceberItem.class);
  }

}
