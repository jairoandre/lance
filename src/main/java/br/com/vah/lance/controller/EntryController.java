package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.entity.Comment;
import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.entity.EntryValue;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.LanceUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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

  private Long serviceId;

  private Comment comment;

  private Date searchMonth = new Date();

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
}
