package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceValue;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ServiceController extends AbstractController<Service> {

	private @Inject transient Logger logger;

	private @Inject ServiceService service;

	private ServiceValue serviceValue;

	@PostConstruct
	public void init() {
		serviceValue = new ServiceValue();
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<Service>(service));
	}

	@Override
	public DataAccessService<Service> getService() {
		return service;
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

	public void addValue() {
		getItem().getValues().add(serviceValue);
		serviceValue = new ServiceValue();
	}

	@Override
	public String listPage() {
		return "/pages/service/list.xhtml";
	}

	public String currency(Service item) {
		return "/pages/service/currency.xhtml" + "?faces-redirect=true&id=" + item.getId() + "&editing=true";
	}

	@Override
	public String getEntityName() {
		return "serviço";
	}

	public void setDefaultHistory() {
		System.out.println(getItem().getDefaultHistory());
	}

	/**
	 * @return the serviceValue
	 */
	public ServiceValue getServiceValue() {
		return serviceValue;
	}

	/**
	 * @param serviceValue
	 *            the serviceValue to set
	 */
	public void setServiceValue(ServiceValue serviceValue) {
		this.serviceValue = serviceValue;
	}

}
