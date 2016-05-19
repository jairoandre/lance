package br.com.vah.lance.controller;

import java.util.*;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.ContractSector;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.ClientService;
import br.com.vah.lance.service.ContractService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.ServiceService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ContractController extends AbstractController<Contract> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ContractService service;

  private
  @Inject
  ClientService clientService;

  private
  @Inject
  ServiceService serviceService;

  private Map<Long, ClientController> clientControllers;

  private Map<Long, ServiceController> serviceControllers;

  private Service serviceBean;

  private List<Service> compulsoryService = new ArrayList<>();

  public static final String[] RELATIONS = {"contractSectors", "subject"};

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    initLazyModel(service, RELATIONS);
    List<Service> allServices = serviceService.findWithNamedQuery(Service.ALL);
    for (Service item : allServices) {
      if (item.getCompulsory()) {
        compulsoryService.add(item);
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

  public List<Long> recordedSectorsId() {
    List<Long> ids = new ArrayList<>();
    for (ContractSector contractSector : getItem().getContractSectors()) {
      ids.add(contractSector.getSector().getId());
    }
    return ids;
  }

  public void onLoadSubject() {
    MvClient subject = getItem().getSubject();
    clientControllers = new HashMap<>();
    serviceControllers = new HashMap<>();
    List<Long> ids = recordedSectorsId();
    if (subject != null) {
      for (MvSector sector : subject.getSectors()) {
        if (!ids.contains(sector.getId())) {
          ContractSector service = new ContractSector();
          service.setSector(sector);
          service.setContract(getItem());
          service.setServices(new LinkedHashSet<>(compulsoryService));
          getItem().getContractSectors().add(service);
        }
        clientControllers.put(sector.getId(), new ClientController(clientService));
        serviceControllers.put(sector.getId(), new ServiceController(serviceService));
      }
    }
  }

  @Override
  public DataAccessService<Contract> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Contract createNewItem() {
    return new Contract();
  }

  @Override
  public String path() {
    return "contract";
  }

  @Override
  public String getEntityName() {
    return "contrato";
  }


  public Map<Long, ClientController> getClientControllers() {
    return clientControllers;
  }

  public Map<Long, ServiceController> getServiceControllers() {
    return serviceControllers;
  }

  /**
   * @return the serviceBean
   */
  public Service getServiceBean() {
    return serviceBean;
  }

  /**
   * @param serviceBean the serviceBean to set
   */
  public void setServiceBean(Service serviceBean) {
    this.serviceBean = serviceBean;
  }

  public void toggleService(ContractSector service) {
    if (service.getServices().contains(serviceBean)) {
      service.getServices().remove(serviceBean);
    } else {
      service.getServices().add(serviceBean);
    }

  }

  @Override
  public void search() {
    super.search();

  }
}
