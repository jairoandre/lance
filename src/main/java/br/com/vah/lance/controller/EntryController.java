package br.com.vah.lance.controller;

import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.LanceUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class EntryController extends AbstractController<Entry> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  EntryService service;

  private
  @Inject
  LoginController loginController;

  private
  @Inject
  ServiceService serviceService;

  private Service serviceItem;

  private List<Entry> entries;

  private BigDecimal variableValueTotal = BigDecimal.ZERO;

  private BigDecimal contractValueTotal = BigDecimal.ZERO;

  private BigDecimal valueTotal = BigDecimal.ZERO;

  private Long serviceId;

  @SuppressWarnings({"rawtypes", "unchecked"})
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    entries = service.retrieveEntriesForUser(loginController.getUser().getId(), LanceUtils.getDateRangeForThisMonth());
  }

  @Override
  public DataAccessService<Entry> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Entry createNewItem() {
    return new Entry();
  }

  @Override
  public String editPage() {
    return "/pages/entry/edit.xhtml";
  }

  @Override
  public String listPage() {
    return "/pages/entry/list.xhtml";
  }

  @Override
  public String getEntityName() {
    return "setor";
  }

  /**
   * @return
   */
  public List<Entry> getEntries() {
    return entries;
  }

  /**
   * @param entries
   */
  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  /**
   * @return the serviceItem
   */
  public Service getServiceItem() {
    return serviceItem;
  }

  /**
   * @param serviceItem the serviceItem to set
   */
  public void setServiceItem(Service serviceItem) {
    this.serviceItem = serviceItem;
  }

  /**
   * @return the variableValueTotal
   */
  public BigDecimal getVariableValueTotal() {
    return variableValueTotal;
  }

  /**
   * @return the contractValueTotal
   */
  public BigDecimal getContractValueTotal() {
    return contractValueTotal;
  }

  /**
   * @return the valueTotal
   */
  public BigDecimal getValueTotal() {
    return valueTotal;
  }

  public Long getServiceId() {
    return serviceId;
  }

  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
  }

  public String validate(Entry item) {
    return "/pages/entry/validate.xhtml?faces-redirect=true&id=" + item.getId() + "&editing=true";
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (serviceId != null && getItem().getId() != null) {
      setItem(service.prepareNewEntry(loginController.getUser().getId(), serviceId));
    }
  }

  @Override
  public String edit(Entry item) {
    if (item.getId() == null) {
      return editPage() + String.format("?faces-redirect=true&editing=true&serviceId=%d",
          item.getService().getId());
    } else {
      return super.edit(item);
    }
  }
}
