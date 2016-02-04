package br.com.vah.lance.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.ClientService;
import br.com.vah.lance.service.DataAccessService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ClientController extends AbstractController<MvClient> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ClientService service;

  private Long sectorIdToAdd;

  @Override
  public String getSearchTerm() {
    return super.getSearchTerm();
  }

  private MvSector sector;

  @Override
  public String search() {
    return super.search();
  }

  public static final String[] RELATIONS = {"sectors"};

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    initLazyModel(service, RELATIONS);
    getLazyModel().getSearchParams().getParams().put("type",new Object[] {"A","C"});
  }

  public ClientController() {
  }

  public ClientController(ClientService service) {
    this();
    initLazyModel(service, RELATIONS);
  }

  @Override
  public DataAccessService<MvClient> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public MvClient createNewItem() {
    return new MvClient();
  }

  @Override
  public String path() {
    return "client";
  }

  @Override
  public String getEntityName() {
    return "cliente";
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if(getItem().getId() == null){

    }
  }

  /**
   * @return the sectorIdToAdd
   */
  public Long getSectorIdToAdd() {
    return sectorIdToAdd;
  }

  /**
   * @param sectorIdToAdd the sectorIdToAdd to set
   */
  public void setSectorIdToAdd(Long sectorIdToAdd) {
    this.sectorIdToAdd = sectorIdToAdd;
  }

  /**
   * @return the sector
   */
  public MvSector getSector() {
    return sector;
  }

  /**
   * @param sector the sector to set
   */
  public void setSector(MvSector sector) {
    this.sector = sector;
  }

  public void toggle() {
    if (getItem().getSectors().contains(sector)) {
      getItem().getSectors().remove(sector);
    } else {
      getItem().getSectors().add(sector);
    }
  }

  public Boolean selected(MvSector sector) {
    return getItem().getSectors().contains(sector);
  }

  @Override
  public String doSave() {
    List<String> errors = service.verifySectors(getItem());
    if (errors.isEmpty()) {
      return super.doSave();
    } else {
      for (String error : errors) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", error), false);
      }
    }
    return null;

  }
}
