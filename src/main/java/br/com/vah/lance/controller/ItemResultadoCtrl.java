package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.dbamv.ItemResultado;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ItemResultadoService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ItemResultadoCtrl extends AbstractController<ItemResultado> {

	private @Inject transient Logger logger;

	private @Inject
	ItemResultadoService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service);
	}

	@Override
	public DataAccessService<ItemResultado> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ItemResultado createNewItem() {
		return new ItemResultado();
	}

	@Override
	public String path() {
		return "itemResultado";
	}

	@Override
	public String getEntityName() {
		return "conta de custo";
	}

}
