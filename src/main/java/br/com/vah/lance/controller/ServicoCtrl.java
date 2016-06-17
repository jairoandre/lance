package br.com.vah.lance.controller;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.usrdbvah.Servico;
import br.com.vah.lance.entity.usrdbvah.ServicoValor;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServicoService;
import br.com.vah.lance.util.ViewUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ServicoCtrl extends AbstractController<Servico> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ServicoService service;

  private ServicoValor servicoValor;

  private TipoServicoEnum[] types;

  private static final String[] RELATIONS = {"values"};

  private List<ServicoValor> values;

  private List<Servico> servicos;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    prepareNewServiceValue();
    initLazyModel(service, RELATIONS);
    types = TipoServicoEnum.values();
    getLazyModel().getSearchParams().setOrderBy("title");
  }

  public void prepareNewServiceValue() {
    servicoValor = new ServicoValor();
    Date[] range = ViewUtils.getDateRangeForThisMonth();
    servicoValor.setBeginDate(range[0]);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    values = new ArrayList<>(getItem().getValues());
  }

  public ServicoCtrl() {
  }

  public ServicoCtrl(ServicoService service) {
    this();
    initLazyModel(service, RELATIONS);
  }

  @Override
  public DataAccessService<Servico> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Servico createNewItem() {
    return new Servico();
  }

  @Override
  public String path() {
    return "servico";
  }

  @Override
  public String doSave() {
    return super.doSave();
  }

  public void addValue() {
    servicoValor.setServico(getItem());
    if(service.canAddServiceValue(getItem(), servicoValor)){
      for(ServicoValor servicoValorIterator : getItem().getValues()){
        if(servicoValorIterator.getEndDate() == null){
          servicoValorIterator.setEndDate(servicoValor.getBeginDate());
          break;
        }
      }
      getItem().getValues().add(servicoValor);
      values = new ArrayList<>(getItem().getValues());
      prepareNewServiceValue();
    } else {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Período de vigência já utilizado."), false);
    }
  }

  public void removeValue(ServicoValor value) {
    getItem().getValues().remove(value);
    values = new ArrayList<>(getItem().getValues());
  }

  public String currency(Servico item) {
    return String.format("/pages/servico/currency.xhtml?faces-redirect=true&id=%d&editing=true", item.getId());
  }

  @Override
  public String getEntityName() {
    return "serviço";
  }

  public void setDefaultHistory() {
    System.out.println(getItem().getHistoricoPadrao());
  }

  /**
   * @return the servicoValor
   */
  public ServicoValor getServicoValor() {
    return servicoValor;
  }

  /**
   * @param servicoValor the servicoValor to set
   */
  public void setServicoValor(ServicoValor servicoValor) {
    this.servicoValor = servicoValor;
  }

  /**
   * @return the types
   */
  public TipoServicoEnum[] getTypes() {
    return types;
  }

  /**
   * @param types the types to set
   */
  public void setTypes(TipoServicoEnum[] types) {
    this.types = types;
  }

  /**
   *
   * @return
   */
  public List<ServicoValor> getValues() {
    return values;
  }

  /**
   *
   * @param values
   */
  public void setValues(List<ServicoValor> values) {
    this.values = values;
  }

  public void updatePeakValue(){
    servicoValor.setValueD(BigDecimal.ONE.subtract(servicoValor.getValueB()));
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());
  }

}
