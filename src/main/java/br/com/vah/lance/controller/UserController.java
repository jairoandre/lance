package br.com.vah.lance.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.constant.RolesEnum;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.User;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.service.UserService;
import br.com.vah.lance.util.GenericLazyDataModel;
import br.com.vah.lance.util.LanceUtils;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class UserController extends AbstractController<User> {

	private @Inject transient Logger logger;

	private @Inject UserService service;

	private @Inject ServiceService serviceService;

	private List<SelectItem> services;

	private Long serviceIdToAdd;

	private RolesEnum roleToAdd;

	private List<SelectItem> roles;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		setLazyModel(new GenericLazyDataModel<User>(service));
		this.services = LanceUtils.createSelectItem(serviceService.findWithNamedQuery(Service.ALL), true);
		this.roles = RolesEnum.getSelectItems();
	}

	@Override
	public DataAccessService<User> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public User createNewItem() {
		return new User();
	}

	@Override
	public String editPage() {
		return "/admin/user/edit.xhtml";
	}

	@Override
	public String listPage() {
		return "/admin/user/list.xhtml";
	}

	@Override
	public String getEntityName() {
		return "usuário";
	}

	/**
	 * @return the serviceService
	 */
	public ServiceService getServiceService() {
		return serviceService;
	}

	/**
	 * @param serviceService
	 *            the serviceService to set
	 */
	public void setServiceService(ServiceService serviceService) {
		this.serviceService = serviceService;
	}

	/**
	 * @return the services
	 */
	public List<SelectItem> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<SelectItem> services) {
		this.services = services;
	}

	/**
	 * @return the roles
	 */
	public List<SelectItem> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
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

	/**
	 * @return the roleToAdd
	 */
	public RolesEnum getRoleToAdd() {
		return roleToAdd;
	}

	/**
	 * @param roleToAdd
	 *            the roleToAdd to set
	 */
	public void setRoleToAdd(RolesEnum roleToAdd) {
		this.roleToAdd = roleToAdd;
	}

	public String addService() {
		Service service = serviceService.find(serviceIdToAdd);
		getItem().getServices().add(service);
		services = LanceUtils.splice(services, serviceIdToAdd);
		serviceIdToAdd = null;
		return null;
	}

	public String removeService(Integer index) {
		Service serv = getItem().getServices().get(index);
		getItem().getServices().remove(index.intValue());
		services.add(new SelectItem(serv.getId(), serv.getTitle()));
		return null;
	}

	public String addRole() {
		getItem().getRoles().add(roleToAdd);
		roleToAdd = null;
		return null;
	}

	public String removeRole(RolesEnum role) {
		getItem().getRoles().remove(role);
		return null;
	}

}