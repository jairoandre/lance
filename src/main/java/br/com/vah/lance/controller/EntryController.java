package br.com.vah.lance.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class EntryController extends AbstractController<Entry> {

	private @Inject transient Logger logger;

	private @Inject EntryService das;

	private @Inject LoginController loginController;

	private List<Map.Entry<Service, Map<Contract, Entry>>> entries;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		// setLazyModel(new GenericLazyDataModel<Entry>(das, new Entry()));
		entries = new ArrayList(das.retrieveEntrysForUser(loginController.getUser().getId(), new Date()).entrySet());

	}

	@Override
	public DataAccessService<Entry> getService() {
		return das;
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
	 * @return the entries
	 */
	public List<Map.Entry<Service, Map<Contract, Entry>>> getEntries() {
		return entries;
	}

	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(List<Map.Entry<Service, Map<Contract, Entry>>> entries) {
		this.entries = entries;
	}

	@Override
	public GenericLazyDataModel<Entry> getLazyModel() {
		return super.getLazyModel();
	}

}
