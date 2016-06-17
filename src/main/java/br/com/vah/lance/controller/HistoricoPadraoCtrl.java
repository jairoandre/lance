package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.dbamv.HistoricoPadrao;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.HistoricoPadraoService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class HistoricoPadraoCtrl extends AbstractController<HistoricoPadrao> {

	private @Inject transient Logger logger;

	private @Inject
	HistoricoPadraoService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service);
	}

	@Override
	public DataAccessService<HistoricoPadrao> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public HistoricoPadrao createNewItem() {
		return new HistoricoPadrao();
	}

	@Override
	public String path() {
		return "historicoPadrao";
	}

	@Override
	public String getEntityName() {
		return "histórico padrão";
	}

}
