package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.ItemResultado;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ItemResultadoService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ItemResultadoConverter extends GenericConverter<ItemResultado> {

  private
  @Inject
  ItemResultadoService service;

  @Override
  public DataAccessService<ItemResultado> getService() {
    return service;
  }
}