package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.DescontoAcrescimo;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.DescontoAcrescimoService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jairoportela on 09/08/2016.
 */
@Named
public class DescontoAcrescimoConverter extends GenericConverter<DescontoAcrescimo> {

  private @Inject
  DescontoAcrescimoService service;

  @Override
  public DataAccessService<DescontoAcrescimo> getService() {
    return service;
  }
}
