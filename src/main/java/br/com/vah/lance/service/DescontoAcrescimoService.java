package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.DescontoAcrescimo;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 09/08/2016.
 */
@Stateless
public class DescontoAcrescimoService extends DataAccessService<DescontoAcrescimo> {

  public DescontoAcrescimoService() {
    super(DescontoAcrescimo.class);
  }
}
