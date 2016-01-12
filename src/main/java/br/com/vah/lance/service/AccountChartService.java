package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvAccountChart;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class AccountChartService extends DataAccessService<MvAccountChart> {

	public AccountChartService() {
		super(MvAccountChart.class);
	}

}
