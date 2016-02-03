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

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  UserService service;

  private
  @Inject
  ServiceService serviceService;

  private Service beanService;

  private List<SelectItem> services;

  private RolesEnum[] roles;

  @SuppressWarnings("unchecked")
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    setLazyModel(new GenericLazyDataModel<User>(service));
    getLazyModel().getSearchParams().addRelations("services", "roles");
    this.services = LanceUtils.createSelectItem(serviceService.findWithNamedQuery(Service.ALL), true);
    setSearchField("login");
    roles = RolesEnum.values();
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
  public String path() {
    return "user";
  }

  @Override
  public String detailPage() {
    setEditing(true);
    return "/admin/user/detail.xhtml";
  }

  @Override
  public String editPage() {
    setEditing(true);
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

  public void setService(UserService service) {
    this.service = service;
  }

  public Service getBeanService() {
    return beanService;
  }

  public void setBeanService(Service beanService) {
    this.beanService = beanService;
  }

  public List<SelectItem> getServices() {
    return services;
  }

  public void setServices(List<SelectItem> services) {
    this.services = services;
  }

  public RolesEnum[] getRoles() {
    return roles;
  }

  public void toggleService() {
    if (getItem().getServices().contains(beanService)) {
      getItem().getServices().remove(beanService);
    } else {
      getItem().getServices().add(beanService);
    }
  }

  public void toggleRole(RolesEnum role) {
    if (getItem().getRoles().contains(role)) {
      getItem().getRoles().remove(role);
    } else {
      getItem().getRoles().add(role);
    }
  }
}