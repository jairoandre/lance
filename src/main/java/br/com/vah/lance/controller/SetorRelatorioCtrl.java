package br.com.vah.lance.controller;

import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.service.RelatorioService;
import br.com.vah.lance.service.SetorService;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jairoportela on 06/07/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class SetorRelatorioCtrl implements Serializable {

  private @Inject
  RelatorioService service;

  public StreamedContent getRelatorio() {
    try {
      return service.getRelatorioSetor();
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "relatório"), false);
    }
    return null;
  }

  public void addMsg(FacesMessage msg, boolean flash) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.addMessage(null, msg);
    if (flash) {
      ctx.getExternalContext().getFlash().setKeepMessages(true);
    }
  }

}
