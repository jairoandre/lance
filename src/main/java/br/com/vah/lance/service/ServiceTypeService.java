package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.usrdbvah.ServicoValor;

@Stateless
public class ServiceTypeService extends DataAccessService<ServicoValor> {

	public ServiceTypeService() {
		super(ServicoValor.class);
	}

}
