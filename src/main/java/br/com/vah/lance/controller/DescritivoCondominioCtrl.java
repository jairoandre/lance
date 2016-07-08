package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.service.RelatorioService;
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
public class DescritivoCondominioCtrl implements Serializable {

  private Fornecedor fornecedor;

  private Date vigencia;

  private EstadoLancamentoEnum[] estados = EstadoLancamentoEnum.values();

  private EstadoLancamentoEnum estado;

  private @Inject
  RelatorioService service;

  private Boolean condominio = true;

  public StreamedContent getRelatorio() {
    try {
      return service.getRelatorioDescritivoCondominio(fornecedor, vigencia, estado, condominio);
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

  public Fornecedor getFornecedor() {
    return fornecedor;
  }

  public void setFornecedor(Fornecedor fornecedor) {
    this.fornecedor = fornecedor;
  }

  public Date getVigencia() {
    return vigencia;
  }

  public void setVigencia(Date vigencia) {
    this.vigencia = vigencia;
  }

  public EstadoLancamentoEnum getEstado() {
    return estado;
  }

  public void setEstado(EstadoLancamentoEnum estado) {
    this.estado = estado;
  }

  public EstadoLancamentoEnum[] getEstados() {
    return estados;
  }

  public Boolean getCondominio() {
    return condominio;
  }

  public void setCondominio(Boolean condominio) {
    this.condominio = condominio;
  }
}
