package br.com.vah.lance.controller;

import java.util.*;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.usrdbvah.Contrato;
import br.com.vah.lance.entity.usrdbvah.ContratoSetor;
import br.com.vah.lance.entity.usrdbvah.Servico;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.service.FornecedorService;
import br.com.vah.lance.service.ContratoService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServicoService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContratoCtrl extends AbstractController<Contrato> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ContratoService service;

  private
  @Inject
  FornecedorService fornecedorService;

  private
  @Inject
  ServicoService servicoService;

  private Map<Long, FornecedorCtrl> clientControllers;

  private Map<Long, ServicoCtrl> serviceControllers;

  private Servico servicoBean;

  private List<Servico> compulsoryServico = new ArrayList<>();

  public static final String[] RELATIONS = {"setores", "contratante"};

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    initLazyModel(service, RELATIONS);
    List<Servico> allServicos = servicoService.findWithNamedQuery(Servico.ALL);
    for (Servico item : allServicos) {
      if (item.getCompulsorio()) {
        compulsoryServico.add(item);
      }
    }
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() != null) {
      onLoadSubject();
    }
  }

  public List<Long> recordedSetoresId() {
    List<Long> ids = new ArrayList<>();
    for (ContratoSetor contratoSetor : getItem().getSetores()) {
      ids.add(contratoSetor.getSetor().getId());
    }
    return ids;
  }

  public void onLoadSubject() {
    Fornecedor contratante = getItem().getContratante();
    clientControllers = new HashMap<>();
    serviceControllers = new HashMap<>();
    List<Long> ids = recordedSetoresId();
    if (contratante != null) {
      for (Setor setor : contratante.getSetores()) {
        if (!ids.contains(setor.getId())) {
          ContratoSetor service = new ContratoSetor();
          service.setSetor(setor);
          service.setContrato(getItem());
          service.setServicos(new LinkedHashSet<>(compulsoryServico));
          getItem().getSetores().add(service);
        }
        clientControllers.put(setor.getId(), new FornecedorCtrl(fornecedorService));
        serviceControllers.put(setor.getId(), new ServicoCtrl(servicoService));
      }
    }
  }

  @Override
  public DataAccessService<Contrato> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Contrato createNewItem() {
    return new Contrato();
  }

  @Override
  public String path() {
    return "contrato";
  }

  @Override
  public String getEntityName() {
    return "contrato";
  }


  public Map<Long, FornecedorCtrl> getClientControllers() {
    return clientControllers;
  }

  public Map<Long, ServicoCtrl> getServiceControllers() {
    return serviceControllers;
  }

  /**
   * @return the servicoBean
   */
  public Servico getServicoBean() {
    return servicoBean;
  }

  /**
   * @param servicoBean the servicoBean to set
   */
  public void setServicoBean(Servico servicoBean) {
    this.servicoBean = servicoBean;
  }

  public void toggleService(ContratoSetor service) {
    if (service.getServicos().contains(servicoBean)) {
      service.getServicos().remove(servicoBean);
    } else {
      service.getServicos().add(servicoBean);
    }

  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());

  }
}
