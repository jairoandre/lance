package br.com.vah.lance.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.constant.RolesEnum;
import br.com.vah.lance.entity.usrdbvah.Servico;
import br.com.vah.lance.entity.usrdbvah.User;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServicoService;
import br.com.vah.lance.service.UserService;
import br.com.vah.lance.util.VahUtils;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class UserCtrl extends AbstractController<User> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  UserService service;

  private
  @Inject
  ServicoService servicoService;

  private List<SelectItem> services;

  private RolesEnum[] roles;

  private String[] RELATIONS = {"servicos", "roles"};

  @SuppressWarnings("unchecked")
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    initLazyModel(service, RELATIONS);
    services = VahUtils.createSelectItem(servicoService.findWithNamedQuery(Servico.ALL), true);
    roles = RolesEnum.values();
    setSearchField("login");
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

  public List<SelectItem> getServices() {
    return services;
  }

  public void setServices(List<SelectItem> services) {
    this.services = services;
  }

  public RolesEnum[] getRoles() {
    return roles;
  }

  public void incluirTodosServicos() {
    List<Servico> todosServicos = servicoService.findWithNamedQuery(Servico.ALL);
    for (Servico servico : todosServicos) {
      if (!getItem().getServicos().contains(servico)) {
        getItem().getServicos().add(servico);
      }
    }
  }

  public void toggleRole(RolesEnum role) {
    if (getItem().getRoles().contains(role)) {
      getItem().getRoles().remove(role);
    } else {
      getItem().getRoles().add(role);
    }
  }

  @Override
  public void prepareSearch() {
    setSearchParam("login", getSearchTerm());
  }
}