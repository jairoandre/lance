package br.com.vah.lance.controller;

import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceValue;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.LanceUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ServiceController extends AbstractController<Service> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ServiceService service;

  private ServiceValue serviceValue;

  private ServiceTypesEnum[] types;

  private static final String[] RELATIONS = {"values"};

  private List<ServiceValue> values;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    prepareNewServiceValue();
    initLazyModel(service, RELATIONS);
    types = ServiceTypesEnum.values();
  }

  public void prepareNewServiceValue() {
    serviceValue = new ServiceValue();
    Date[] range = LanceUtils.getDateRangeForThisMonth();
    serviceValue.setBeginDate(range[0]);
    serviceValue.setEndDate(range[1]);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    values = new ArrayList<>(getItem().getValues());
  }

  public ServiceController() {
  }

  public ServiceController(ServiceService service) {
    this();
    initLazyModel(service, RELATIONS);
  }

  @Override
  public DataAccessService<Service> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Service createNewItem() {
    return new Service();
  }

  @Override
  public String editPage() {
    return "/pages/service/edit.xhtml";
  }

  public void addValue() {
    serviceValue.setService(getItem());
    if(service.canAddServiceValue(getItem(), serviceValue)){
      getItem().getValues().add(serviceValue);
      values = new ArrayList<>(getItem().getValues());
      prepareNewServiceValue();
    } else {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Período de vigência já utilizado."), false);
    }
  }

  public void removeValue(ServiceValue value) {
    getItem().getValues().remove(value);
    values = new ArrayList<>(getItem().getValues());
  }

  @Override
  public String listPage() {
    return "/pages/service/list.xhtml";
  }

  public String currency(Service item) {
    return String.format("/pages/service/currency.xhtml?faces-redirect=true&id=%d&editing=true", item.getId());
  }

  @Override
  public String getEntityName() {
    return "serviço";
  }

  public void setDefaultHistory() {
    System.out.println(getItem().getDefaultHistory());
  }

  /**
   * @return the serviceValue
   */
  public ServiceValue getServiceValue() {
    return serviceValue;
  }

  /**
   * @param serviceValue the serviceValue to set
   */
  public void setServiceValue(ServiceValue serviceValue) {
    this.serviceValue = serviceValue;
  }

  /**
   * @return the types
   */
  public ServiceTypesEnum[] getTypes() {
    return types;
  }

  /**
   * @param types the types to set
   */
  public void setTypes(ServiceTypesEnum[] types) {
    this.types = types;
  }

  /**
   *
   * @return
   */
  public List<ServiceValue> getValues() {
    return values;
  }

  /**
   *
   * @param values
   */
  public void setValues(List<ServiceValue> values) {
    this.values = values;
  }
}
