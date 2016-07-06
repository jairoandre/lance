package br.com.vah.lance.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.service.FornecedorService;
import br.com.vah.lance.service.DataAccessService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class FornecedorCtrl extends AbstractController<Fornecedor> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  FornecedorService service;

  private Long setorIdToAdd;

  @Override
  public String getSearchTerm() {
    return super.getSearchTerm();
  }

  private Setor setor;

  public static final String[] RELATIONS = {"setores"};

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    initLazyModel(service, RELATIONS);
    getLazyModel().getSearchParams().getParams().put("type",new Object[] {"A","C","T"});
  }

  public FornecedorCtrl() {
  }

  public FornecedorCtrl(FornecedorService service) {
    this();
    initLazyModel(service, RELATIONS);
  }

  @Override
  public DataAccessService<Fornecedor> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Fornecedor createNewItem() {
    return new Fornecedor();
  }

  @Override
  public String path() {
    return "fornecedor";
  }

  @Override
  public String getEntityName() {
    return "fornecedor";
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if(getItem().getId() == null){

    }
  }

  /**
   * @return the setorIdToAdd
   */
  public Long getSetorIdToAdd() {
    return setorIdToAdd;
  }

  /**
   * @param setorIdToAdd the setorIdToAdd to set
   */
  public void setSetorIdToAdd(Long setorIdToAdd) {
    this.setorIdToAdd = setorIdToAdd;
  }

  /**
   * @return the setor
   */
  public Setor getSetor() {
    return setor;
  }

  /**
   * @param setor the setor to set
   */
  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  public void toggle() {
    if (getItem().getSetores().contains(setor)) {
      getItem().getSetores().remove(setor);
    } else {
      getItem().getSetores().add(setor);
    }
  }

  public void addSetor() {
    if (!getItem().getSetores().contains(setor)) {
      getItem().getSetores().add(setor);
    }
  }

  public void removeSetor(Setor setorToRemove) {
    getItem().getSetores().remove(setorToRemove);
  }

  public Boolean selected(Setor setor) {
    return getItem().getSetores().contains(setor);
  }

  @Override
  public String doSave() {
    List<String> errors = service.verifySetores(getItem());
    if (errors.isEmpty()) {
      return super.doSave();
    } else {
      for (String error : errors) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", error), false);
      }
    }
    return null;

  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());
    setSearchParam("nomeFantasia", getSearchTerm());
  }
}
