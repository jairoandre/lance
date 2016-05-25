package br.com.vah.lance.service;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.*;
import br.com.vah.lance.entity.mv.*;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.util.LanceUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
  ContaReceberService contaReceberService;

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

  public List<Entry> recuperarLancamentosCondominial() {
    return findWithNamedQuery(Entry.CONDOMINIAL);
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

    ServiceValue currentServiceValue = entry.getServiceValue();

    // Para quando o serviço não possuir valor definido
    if (currentServiceValue == null) {
      currentServiceValue = new ServiceValue();
    }

    for (EntryValue entryValue : entry.getValues()) {
      switch (type) {
        case T:
          entryValue.setValueA(currentServiceValue.getValueA());
          break;
        case CTR:
          BigDecimal valueToSet = BigDecimal.ZERO;
          SectorDetail sectorDetail = entryValue.getContractSector().getSector().getSectorDetail();
          if (sectorDetail != null) {
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
    }

    entry.setTotalValue(BigDecimal.ZERO);

    for (EntryValue entryValue : entry.getValues()) {

      entryValue.setValue(BigDecimal.ZERO);

      switch (entry.getService().getType()) {
        // Tabelado
        case T:
          // Soma o valor vigente do serviço com o valor variável informado pelo usuário.
          entryValue.setValue(entryValue.getValueA());
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

  @Override
  public Entry find(Object id) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Entry.class);

    criteria.add(Restrictions.idEq(id));
    criteria.setFetchMode("values", FetchMode.SELECT);
    criteria.setFetchMode("meterValues", FetchMode.SELECT);
    criteria.setFetchMode("contasReceber", FetchMode.SELECT);

    return (Entry) criteria.uniqueResult();
  }

  public List<Entry> listOldEntries(Service service) {

    Map<String, Object> entriesParams = new LinkedHashMap<>();
    Calendar cl = Calendar.getInstance();
    cl.setTime(new Date());
    cl.add(Calendar.MONTH, -6);
    cl.set(Calendar.DAY_OF_MONTH, 1);
    cl.set(Calendar.HOUR, 0);
    cl.set(Calendar.MINUTE, 0);
    cl.set(Calendar.SECOND, 0);
    entriesParams.put("date", cl.getTime());
    entriesParams.put("service", service);
    entriesParams.put("status", EntryStatusEnum.V);


    return findWithNamedQuery(Entry.BY_SERVICE_DATE_STATUS, entriesParams);
  }

  public Date getDataVencimento (Entry entry, Date dataLancamento) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(dataLancamento);
    cl.add(Calendar.MONTH, 1);
    Integer vencimento  = 5;
    if(entry.getService() != null && entry.getService().getDiaVencimento() != null) {
      vencimento = entry.getService().getDiaVencimento();
    }
    cl.set(Calendar.DAY_OF_MONTH, vencimento);
    return cl.getTime();
  }


  public MvContaReceber createContaReceber(EntryValue entryValue, String numDocPrefix, Integer count, Date dataLancamento) {

    MvContaReceber conRecToAdd = new MvContaReceber();

    conRecToAdd.setCdProcesso(132l);
    conRecToAdd.setCdMultiEmpresa(1);
    conRecToAdd.setTipoDocumento("C");
    conRecToAdd.setDataEmissao(new Date());
    conRecToAdd.setMoeda("1");
    conRecToAdd.setTipoVencimento("V");
    conRecToAdd.setValorBruto(entryValue.getValue());
    String numeroDocumento = numDocPrefix + LanceUtils.paddingZeros(String.valueOf(count), 3);
    conRecToAdd.setNumeroDocumento(numeroDocumento);
    conRecToAdd.setDataLancamento(dataLancamento);

    MvClient client = entryValue.getContractSector().getContract().getSubject();

    MvDefaultHistory historicoPadrao = entryValue.getEntry().getService().getDefaultHistory();

    conRecToAdd.setCliente(client);
    conRecToAdd.setNomeCliente(client.getTitle());
    conRecToAdd.setHistoricoPadrao(historicoPadrao);
    conRecToAdd.setDescricao(historicoPadrao.getTitle());
    conRecToAdd.setObservacao(numeroDocumento + " - " + client.getTitle());
    conRecToAdd.setGlosaAceita("N");

    // ITEM DA CONTA A RECEBER
    MvContaReceberItem conRecItem = new MvContaReceberItem();
    conRecItem.setCodigoMoeda("1");
    conRecItem.setNumeroParcela(1);
    conRecItem.setValorDuplicata(entryValue.getValue());
    conRecItem.setTipoQuitacao("C");
    conRecItem.setValorMoeda(entryValue.getValue());
    conRecItem.setContaReceber(conRecToAdd);

    Date dataVencimento = getDataVencimento(entryValue.getEntry(), dataLancamento);

    conRecItem.setDataVencimento(dataVencimento);
    conRecItem.setDataPrevistaRecebimento(dataVencimento);

    MvContaReceberRateio conRecRateio = new MvContaReceberRateio();
    MvPlanoConta contaContabil = entryValue.getEntry().getService().getLedgerAccount();
    SectorDetail sectorDetail = entryValue.getContractSector().getSector().getSectorDetail();
    if (sectorDetail != null && sectorDetail.getLedgerAccount() != null) {
      contaContabil = sectorDetail.getLedgerAccount();
    }
    conRecToAdd.setContaContabil(contaContabil);
    conRecRateio.setContaContabil(contaContabil);
    conRecRateio.getPk().setNumeroLinha(1);
    conRecRateio.getPk().setContaReceber(conRecToAdd);
    conRecRateio.setContaResultado(entryValue.getEntry().getService().getCostAccount());
    conRecRateio.setValorRateio(entryValue.getValue());
    conRecRateio.setCdSetor(entryValue.getContractSector().getSector().getId());

    conRecToAdd.getItensRateio().add(conRecRateio);
    conRecToAdd.getItensConta().add(conRecItem);

    return conRecToAdd;

  }

  public List<Entry> updateList(List<Entry> entries) {
    List<Entry> persistedList = new ArrayList<>();
    for (Entry entry : entries) {
      persistedList.add(update(entry));
    }
    return persistedList;
  }

  public List<Entry> validarLancamentosAgrupados(List<Entry> lancamentos, Date dataLancamento) throws LanceBusinessException {
    Map<MvSector, MvContaReceber> contasReceberMap = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String numDocPrefix = sdf.format(dataLancamento);
    numDocPrefix = numDocPrefix + "999-";

    if (lancamentos != null) {
      for (Entry entry : lancamentos) {

        Integer count = 0;
        for (EntryValue entryValue : entry.getValues()) {
          // Recupera a conta de consolidação do setor
          MvContaReceber mvContaReceber = contasReceberMap.get(entryValue.getContractSector().getSector());

          if (mvContaReceber == null) {
            contasReceberMap.put(entryValue.getContractSector().getSector(), createContaReceber(entryValue, numDocPrefix, count++, dataLancamento));
            continue;
          } else {
            MvContaReceberRateio itemRateio = mvContaReceber.getItensRateio().get(0);
            MvContaReceberItem itemConta = mvContaReceber.getItensConta().get(0);

            // Antes de atualizar os valores, verifica se a conta receber previamente inserida no mapping apresenta parâmetros contábeis
            // condizentes com o serviço iterado
            Service servico = entry.getService();

            MvPlanoConta contaContabil = servico.getLedgerAccount();
            SectorDetail sectorDetail = entryValue.getContractSector().getSector().getSectorDetail();
            if (sectorDetail != null && sectorDetail.getLedgerAccount() != null) {
              contaContabil = sectorDetail.getLedgerAccount();
            }

            if (!mvContaReceber.getHistoricoPadrao().equals(servico.getDefaultHistory()) ||
                !mvContaReceber.getContaContabil().equals(contaContabil) ||
                !itemRateio.getContaResultado().equals(servico.getCostAccount())) {
              throw new LanceBusinessException("Não é possível realizar lançamento agrupado. Parâmetros contábeis divergentes entre os serviços selecionados. Serviço [%s]", entry.getService().getClass().getSimpleName());
            }

            // Conta a receber para o setor já existe. Neste caso, atualize somente os valores da conta.
            BigDecimal entryValueNumber = entryValue.getValue();

            // Valor total da conta
            mvContaReceber.setValorBruto(mvContaReceber.getValorBruto().add(entryValueNumber));


            // Valor do item de rateio
            itemRateio.setValorRateio(itemRateio.getValorRateio().add(entryValueNumber));

            // Valor do item da conta
            itemConta.setValorDuplicata(itemConta.getValorDuplicata().add(entryValueNumber));
            itemConta.setValorMoeda(itemConta.getValorMoeda().add(entryValueNumber));

          }

        }
      } // for
    } // if

    // Persiste as contas a receber
    List<MvContaReceber> persistedContas = contaReceberService.createList(new ArrayList<>(contasReceberMap.values()));

    // Itera a lista de lançamentos selecionados para atualiza-los
    for (Entry entry : lancamentos) {
      // Modifica o status para validado
      entry.setStatus(EntryStatusEnum.V);
      entry.setContasReceber(persistedContas);
    }

    return updateList(lancamentos);
  }
}