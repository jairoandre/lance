package br.com.vah.lance.controller;

import br.com.vah.lance.entity.dbamv.DescontoAcrescimo;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.DescontoAcrescimoService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 09/08/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class DescontoAcrescimoCtrl extends AbstractController<DescontoAcrescimo> {

  private
  @Inject
  DescontoAcrescimoService service;

  private
  @Inject
  transient Logger logger;

  @Override
  public DataAccessService<DescontoAcrescimo> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public DescontoAcrescimo createNewItem() {
    return new DescontoAcrescimo();
  }

  @Override
  public String path() {
    return "descontoAcrescimo";
  }

  @Override
  public String getEntityName() {
    return "Desconto/Acréscimo";
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("descricao", getSearchTerm());
  }
}
