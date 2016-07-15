package br.com.vah.lance.controller;

import br.com.vah.lance.entity.usrdbvah.Lancamento;
import br.com.vah.lance.service.BoletoService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.LancamentoService;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by jairoportela on 14/07/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class BoletoCtrl extends AbstractController<Lancamento> {

  private
  @Inject
  LancamentoService service;

  private
  @Inject
  BoletoService boletoService;

  private
  @Inject
  transient Logger logger;

  @Override
  public DataAccessService<Lancamento> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Lancamento createNewItem() {
    return new Lancamento();
  }

  @Override
  public String path() {
    return "boleto";
  }

  @Override
  public String getEntityName() {
    return "Boleto";
  }

  public StreamedContent getBoleto() {
    try {
      return boletoService.createBoleto(null);
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "relatório"), false);
    }
    return null;
  }


}
