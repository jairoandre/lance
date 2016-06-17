package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.dbamv.TipoDocumento;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class TipoDocumentoService extends DataAccessService<TipoDocumento> {

	public TipoDocumentoService() {
		super(TipoDocumento.class);
	}

}
