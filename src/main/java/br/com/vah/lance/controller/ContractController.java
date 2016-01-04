package br.com.vah.lance.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Client;
import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceContract;
import br.com.vah.lance.service.ClientService;
import br.com.vah.lance.service.ContractService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.GenericLazyDataModel;
import br.com.vah.lance.util.LanceUtils;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContractController extends AbstractController<Contract> {

	private @Inject transient Logger logger;

	private @Inject ContractService das;

	private @Inject ClientService clientService;

	private @Inject ServiceService serviceService;

	private List<SelectItem> clients;

	private List<SelectItem> services;

	private Long serviceIdToAdd;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<Contract>(das, new Contract()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad() {
		super.onLoad();
		this.clients = LanceUtils.createSelectItem(clientService.findWithNamedQuery(Client.ALL), true);
		this.services = LanceUtils.createSelectItem(serviceService.findWithNamedQuery(Service.ALL), true);
	}

	@Override
	public DataAccessService<Contract> getService() {
		return das;
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

	/**
	 * @return the clients
	 */
	public List<SelectItem> getClients() {
		return clients;
	}

	/**
	 * @return the services
	 */
	public List<SelectItem> getServices() {
		return services;
	}

	/**
	 * @return the serviceIdToAdd
	 */
	public Long getServiceIdToAdd() {
		return serviceIdToAdd;
	}

	/**
	 * @param serviceIdToAdd
	 *            the serviceIdToAdd to set
	 */
	public void setServiceIdToAdd(Long serviceIdToAdd) {
		this.serviceIdToAdd = serviceIdToAdd;
	}

	public String addService() {
		ServiceContract serviceContract = new ServiceContract();
		Service service = serviceService.find(serviceIdToAdd);
		serviceContract.setService(service);
		getItem().getServices().add(serviceContract);
		services = LanceUtils.splice(services, serviceIdToAdd);
		serviceIdToAdd = null;
		return null;
	}
	
	public String removeService(Integer index) {
		ServiceContract cs = getItem().getServices().get(index);
		getItem().getServices().remove(index);
		services.add(new SelectItem(cs.getService().getId(),cs.getService().getTitle()));
		return null;
	}

}
