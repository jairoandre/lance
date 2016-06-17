package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.dbamv.PlanoConta;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class PlanoContaService extends DataAccessService<PlanoConta> {

	public PlanoContaService() {
		super(PlanoConta.class);
	}

}
