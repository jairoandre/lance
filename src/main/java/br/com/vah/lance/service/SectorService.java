package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvSector;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class SectorService extends DataAccessService<MvSector> {
	
	public SectorService(){
		super(MvSector.class);
	}

}
