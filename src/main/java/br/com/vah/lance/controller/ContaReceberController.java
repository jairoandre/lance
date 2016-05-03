package br.com.vah.lance.controller;

import br.com.vah.lance.entity.mv.MvContaReceber;
import br.com.vah.lance.service.ContaReceberService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContaReceberController extends AbstractController<MvContaReceber> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ContaReceberService service;

  @SuppressWarnings("unchecked")
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    setLazyModel(new GenericLazyDataModel<MvContaReceber>(service));
    getLazyModel().getSearchParams().addRelations("itensRateio");
    setSearchField("login");
  }

  @Override
  public DataAccessService<MvContaReceber> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public MvContaReceber createNewItem() {
    return new MvContaReceber();
  }

  @Override
  public String path() {
    return "contaReceber";
  }

  @Override
  public String detailPage() {
    setEditing(true);
    return "/pages/contaReceber/detail.xhtml";
  }

  @Override
  public String editPage() {
    setEditing(true);
    return "/pagees/contaReceber/edit.xhtml";
  }

  @Override
  public String listPage() {
    return "/pages/contaReceber/list.xhtml";
  }

  @Override
  public String getEntityName() {
    return "contas a receber";
  }
}