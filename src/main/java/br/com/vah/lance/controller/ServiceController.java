package br.com.vah.lance.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceType;
import br.com.vah.lance.entity.mv.MvDefaultHistory;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.service.ServiceTypeService;
import br.com.vah.lance.util.GenericLazyDataModel;
import br.com.vah.lance.util.LanceUtils;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ServiceController extends AbstractController<Service> {

	private @Inject transient Logger logger;

	private @Inject ServiceService service;

	private @Inject ServiceTypeService serviceTypeService;

	private List<SelectItem> serviceTypes;
	
	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<Service>(service));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad() {
		super.onLoad();
		serviceTypes = LanceUtils.createSelectItem(serviceTypeService.findWithNamedQuery(ServiceType.ALL), true);

	}

	/**
	 * @return the serviceTypes
	 */
	public List<SelectItem> getServiceTypes() {
		return serviceTypes;
	}

	/**
	 * @param serviceTypes
	 *            the serviceTypes to set
	 */
	public void setServiceTypes(List<SelectItem> serviceTypes) {
		this.serviceTypes = serviceTypes;
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

	@Override
	public String listPage() {
		return "/pages/service/list.xhtml";
	}

	@Override
	public String getEntityName() {
		return "serviço";
	}

	public void setDefaultHistory() {
		System.out.println(getItem().getDefaultHistory());
	}

}
