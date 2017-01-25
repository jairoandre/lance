package br.com.vah.lance.controller;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.dto.CobrancaDTO;
import br.com.vah.lance.entity.dbamv.DescontoAcrescimo;
import br.com.vah.lance.entity.usrdbvah.Cobranca;
import br.com.vah.lance.entity.usrdbvah.ItemCobranca;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.service.ArquivoRemessaService;
import br.com.vah.lance.service.CobrancaService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.RelatorioService;
import br.com.vah.lance.util.DateUtility;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
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

  private DescontoAcrescimo descontoAcrescimo;

  private Boolean ocultarRecebidos = false;

  private Boolean exibirCancelados = false;

  private Cobranca[] selectedCobrancas;

  private CobrancaDTO[] selectedCobrancaDTOs;

  private Date vigencia = new Date();

  private Integer vencimento;

  private List<Cobranca> cobrancas;

  private List<CobrancaDTO> cobrancaDTOs;

  private Cobranca cobranca;

  private Integer qtdSelecionados;

  private BigDecimal totalSelecionados = BigDecimal.ZERO;

  private Date dataRecebimento;

  private Boolean showRecebimentosDlg = false;

  private Boolean showRecebimentoDlg = false;

  private List<DescontoAcrescimo> descontosAcrescimos;

  private BigDecimal multaAcrescimo = BigDecimal.ZERO;

  private BigDecimal totalRecebimento;

  private Date dataCompensado;

  private boolean validarObrigatorios() {
    if (vigencia == null) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a vigência");
      return false;
    }
    return true;
  }

  public void updateTotalSelecionados() {
    totalSelecionados = BigDecimal.ZERO;

    if (selectedCobrancas != null) {
      for (Cobranca cobranca : selectedCobrancas) {
        totalSelecionados = totalSelecionados.add(cobranca.getValor());
      }
    }
  }

  public void updateTotalSelecionadosDTO() {
    totalSelecionados = BigDecimal.ZERO;

    if (selectedCobrancaDTOs != null) {
      for (CobrancaDTO dto : selectedCobrancaDTOs) {
        totalSelecionados = totalSelecionados.add(dto.getCobranca().getValor()).add(dto.getMulta());
      }
    }
  }

  public void clearCobrancas() {
    cobrancas = null;
  }

  public void gerarCobrancas() {
    try {
      if (validarObrigatorios()) {
        selectedCobrancas = null;
        cobrancas = service.gerarCobrancas(DateUtility.monthRange(vigencia), vencimento);
      }
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void buscarCobrancas() {
    try {
      if (validarObrigatorios()) {
        selectedCobrancas = null;
        cobrancas = service.buscarCobrancas(DateUtility.monthRange(vigencia), vencimento, ocultarRecebidos, exibirCancelados);
      }
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void cancelarCobranca(Cobranca cobranca) {
    try {
      service.cancelarCobranca(cobranca);
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Cobrança Nº. %d cancelada.", cobranca.getId()));
      addMsg(msg, false);
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void gravarSelecionados() {
    try {
      cobrancas = service.gravarCobrancas(selectedCobrancas);
      addMsg(FacesMessage.SEVERITY_INFO, "Sucesso", "Cobrança(s) gravada(s)");
      // buscarCobrancas();
      selectedCobrancas = null;
    } catch (LanceBusinessException lbe) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", lbe.getMsg());
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public StreamedContent getProtocoloEntrega() {
    if (selectedCobrancas == null || selectedCobrancas.length == 0) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione pelo menos uma cobrança.");
      return null;
    }
    return relatorioService.getProtocoloEntrega(selectedCobrancas, vigencia);
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

  public StreamedContent getDescritivoCobrancas() {
    try {
      if (selectedCobrancas != null && selectedCobrancas.length > 0) {
        return arquivoService.gerarDescritivo(Arrays.asList(selectedCobrancas));
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
      for (ItemCobranca item : cobranca.getDescritivo()) {
        if (item.getServico().getType().equals(TipoServicoEnum.COLETA_INFECTANTE)) {
          return relatorioService.descritivoGeral(cobranca, sessionCtrl.getUser());
        }
      }
      return relatorioService.descritivo(cobranca, sessionCtrl.getUser());
    } catch (Exception e) {
      addErrorMessage(e);
    }
    return null;
  }

  public void preNotaFiscal(Cobranca cobranca) {
    this.cobranca = cobranca;
  }

  public void salvarContaReceber() {
    try {
      service.salvarNotaFiscal(cobranca);
      addMsg(FacesMessage.SEVERITY_INFO, "Informação", "Registros atualizados");
    } catch (LanceBusinessException lbe) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", lbe.getMsg());
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void receberSelecionados() {
    Map<String, Object> params = new HashMap<>();
    params.put(CobrancaService.USUARIO, sessionCtrl.getUser().getTitle());
    params.put(CobrancaService.DATA_BAIXA, dataRecebimento);

    if (multaAcrescimo != null && !BigDecimal.ZERO.equals(multaAcrescimo)) {
      params.put(CobrancaService.MULTA_ACRESCIMO, multaAcrescimo);
    }

    List<Cobranca> cobrancas = new ArrayList<>(Arrays.asList(selectedCobrancas));
    try {
      service.receberCobrancas(cobrancas, params, false);
      buscarCobrancas();
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Recebimento efetuado com sucesso"), false);
      showRecebimentosDlg = false;
    } catch (LanceBusinessException lbe) {
      addErrorMessage(lbe);
    } catch (Exception e) {
      addErrorMessage(e);
    }

  }

  public void receberCobranca() {
    selectedCobrancas = new Cobranca[1];
    selectedCobrancas[0] = cobranca;
    receberSelecionados();
  }

  public void receberCobrancaDTOs() {
    boolean noValidationErrors = true;
    if (dataCompensado == null) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a data de compensação da cobrança");
      noValidationErrors = false;
    }
    if (selectedCobrancaDTOs == null || selectedCobrancaDTOs.length == 0) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione ao menos uma cobrança.");
      noValidationErrors = false;
    }
    if (noValidationErrors) {
      try {
        service.receberCobrancasDTOs(selectedCobrancaDTOs, sessionCtrl.getUser().getLogin(), dataCompensado);
        addMsg(FacesMessage.SEVERITY_INFO, "Sucesso", "Cobranças baixadas com sucesso!");
        for (CobrancaDTO cobrancaDTO : selectedCobrancaDTOs) {
          cobrancaDTOs.remove(cobrancaDTO);
        }
        selectedCobrancaDTOs = null;
        mensagens = null;
      } catch (Exception e) {
        addErrorMessage(e);
      }
    }
  }

  public void fecharModalRecebimento() {
    showRecebimentosDlg = false;
    showRecebimentoDlg = false;
  }

  public void preReceberCobranca(Cobranca cobranca) {
    this.cobranca = cobranca;
    multaAcrescimo = BigDecimal.ZERO;
    showRecebimentoDlg = true;
    descontosAcrescimos = new ArrayList<>();
    totalRecebimento = cobranca.getValor();
  }

  public void atualizarTotalReceb() {
    totalRecebimento = cobranca.getValor().add(multaAcrescimo);
  }

  public void preReceberSelecionados() {
    qtdSelecionados = 0;
    totalSelecionados = BigDecimal.ZERO;
    multaAcrescimo = null;
    showRecebimentosDlg = false;
    if (selectedCobrancas != null && selectedCobrancas.length > 0) {
      RequestContext.getCurrentInstance().addCallbackParam("showRecebimentosDlg", true);
      qtdSelecionados = selectedCobrancas.length;
      for (Cobranca cobranca : selectedCobrancas) {
        totalSelecionados = totalSelecionados.add(cobranca.getValor());
      }
      showRecebimentosDlg = true;
    } else {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Nenhuma cobrança selecionada.");
    }
  }


  public void cancelarRecebimento() {
    try {
      service.cancelarRecebimento(cobrancaToCancel);
      addMsg(FacesMessage.SEVERITY_INFO, "Informação", String.format("Recebimento para a cobrança nº %d cancelada com sucesso!", cobrancaToCancel.getId()));
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  private List<String> mensagens;

  public List<String> getMensagens() {
    return mensagens;
  }

  private List<Cobranca> confirmadas;

  public List<Cobranca> getConfirmadas() {
    return this.confirmadas;
  }

  public void setMensagens(List<String> mensagens) {
    this.mensagens = mensagens;
  }

  public void uploadArquivoRetorno(FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    if (file != null) {
      try {
        Map<String, Object> processamento = service.processarArquivoRetorno(file.getContents());
        mensagens = (List<String>) processamento.get(CobrancaService.MENSAGENS);
        confirmadas = (List<Cobranca>) processamento.get(CobrancaService.CONFIRMADAS);
        cobrancaDTOs = (List<CobrancaDTO>) processamento.get(CobrancaService.COBRANCAS);
      } catch (Exception e) {
        addErrorMessage(e);
      }
    }
  }

  public void criarMovimentacoes(Cobranca cobranca) {
    try {
      service.criarMovimentacoes(cobranca);
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Movimentação criadas"), false);
    } catch (Exception e) {
      addErrorMessage(e);
    }

  }


  public void addDescontoAcrescimo() {
    descontosAcrescimos.add(descontoAcrescimo);
    this.descontoAcrescimo = null;
  }

  public Boolean getOcultarRecebidos() {
    return ocultarRecebidos;
  }

  public void setOcultarRecebidos(Boolean ocultarRecebidos) {
    this.ocultarRecebidos = ocultarRecebidos;
  }

  public DescontoAcrescimo getDescontoAcrescimo() {
    return descontoAcrescimo;
  }

  public void setDescontoAcrescimo(DescontoAcrescimo descontoAcrescimo) {
    this.descontoAcrescimo = descontoAcrescimo;
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

  public Cobranca getCobranca() {
    return cobranca;
  }

  public void setCobranca(Cobranca cobranca) {
    this.cobranca = cobranca;
  }

  public List<CobrancaDTO> getCobrancaDTOs() {
    return cobrancaDTOs;
  }

  public CobrancaDTO[] getSelectedCobrancaDTOs() {
    return selectedCobrancaDTOs;
  }

  public void setSelectedCobrancaDTOs(CobrancaDTO[] selectedCobrancaDTOs) {
    this.selectedCobrancaDTOs = selectedCobrancaDTOs;
  }

  public Integer getQtdSelecionados() {
    return qtdSelecionados;
  }

  public void setQtdSelecionados(Integer qtdSelecionados) {
    this.qtdSelecionados = qtdSelecionados;
  }

  public BigDecimal getTotalSelecionados() {
    return totalSelecionados;
  }

  public void setTotalSelecionados(BigDecimal totalSelecionados) {
    this.totalSelecionados = totalSelecionados;
  }

  public Date getDataRecebimento() {
    return dataRecebimento;
  }

  public void setDataRecebimento(Date dataRecebimento) {
    this.dataRecebimento = dataRecebimento;
  }

  public Boolean getShowRecebimentosDlg() {
    return showRecebimentosDlg;
  }

  public void setShowRecebimentosDlg(Boolean showRecebimentosDlg) {
    this.showRecebimentosDlg = showRecebimentosDlg;
  }

  public Boolean getShowRecebimentoDlg() {
    return showRecebimentoDlg;
  }

  public void setShowRecebimentoDlg(Boolean showRecebimentoDlg) {
    this.showRecebimentoDlg = showRecebimentoDlg;
  }

  public List<DescontoAcrescimo> getDescontosAcrescimos() {
    return descontosAcrescimos;
  }

  public void setDescontosAcrescimos(List<DescontoAcrescimo> descontosAcrescimos) {
    this.descontosAcrescimos = descontosAcrescimos;
  }

  public BigDecimal getMultaAcrescimo() {
    return multaAcrescimo;
  }

  public void setMultaAcrescimo(BigDecimal multaAcrescimo) {
    this.multaAcrescimo = multaAcrescimo;
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

  private String confirmarCancelamentoRecebMsg = "";

  private String cancelamentoRecebAnwser;

  private Cobranca cobrancaToCancel;

  public void preExibirDescritivo(Cobranca cobranca) {
    this.cobranca = cobranca;
  }

  public List<ItemCobranca> getDescritivoOrdenado() {
    if (cobranca != null && cobranca.getDescritivo() != null) {
      List<ItemCobranca> ordenado = new ArrayList<>(cobranca.getDescritivo());
      Collections.sort(ordenado, new Comparator<ItemCobranca>() {
        @Override
        public int compare(ItemCobranca o1, ItemCobranca o2) {
          return o1.getServico().getTitle().compareTo(o2.getServico().getTitle());
        }
      });
      return ordenado;
    }
    return null;
  }

  public void preCancelarRecebimento(Cobranca cobranca) {
    cancelamentoRecebAnwser = "";
    cobrancaToCancel = cobranca;
    StringBuilder builder = new StringBuilder();
    builder.append("Cancelar recebimento da cobrança Nº <b>");
    builder.append(cobranca.getId());
    builder.append("</b> de <b>R$ ");
    builder.append(new DecimalFormat("#,##0.00").format(cobranca.getValor()));
    builder.append("</b>?<br/>");
    builder.append("Se sim, digite <b>CANCELAR</b>.");
    confirmarCancelamentoRecebMsg = builder.toString();
  }

  public void upperCaseAnwser() {
    cancelamentoRecebAnwser = cancelamentoRecebAnwser.toUpperCase();
  }

  public String getConfirmarCancelamentoRecebMsg() {
    return confirmarCancelamentoRecebMsg;
  }

  public String getCancelamentoRecebAnwser() {
    return cancelamentoRecebAnwser;
  }

  public void setCancelamentoRecebAnwser(String cancelamentoRecebAnwser) {
    this.cancelamentoRecebAnwser = cancelamentoRecebAnwser;
  }

  public Cobranca getCobrancaToCancel() {
    return cobrancaToCancel;
  }

  public void setCobrancaToCancel(Cobranca cobrancaToCancel) {
    this.cobrancaToCancel = cobrancaToCancel;
  }

  public BigDecimal getTotalRecebimento() {
    return totalRecebimento;
  }

  public void setTotalRecebimento(BigDecimal totalRecebimento) {
    this.totalRecebimento = totalRecebimento;
  }

  public Boolean getExibirCancelados() {
    return exibirCancelados;
  }

  public void setExibirCancelados(Boolean exibirCancelados) {
    this.exibirCancelados = exibirCancelados;
  }

  public Date getDataCompensado() {
    return dataCompensado;
  }

  public void setDataCompensado(Date dataCompensado) {
    this.dataCompensado = dataCompensado;
  }
}
