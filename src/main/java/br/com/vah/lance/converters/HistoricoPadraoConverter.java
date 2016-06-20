package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.HistoricoPadrao;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.HistoricoPadraoService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class HistoricoPadraoConverter extends GenericConverter<HistoricoPadrao> {

  private
  @Inject
  HistoricoPadraoService service;

  @Override
  public DataAccessService<HistoricoPadrao> getService() {
    return service;
  }
}