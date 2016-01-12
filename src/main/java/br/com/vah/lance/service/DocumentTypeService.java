package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.mv.MvDocumentType;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class DocumentTypeService extends DataAccessService<MvDocumentType> {

	public DocumentTypeService() {
		super(MvDocumentType.class);
	}

}
