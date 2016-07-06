package br.com.vah.lance.converters;

import br.com.vah.lance.entity.usrdbvah.Servico;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServicoService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ServicoConverter extends GenericConverter<Servico> {

  private
  @Inject
  ServicoService service;

  @Override
  public DataAccessService<Servico> getService() {
    return service;
  }
}