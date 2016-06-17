package br.com.vah.lance.controller;

import br.com.vah.lance.entity.usrdbvah.MedidorConsumo;
import br.com.vah.lance.entity.usrdbvah.SetorMedidorConsumo;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.service.MedidorConsumoService;
import br.com.vah.lance.service.DataAccessService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class MedidorConsumoCtrl extends AbstractController<MedidorConsumo> {

	private @Inject transient Logger logger;

	private @Inject
	MedidorConsumoService service;

	private Setor setor;

	private SetorMedidorConsumo itemToRemove;

	private Set<Setor> setores = new LinkedHashSet<>();

  public static final String[] RELATIONS = {"setores"};

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service, RELATIONS);
		getLazyModel().getSearchParams().setOrderBy("code");
	}

	@Override
	public DataAccessService<MedidorConsumo> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public MedidorConsumo createNewItem() {
		return new MedidorConsumo();
	}

	@Override
	public String path() {
		return "medidorConsumo";
	}

	@Override
	public String getEntityName() {
		return "medidor";
	}

	public void setService(MedidorConsumoService service) {
		this.service = service;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Set<Setor> getSetores() {
		return setores;
	}

	public void setSetores(Set<Setor> setores) {
		this.setores = setores;
	}

	public SetorMedidorConsumo getItemToRemove() {
		return itemToRemove;
	}

	public void setItemToRemove(SetorMedidorConsumo itemToRemove) {
		this.itemToRemove = itemToRemove;
	}

	@Override
	public void prepareSearch() {
		super.prepareSearch();
		getLazyModel().getSearchParams().getParams().put("id", null);
		getLazyModel().getSearchParams().getParams().put("code", getSearchTerm());
	}

	@Override
	public String doSave() {
		return super.doSave();
	}

	public void toggle() {
		if (setores.contains(setor)) {
			setores.remove(setor);
			SetorMedidorConsumo toRemove = null;
			for (SetorMedidorConsumo setorMedidorConsumo : getItem ().getSetores()) {
				if(setorMedidorConsumo.getSetor().equals(setor)){
					toRemove = setorMedidorConsumo;
					break;
				}
			}
			getItem().getSetores().remove(toRemove);
		} else {
			setores.add(setor);
			SetorMedidorConsumo toAdd = new SetorMedidorConsumo();
			toAdd.setMedidorConsumo(getItem());
			toAdd.setSetor(setor);
			getItem().getSetores().add(toAdd);
		}
	}

	public void removeSetor () {
		getItem().getSetores().remove(itemToRemove);
		setores.remove(itemToRemove.getSetor());
	}

}
