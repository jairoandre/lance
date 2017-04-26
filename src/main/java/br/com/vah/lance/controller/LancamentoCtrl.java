package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.constant.RolesEnum;
import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.*;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.service.*;
import br.com.vah.lance.util.VahUtils;
import com.opencsv.CSVReader;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class LancamentoCtrl extends AbstractController<Lancamento> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  LancamentoService service;

  private
  @Inject
  SessionCtrl sessionCtrl;

  private
  @Inject
  ServicoService servicoService;

  private
  @Inject
  RelatorioService relatorioService;

  private
  @Inject
  CobrancaService cobrancaService;

  private List<ContaReceber> contasLancamento;

  private List<Lancamento> entries;

  private List<LancamentoValor> lancamentoValors;

  private String vigencia;

  private List groupValues;

  private Long serviceId;

  private Comentario comentario;

  private Date searchMonth = new Date();

  private String groupDateStr;

  private Boolean sharedPerArea = false;

  public static final String[] RELATIONS = {"meterValues", "values", "contasReceber"};

  private Map<MedidorConsumo, BigDecimal> outPeakValues = new HashMap<>();

  private Map<MedidorConsumo, BigDecimal> peakValues = new HashMap<>();

  private BigDecimal ammoutVah = BigDecimal.ZERO;

  private BigDecimal ammountProviders = BigDecimal.ZERO;

  private BigDecimal ammountClinics = BigDecimal.ZERO;

  private BigDecimal ammountShopping = BigDecimal.ZERO;

  private BigDecimal taxProviders = BigDecimal.ZERO;

  private BigDecimal taxClinics = BigDecimal.ZERO;

  private BigDecimal taxShopping = BigDecimal.ZERO;

  private List<String> ignoredMeters = new ArrayList<>();

  private List<String> ignoredCSV = new ArrayList<>();

  private List<ContaReceber> contasReceberToAdd = new ArrayList<>();

  private Integer diaAtual;

  private Date parsedVigencia;

  public void onLoadContaReceber() {
    getLogger().info("Load params");
    if (getId() != null) {
      try {
        Lancamento lancamento = service.initializeLists(getId(), false, true, false, false);
        contasLancamento = lancamento.getContasReceber();
        setItem(lancamento);
      } catch (Exception e) {
        addMsg(FacesMessage.SEVERITY_ERROR, "Aviso", "Erro na recuperação do lançamento");
      }
    }
  }

  public void buscar() {
    entries = service.retrieveEntriesForUser(sessionCtrl.getUser().getId(), VahUtils.getDateRangeForThisMonth());
    initLazyModel(service, RELATIONS);
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    Date today = new Date();
    SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
    SimpleDateFormat sdfVigencia = new SimpleDateFormat("MMyyyy");
    try {
      diaAtual = Integer.valueOf(sdfDia.format(today));
      if (vigencia == null) {
        vigencia = sdfVigencia.format(today);
      }
      parsedVigencia = sdfVigencia.parse(vigencia);
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public void filterByDate() {
    Calendar cld = GregorianCalendar.getInstance();
    cld.setTime(searchMonth);
    cld.add(Calendar.DAY_OF_YEAR, 15);
    entries = service.retrieveEntriesForUser(sessionCtrl.getUser().getId(), VahUtils.getDateRange(cld.getTime()));
  }

  public List<ContaReceber> getContasLancamento() {
    return contasLancamento;
  }

  public void setContasLancamento(List<ContaReceber> contasLancamento) {
    this.contasLancamento = contasLancamento;
  }

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
    return "lancamento";
  }

  public Date getSearchMonth() {
    return searchMonth;
  }

  public void setSearchMonth(Date searchMonth) {
    this.searchMonth = searchMonth;
  }

  @Override
  public String getEntityName() {
    return "lançamento";
  }

  public List<Lancamento> getEntries() {
    return entries;
  }

  public void setEntries(List<Lancamento> entries) {
    this.entries = entries;
  }

  public Long getServiceId() {
    return serviceId;
  }

  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
  }

  public String getVigencia() {
    return vigencia;
  }

  public void setVigencia(String vigencia) {
    this.vigencia = vigencia;
  }

  public Comentario getComentario() {
    return comentario;
  }

  public void setComentario(Comentario comentario) {
    this.comentario = comentario;
  }

  public String getGroupDateStr() {
    return groupDateStr;
  }

  public void setGroupDateStr(String groupDateStr) {
    this.groupDateStr = groupDateStr;
  }

  public List getGroupValues() {
    return groupValues;
  }

  public void setGroupValues(List groupValues) {
    this.groupValues = groupValues;
  }

  public BigDecimal getAmmoutVah() {
    return ammoutVah;
  }

  public BigDecimal getAmmountProviders() {
    return ammountProviders;
  }

  public BigDecimal getAmmountClinics() {
    return ammountClinics;
  }

  public BigDecimal getAmmountShopping() {
    return ammountShopping;
  }

  public BigDecimal getTaxProviders() {
    return taxProviders;
  }

  public BigDecimal getTaxClinics() {
    return taxClinics;
  }

  public BigDecimal getTaxShopping() {
    return taxShopping;
  }

  public Boolean getSharedPerArea() {
    return sharedPerArea;
  }

  public String validate(Lancamento item) {
    return "/pages/lancamento/validate.xhtml?faces-redirect=true&id=" + item.getId() + "&editing=true";
  }


  public void adicionarNovasEntradas() {
    Lancamento lancamentoAtualizado = service.addNovosLancamentos(getItem());
    setItem(lancamentoAtualizado);
    lancamentoValors = new ArrayList<>(lancamentoAtualizado.getValues());
  }


  /**
   * Carrega as informações do lançamento
   */
  @Override
  public void onLoad() {
    super.onLoad();
    SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
    try {
      if (serviceId != null && getItem().getId() == null) {
        parsedVigencia = sdf.parse(vigencia);
        setItem(service.prepareNewEntry(sessionCtrl.getUser().getId(), serviceId, parsedVigencia));
        service.computeInitialValues(getItem());
      } else if (getItem().getId() != null) {
        setItem(service.initializeLists(getItem()));
      }
      TipoServicoEnum serviceType = getItem().getServico().getType();
      getItem().getMeterValues();
      sharedPerArea = TipoServicoEnum.CP.equals(serviceType) || TipoServicoEnum.CR.equals(serviceType) || TipoServicoEnum.CRE.equals(serviceType);
      comentario = createComment();
      lancamentoValors = new ArrayList<>(getItem().getValues());
      if (sharedPerArea) {
        shareAmmount();
      }
    } catch (Exception e) {
      addErrorMessage(e);
    }


  }

  public Boolean verificarDataLimite(Lancamento lancamento) {
    Date today = new Date();
    return lancamento.getEffectiveOn().after(today) ? true : (diaAtual <= lancamento.getServico().getDiaLimite());
  }

  private List<Lancamento> oldEntries;

  public List<Lancamento> getOldEntries() {
    return oldEntries;
  }

  private Date dtLancamentoConRec;

  private Date dtVencConRec;

  public Date getDtLancamentoConRec() {
    return dtLancamentoConRec;
  }

  public void setDtLancamentoConRec(Date dtLancamentoConRec) {
    this.dtLancamentoConRec = dtLancamentoConRec;
  }

  public Date getDtVencConRec() {
    return dtVencConRec;
  }

  public void setDtVencConRec(Date dtVencConRec) {
    this.dtVencConRec = dtVencConRec;
  }

  public void onLoadValidation() {

    onLoad();

    oldEntries = service.listOldEntries(getItem().getServico());

    dtLancamentoConRec = new Date();

    dtVencConRec =
        VahUtils.calcNextMonthDate(dtLancamentoConRec, getItem().getServico().getDiaVencimento());

  }

  public void loadGroupByClient() {
    if (groupDateStr != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      try {
        Date date = sdf.parse(groupDateStr);
        entries = service.retrieveEntriesForUser(null, VahUtils.getDateRange(date));
        Map<Fornecedor, Map<Servico, BigDecimal>> values = service.groupByClient(entries);
        Map<Fornecedor, List<Map.Entry<Servico, BigDecimal>>> mapOfList = new HashMap<>();
        for (Fornecedor client : values.keySet()) {
          mapOfList.put(client, new ArrayList(values.get(client).entrySet()));
        }
        groupValues = new ArrayList(mapOfList.entrySet());

      } catch (Exception e) {
        // Exceção
      }
    }
  }

  public void computeValues() {
    service.computeValues(getItem());
  }

  @Override
  public String edit(Lancamento item) {
    if (item.getId() == null) {
      try {
        return editPage() + String.format("?faces-redirect=true&editing=true&serviceId=%d&vigencia=%s",
            item.getServico().getId(),
            new SimpleDateFormat("MMyyyy").format(item.getEffectiveOn()));
      } catch (Exception e) {
        addErrorMessage(e);
        return "";
      }

    } else {
      return super.edit(item);
    }
  }

  public Comentario createComment() {
    Comentario comentario = new Comentario();
    comentario.setAuthor(sessionCtrl.getUser());
    comentario.setCreatedOn(new Date());
    comentario.setLancamento(getItem());
    return comentario;
  }


  public String addComment() {
    getItem().getComentarios().add(comentario);
    comentario = createComment();
    return null;
  }

  public List<LancamentoValor> getLancamentoValors() {
    return lancamentoValors;
  }

  private
  @Inject
  ContaReceberService contaReceberService;

  public String doValidateSave() {
    try {

      SimpleDateFormat sdf = new SimpleDateFormat("yyMM");

      String numDocPrefix = sdf.format(dtLancamentoConRec);

      numDocPrefix = numDocPrefix + VahUtils.leftPadZeros(getItem().getServico().getId(), 2);

      int count = 0;

      // CRIA UMA CONTA A RECEBER PARA CADA ITEM/SETOR DE LANÇAMENTO
      for (LancamentoValor lancamentoValor : getItem().getValues()) {

        ContaReceber conRecToAdd = service.createContaReceber(lancamentoValor, numDocPrefix, count++, dtLancamentoConRec);

        contasReceberToAdd.add(conRecToAdd);
      }

      List<ContaReceber> persistedContas = contaReceberService.createList(contasReceberToAdd);
      getItem().setContasReceber(persistedContas);
      getItem().setStatus(EstadoLancamentoEnum.V);
      service.update(getItem());
      addMsg(new FacesMessage("Sucesso!", "Registro salvo"), true);
      return back();
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Ops! Erro inesperado: " + e.getMessage()),
          true);
    }
    return null;
  }

  public String doEntrySave() {
    if (sessionCtrl.isUserInRoles(RolesEnum.REGISTER.toString())) {
      getItem().setStatus(EstadoLancamentoEnum.PL);
    } else {
      getItem().setStatus(EstadoLancamentoEnum.L);
    }
    return doSave();
  }

  public String doDenySave() {
    getItem().setStatus(EstadoLancamentoEnum.P);
    return doSave();
  }

  public String doFixSave() {
    getItem().setStatus(EstadoLancamentoEnum.F);
    return doSave();
  }

  public String doModifySave() {
    getItem().setStatus(EstadoLancamentoEnum.M);
    return doSave();
  }

  public List<String> getIgnoredMeters() {
    return ignoredMeters;
  }

  public List<String> getIgnoredCSV() {
    return ignoredCSV;
  }

  public List<ContaReceber> getContasReceberToAdd() {
    return contasReceberToAdd;
  }

  public void setContasReceberToAdd(List<ContaReceber> contasReceberToAdd) {
    this.contasReceberToAdd = contasReceberToAdd;
  }

  /**
   * Realiza o carregamento do arquivo CSV para os medidores
   *
   * @param evt
   */
  public void uploadValues(FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    byte[] data = file.getContents();
    CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(data)), ';');
    try {
      Map<Long, BigDecimal[]> map = new HashMap<>();
      Integer line = 1;
      try {
        for (String[] str : reader.readAll()) {
          Long setor = Long.valueOf(str[0]);
          BigDecimal valueA = new BigDecimal(str[1].replace(',', '.'));

          BigDecimal valueB = BigDecimal.ZERO;
          if (str.length >= 3) {
            valueB = new BigDecimal(str[2].replace(',', '.'));
          }
          line++;
          BigDecimal[] arr = {valueA, valueB};
          map.put(setor, arr);
        }
        Integer importedValues = 0;
        Integer ignoredValues;
        for (LancamentoValor lancamentoValor : getItem().getValues()) {
          Long setor = lancamentoValor.getContratoSetor().getSetor().getId();
          BigDecimal[] csvValues = map.get(setor);
          if (csvValues != null) {
            lancamentoValor.setValueA(csvValues[0]);
            lancamentoValor.setValueB(csvValues[1]);
            importedValues++;
            map.remove(setor);
          }
        }
        computeValues();
        ignoredValues = map.size();
        for (Long code : map.keySet()) {
          ignoredCSV.add(code.toString());
        }
        addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Importação realizada com sucesso: %d importados, %d ignorados.", importedValues, ignoredValues)), false);
      } catch (Exception e) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação: linha %s.", line)), false);
      }
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação:\n%s", e.getMessage())), false);
    }

  }

  /**
   * Realiza o carregamento do arquivo CSV para os medidores
   *
   * @param evt
   */
  public void uploadMeterValues(FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    byte[] data = file.getContents();
    CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(data)), ';');
    try {
      Map<String, BigDecimal[]> map = new HashMap<>();
      Integer line = 1;
      try {
        for (String[] str : reader.readAll()) {
          String code = str[0];
          BigDecimal prevRead = new BigDecimal(str[1].replace(',', '.'));
          BigDecimal currRead = new BigDecimal(str[2].replace(',', '.'));
          BigDecimal[] array = {prevRead, currRead};
          map.put(code, array);
          line++;
        }
        Integer importedValues = 0;
        Integer ignoredValues;
        for (LancamentoMedidorValor lancamentoMeter : getItem().getMeterValues()) {
          String code = lancamentoMeter.getMedidorConsumo().getCode();
          BigDecimal[] csvValues = map.get(code);
          if (csvValues != null) {
            lancamentoMeter.setPreviousValue(csvValues[0]);
            lancamentoMeter.setCurrentValue(csvValues[1]);
            importedValues++;
            map.remove(code);
          }
        }
        ignoredValues = map.size();
        for (String code : map.keySet()) {
          ignoredMeters.add(code);
        }
        addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Importação realizada com sucesso: %d importados, %d ignorados.", importedValues, ignoredValues)), false);
      } catch (Exception e) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação: linha %s.", line)), false);
      }
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação:\n%s", e.getMessage())), false);
    }

  }

  /**
   * Realiza o rateio entre os grupos condominiais
   */
  public void shareAmmount() {
    if (getItem().getAmmountToShare() != null && getItem().getServicoValor() != null) {
      ServicoValor servicoValor = getItem().getServicoValor();
      ammoutVah = getItem().getAmmountToShare().multiply(servicoValor.getValueD());
      ammountProviders = getItem().getAmmountToShare().multiply(servicoValor.getValueA());
      ammountClinics = getItem().getAmmountToShare().multiply(servicoValor.getValueB());
      ammountShopping = getItem().getAmmountToShare().multiply(servicoValor.getValueC());
      if (!BigDecimal.ZERO.equals(getItem().getTotalAreaA())) {
        taxProviders = ammountProviders.divide(getItem().getTotalAreaA(), 10, BigDecimal.ROUND_HALF_UP);
      }
      if (!BigDecimal.ZERO.equals(getItem().getTotalAreaB())) {
        taxClinics = ammountClinics.divide(getItem().getTotalAreaB(), 10, BigDecimal.ROUND_HALF_UP);
      }
      if (!BigDecimal.ZERO.equals(getItem().getTotalAreaC())) {
        taxShopping = ammountShopping.divide(getItem().getTotalAreaC(), 10, BigDecimal.ROUND_HALF_UP);
      }
    }
  }

  /**
   * Realiza o rateio por tipo de unidade condominial
   */
  public void fillSharedFields() {
    for (LancamentoValor lancamentoValor : getItem().getValues()) {
      SetorDetalhe setorDetalhe = lancamentoValor.getContratoSetor().getSetor().getSetorDetalhe();
      if (setorDetalhe != null) {
        BigDecimal area = setorDetalhe.getArea();
        if (area != null) {
          switch (setorDetalhe.getType()) {
            case TERCEIROS:
              lancamentoValor.setValueA(area.multiply(taxProviders));
              break;
            case CONSULTORIOS:
              lancamentoValor.setValueA(area.multiply(taxClinics));
              break;
            case SHOPPING:
              lancamentoValor.setValueA(area.multiply(taxShopping));
              break;
            default:
              break;
          }
        }
      }
    }
    computeValues();
  }

  public void updateDtVencimento() {
    dtVencConRec = VahUtils.calcNextMonthDate(dtLancamentoConRec, getItem().getServico().getDiaVencimento());
  }

  /**
   * Atualiza as diferenças de leituras dos medidores
   */
  public void updateMeterTotalValues() {
    outPeakValues = new HashMap<>();
    peakValues = new HashMap<>();
    ServicoValor servicoValor = getItem().getServicoValor();
    BigDecimal outPeakValue = servicoValor.getValueA();
    BigDecimal outPeakShare = servicoValor.getValueB();
    BigDecimal peakValue = servicoValor.getValueC();
    BigDecimal peakShare = servicoValor.getValueD();

    for (LancamentoMedidorValor meterValue : getItem().getMeterValues()) {
      BigDecimal factor = meterValue.getMedidorConsumo().getFactor();
      BigDecimal delta = meterValue.getCurrentValue().subtract(meterValue.getPreviousValue());
      BigDecimal meterOutPeakValue = delta.multiply(outPeakShare).multiply(outPeakValue).multiply(factor);
      BigDecimal meterPeakValue = delta.multiply(peakShare).multiply(peakValue).multiply(factor);
      outPeakValues.put(meterValue.getMedidorConsumo(), meterOutPeakValue);
      peakValues.put(meterValue.getMedidorConsumo(), meterPeakValue);
    }

  }


  /**
   * Preenche os valores monetários dos setores de acordo com as leituras dos medidores.
   */
  public void fillSetorMeterFields() {
    updateMeterTotalValues();
    for (LancamentoValor lancamentoValor : getItem().getValues()) {
      lancamentoValor.setValueA(BigDecimal.ZERO);
      Setor setorDetail = lancamentoValor.getContratoSetor().getSetor();
      Set<SetorMedidorConsumo> lancamentoValueMeters = setorDetail.getMeters();
      for (SetorMedidorConsumo setorMedidorConsumo : lancamentoValueMeters) {
        MedidorConsumo itemMeter = setorMedidorConsumo.getMedidorConsumo();
        BigDecimal percent = setorMedidorConsumo.getPercent();
        BigDecimal meterOutPeakValue = outPeakValues.get(itemMeter);
        BigDecimal meterPeakValue = peakValues.get(itemMeter);
        if (meterOutPeakValue != null && meterPeakValue != null) {
          lancamentoValor.setValueA(lancamentoValor.getValueA().add(meterOutPeakValue.multiply(percent)).add(meterPeakValue.multiply(percent)));
        }
      }
    }
    computeValues();
  }

  public void carregarValoresAnteriores() {
    try {
      setItem(service.carregarValoresAnteriores(getItem()));
    } catch (LanceBusinessException lbe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", lbe.getMsg()), true);
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()), true);
    }
  }

  public void salvarContaReceber() {
    try {
      cobrancaService.salvarNotaFiscal(getItem());
      addMsg(FacesMessage.SEVERITY_INFO, "Informação", "Registros atualizados");
    } catch (LanceBusinessException lbe) {
      addMsg(FacesMessage.SEVERITY_WARN, "Atenção", lbe.getMsg());
    } catch (Exception e) {
      addErrorMessage(e);
    }
  }

  public StreamedContent relatorioContabil(Lancamento lancamento) {
    return relatorioService.relatorioBalanco(service.initializeLists(lancamento), sessionCtrl.getUser());
  }

  private Boolean showContasReceberDlg = false;

  public void preOpenContasReceberDlg(Lancamento lancamento) {
    showContasReceberDlg = true;
    setItem(service.initializeLists(lancamento, false, true, false, false));
  }

  public void closeOpenContasReceberDlg() {
    showContasReceberDlg = false;
  }

  public Boolean getShowContasReceberDlg() {
    return showContasReceberDlg;
  }

  public String contasReceberLancamento(Lancamento lancamento) {
    return "/pages/contaReceber/list-for-nf.xhtml" + _FACES_REDIRECT + _ID_PARAM + lancamento.getId();
  }
}
