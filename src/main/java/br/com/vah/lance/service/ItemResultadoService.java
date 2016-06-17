package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.dbamv.ItemResultado;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ItemResultadoService extends DataAccessService<ItemResultado> {

	public ItemResultadoService() {
		super(ItemResultado.class);
	}

}
