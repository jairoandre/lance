package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.dbamv.TipoDocumento;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.TipoDocumentoService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class TipoDocumentoCtrl extends AbstractController<TipoDocumento> {

	private @Inject transient Logger logger;

	private @Inject
	TipoDocumentoService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service);
	}

	@Override
	public DataAccessService<TipoDocumento> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public TipoDocumento createNewItem() {
		return new TipoDocumento();
	}

	@Override
	public String path() {
		return "tipoDocumento";
	}

	@Override
	public String getEntityName() {
		return "tipo de documento";
	}

}
