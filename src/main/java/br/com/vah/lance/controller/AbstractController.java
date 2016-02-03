package br.com.vah.lance.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
public abstract class AbstractController<T extends BaseEntity> implements Serializable {

  private T item;

  private Long id;

  private Integer index;

  private Boolean editing = true;

  private GenericLazyDataModel<T> lazyModel;

  private String searchTerm;

  private String searchField = "title";

  private T selectedItem;

  private T[] selectedItems;

  private final String _PREFIX_URL = "/pages/";

  private final String _EDIT_XHTML = "/edit.xhtml";

  private final String _LIST_XHTML = "/list.xhtml";

  private final String _DETAIL_XHTML = "/detail.xhtml";

  private final String _FACES_REDIRECT = "?faces-redirect=true";

  private final String _ID_PARAM = "&id=";

  /**
   * @return the service
   */
  public abstract DataAccessService<T> getService();

  /**
   * @return
   */
  public abstract Logger getLogger();

  /**
   * @return
   */
  public abstract T createNewItem();

  /**
   * The path of resources
   *
   * @return
   */
  public abstract String path();

  /**
   * The edit page
   *
   * @return
   */
  public String editPage() {
    return _PREFIX_URL + path() + _EDIT_XHTML;
  }

  /**
   * The list page
   *
   * @return
   */
  public String listPage() {
    return _PREFIX_URL + path() + _LIST_XHTML;
  }

  /**
   * The detail page
   *
   * @return
   */
  public String detailPage() {
    return _PREFIX_URL + path() + _DETAIL_XHTML;
  }

  /**
   * @return
   */
  public abstract String getEntityName();

  /**
   * @return the searchTerm
   */
  public String getSearchTerm() {
    return searchTerm;
  }

  /**
   * @param searchTerm the searchTerm to set
   */
  public void setSearchTerm(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  /**
   *
   */
  public String search() {
    lazyModel.getSearchParams().getParams().put(getSearchField(), getSearchTerm());
    lazyModel.getSearchParams().setResetPage(true);
    return null;
  }

  public void onLoad() {
    getLogger().info("Load params");
    if (id == null) {
      item = createNewItem();
    } else {
      item = getService().find(id);
    }
  }

  public void addMsg(FacesMessage msg, boolean flash) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.addMessage(null, msg);
    if (flash) {
      ctx.getExternalContext().getFlash().setKeepMessages(true);
    }
  }

  /**
   * Create, Update and Delete operations
   */
  public String doSave() {
    try {
      if (item.getId() == null) {
        getService().create(item);
      } else {
        getService().update(item);
      }
      addMsg(new FacesMessage("Sucesso!", "Registro salvo"), true);
      return back();
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Ops! Erro inesperado: " + e.getMessage()),
          true);
    }
    return null;
  }

  /**
   * @return the item
   */
  public T getItem() {
    return item;
  }

  /**
   * @param item the item to set
   */
  public void setItem(T item) {
    this.item = item;
  }

  /**
   * @return the lazyModel
   */
  public GenericLazyDataModel<T> getLazyModel() {
    return lazyModel;
  }

  /**
   * @param lazyModel the lazyModel to set
   */
  public void setLazyModel(GenericLazyDataModel<T> lazyModel) {
    this.lazyModel = lazyModel;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the index
   */
  public Integer getIndex() {
    return index;
  }

  /**
   * @param index the index to set
   */
  public void setIndex(Integer index) {
    this.index = index;
  }

  /**
   * @return the editing
   */
  public Boolean getEditing() {
    return editing;
  }

  /**
   * @param editing the editing to set
   */
  public void setEditing(Boolean editing) {
    this.editing = editing;
  }

  protected void initLazyModel(DataAccessService<T> service, String... relations) {
    setLazyModel(new GenericLazyDataModel<T>(service));
    if (relations != null && relations.length > 0) {
      getLazyModel().getSearchParams().addRelations(relations);
    }
  }

  /*
   * ACTIONS
   */
  public String preDelete(Long id, Integer index) {
    this.id = id;
    this.index = index;
    return null;
  }

  public String doDelete() {
    getService().delete(id);
    getLazyModel().remove(index);
    addMsg(new FacesMessage("Sucesso", "Registro removido [id=" + id + "]"), false);
    return null;
  }

  public String add() {
    this.editing = true;
    return editPage() + _FACES_REDIRECT;
  }

  public String back() {
    return listPage() + _FACES_REDIRECT;
  }

  public String edit(T item) {
    this.editing = true;
    return editPage() + _FACES_REDIRECT + _ID_PARAM + item.getId();
  }

  public String detail(T item) {
    this.editing = false;
    return detailPage() + _FACES_REDIRECT + _ID_PARAM + item.getId();
  }

  public String getEditLabel() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(id == null ? "Novo " : editing ? "Editar " : "Visualizar ");
    buffer.append(getEntityName());
    return buffer.toString();
  }

  /**
   * @return the searchField
   */
  public String getSearchField() {
    return searchField;
  }

  /**
   * @param searchField the searchField to set
   */
  public void setSearchField(String searchField) {
    this.searchField = searchField;
  }

  /**
   * @return the selectedItem
   */
  public T getSelectedItem() {
    return selectedItem;
  }

  /**
   * @param selectedItem the selectedItem to set
   */
  public void setSelectedItem(T selectedItem) {
    this.selectedItem = selectedItem;
  }

  /**
   * @return the selectedItems
   */
  public T[] getSelectedItems() {
    return selectedItems;
  }

  /**
   * @param selectedItems the selectedItems to set
   */
  public void setSelectedItems(T[] selectedItems) {
    this.selectedItems = selectedItems;
  }

}
