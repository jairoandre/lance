package br.com.vah.lance.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.vah.lance.entity.Contract;
import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceContract;
import br.com.vah.lance.entity.User;

@Stateless
public class EntryService extends DataAccessService<Entry> {

	private @Inject ContractService contractService;

	private @Inject UserService userService;

	public EntryService() {
		super(Entry.class);
	}

	@SuppressWarnings("unchecked")
	public Map<Service, Map<Contract, Entry>> retrieveEntrysForUser(Long userId, Date date) {
		Map<Service, Map<Contract, Entry>> entries = new LinkedHashMap<>();
		User user = userService.find(userId);
		/*
		 * Para cada serviço do usuário, inclui entradas no mapeamento
		 */
		for (Service service : user.getServices()) {
			entries.put(service, new LinkedHashMap<Contract, Entry>());
		}

		Map<String, Object> contractParams = new LinkedHashMap<>();
		contractParams.put("date", date);
		/*
		 * Recupera os contratos vigentes para a data
		 */
		List<Contract> contracts = contractService.findWithNamedQuery(Contract.VALIDS_IN_DATE, contractParams);
		Map<String, Object> serviceParams = new LinkedHashMap<>(contractParams);
		serviceParams.put("services", user.getServices());
		/*
		 * Recupera as entradas já lançadas para os serviços atrelados ao
		 * usuário e a data
		 */
		List<Entry> currentEntries = findWithNamedQuery(Entry.BY_DATE_AND_SERVICE, serviceParams);
		/*
		 * Para cada contrato vigente, verifica se o mesmo possui serviços
		 */
		for (Contract contract : contracts) {
			for (ServiceContract service : contract.getServices()) {
				if (user.getServices().contains(service.getService())) {
					Map<Contract, Entry> map = entries.get(service.getService());
					Entry entry = new Entry(contract);
					entry.setService(service.getService());
					entry.setContractValue(service.getAmount());
					entry.setUserForEntry(user);
					map.put(contract, entry);
				}
			}
		}
		/*
		 * Atualiza o map com os lançamentos já efetuados para a data
		 */
		for (Entry entry : currentEntries) {
			entries.get(entry.getService()).put(entry.getContract(), entry);
		}
		return entries;
	}

}