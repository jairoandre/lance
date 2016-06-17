package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.SetorService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SetorConverter extends GenericConverter<Setor> {

  private
  @Inject
  SetorService service;

  @Override
  public DataAccessService<Setor> getService() {
    return service;
  }
}