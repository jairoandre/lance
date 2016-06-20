package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.TipoDocumento;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.TipoDocumentoService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class TipoDocumentoConverter extends GenericConverter<TipoDocumento> {

  private
  @Inject
  TipoDocumentoService service;

  @Override
  public DataAccessService<TipoDocumento> getService() {
    return service;
  }
}