package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.dbamv.HistoricoPadrao;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class HistoricoPadraoService extends DataAccessService<HistoricoPadrao> {

	public HistoricoPadraoService() {
		super(HistoricoPadrao.class);
	}

}
