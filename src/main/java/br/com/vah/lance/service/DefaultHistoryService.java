package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvDefaultHistory;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class DefaultHistoryService extends DataAccessService<MvDefaultHistory> {

	public DefaultHistoryService() {
		super(MvDefaultHistory.class);
	}

}
