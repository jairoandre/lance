package br.com.vah.lance.service;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.constant.TipoSetorEnum;
import br.com.vah.lance.entity.dbamv.*;
import br.com.vah.lance.entity.usrdbvah.*;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.util.VahUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class LancamentoService extends DataAccessService<Lancamento> {

  private
  @Inject
  ContratoService contratoService;

  private
  @Inject
  ServicoService servicoService;

  private
  @Inject
  ContaReceberService contaReceberService;

  private
  @Inject
  UserService userService;

  private
  @Inject
  MedidorConsumoService meterService;

  public LancamentoService() {
    super(Lancamento.class);
  }

  /**
   * Recupera os lançamentos validados para a vigência e/ou vencimento informados.
   *
   * @param range
   * @param vencimento
   * @return
   */
  public List<Lancamento> recuperarLancamentosValidados(Date[] range, Integer vencimento) {
    String namedQuery;
    Map<String, Object> params = new HashMap<>();
    params.put("begin", range[0]);
    params.put("end", range[1]);
    params.put("status", EstadoLancamentoEnum.V);
    if (vencimento == null) {
      namedQuery = Lancamento.BY_PERIOD_STATUS;
    } else {
      params.put("vencimento", vencimento);
      namedQuery = Lancamento.BY_PERIOD_STATUS_VENC;
    }

    return findWithNamedQuery(namedQuery, params);
  }

  /**
   * Recupera as entradas de lançamentos já persistidas. Quando necessário cria novas entradas (em memória) para lançamentos pendentes.
   *
   * @param userId
   * @param range
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Lancamento> retrieveEntriesForUser(Long userId, Date[] range) {

    List<Lancamento> entries = new ArrayList<>();

    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", range[1]);

		/*
     * Recupera os contratos vigentes para a data
		 */
    // List<Contrato> contratos = validesContracts(range[1]);

    Map<String, Object> entriesParams = new LinkedHashMap<>();
    entriesParams.put("begin", range[0]);
    entriesParams.put("end", range[1]);

    Set<Servico> userServicos = null;

    if (userId != null) {
      userServicos = userService.find(userId).getServicos();
    }


    List<Lancamento> currentEntries;

    /**
     * Quando o usuário não possuir serviços atrelados, exibe todos. (Controladoria)
     */
    if (userServicos == null || userServicos.isEmpty()) {
      currentEntries = findWithNamedQuery(Lancamento.BY_PERIOD, entriesParams);
    } else {
      /**
       * Recupera as entradas já lançadas para os serviços atrelados ao usuário e a data
       */
      entriesParams.put("services", userServicos);
      currentEntries = findWithNamedQuery(Lancamento.BY_PERIOD_AND_SERVICES, entriesParams);

      /**
       * Verifica se o período consultado é do mês vigente (ou posterior), caso seja, monta lançamentos que ainda não foram lançados.
       */
      Date today = new Date();

      if (VahUtils.checkBetween(today, range[0], range[1]) || today.before(range[0])) {

        Set<Servico> includedServicos = new HashSet<>();

        /**
         * Realiza um mapeamento dos serviços que já foram lançados
         */
        for (Lancamento lancamento : currentEntries) {
          includedServicos.add(lancamento.getServico());
        }

        /**
         * Para cada contrato vigente, verifica se o mesmo possui serviços
         * associados ao usuário. Caso possua, cria uma instância de lançamento
         * para o serviço.
         */

        List<Servico> servicos = servicoService.list();

        for (Servico servico : servicos) {
          if (userServicos.contains(servico)) {
            // Cria um novo agrupamento de serviços se necessário (por questões de exibição)
            if (!includedServicos.contains(servico)) {
              entries.add(new Lancamento(servico, range[0]));
              includedServicos.add(servico);
            }
          }
        }

        currentEntries.addAll(entries);

/*
        for (Contrato contrato : contratos) {

          for (ContratoSetor contratoSetor : contrato.getSetores()) {


          }

        }
*/

      }

    }

    Collections.sort(currentEntries, new Comparator<Lancamento>() {
      @Override
      public int compare(Lancamento o1, Lancamento o2) {
        return o1.getServico().getTitle().compareTo(o2.getServico().getTitle());
      }
    });


    return currentEntries;
  }

  private List<Contrato> validesContracts(Date date) {
    Map<String, Object> contractParams = new LinkedHashMap<>();
    contractParams.put("date", date);

		/*
     * Recupera os contratos vigentes para a data
		 */
    List<Contrato> contratos = contratoService.findWithNamedQuery(Contrato.VALIDS_IN_DATE, contractParams);


    return contratos;
  }

  public Lancamento prepareNewEntry(Long userId, Long serviceId, Date vigencia) {
    User user = userService.find(userId);
    Lancamento lancamento = prepareNewEntry(serviceId, vigencia);
    lancamento.setAutor(user);
    return lancamento;
  }

  public Lancamento initializeLists(Lancamento lancamento) {
    Lancamento att = find(lancamento.getId());
    new HashSet(att.getComentarios());
    new HashSet(att.getContasReceber());
    new HashSet(att.getMeterValues());
    new HashSet(att.getValues());
    return att;
  }

  public Lancamento initializeLists(Lancamento lancamento, Boolean comentarios, Boolean contas, Boolean medidores, Boolean valores) {
    return initializeLists(lancamento.getId(), comentarios,contas, medidores, valores);
  }

  public Lancamento initializeLists(Long id, Boolean comentarios, Boolean contas, Boolean medidores, Boolean valores) {
    Lancamento att = find(id);
    if (comentarios) {
      new HashSet(att.getComentarios());
    }

    if (contas) {
      new HashSet(att.getContasReceber());
    }

    if (medidores) {
      new HashSet(att.getMeterValues());
    }

    if (valores) {
      new HashSet(att.getValues());
    }

    return att;
  }

  public Lancamento prepareNewEntry(Long serviceId, Date vigencia) {

    Servico servico = servicoService.find(serviceId);

    Lancamento lancamento = new Lancamento(servico, vigencia);
    lancamento.setTotalValue(BigDecimal.ZERO);

    // Verifica necessidade de se carregar lista de medidores (Energia Individual, Gás, etc...)
    if (TipoServicoEnum.E.equals(servico.getType())) {
      List<MedidorConsumo> meters = meterService.findByType(TipoServicoEnum.E);
      for (MedidorConsumo meter : meters) {
        lancamento.getMeterValues().add(new LancamentoMedidorValor(lancamento, meter));
      }
    }

    for (ServicoValor servicoValor : lancamento.getServico().getValues()) {
      if (servicoValor.getEndDate() == null) {
        lancamento.setServicoValor(servicoValor);
        break;
      }
    }


    /*
     * Para cada contrato vigente, verifica se o mesmo possui serviços
		 * associados ao usuário. Caso possua, cria uma instância de lançamento
		 * para o serviço.
		 *
		 */
    for (Contrato contrato : validesContracts(new Date())) {

      for (ContratoSetor contratoSetor : contrato.getSetores()) {

        for (Servico iterator : contratoSetor.getServicos()) {

          if (servico.equals(iterator)) {
            SetorDetalhe setorDetalhe = contratoSetor.getSetor().getSetorDetalhe();
            if (setorDetalhe != null && setorDetalhe.getArea() != null) {
              switch (setorDetalhe.getType()) {
                case TERCEIROS:
                  lancamento.setTotalAreaA(lancamento.getTotalAreaA().add(setorDetalhe.getArea()));
                  break;
                case CONSULTORIOS:
                  lancamento.setTotalAreaB(lancamento.getTotalAreaB().add(setorDetalhe.getArea()));
                  break;
                case SHOPPING:
                  lancamento.setTotalAreaC(lancamento.getTotalAreaC().add(setorDetalhe.getArea()));
                  break;
                default:
                  break;
              }

            }
            lancamento.getValues().add(new LancamentoValor(lancamento, contratoSetor));
          }

        }

      }

    }
    return lancamento;
  }

  public List<Lancamento> recuperarLancamentosCondominial() {
    return findWithNamedQuery(Lancamento.CONDOMINIAL);
  }


  @Override
  public Lancamento update(Lancamento item) {
    return super.update(item);
  }

  /**
   * Modifica o estado do lançamento
   * <p/>
   * TODO: Incluir outros estados
   *
   * @param lancamento
   */
  public void changeStatus(Lancamento lancamento) {
    switch (lancamento.getStatus()) {
      // Estado não lançado
      case N:
        // Estado lançado
        lancamento.setStatus(EstadoLancamentoEnum.L);
        break;
      // Estado pendente
      case P:
        // Estado corrigido
        lancamento.setStatus(EstadoLancamentoEnum.F);
        break;
      default:
        break;
    }
  }

  public void computeInitialValues(Lancamento lancamento) {
    TipoServicoEnum type = lancamento.getServico().getType();

    ServicoValor currentServicoValor = lancamento.getServicoValor();

    // Para quando o serviço não possuir valor definido
    if (currentServicoValor == null) {
      currentServicoValor = new ServicoValor();
    }

    for (LancamentoValor lancamentoValor : lancamento.getValues()) {
      switch (type) {
        case T:
          lancamentoValor.setValueA(currentServicoValor.getValueA());
          break;
        case CTR:
          /*
          BigDecimal valueToSet = BigDecimal.ZERO;
          SetorDetalhe setorDetalhe = lancamentoValor.getContratoSetor().getSetor().getSetorDetalhe();
          if (setorDetalhe != null) {
            valueToSet = setorDetalhe.getRtQuantity() == null ? valueToSet : setorDetalhe.getRtQuantity();
          }
          lancamentoValor.setValueA(valueToSet);
          */
          break;
        default:
          break;
      }
    }
    computeValues(lancamento);
  }

  /**
   * @param lancamento
   */
  public void computeValues(Lancamento lancamento) {

    ServicoValor currentServicoValor = lancamento.getServicoValor();

    // Para quando o serviço não possuir valor definido
    if (currentServicoValor == null) {
      currentServicoValor = new ServicoValor();
    }

    lancamento.setTotalValue(BigDecimal.ZERO);

    for (LancamentoValor lancamentoValor : lancamento.getValues()) {

      lancamentoValor.setValue(BigDecimal.ZERO);
      if (lancamentoValor.getValueA() == null) {
        lancamentoValor.setValueA(BigDecimal.ZERO);
      }
      if (lancamentoValor.getValueB() == null) {
        lancamentoValor.setValueB(BigDecimal.ZERO);
      }

      switch (lancamento.getServico().getType()) {
        // Tabelado
        case T:
          // Soma o valor vigente do serviço com o valor variável informado pelo usuário.
          lancamentoValor.setValue(lancamentoValor.getValueA());
          break;
        // Energia Individual
        // Venda (venda comissionada)
        case V:
          BigDecimal sellValue = lancamentoValor.getValueA();
          BigDecimal commission = lancamentoValor.getValueB();
          lancamentoValor.setValue(sellValue.add(sellValue.multiply(commission)).setScale(2, BigDecimal.ROUND_HALF_UP));
          break;
        // Cobrança (ex: taxas de residência)
        case C:
        case E:
        case CR:
        case CRE:
        case CI:
        case CTR:
          lancamentoValor.setValue(lancamentoValor.getValueA());
          break;
        case CTP:
          // case CTR:
          lancamentoValor.setValue(lancamentoValor.getValueA().multiply(currentServicoValor.getValueA()).setScale(2, BigDecimal.ROUND_HALF_UP));
        default:
          break;
      }
      // Soma os valores
      lancamento.setTotalValue(lancamento.getTotalValue().add(lancamentoValor.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
  }

  public Map<Fornecedor, Map<Servico, BigDecimal>> groupByClient(List<Lancamento> entries) {

    Map<Fornecedor, Map<Servico, BigDecimal>> groups = new HashMap<>();

    for (Lancamento lancamento : entries) {
      Servico servico = lancamento.getServico();
      for (LancamentoValor lancamentoValor : lancamento.getValues()) {
        Fornecedor client = lancamentoValor.getContratoSetor().getInquilino();
        if (groups.get(client) == null) {
          groups.put(client, new HashMap<Servico, BigDecimal>());
        }
        Map<Servico, BigDecimal> values = groups.get(client);
        if (values.get(servico) == null) {
          values.put(servico, BigDecimal.ZERO);
        }
        BigDecimal currValue = values.get(servico);
        values.put(servico, currValue.add(lancamentoValor.getValue()));
      }
    }

    return groups;

  }

  public List<Lancamento> getLancamentosPorPeriodoStatus(EstadoLancamentoEnum status, Date[] range) {

    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Lancamento.class);

    criteria.add(Restrictions.between("effectiveOn", range[0], range[1]));

    if (status != null) {
      criteria.add(Restrictions.eq("status", status));
    }

    criteria.setFetchMode("values", FetchMode.SELECT);
    criteria.setFetchMode("meterValues", FetchMode.SELECT);
    criteria.setFetchMode("contasReceber", FetchMode.SELECT);
    criteria.setFetchMode("servico", FetchMode.SELECT);
    criteria.setFetchMode("comentarios", FetchMode.SELECT);
    criteria.setFetchMode("servicoValor", FetchMode.SELECT);

    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    return criteria.list();
  }

  @Override
  public Lancamento find(Object id) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Lancamento.class);

    criteria.add(Restrictions.idEq(id));
    criteria.setFetchMode("values", FetchMode.SELECT);
    criteria.setFetchMode("meterValues", FetchMode.SELECT);
    criteria.setFetchMode("contasReceber", FetchMode.SELECT);

    return (Lancamento) criteria.uniqueResult();
  }

  public Lancamento addNovosLancamentos(Lancamento lancamento) {
    Map<Fornecedor, LancamentoValor> map = new HashMap<>();
    Lancamento novasEntradas = prepareNewEntry(lancamento.getServico().getId(), lancamento.getEffectiveOn());
   for (LancamentoValor servicoValor : lancamento.getValues()) {
      map.put(servicoValor.getContratoSetor().getContrato().getContratante(), servicoValor);
    }
    for (LancamentoValor novaEntrada : novasEntradas.getValues()) {
      if (map.get(novaEntrada.getContratoSetor().getContrato().getContratante()) == null) {
        novaEntrada.setLancamento(lancamento);
        lancamento.getValues().add(novaEntrada);
      }
    }
    return lancamento;
  }

  public List<Lancamento> listOldEntries(Servico servico) {

    Map<String, Object> entriesParams = new LinkedHashMap<>();
    Calendar cl = Calendar.getInstance();
    cl.setTime(new Date());
    cl.add(Calendar.MONTH, -6);
    cl.set(Calendar.DAY_OF_MONTH, 1);
    cl.set(Calendar.HOUR, 0);
    cl.set(Calendar.MINUTE, 0);
    cl.set(Calendar.SECOND, 0);
    entriesParams.put("date", cl.getTime());
    entriesParams.put("servico", servico);
    entriesParams.put("status", EstadoLancamentoEnum.V);


    return findWithNamedQuery(Lancamento.BY_SERVICE_DATE_STATUS, entriesParams);
  }

  public ContaReceber createContaReceber(LancamentoValor lancamentoValor, String numDocPrefix, Integer count, Date dataLancamento) {

    ContaReceber conRecToAdd = new ContaReceber();

    Servico servico = lancamentoValor.getLancamento().getServico();

    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

    String obsPrefix = "VIG.";
    Date dataRefVig = lancamentoValor.getLancamento().getEffectiveOn();
    if (servico.getAgrupavel()) {
      Calendar cld = Calendar.getInstance();
      cld.setTime(dataRefVig);
      cld.add(Calendar.MONTH, -1);
      dataRefVig = cld.getTime();
      obsPrefix = "REF.";
    }

    conRecToAdd.setCdProcesso(132l);
    conRecToAdd.setCdMultiEmpresa(1);
    conRecToAdd.setTipoDocumento("D");
    conRecToAdd.setDataEmissao(new Date());
    conRecToAdd.setMoeda("1");
    conRecToAdd.setCdTipDoc(servico.getTipoDocumento().getId().intValue());
    conRecToAdd.setTipoVencimento("V");
    conRecToAdd.setValorBruto(lancamentoValor.getValue());
    String numeroDocumento = numDocPrefix + VahUtils.leftPadZeros(count, 3);
    conRecToAdd.setNumeroDocumento(numeroDocumento);
    conRecToAdd.setDataLancamento(dataLancamento);

    Fornecedor client = lancamentoValor.getContratoSetor().getContrato().getContratante();

    HistoricoPadrao historicoPadrao = lancamentoValor.getLancamento().getServico().getHistoricoPadrao();

    conRecToAdd.setCliente(client);
    conRecToAdd.setNomeCliente(client.getTitle());
    conRecToAdd.setHistoricoPadrao(historicoPadrao);
    conRecToAdd.setObservacao(String.format("%s %s", obsPrefix, sdf.format(dataRefVig)));
    conRecToAdd.setDescricao(numeroDocumento + " - " + client.getTitle());
    conRecToAdd.setGlosaAceita("N");

    PlanoConta contaContabil = lancamentoValor.getLancamento().getServico().getContaContabil();

    if (lancamentoValor.getLancamento().getServico().getContaPorSetor()) {
      SetorDetalhe setorDetalhe = lancamentoValor.getContratoSetor().getSetor().getSetorDetalhe();
      if (setorDetalhe != null && setorDetalhe.getContaContabil() != null) {
        contaContabil = setorDetalhe.getContaContabil();
      }
    }
    conRecToAdd.setContaContabil(contaContabil);

    // ITEM DA CONTA A RECEBER - ABA PARCELAMENTO
    ContaReceberItem conRecItem = new ContaReceberItem();
    conRecItem.setCodigoMoeda("1");
    conRecItem.setNumeroParcela(1);
    conRecItem.setValorDuplicata(lancamentoValor.getValue());
    conRecItem.setTipoQuitacao("C");
    conRecItem.setValorMoeda(lancamentoValor.getValue());
    conRecItem.setContaReceber(conRecToAdd);

    Date dataVencimento = VahUtils.calcNextMonthDate(dataLancamento, lancamentoValor.getLancamento().getServico().getDiaVencimento());

    conRecItem.setDataVencimento(dataVencimento);
    conRecItem.setDataPrevistaRecebimento(dataVencimento);


    // ITEM RATEIO - ABA COMPARTILHAMENTO
    ContaReceberRateio conRecRateio = new ContaReceberRateio();
    PlanoConta contaContabilCompart = lancamentoValor.getLancamento().getServico().getContaResultado();
    conRecRateio.setContaContabil(contaContabilCompart);
    conRecRateio.getPk().setNumeroLinha(1);
    conRecRateio.getPk().setContaReceber(conRecToAdd);
    conRecRateio.setContaResultado(lancamentoValor.getLancamento().getServico().getContaCusto());
    conRecRateio.setValorRateio(lancamentoValor.getValue());
    conRecRateio.setCdSetor(lancamentoValor.getContratoSetor().getSetor().getId());

    conRecToAdd.getItensRateio().add(conRecRateio);
    conRecToAdd.getItensConta().add(conRecItem);

    return conRecToAdd;

  }

  public List<Lancamento> updateList(List<Lancamento> entries) {
    List<Lancamento> persistedList = new ArrayList<>();
    for (Lancamento lancamento : entries) {
      persistedList.add(update(lancamento));
    }
    return persistedList;
  }

  public List<Lancamento> validarLancamentosAgrupados(List<Lancamento> lancamentos, Date dataLancamento) throws LanceBusinessException {
    Map<Setor, ContaReceber> contasReceberMap = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
    String numDocPrefix = sdf.format(dataLancamento);
    numDocPrefix = numDocPrefix + "99";

    List<Lancamento> attacheds = new ArrayList<>();

    if (lancamentos != null) {
      for (Lancamento lancamento : lancamentos) {

        // Reatacha o objeto na sessão

        lancamento = find(lancamento.getId());
        attacheds.add(lancamento);

        Integer count = 0;
        for (LancamentoValor lancamentoValor : lancamento.getValues()) {
          // Recupera a conta de consolidação do setor
          ContaReceber contaReceber = contasReceberMap.get(lancamentoValor.getContratoSetor().getSetor());

          if (contaReceber == null) {
            contaReceber = createContaReceber(lancamentoValor, numDocPrefix, count++, dataLancamento);
            contasReceberMap.put(lancamentoValor.getContratoSetor().getSetor(), contaReceber);
          } else {
            ContaReceberRateio itemRateio = contaReceber.getItensRateio().iterator().next();
            ContaReceberItem itemConta = contaReceber.getItensConta().iterator().next();

            // Antes de atualizar os valores, verifica se a conta receber previamente inserida no mapping apresenta parâmetros contábeis
            // condizentes com o serviço iterado
            Servico servico = lancamento.getServico();

            PlanoConta contaContabil = servico.getContaContabil();

            if (lancamentoValor.getLancamento().getServico().getContaPorSetor()) {
              SetorDetalhe setorDetalhe = lancamentoValor.getContratoSetor().getSetor().getSetorDetalhe();
              if (setorDetalhe != null && setorDetalhe.getContaContabil() != null) {
                contaContabil = setorDetalhe.getContaContabil();
              }
            }

            if (!contaReceber.getHistoricoPadrao().equals(servico.getHistoricoPadrao()) ||
                !contaReceber.getContaContabil().equals(contaContabil) ||
                !itemRateio.getContaResultado().equals(servico.getContaCusto())) {
              throw new LanceBusinessException("Não é possível realizar lançamento agrupado. Parâmetros contábeis divergentes entre os serviços selecionados. Serviço [%s]", lancamento.getServico().getClass().getSimpleName());
            }

            // Conta a receber para o setor já existe. Neste caso, atualize somente os valores da conta.
            BigDecimal entryValueNumber = lancamentoValor.getValue();

            // Valor total da conta
            contaReceber.setValorBruto(contaReceber.getValorBruto().add(entryValueNumber));


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
    List<ContaReceber> persistedContas = contaReceberService.createList(new ArrayList<>(contasReceberMap.values()));

    // Itera a lista de lançamentos selecionados para atualiza-los
    for (Lancamento lancamento : attacheds) {
      // Modifica o status para validado
      lancamento.setStatus(EstadoLancamentoEnum.V);
      lancamento.setContasReceber(persistedContas);
    }
    return updateList(attacheds);
  }

  public Lancamento carregarValoresAnteriores(Lancamento lancamento) throws LanceBusinessException {
    Date effectiveOn = lancamento.getEffectiveOn();
    Calendar cld = Calendar.getInstance();
    cld.setTime(effectiveOn);
    cld.set(Calendar.DAY_OF_MONTH, 1);
    Query query = getEm().createNamedQuery(Lancamento.LAST_LANCAMENTO);
    query.setParameter("date", cld.getTime());
    query.setParameter("servico", lancamento.getServico());
    List<Lancamento> lista = query.getResultList();
    if (lista.isEmpty()) {
      throw new LanceBusinessException("Sem lançamentos anteriores");
    } else {
      Lancamento lastLancamento = lista.iterator().next();
      Map<Object, BigDecimal> mapValorA = new HashMap<>();
      Map<Object, BigDecimal> mapValorB = new HashMap<>();

      lancamento.setAmmountToShare(lastLancamento.getAmmountToShare());

      for (LancamentoValor valorAnterior : lastLancamento.getValues()) {
        SetorDetalhe detalhe = valorAnterior.getContratoSetor().getSetor().getSetorDetalhe();
        Object key = valorAnterior.getContratoSetor().getSetor();
        if (detalhe != null) {
          if (TipoSetorEnum.VAH.equals(detalhe.getType())) {
            key = valorAnterior.getContratoSetor().getContrato().getContratante();
          }
        }
        mapValorA.put(key, valorAnterior.getValueA());
        mapValorB.put(key, valorAnterior.getValueB());
      }


      for (LancamentoValor valorLancamento : lancamento.getValues()) {
        SetorDetalhe detalhe = valorLancamento.getContratoSetor().getSetor().getSetorDetalhe();
        Object key = valorLancamento.getContratoSetor().getSetor();
        if (detalhe != null) {
          if (TipoSetorEnum.VAH.equals(detalhe.getType())) {
            key = valorLancamento.getContratoSetor().getContrato().getContratante();
          }
        }
        valorLancamento.setValueA(mapValorA.get(key));
        valorLancamento.setValueB(mapValorB.get(key));
      }
    }


    computeValues(lancamento);

    return lancamento;
  }
}