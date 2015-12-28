package br.com.vah.lance.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.com.vah.lance.entity.Sector;
import br.com.vah.lance.service.SectorService;

@Named
@ViewScoped
public class SectorController implements Serializable{
	
	private @Inject
	transient Logger logger;
	
	private @Inject
	SectorService das;
	
	private Sector[] selectedItems;
	
	private Sector selectedItem;
	
	private LazyDataModel<Sector> lazyModel;
	
	/**
	 * Default constructor
	 */
	public SectorController() {
		
	}
	
	@PostConstruct
	public void init() {
		lazyModel = new LazySectorDataModel(das);
	}

	/**
	 * @return the selectedItems
	 */
	public Sector[] getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param selectedItems the selectedItems to set
	 */
	public void setSelectedItems(Sector[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	/**
	 * @return the selectedItem
	 */
	public Sector getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem the selectedItem to set
	 */
	public void setSelectedItem(Sector selectedItem) {
		this.selectedItem = selectedItem;
	}

	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sector> getLazyModel() {
		return lazyModel;
	}

	/**
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<Sector> lazyModel) {
		this.lazyModel = lazyModel;
	}

		

}
