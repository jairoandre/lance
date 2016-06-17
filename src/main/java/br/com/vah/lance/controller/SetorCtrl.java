package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.constant.TipoSetorEnum;
import br.com.vah.lance.entity.usrdbvah.SetorDetalhe;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.SetorService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class SetorCtrl extends AbstractController<Setor> {

	private @Inject transient Logger logger;

	private @Inject
	SetorService service;

	private TipoSetorEnum[] types;

	private static final String[] RELATIONS = {"meters"};

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service, RELATIONS);
		types = TipoSetorEnum.values();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if(getItem().getSetorDetalhe() == null) {
			getItem().setSetorDetalhe(new SetorDetalhe(getItem()));
		}
	}

	@Override
	public DataAccessService<Setor> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public Setor createNewItem() {
		return new Setor();
	}

	@Override
	public String path() {
		return "setor";
	}

	@Override
	public String getEntityName() {
		return "setor";
	}

	public TipoSetorEnum[] getTypes() {
		return types;
	}

	@Override
	public void prepareSearch() {
		super.prepareSearch();
		setSearchParam("title", getSearchTerm());
	}
}
