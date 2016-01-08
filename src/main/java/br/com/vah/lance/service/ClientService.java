package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvClient;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ClientService extends DataAccessService<MvClient> {
	
	public ClientService(){
		super(MvClient.class);
	}

}
