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
		initLazyModel(service);
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
	public String path() {
		return "resultItem";
	}

	@Override
	public String getEntityName() {
		return "conta de custo";
	}

	@Override
	public String search() {
		getLazyModel().getSearchParams().getParams().put("id", null);
		try {
			Long convertedValue = Long.valueOf(getSearchTerm());
			getLazyModel().getSearchParams().getParams().put("id", convertedValue);
		} catch (Exception e) {
			/**
			 * Cannot convert to integer
			 */
		}
		return super.search();
	}
}
