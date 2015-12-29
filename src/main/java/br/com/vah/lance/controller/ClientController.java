package br.com.vah.lance.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Client;
import br.com.vah.lance.service.ClientService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.GenericLazyDataModel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ClientController extends AbstractController<Client> {

	private @Inject transient Logger logger;

	private @Inject ClientService das;	

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setLazyModel(new GenericLazyDataModel<Client>(das, new Client()));
	}

	@Override
	public DataAccessService<Client> getService() {
		return das;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public Client createNewItem() {
		return new Client();
	}

	@Override
	public String getEditPage() {
		return "pages/client/edit.xhtml";
	}
	
	@Override
	public String getListPage() {
		return "/pages/client/list.xhtml";
	}

}
