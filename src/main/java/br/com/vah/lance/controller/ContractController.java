package br.com.vah.lance.controller;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceContract;
import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.ContractService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContractController extends AbstractController<Contract> {

	private @Inject transient Logger logger;

	private @Inject ContractService service;

	private Service serviceBean;

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

	public void onLoadSubject() {
		MvClient subject = getItem().getSubject();
		if (subject != null) {
			Set<ServiceContract> services = new LinkedHashSet<>();
			for (MvSector sector : subject.getSectors()) {
				ServiceContract service = new ServiceContract();
				service.setSector(sector);
				service.setContract(getItem());
				service.setServices(new LinkedHashSet<Service>());
				services.add(service);
			}
			getItem().setServices(services);
		}
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

	/**
	 * @return the serviceBean
	 */
	public Service getServiceBean() {
		return serviceBean;
	}

	/**
	 * @param serviceBean
	 *            the serviceBean to set
	 */
	public void setServiceBean(Service serviceBean) {
		this.serviceBean = serviceBean;
	}

	public void toggleService(ServiceContract service) {
		if (service.getServices().contains(serviceBean)) {
			service.getServices().remove(serviceBean);
		} else {
			service.getServices().add(serviceBean);
		}

	}

}
