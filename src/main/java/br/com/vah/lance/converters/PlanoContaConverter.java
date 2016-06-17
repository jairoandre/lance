package br.com.vah.lance.converters;

import br.com.vah.lance.entity.dbamv.PlanoConta;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.PlanoContaService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class PlanoContaConverter extends GenericConverter<PlanoConta> {

  private
  @Inject
  PlanoContaService service;

  @Override
  public DataAccessService<PlanoConta> getService() {
    return service;
  }
}