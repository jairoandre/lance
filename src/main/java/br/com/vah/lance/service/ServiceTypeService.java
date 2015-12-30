package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.ServiceType;

@Stateless
public class ServiceTypeService extends DataAccessService<ServiceType> {

	public ServiceTypeService() {
		super(ServiceType.class);
	}

}
