package br.com.vah.lance.controller;

import br.com.vah.lance.entity.usrdbvah.Cobranca;
import br.com.vah.lance.service.ArquivoRemessaService;
import br.com.vah.lance.service.CobrancaService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.RelatorioService;
import br.com.vah.lance.util.DateUtility;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jairoportela on 22/07/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class CobrancaCtrl extends AbstractController<Cobranca> {

  private
  @Inject
  CobrancaService service;

  private
  @Inject
  RelatorioService relatorioService;

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  SessionCtrl sessionCtrl;

  private
  @Inject
  ArquivoRemessaService arquivoService;

  private Cobranca[] selectedCobrancas;

  private Date vigencia;

  private Integer vencimento;

  private List<Cobranca> cobrancas;

  public void recuperarCobrancas() {
    try {
      boolean recuperar = true;
      if (vigencia == null) {
        addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a vigência");
        recuperar = false;
      }
      if (recuperar) {
        cobrancas = service.recuperarCobrancas(DateUtility.monthRange(vigencia), vencimento);
      }
    } catch (Exception e) {
      addMsg(FacesMessage.SEVERITY_ERROR, "Ops", "Erro na recuperação das cobranças");
    }
  }

  public StreamedContent getArquivoRemessa() {
    if (selectedCobrancas != null && selectedCobrancas.length > 0) {
      return arquivoService.gerarArquivo(Arrays.asList(selectedCobrancas));
    } else {
      return null;
    }

  }

  public StreamedContent descritivo(Cobranca cobranca) {
    return relatorioService.descritivo(cobranca, sessionCtrl.getUser());
  }

  public Cobranca[] getSelectedCobrancas() {
    return selectedCobrancas;
  }

  public void setSelectedCobrancas(Cobranca[] selectedCobrancas) {
    this.selectedCobrancas = selectedCobrancas;
  }

  public Date getVigencia() {
    return vigencia;
  }

  public void setVigencia(Date vigencia) {
    this.vigencia = vigencia;
  }

  public Integer getVencimento() {
    return vencimento;
  }

  public void setVencimento(Integer vencimento) {
    this.vencimento = vencimento;
  }

  public List<Cobranca> getCobrancas() {
    return cobrancas;
  }

  public void setCobrancas(List<Cobranca> cobrancas) {
    this.cobrancas = cobrancas;
  }

  @Override
  public DataAccessService<Cobranca> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Cobranca createNewItem() {
    return new Cobranca();
  }

  @Override
  public String path() {
    return "cobranca";
  }

  @Override
  public String getEntityName() {
    return "Cobrança";
  }
}
