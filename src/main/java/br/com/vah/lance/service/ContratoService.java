package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.usrdbvah.Contrato;

@Stateless
public class ContratoService extends DataAccessService<Contrato> {

	public ContratoService() {
		super(Contrato.class);
	}

}
