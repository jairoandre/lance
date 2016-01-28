package br.com.vah.lance.service;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.entity.*;
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

    if (userId == null) {
      return entries;
    }

    User user = userService.find(userId);

    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", new Date());

		/*
     * Recupera os contratos vigentes para a data
		 */
    List<Contract> contracts = validesContracts();

    Map<String, Object> entriesParams = new LinkedHashMap<>();
    entriesParams.put("begin", range[0]);
    entriesParams.put("end", range[1]);
    entriesParams.put("services", user.getServices());

		/*
     * Recupera as entradas já lançadas para os serviços atrelados ao usuário e a data
		 */
    List<Entry> currentEntries = findWithNamedQuery(Entry.BY_DATE_AND_SERVICE, entriesParams);

    Set<Service> includedServices = new HashSet<>();

    /*
     * Realiza um mapeamento dos serviços que já foram lançados
     */
    for (Entry entry : currentEntries) {
      includedServices.add(entry.getService());
    }

		/*
     * Para cada contrato vigente, verifica se o mesmo possui serviços
		 * associados ao usuário. Caso possua, cria uma instância de lançamento
		 * para o serviço.
		 *
		 */
    for (Contract contract : contracts) {

      for (ContractSector contractSector : contract.getContractSectors()) {

        for (Service service : contractSector.getServices()) {

          if (user.getServices().contains(service)) {

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

    return currentEntries;
  }

  private List<Contract> validesContracts() {
    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", new Date());

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

    Date[] dateRange = LanceUtils.getDateRangeForThisMonth();
    for (ServiceValue serviceValue : entry.getService().getValues()) {
      if (LanceUtils.checkBetweenDates(dateRange[0], dateRange[1], serviceValue.getBeginDate(), serviceValue.getEndDate())) {
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
    for (Contract contract : validesContracts()) {

      for (ContractSector contractSector : contract.getContractSectors()) {

        for (Service iterator : contractSector.getServices()) {

          if (service.equals(iterator)) {
            entry.getValues().add(new EntryValue(entry, contractSector));
          }

        }

      }

    }

    return entry;
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

  /**
   * @param entry
   */
  public void computeValues(Entry entry) {
    ServiceValue currentServiceValue = entry.getServiceValue();

    entry.setTotalValue(BigDecimal.ZERO);

    for (EntryValue entryValue : entry.getValues()) {

      entryValue.setValue(BigDecimal.ZERO);

      switch (entry.getService().getType()) {
        // Tabelado
        case T:
          // Soma o valor vigente do serviço com o valor variável informado pelo usuário.
          entryValue.setValue(currentServiceValue.getValue().add(entryValue.getValueA()));
          break;
        // Energia
        case E:
          BigDecimal delta = entryValue.getValueB().subtract(entryValue.getValueA());
          BigDecimal outPeakValue = delta.multiply(currentServiceValue.getValueA()).multiply(currentServiceValue.getValue());
          BigDecimal peakValue = delta.multiply(currentServiceValue.getValueC()).multiply(currentServiceValue.getValueB());
          entryValue.setValue(outPeakValue.add(peakValue));
          break;
        // Venda (venda comissionada)
        case V:
          BigDecimal sellValue = entryValue.getValueA();
          BigDecimal commission = entryValue.getValueB();
          entryValue.setValue(sellValue.add(sellValue.multiply(commission)));
          break;
        // Cobrança (ex: taxas de residência)
        case C:
          entryValue.setValue(entryValue.getValueA());
        default:
          break;
      }
      // Soma os valores
      entry.setTotalValue(entry.getTotalValue().add(entryValue.getValue()));
    }
  }

}