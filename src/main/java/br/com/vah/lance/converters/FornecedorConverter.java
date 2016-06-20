package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.FornecedorService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jairoportela on 20/06/2016.
 */
@Named
public class FornecedorConverter extends GenericConverter<Fornecedor> {

  private @Inject
  FornecedorService service;

  @Override
  public DataAccessService<Fornecedor> getService() {
    return service;
  }
}
