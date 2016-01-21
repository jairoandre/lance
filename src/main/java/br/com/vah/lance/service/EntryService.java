package br.com.vah.lance.service;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.entity.*;

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

  private List<Contract> validesContracts(){
    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", new Date());

		/*
     * Recupera os contratos vigentes para a data
		 */
    List<Contract> contracts = contractService.findWithNamedQuery(Contract.VALIDS_IN_DATE, contractParams);


    return contracts;
  }

  public Entry prepareNewEntry(Long userId, Long serviceId){

    User user = userService.find(userId);
    Service service = serviceService.find(serviceId);

    Entry entry = new Entry(service);
    entry.setTotalValue(BigDecimal.ZERO);
    entry.setUserForEntry(user);

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

  public void changeStatus(Entry entry) {
    switch (entry.getStatus()) {
      case N:
        entry.setStatus(EntryStatusEnum.L);
        break;
      case P:
        entry.setStatus(EntryStatusEnum.F);
        break;
      default:
        break;
    }
  }

  public List<Entry> saveEntries(List<Entry> entries) {
    for (Entry entry : entries) {
      changeStatus(entry);
    }
    return update(entries);
  }

}