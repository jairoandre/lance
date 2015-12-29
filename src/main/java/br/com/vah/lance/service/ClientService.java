package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.Client;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ClientService extends DataAccessService<Client> {
	
	public ClientService(){
		super(Client.class);
	}

}
