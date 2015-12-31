package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.Service;

@Stateless
public class ServiceService extends DataAccessService<Service> {

	public ServiceService() {
		super(Service.class);
	}

}
