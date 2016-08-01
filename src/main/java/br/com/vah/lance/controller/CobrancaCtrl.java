package br.com.vah.lance.controller;

import br.com.vah.lance.entity.usrdbvah.Cobranca;
import br.com.vah.lance.exception.LanceBusinessException;
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

  private Boolean previa = false;

  private Cobranca[] selectedCobrancas;

  private Date vigencia = new Date();

  private Integer vencimento;

  private List<Cobranca> cobrancas;

  private boolean validarObrigatorios() {
    if (vigencia == null) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a vigência");
      return false;
    }
    return true;
  }

  private void addErrorMessage(Exception e) {
    addMsg(FacesMessage.SEVERITY_ERROR, "Ops", String.format("Isto não deveria acontecer: %s", e.getMessage()));
  }

  public void clearCobrancas() {
    cobrancas = null;
  }

  public void gerarCobrancas() {
    try {
      if (validarObrigatorios()) {
        cobrancas = service.gerarCobrancas(DateUtility.monthRange(vigencia), vencimento, previa);
      }
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void buscarCobrancas() {
    try {
      if (validarObrigatorios()) {
        cobrancas = service.buscarCobrancas(DateUtility.monthRange(vigencia), vencimento);
      }
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void gravarSelecionados() {
    try {
      cobrancas = service.gravarCobrancas(selectedCobrancas);
      addMsg(FacesMessage.SEVERITY_INFO, "Sucesso", "Registros atualizados");
      selectedCobrancas = null;
    } catch (LanceBusinessException lbe) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", lbe.getMsg());
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public StreamedContent getDescritivos() {
    if (selectedCobrancas == null || selectedCobrancas.length == 0) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione pelo menos uma cobrança para gerar o descritivo.");
      return null;
    }
    return relatorioService.descritivos(selectedCobrancas, sessionCtrl.getUser());
  }

  public StreamedContent getArquivoRemessa() {
    try {
      if (selectedCobrancas != null && selectedCobrancas.length > 0) {
        return arquivoService.gerarArquivo(Arrays.asList(selectedCobrancas));
      }
    } catch (LanceBusinessException lbe) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", lbe.getMsg());
    } catch (Exception e) {
      addErrorMessage(e);
    }
    return null;
  }

  public StreamedContent descritivoGeral(Cobranca cobranca) {
    try {
      return relatorioService.descritivoGeral(cobranca, sessionCtrl.getUser());
    } catch (Exception e) {
      addErrorMessage(e);
    }
    return null;
  }

  public StreamedContent descritivo(Cobranca cobranca) {
    try {
      return relatorioService.descritivo(cobranca, sessionCtrl.getUser());
    } catch (Exception e) {
      addErrorMessage(e);
    }
    return null;
  }

  public Boolean getPrevia() {
    return previa;
  }

  public void setPrevia(Boolean previa) {
    this.previa = previa;
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
