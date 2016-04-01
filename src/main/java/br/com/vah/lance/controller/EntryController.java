package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.*;
import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;
import br.com.vah.lance.service.ServiceService;
import br.com.vah.lance.util.LanceUtils;
import com.opencsv.CSVReader;
import org.primefaces.event.FileUploadEvent;
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
public class EntryController extends AbstractController<Entry> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  EntryService service;

  private
  @Inject
  LoginController loginController;

  private
  @Inject
  ServiceService serviceService;

  private List<Entry> entries;

  private List<EntryValue> entryValues;

  private List groupValues;

  private Long serviceId;

  private Comment comment;

  private Date searchMonth = new Date();

  private String groupDateStr;

  private Boolean sharedPerArea = false;

  public static final String[] RELATIONS = {"meterValues", "values"};

  private Map<ConsumptionMeter, BigDecimal> outPeakValues = new HashMap<>();

  private Map<ConsumptionMeter, BigDecimal> peakValues = new HashMap<>();

  private BigDecimal ammoutVah = BigDecimal.ZERO;

  private BigDecimal ammountProviders = BigDecimal.ZERO;

  private BigDecimal ammountClinics = BigDecimal.ZERO;

  private BigDecimal ammountShopping = BigDecimal.ZERO;

  private BigDecimal taxProviders = BigDecimal.ZERO;

  private BigDecimal taxClinics = BigDecimal.ZERO;

  private BigDecimal taxShopping = BigDecimal.ZERO;

  private List<String> ignoredMeters = new ArrayList<>();

  @SuppressWarnings({"rawtypes", "unchecked"})
  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    entries = service.retrieveEntriesForUser(loginController.getUser().getId(), LanceUtils.getDateRangeForThisMonth());
    initLazyModel(service, RELATIONS);
  }

  public void filterByDate() {
    Calendar cld = GregorianCalendar.getInstance();
    cld.setTime(searchMonth);
    cld.add(Calendar.DAY_OF_YEAR, 15);
    entries = service.retrieveEntriesForUser(loginController.getUser().getId(), LanceUtils.getDateRange(cld.getTime()));
  }

  @Override
  public DataAccessService<Entry> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Entry createNewItem() {
    return new Entry();
  }

  @Override
  public String path() {
    return "entry";
  }

  public Date getSearchMonth() {
    return searchMonth;
  }

  public void setSearchMonth(Date searchMonth) {
    this.searchMonth = searchMonth;
  }

  @Override
  public String getEntityName() {
    return "setor";
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  public Long getServiceId() {
    return serviceId;
  }

  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
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

  public String validate(Entry item) {
    return "/pages/entry/validate.xhtml?faces-redirect=true&id=" + item.getId() + "&editing=true";
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (serviceId != null && getItem().getId() == null) {
      setItem(service.prepareNewEntry(loginController.getUser().getId(), serviceId));
      service.computeInitialValues(getItem());
    }
    ServiceTypesEnum serviceType = getItem().getService().getType();
    getItem().getMeterValues();
    sharedPerArea = ServiceTypesEnum.CP.equals(serviceType) || ServiceTypesEnum.CR.equals(serviceType) || ServiceTypesEnum.CRE.equals(serviceType);
    comment = createComment();
    entryValues = new ArrayList<>(getItem().getValues());
    if (sharedPerArea) {
      shareAmmount();
    }
  }

  public void loadGroupByClient() {
    if (groupDateStr != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      try {
        Date date = sdf.parse(groupDateStr);
        entries = service.retrieveEntriesForUser(null, LanceUtils.getDateRange(date));
        Map<MvClient, Map<Service, BigDecimal>> values = service.groupByClient(entries);
        Map<MvClient, List<Map.Entry<Service, BigDecimal>>> mapOfList = new HashMap<>();
        for (MvClient client : values.keySet()) {
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
  public String edit(Entry item) {
    if (item.getId() == null) {
      return editPage() + String.format("?faces-redirect=true&editing=true&serviceId=%d",
          item.getService().getId());
    } else {
      return super.edit(item);
    }
  }

  public Comment createComment() {
    Comment comment = new Comment();
    comment.setAuthor(loginController.getUser());
    comment.setCreatedOn(new Date());
    comment.setEntry(getItem());
    return comment;
  }


  public String addComment() {
    getItem().getComments().add(comment);
    comment = createComment();
    return null;
  }

  public List<EntryValue> getEntryValues() {
    return entryValues;
  }

  public String doValideSave() {
    getItem().setStatus(EntryStatusEnum.V);
    return doSave();
  }

  public String doDenySave() {
    getItem().setStatus(EntryStatusEnum.P);
    return doSave();
  }

  public String doFixSave() {
    getItem().setStatus(EntryStatusEnum.F);
    return doSave();
  }

  public String doModifySave() {
    getItem().setStatus(EntryStatusEnum.M);
    return doSave();
  }

  public List<String> getIgnoredMeters() {
    return ignoredMeters;
  }

  /**
   * Realiza o carregamento do arquivo CSV para os medidores
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
          Long sector = Long.valueOf(str[0]);
          BigDecimal valueA = new BigDecimal(str[1].replace(',', '.'));

          BigDecimal valueB = BigDecimal.ZERO;
          if(str.length >= 3) {
            valueB = new BigDecimal(str[2].replace(',', '.'));
          }
          line++;
          BigDecimal[] arr = {valueA, valueB};
          map.put(sector, arr);
        }
        Integer importedValues = 0;
        Integer ignoredValues;
        for (EntryValue entryValue : getItem().getValues()) {
          Long sector = entryValue.getContractSector().getSector().getId();
          BigDecimal[] csvValues = map.get(sector);
          if (csvValues != null) {
            entryValue.setValueA(csvValues[0]);
            entryValue.setValueB(csvValues[1]);
            importedValues++;
            map.remove(sector);
          }
        }
        computeValues();
        ignoredValues = map.size();
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
        for (EntryMeterValue entryMeter : getItem().getMeterValues()) {
          String code = entryMeter.getConsumptionMeter().getCode();
          BigDecimal[] csvValues = map.get(code);
          if (csvValues != null) {
            entryMeter.setPreviousValue(csvValues[0]);
            entryMeter.setCurrentValue(csvValues[1]);
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
    if (getItem().getAmmountToShare() != null && getItem().getServiceValue() != null) {
      ServiceValue serviceValue = getItem().getServiceValue();
      ammoutVah = getItem().getAmmountToShare().multiply(serviceValue.getValueD());
      ammountProviders = getItem().getAmmountToShare().multiply(serviceValue.getValueA());
      ammountClinics = getItem().getAmmountToShare().multiply(serviceValue.getValueB());
      ammountShopping = getItem().getAmmountToShare().multiply(serviceValue.getValueC());
      if (!BigDecimal.ZERO.equals(getItem().getTotalAreaA())) {
        taxProviders = ammountProviders.divide(getItem().getTotalAreaA(), 10, BigDecimal.ROUND_CEILING);
      }
      if (!BigDecimal.ZERO.equals(getItem().getTotalAreaB())) {
        taxClinics = ammountClinics.divide(getItem().getTotalAreaB(), 10, BigDecimal.ROUND_CEILING);
      }
      if (!BigDecimal.ZERO.equals(getItem().getTotalAreaC())) {
        taxShopping = ammountShopping.divide(getItem().getTotalAreaC(), 10, BigDecimal.ROUND_CEILING);
      }
    }
  }

  /**
   * Realiza o rateio por tipo de unidade condominial
   */
  public void fillSharedFields() {
    for (EntryValue entryValue : getItem().getValues()) {
      SectorDetail sectorDetail = entryValue.getContractSector().getSector().getSectorDetail();
      if (sectorDetail != null) {
        BigDecimal area = sectorDetail.getArea();
        if (area != null) {
          switch (sectorDetail.getType()) {
            case TERCEIROS:
              entryValue.setValueA(area.multiply(taxProviders));
              break;
            case CONSULTORIOS:
              entryValue.setValueA(area.multiply(taxClinics));
              break;
            case SHOPPING:
              entryValue.setValueA(area.multiply(taxShopping));
              break;
            default:
              break;
          }
        }
      }
    }
    computeValues();
  }

  /**
   * Atualiza as diferenças de leituras dos medidores
   */
  public void updateMeterTotalValues() {
    outPeakValues = new HashMap<>();
    peakValues = new HashMap<>();
    ServiceValue serviceValue = getItem().getServiceValue();
    BigDecimal outPeakValue = serviceValue.getValueA();
    BigDecimal outPeakShare = serviceValue.getValueB();
    BigDecimal peakValue = serviceValue.getValueC();
    BigDecimal peakShare = serviceValue.getValueD();

    for (EntryMeterValue meterValue : getItem().getMeterValues()) {
      BigDecimal factor = meterValue.getConsumptionMeter().getFactor();
      BigDecimal delta = meterValue.getCurrentValue().subtract(meterValue.getPreviousValue());
      BigDecimal meterOutPeakValue = delta.multiply(outPeakShare).multiply(outPeakValue).multiply(factor);
      BigDecimal meterPeakValue = delta.multiply(peakShare).multiply(peakValue).multiply(factor);
      outPeakValues.put(meterValue.getConsumptionMeter(), meterOutPeakValue);
      peakValues.put(meterValue.getConsumptionMeter(), meterPeakValue);
    }

  }


  /**
   * Preenche os valores monetários dos setores de acordo com as leituras dos medidores.
   *
   */
  public void fillSectorMeterFields() {
    updateMeterTotalValues();
    for (EntryValue entryValue : getItem().getValues()) {
      entryValue.setValueA(BigDecimal.ZERO);
      MvSector sectorDetail = entryValue.getContractSector().getSector();
      Set<SectorConsumptionMeter> entryValueMeters = sectorDetail.getMeters();
      for (SectorConsumptionMeter sectorConsumptionMeter : entryValueMeters) {
        ConsumptionMeter itemMeter = sectorConsumptionMeter.getConsumptionMeter();
        BigDecimal percent = sectorConsumptionMeter.getPercent();
        BigDecimal meterOutPeakValue = outPeakValues.get(itemMeter);
        BigDecimal meterPeakValue = peakValues.get(itemMeter);
        if (meterOutPeakValue != null && meterPeakValue != null) {
          entryValue.setValueA(entryValue.getValueA().add(meterOutPeakValue.multiply(percent)).add(meterPeakValue.multiply(percent)));
        }
      }
    }
    computeValues();
  }
}
