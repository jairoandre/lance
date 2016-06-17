package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.dbamv.Setor;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class SetorService extends DataAccessService<Setor> {
	
	public SetorService(){
		super(Setor.class);
	}

}
