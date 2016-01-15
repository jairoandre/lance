package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvAccountChart;
import br.com.vah.lance.service.AccountChartService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class AccountChartController extends AbstractController<MvAccountChart> {

	private @Inject transient Logger logger;

	private @Inject AccountChartService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<MvAccountChart>(service));
	}

	@Override
	public DataAccessService<MvAccountChart> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public MvAccountChart createNewItem() {
		return new MvAccountChart();
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
		return "conta contábil";
	}

	@Override
	public String search() {
		getLazyModel().getSearchParams().getParams().put("accountingCode", getSearchTerm());
		getLazyModel().getSearchParams().getParams().put("resultItemCode", null);
		try {
			Integer convertedValue = Integer.valueOf(getSearchTerm());
			getLazyModel().getSearchParams().getParams().put("resultItemCode", convertedValue);
		} catch (Exception e) {
			/**
			 * Cannot convert to integer
			 */
		}
		return super.search();
	}

}
