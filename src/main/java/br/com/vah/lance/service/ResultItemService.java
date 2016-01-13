package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvResultItem;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ResultItemService extends DataAccessService<MvResultItem> {

	public ResultItemService() {
		super(MvResultItem.class);
	}

}
