package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.entity.*;
import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.LanceUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
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

  private List<Entry> entries;

  private List<EntryValue> entryValues;

  private List groupValues;

  private Long serviceId;

  private Comment comment;

  private Date searchMonth = new Date();

  private String groupDateStr;

  @SuppressWarnings({"rawtypes", "unchecked"})
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    entries = service.retrieveEntriesForUser(loginController.getUser().getId(), LanceUtils.getDateRangeForThisMonth());
  }

  public void filterByDate() {
    Calendar cld = GregorianCalendar.getInstance();
    cld.setTime(searchMonth);
    cld.add(Calendar.DAY_OF_YEAR, 15);
    entries = service.retrieveEntriesForUser(loginController.getUser().getId(), LanceUtils.getDateRange(cld.getTime()));
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
  public String path() {
    return "entry";
  }

  public Date getSearchMonth() {
    return searchMonth;
  }

  public void setSearchMonth(Date searchMonth) {
    this.searchMonth = searchMonth;
  }

  @Override
  public String getEntityName() {
    return "setor";
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  public Long getServiceId() {
    return serviceId;
  }

  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

  public String validate(Entry item) {
    return "/pages/entry/validate.xhtml?faces-redirect=true&id=" + item.getId() + "&editing=true";
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (serviceId != null && getItem().getId() == null) {
      setItem(service.prepareNewEntry(loginController.getUser().getId(), serviceId));
      service.computeValues(getItem());
    }
    //
    comment = createComment();
    entryValues = new ArrayList<>(getItem().getValues());
  }

  public void loadGroupByClient() {
    if (groupDateStr != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      try {
        Date date = sdf.parse(groupDateStr);
        entries = service.retrieveEntriesForUser(null, LanceUtils.getDateRange(date));
        Map<MvClient, Map<Service, BigDecimal>> values = service.groupByClient(entries);
        Map<MvClient, List<Map.Entry<Service, BigDecimal>>> mapOfList = new HashMap<>();
        for (MvClient client : values.keySet()) {
          mapOfList.put(client, new ArrayList(values.get(client).entrySet()));
        }
        groupValues = new ArrayList(mapOfList.entrySet());

      } catch (Exception e) {
        // Exceção
      }
    }
  }

  public void computeValues() {
    service.computeValues(getItem());
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

  public Comment createComment() {
    Comment comment = new Comment();
    comment.setAuthor(loginController.getUser());
    comment.setCreatedOn(new Date());
    comment.setEntry(getItem());
    return comment;
  }

  public String getGroupDateStr() {
    return groupDateStr;
  }

  public void setGroupDateStr(String groupDateStr) {
    this.groupDateStr = groupDateStr;
  }

  public String addComment() {
    getItem().getComments().add(comment);
    comment = createComment();
    return null;
  }

  public List<EntryValue> getEntryValues() {
    return entryValues;
  }

  public String doValideSave() {
    getItem().setStatus(EntryStatusEnum.V);
    return doSave();
  }

  public String doDenySave() {
    getItem().setStatus(EntryStatusEnum.P);
    return doSave();
  }

  public String doFixSave() {
    getItem().setStatus(EntryStatusEnum.F);
    return doSave();
  }

  public String doModifySave() {
    getItem().setStatus(EntryStatusEnum.M);
    return doSave();
  }

  public List getGroupValues() {
    return groupValues;
  }

  public void setGroupValues(List groupValues) {
    this.groupValues = groupValues;
  }

  public BigDecimal getTaxAreaA() {
    if (getItem().getTotalAreaA() != null && !BigDecimal.ZERO.equals(getItem().getTotalAreaA())) {
      return getItem().getTotalAreaA().divide(getItem().getTotalAreaA(), 2, BigDecimal.ROUND_HALF_UP);
    }
    return null;
  }

  private BigDecimal ammoutVah = BigDecimal.ZERO;
  private BigDecimal ammountProviders = BigDecimal.ZERO;
  private BigDecimal ammountClinics = BigDecimal.ZERO;
  private BigDecimal ammountShopping = BigDecimal.ZERO;
  private BigDecimal taxProviders = BigDecimal.ZERO;
  private BigDecimal taxClinics = BigDecimal.ZERO;
  private BigDecimal taxShopping = BigDecimal.ZERO;

  public BigDecimal getAmmoutVah() {
    return ammoutVah;
  }

  public BigDecimal getAmmountProviders() {
    return ammountProviders;
  }

  public BigDecimal getAmmountClinics() {
    return ammountClinics;
  }

  public BigDecimal getAmmountShopping() {
    return ammountShopping;
  }

  public BigDecimal getTaxProviders() {
    return taxProviders;
  }

  public BigDecimal getTaxClinics() {
    return taxClinics;
  }

  public BigDecimal getTaxShopping() {
    return taxShopping;
  }

  public void shareAmmount() {
    ServiceValue serviceValue = getItem().getServiceValue();
    ammoutVah = getItem().getAmmountToShare().multiply(serviceValue.getValueD());
    ammountProviders = getItem().getAmmountToShare().multiply(serviceValue.getValueA());
    ammountClinics = getItem().getAmmountToShare().multiply(serviceValue.getValueB());
    ammountShopping = getItem().getAmmountToShare().multiply(serviceValue.getValueC());
    if (!BigDecimal.ZERO.equals(getItem().getTotalAreaA())) {
      taxProviders = ammountProviders.divide(getItem().getTotalAreaA(), 2, BigDecimal.ROUND_HALF_UP);
    }
    if (!BigDecimal.ZERO.equals(getItem().getTotalAreaB())) {
      taxClinics = ammountClinics.divide(getItem().getTotalAreaB(), 2, BigDecimal.ROUND_HALF_UP);
    }
    if (!BigDecimal.ZERO.equals(getItem().getTotalAreaC())) {
      taxShopping = ammountShopping.divide(getItem().getTotalAreaC(), 2, BigDecimal.ROUND_HALF_UP);
    }
  }

  public void fillSharedFields() {
    for (EntryValue entryValue : getItem().getValues()) {
      SectorDetail sectorDetail = entryValue.getContractSector().getSector().getSectorDetail();
      if (sectorDetail != null) {
        BigDecimal area = sectorDetail.getArea();
        if (area != null) {
          switch (sectorDetail.getType()) {
            case TERCEIROS:
              entryValue.setValueA(area.multiply(taxProviders));
              break;
            case CONSULTORIOS:
              entryValue.setValueA(area.multiply(taxClinics));
              break;
            case SHOPPING:
              entryValue.setValueA(area.multiply(taxShopping));
              break;
            default:
              break;
          }
        }
      }
    }
    computeValues();
  }
}
