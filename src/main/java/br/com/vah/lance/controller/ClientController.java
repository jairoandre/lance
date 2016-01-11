package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.ClientService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ClientController extends AbstractController<MvClient> {

	private @Inject transient Logger logger;

	private @Inject ClientService service;

	private Long sectorIdToAdd;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setLazyModel(new GenericLazyDataModel<MvClient>(service));
		getLazyModel().getSearchParams().getRelations().add("sectors");
	}

	@Override
	public void onLoad() {
		super.onLoad();
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
	public String editPage() {
		return "/pages/client/edit.xhtml";
	}

	@Override
	public String listPage() {
		return "/pages/client/list.xhtml";
	}

	@Override
	public String getEntityName() {
		return "cliente";
	}

	/**
	 * @return the sectorIdToAdd
	 */
	public Long getSectorIdToAdd() {
		return sectorIdToAdd;
	}

	/**
	 * @param sectorIdToAdd
	 *            the sectorIdToAdd to set
	 */
	public void setSectorIdToAdd(Long sectorIdToAdd) {
		this.sectorIdToAdd = sectorIdToAdd;
	}

	public String toggle(MvSector sector) {
		if (getItem().getSectors().contains(sector)) {
			getItem().getSectors().remove(sector);
		} else {
			getItem().getSectors().add(sector);
		}
		return null;
	}

	public Boolean selected(MvSector sector) {
		return getItem().getSectors().contains(sector);
	}

}
