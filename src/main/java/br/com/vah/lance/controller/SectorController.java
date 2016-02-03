package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.SectorService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class SectorController extends AbstractController<MvSector> {

	private @Inject transient Logger logger;

	private @Inject SectorService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service);
	}

	@Override
	public DataAccessService<MvSector> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public MvSector createNewItem() {
		return new MvSector();
	}

	@Override
	public String path() {
		return "sector";
	}

	@Override
	public String getEntityName() {
		return "setor";
	}

}
