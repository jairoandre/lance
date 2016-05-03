package br.com.vah.lance.service;

import br.com.vah.lance.entity.mv.MvContaReceber;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ContaReceberService extends DataAccessService<MvContaReceber> {

	public ContaReceberService() {
		super(MvContaReceber.class);
	}


}
