package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.ServiceValue;

@Stateless
public class ServiceTypeService extends DataAccessService<ServiceValue> {

	public ServiceTypeService() {
		super(ServiceValue.class);
	}

}
