package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvPlanoConta;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class AccountChartService extends DataAccessService<MvPlanoConta> {

	public AccountChartService() {
		super(MvPlanoConta.class);
	}

}
