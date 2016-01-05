package br.com.vah.lance.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.vah.lance.constant.EntryStatusEnum;
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
	public Map<Service, Map<Contract, Entry>> retrieveEntrysForUser(Long userId, Date[] range) {
		Map<Service, Map<Contract, Entry>> entries = new LinkedHashMap<>();
		User user = userService.find(userId);
		/*
		 * Para cada serviço do usuário, inclui entradas no mapeamento
		 */
		for (Service service : user.getServices()) {
			entries.put(service, new LinkedHashMap<Contract, Entry>());
		}

		Map<String, Object> contractParams = new LinkedHashMap<>();
		contractParams.put("date", new Date());
		/*
		 * Recupera os contratos vigentes para a data
		 */
		List<Contract> contracts = contractService.findWithNamedQuery(Contract.VALIDS_IN_DATE, contractParams);
		Map<String, Object> serviceParams = new LinkedHashMap<>();
		serviceParams.put("begin", range[0]);
		serviceParams.put("end", range[1]);
		serviceParams.put("services", user.getServices());
		/*
		 * Recupera as entradas já lançadas para os serviços atrelados ao
		 * usuário e a data
		 */
		List<Entry> currentEntries = findWithNamedQuery(Entry.BY_DATE_AND_SERVICE, serviceParams);
		/*
		 * Para cada contrato vigente, verifica se o mesmo possui serviços
		 * associados ao usuário. Caso possua, cria uma instância de lançamento
		 * para o serviço.
		 */
		for (Contract contract : contracts) {
			for (ServiceContract service : contract.getServices()) {
				if (user.getServices().contains(service.getService())) {
					Map<Contract, Entry> map = entries.get(service.getService());
					Entry entry = new Entry(contract);
					entry.setService(service.getService());
					entry.setContractValue(service.getAmount());
					entry.setUserForEntry(user);
					entry.setContract(contract);
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

	public List<Entry> saveEntrys(List<Entry> entries) {
		for (Entry entry : entries) {
			changeStatus(entry);
		}
		return update(entries);
	}

}