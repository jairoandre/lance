package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvResultItem;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ResultItemService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ResultItemController extends AbstractController<MvResultItem> {

	private @Inject transient Logger logger;

	private @Inject ResultItemService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<MvResultItem>(service));
	}

	@Override
	public DataAccessService<MvResultItem> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public MvResultItem createNewItem() {
		return new MvResultItem();
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
		return "conta de custo";
	}

}
