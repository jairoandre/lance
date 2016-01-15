package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.ServiceContract;
import br.com.vah.lance.service.ContractService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContractController extends AbstractController<Contract> {

	private @Inject transient Logger logger;

	private @Inject ContractService service;

	private ServiceContract beanService;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<Contract>(service));
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public DataAccessService<Contract> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public Contract createNewItem() {
		return new Contract();
	}

	@Override
	public String editPage() {
		return "/pages/contract/edit.xhtml";
	}

	@Override
	public String listPage() {
		return "/pages/contract/list.xhtml";
	}

	@Override
	public String getEntityName() {
		return "contrato";
	}

	public void toggleService() {
		if (getItem().getServices().contains(beanService)) {
			getItem().getServices().remove(beanService);
		} else {
			getItem().getServices().add(beanService);
		}
	}

	public ServiceContract getBeanService() {
		return beanService;
	}

	public void setBeanService(ServiceContract beanService) {
		this.beanService = beanService;
	}

}
