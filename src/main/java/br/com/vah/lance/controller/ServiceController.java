package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Service;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ServiceController extends AbstractController<Service> {

	private @Inject transient Logger logger;

	private @Inject ServiceService das;	

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<Service>(das, new Service()));
	}

	@Override
	public DataAccessService<Service> getService() {
		return das;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public Service createNewItem() {
		return new Service();
	}

	@Override
	public String editPage() {
		return "/pages/service/edit.xhtml";
	}
	
	@Override
	public String listPage() {
		return "/pages/service/list.xhtml";
	}
	
	@Override
	public String entityTitle() {
		return "serviço";
	}

}
