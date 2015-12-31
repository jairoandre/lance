package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.Contract;

@Stateless
public class ContractService extends DataAccessService<Contract> {

	public ContractService() {
		super(Contract.class);
	}

}
