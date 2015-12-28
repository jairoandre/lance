package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.Sector;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class SectorService extends DataAccessService<Sector> {
	
	public SectorService(){
		super(Sector.class);
	}

}
