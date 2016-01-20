package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.ServiceValue;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceTypeService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ServiceTypeController extends AbstractController<ServiceValue> {

	private @Inject transient Logger logger;

	private @Inject ServiceTypeService service;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service);
	}

	@Override
	public DataAccessService<ServiceValue> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ServiceValue createNewItem() {
		return new ServiceValue();
	}

	@Override
	public String editPage() {
		return "/pages/serviceType/edit.xhtml";
	}

	@Override
	public String listPage() {
		return "/pages/serviceType/list.xhtml";
	}

	@Override
	public String getEntityName() {
		return "tipo de serviço";
	}

}
