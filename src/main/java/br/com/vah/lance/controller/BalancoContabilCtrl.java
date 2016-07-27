package br.com.vah.lance.controller;

import br.com.vah.lance.entity.usrdbvah.Servico;
import br.com.vah.lance.service.RelatorioService;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by jairoportela on 15/07/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class BalancoContabilCtrl implements Serializable {

  private Date vigencia;

  private
  @Inject
  RelatorioService service;

  private
  @Inject
  SessionCtrl sessionCtrl;

  public StreamedContent getRelatorio() {
    try {
      return service.relatorioBalancoCondominial(vigencia, sessionCtrl.getUser());
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ops!", "Ocorreu algum problema na geração do relatório."), false);
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

  public Date getVigencia() {
    return vigencia;
  }

  public void setVigencia(Date vigencia) {
    this.vigencia = vigencia;
  }
}
