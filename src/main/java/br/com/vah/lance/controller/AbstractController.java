package br.com.vah.lance.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
public abstract class AbstractController<T extends BaseEntity> implements Serializable {

	private T[] selectedItems;

	private T selectedItem;

	private T item;

	private GenericLazyDataModel<T> lazyModel;

	/**
	 * @return the service
	 */
	public abstract DataAccessService<T> getService();

	/**
	 * 
	 * @return
	 */
	public abstract Logger getLogger();

	/**
	 * 
	 * @return
	 */
	public abstract T createNewItem();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getEditPage();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getListPage();
	

	/**
	 * Create, Update and Delete operations
	 */
	public void doCreate() {
		FacesContext context = FacesContext.getCurrentInstance();
		getService().create(item);
		context.addMessage(null, new FacesMessage("Sucesso!", "Registro salvo"));
	}

	/**
	 *
	 * @param actionEvent
	 */
	public void doUpdate(ActionEvent actionEvent) {
		getService().update(selectedItem);
	}

	/**
	 *
	 * @param actionEvent
	 */
	public void doDelete(ActionEvent actionEvent) {
		getService().deleteItems(selectedItems);
	}

	public void closeEvent() {
		item = createNewItem();
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
	 * @param lazyModel
	 *            the lazyModel to set
	 */
	public void setLazyModel(GenericLazyDataModel<T> lazyModel) {
		this.lazyModel = lazyModel;
	}

	/**
	 * @return the selectedItems
	 */
	public T[] getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param selectedItems
	 *            the selectedItems to set
	 */
	public void setSelectedItems(T[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	/**
	 * @return the selectedItem
	 */
	public T getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem
	 *            the selectedItem to set
	 */
	public void setSelectedItem(T selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	/*
	 * ACTIONS
	 */
	
	public String preAdd() {
		item = createNewItem();
		return getEditPage();
	}

}
