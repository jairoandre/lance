package br.com.vah.lance.service;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.*;
import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.util.LanceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class EntryService extends DataAccessService<Entry> {

  private
  @Inject
  ContractService contractService;

  private
  @Inject
  ServiceService serviceService;

  private
  @Inject
  UserService userService;

  private
  @Inject
  ConsumptionMeterService meterService;

  public EntryService() {
    super(Entry.class);
  }

  /**
   * Recupera as entradas de lançamentos já persistidas. Quando necessário cria novas entradas (em memória) para lançamentos pendentes.
   *
   * @param userId
   * @param range
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Entry> retrieveEntriesForUser(Long userId, Date[] range) {

    List<Entry> entries = new ArrayList<>();

    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", range[1]);

		/*
     * Recupera os contratos vigentes para a data
		 */
    List<Contract> contracts = validesContracts(range[1]);

    Map<String, Object> entriesParams = new LinkedHashMap<>();
    entriesParams.put("begin", range[0]);
    entriesParams.put("end", range[1]);

    Set<Service> userServices = null;

    if (userId != null) {
      userServices = userService.find(userId).getServices();
    }


    List<Entry> currentEntries;

    /**
     * Quando o usuário não possuir serviços atrelados, exibe todos. (Controladoria)
     */
    if (userServices == null || userServices.isEmpty()) {
      currentEntries = findWithNamedQuery(Entry.BY_PERIOD, entriesParams);
    } else {
      /**
       * Recupera as entradas já lançadas para os serviços atrelados ao usuário e a data
       */
      entriesParams.put("services", userServices);
      currentEntries = findWithNamedQuery(Entry.BY_PERIOD_AND_SERVICE, entriesParams);

      /**
       * Verifica se o período consultado é do mês vigente, caso seja, monta lançamentos que ainda não foram lançados.
       */
      if (LanceUtils.checkBetween(new Date(), range[0], range[1])) {

        Set<Service> includedServices = new HashSet<>();

        /**
         * Realiza um mapeamento dos serviços que já foram lançados
         */
        for (Entry entry : currentEntries) {
          includedServices.add(entry.getService());
        }

        /**
         * Para cada contrato vigente, verifica se o mesmo possui serviços
         * associados ao usuário. Caso possua, cria uma instância de lançamento
         * para o serviço.
         */
        for (Contract contract : contracts) {

          for (ContractSector contractSector : contract.getContractSectors()) {

            for (Service service : contractSector.getServices()) {

              if (userServices.contains(service)) {

                // Cria um novo agrupamento de serviços se necessário (por questões de exibição)
                if (!includedServices.contains(service)) {
                  entries.add(new Entry(service));
                  includedServices.add(service);
                }

              }

            }

          }

        }

        // "Concatena" entradas persistidas com entradas pendentes.
        currentEntries.addAll(entries);
      }

    }

    Collections.sort(currentEntries, new Comparator<Entry>() {
      @Override
      public int compare(Entry o1, Entry o2) {
        return o1.getService().getTitle().compareTo(o2.getService().getTitle());
      }
    });


    return currentEntries;
  }

  private List<Contract> validesContracts(Date date) {
    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", date);

		/*
     * Recupera os contratos vigentes para a data
		 */
    List<Contract> contracts = contractService.findWithNamedQuery(Contract.VALIDS_IN_DATE, contractParams);


    return contracts;
  }

  @Override
  public Entry create(Entry entry) {
    changeStatus(entry);
    return super.create(entry);
  }

  public Entry prepareNewEntry(Long userId, Long serviceId) {

    User user = userService.find(userId);
    Service service = serviceService.find(serviceId);

    Entry entry = new Entry(service);
    entry.setTotalValue(BigDecimal.ZERO);
    entry.setUserForEntry(user);

    // Verifica necessidade de se carregar lista de medidores (Energia Individual, Gás, etc...)
    if (ServiceTypesEnum.E.equals(service.getType())) {
      List<ConsumptionMeter> meters = meterService.findByType(ServiceTypesEnum.E);
      for (ConsumptionMeter meter : meters) {
        entry.getMeterValues().add(new EntryMeterValue(entry, meter));
      }
    }

    for (ServiceValue serviceValue : entry.getService().getValues()) {
      if (serviceValue.getEndDate() == null) {
        entry.setServiceValue(serviceValue);
        break;
      }
    }


    /*
     * Para cada contrato vigente, verifica se o mesmo possui serviços
		 * associados ao usuário. Caso possua, cria uma instância de lançamento
		 * para o serviço.
		 *
		 */
    for (Contract contract : validesContracts(new Date())) {

      for (ContractSector contractSector : contract.getContractSectors()) {

        for (Service iterator : contractSector.getServices()) {

          if (service.equals(iterator)) {
            SectorDetail sectorDetail = contractSector.getSector().getSectorDetail();
            if (sectorDetail != null && sectorDetail.getArea() != null) {
              switch (sectorDetail.getType()) {
                case TERCEIROS:
                  entry.setTotalAreaA(entry.getTotalAreaA().add(sectorDetail.getArea()));
                  break;
                case CONSULTORIOS:
                  entry.setTotalAreaB(entry.getTotalAreaB().add(sectorDetail.getArea()));
                  break;
                case SHOPPING:
                  entry.setTotalAreaC(entry.getTotalAreaC().add(sectorDetail.getArea()));
                  break;
                default:
                  break;
              }

            }
            entry.getValues().add(new EntryValue(entry, contractSector));
          }

        }

      }

    }

    return entry;
  }


  @Override
  public Entry update(Entry item) {
    return super.update(item);
  }

  /**
   * Modifica o estado do lançamento
   * <p/>
   * TODO: Incluir outros estados
   *
   * @param entry
   */
  public void changeStatus(Entry entry) {
    switch (entry.getStatus()) {
      // Estado não lançado
      case N:
        // Estado lançado
        entry.setStatus(EntryStatusEnum.L);
        break;
      // Estado pendente
      case P:
        // Estado corrigido
        entry.setStatus(EntryStatusEnum.F);
        break;
      default:
        break;
    }
  }

  public void computeInitialValues(Entry entry) {
    ServiceTypesEnum type = entry.getService().getType();
    for (EntryValue entryValue : entry.getValues()) {
      switch (type) {
        case CTR:
          BigDecimal valueToSet = BigDecimal.ZERO;
          SectorDetail sectorDetail = entryValue.getContractSector().getSector().getSectorDetail();
          if(sectorDetail != null){
            valueToSet = sectorDetail.getRtQuantity() == null ? valueToSet : sectorDetail.getRtQuantity();
          }
          entryValue.setValueA(valueToSet);
          break;
        default:
          break;
      }
    }
    computeValues(entry);
  }

  /**
   * @param entry
   */
  public void computeValues(Entry entry) {

    ServiceValue currentServiceValue = entry.getServiceValue();

    // Para quando o serviço não possuir valor definido
    if (currentServiceValue == null) {
      currentServiceValue = new ServiceValue();
      currentServiceValue.setValueA(BigDecimal.ZERO);
      currentServiceValue.setValueB(BigDecimal.ZERO);
      currentServiceValue.setValueC(BigDecimal.ZERO);
      currentServiceValue.setValueD(BigDecimal.ZERO);
    }

    entry.setTotalValue(BigDecimal.ZERO);

    for (EntryValue entryValue : entry.getValues()) {

      entryValue.setValue(BigDecimal.ZERO);

      switch (entry.getService().getType()) {
        // Tabelado
        case T:
          // Soma o valor vigente do serviço com o valor variável informado pelo usuário.
          entryValue.setValue(currentServiceValue.getValueA().add(entryValue.getValueA()));
          break;
        // Energia Individual
        // Venda (venda comissionada)
        case V:
          BigDecimal sellValue = entryValue.getValueA();
          BigDecimal commission = entryValue.getValueB();
          entryValue.setValue(sellValue.add(sellValue.multiply(commission)));
          break;
        // Cobrança (ex: taxas de residência)
        case C:
        case E:
        case CR:
        case CRE:
        case CI:
          entryValue.setValue(entryValue.getValueA());
          break;
        case CTR:
          entryValue.setValue(entryValue.getValueA().multiply(currentServiceValue.getValueA()));
        default:
          break;
      }
      // Soma os valores
      entry.setTotalValue(entry.getTotalValue().add(entryValue.getValue()));
    }
  }

  public Map<MvClient, Map<Service, BigDecimal>> groupByClient(List<Entry> entries) {

    Map<MvClient, Map<Service, BigDecimal>> groups = new HashMap<>();

    for (Entry entry : entries) {
      Service service = entry.getService();
      for (EntryValue entryValue : entry.getValues()) {
        MvClient client = entryValue.getContractSector().getTenant();
        if (groups.get(client) == null) {
          groups.put(client, new HashMap<Service, BigDecimal>());
        }
        Map<Service, BigDecimal> values = groups.get(client);
        if (values.get(service) == null) {
          values.put(service, BigDecimal.ZERO);
        }
        BigDecimal currValue = values.get(service);
        values.put(service, currValue.add(entryValue.getValue()));
      }
    }

    return groups;

  }

}