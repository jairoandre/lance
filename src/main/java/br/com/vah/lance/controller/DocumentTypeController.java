package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvDocumentType;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.DocumentTypeService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class DocumentTypeController extends AbstractController<MvDocumentType> {

	private @Inject transient Logger logger;

	private @Inject DocumentTypeService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<MvDocumentType>(service));
	}

	@Override
	public DataAccessService<MvDocumentType> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public MvDocumentType createNewItem() {
		return new MvDocumentType();
	}

	@Override
	public String editPage() {
		return null;
	}

	@Override
	public String listPage() {
		return null;
	}

	@Override
	public String getEntityName() {
		return "tipo de documento";
	}

}
