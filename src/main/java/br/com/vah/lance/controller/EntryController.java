package br.com.vah.lance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;
import br.com.vah.lance.util.GenericLazyDataModel;
import br.com.vah.lance.util.LanceUtils;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class EntryController extends AbstractController<Entry> {

	private @Inject transient Logger logger;

	private @Inject EntryService das;

	private @Inject LoginController loginController;

	private List<Map.Entry<Service, List<Map.Entry<Contract, Entry>>>> entries;

	private Service serviceItem;

	private List<Map.Entry<Contract, Entry>> entryMap;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		// setLazyModel(new GenericLazyDataModel<Entry>(das, new Entry()));
		Map<Service, Map<Contract, Entry>> modelMap = das.retrieveEntrysForUser(loginController.getUser().getId(),
				LanceUtils.getDateRangeForThisMonth());
		Map<Service, Object> transformedMap = new LinkedHashMap<>();
		for (Service key : modelMap.keySet()) {
			transformedMap.put(key, LanceUtils.transformMap(modelMap.get(key)));
		}
		entries = new ArrayList(transformedMap.entrySet());
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (getId() != null) {
			for (Map.Entry<Service, List<Map.Entry<Contract, Entry>>> map : entries) {
				if (getId().equals(map.getKey().getId())) {
					serviceItem = map.getKey();
					entryMap = map.getValue();
					break;
				}
			}
		}

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
	public List<Map.Entry<Service, List<Map.Entry<Contract, Entry>>>> getEntries() {
		return entries;
	}

	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(List<Map.Entry<Service, List<Map.Entry<Contract, Entry>>>> entries) {
		this.entries = entries;
	}

	/**
	 * @return the serviceItem
	 */
	public Service getServiceItem() {
		return serviceItem;
	}

	/**
	 * @param serviceItem
	 *            the serviceItem to set
	 */
	public void setServiceItem(Service serviceItem) {
		this.serviceItem = serviceItem;
	}

	/**
	 * @return the entryMap
	 */
	public List<Map.Entry<Contract, Entry>> getEntryMap() {
		return entryMap;
	}

	/**
	 * @param entryMap
	 *            the entryMap to set
	 */
	public void setEntryMap(List<Map.Entry<Contract, Entry>> entryMap) {
		this.entryMap = entryMap;
	}

	@Override
	public GenericLazyDataModel<Entry> getLazyModel() {
		return super.getLazyModel();
	}

	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (Map.Entry<Contract, Entry> ent : entryMap) {
			total = total.add(ent.getValue().getContractValue()).add(ent.getValue().getVariableValue());
		}
		return total;
	}

	public String saveEntrys() {
		List<Entry> entries = new ArrayList<>();
		for (Map.Entry<Contract, Entry> map : entryMap) {
			map.getValue().setUserForContract(loginController.getUser());
			map.getValue().setUserForEntry(loginController.getUser());
			entries.add(map.getValue());
		}
		das.saveEntrys(entries);
		addMsg(new FacesMessage("Sucesso!", "Lançamentos atualizados"), true);
		return back();
	}

}
