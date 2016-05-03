package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvPlanoConta;
import br.com.vah.lance.service.AccountChartService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class AccountChartController extends AbstractController<MvPlanoConta> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  AccountChartService service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    setLazyModel(new GenericLazyDataModel<MvPlanoConta>(service));
  }

  @Override
  public DataAccessService<MvPlanoConta> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public MvPlanoConta createNewItem() {
    return new MvPlanoConta();
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
  public String search() {
    getLazyModel().getSearchParams().getParams().put("accountingCode", getSearchTerm());
    getLazyModel().getSearchParams().getParams().put("id", null);
    try {
      Long convertedValue = Long.valueOf(getSearchTerm());
      getLazyModel().getSearchParams().getParams().put("id", convertedValue);
    } catch (Exception e) {
      /**
       * Cannot convert to integer
       */
    }
    return super.search();
  }

}
