package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.dbamv.PlanoConta;
import br.com.vah.lance.service.PlanoContaService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PlanoContaCtrl extends AbstractController<PlanoConta> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  PlanoContaService service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    setLazyModel(new GenericLazyDataModel<PlanoConta>(service));
  }

  @Override
  public DataAccessService<PlanoConta> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public PlanoConta createNewItem() {
    return new PlanoConta();
  }

  @Override
  public String path() {
    return null;
  }

  @Override
  public String getEntityName() {
    return "conta contábil";
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());
    setSearchParam("codigoContabil", getSearchTerm());
  }

}
