package br.com.vah.lance.controller;

import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.service.ContaReceberService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;
import br.com.vah.lance.util.PaginatedSearchParam;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContaReceberCtrl extends AbstractController<ContaReceber> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ContaReceberService service;

  public static final String[] RELATIONS = {"itensRateio", "itensConta", "cliente", "contaContabil", "historicoPadrao"};

  @SuppressWarnings("unchecked")
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    initLazyModel(service, RELATIONS);
    PaginatedSearchParam searchParam = getLazyModel().getSearchParams();
    searchParam.setOrderBy("id");
    searchParam.setAsc(false);
  }

  @Override
  public DataAccessService<ContaReceber> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public ContaReceber createNewItem() {
    return new ContaReceber();
  }

  @Override
  public String path() {
    return "contaReceber";
  }

  @Override
  public String getEntityName() {
    return "contas a receber";
  }
}