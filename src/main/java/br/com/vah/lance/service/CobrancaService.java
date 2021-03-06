package br.com.vah.lance.service;

import br.com.vah.lance.constant.EntradasRejeitadasEnum;
import br.com.vah.lance.constant.OcorrenciasRecebimentoEnum;
import br.com.vah.lance.dto.ArquivoUtils;
import br.com.vah.lance.dto.CobrancaDTO;
import br.com.vah.lance.entity.dbamv.*;
import br.com.vah.lance.entity.usrdbvah.*;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.util.VahUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jairoportela on 20/07/2016.
 */
@Stateless
public class CobrancaService extends DataAccessService<Cobranca> {

  public static final String DATA_BAIXA = "DATA_BAIXA";
  public static final String USUARIO = "USUARIO";
  public static final String MULTA_ACRESCIMO = "MULTA_ACRESCIMO";


  private
  @Inject
  LancamentoService lancamentoService;

  private
  @Inject
  ContaReceberService contaReceberService;

  private
  @Inject
  ContaReceberItemService contaReceberItemService;

  private
  @Inject
  RecebimentoService recebimentoService;

  private
  @Inject
  MovimentacaoService movimentacaoService;

  private
  @Inject
  RecebimentoMovimentoService recebMovService;


  public CobrancaService() {
    super(Cobranca.class);
  }

  /**
   * Busca as cobranças gravadas na base de dados pela vigência e/ou vencimento.
   *
   * @param vigencia
   * @param vencimento
   * @return
   */
  public List<Cobranca> buscarCobrancas(Date[] vigencia, Integer vencimento, Boolean ocultarRecebidos, Boolean exibirCancelados) {
    Criteria criteria = createCriteria();

    criteria.add(Restrictions.between("vigencia", vigencia[0], vigencia[1]));
    if (vencimento != null) {
      criteria.add(Restrictions.eq("vencimento", VahUtils.calcNextMonthDate(vigencia[0], vencimento)));
    }

    if (ocultarRecebidos) {
      criteria.add(Restrictions.eq("baixa", false));
    }

    if (!exibirCancelados) {
      criteria.add(Restrictions.eq("cancelado", false));
    }

    criteria.setFetchMode("contas", FetchMode.SELECT);
    criteria.setFetchMode("descritivo", FetchMode.SELECT);

    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    return criteria.list();
  }

  /**
   * Cria ou atualiza na base de dados as cobranças contidas em um array.
   *
   * @param cobrancas
   * @return
   */
  public List<Cobranca> gravarCobrancas(Cobranca[] cobrancas) throws LanceBusinessException {
    List<Cobranca> attachedCobrancas = new ArrayList<>();
    if (cobrancas != null && cobrancas.length > 0) {

      StringBuilder builder = new StringBuilder();

      List<Cobranca> toPersist = new ArrayList<>();

      for (Cobranca cobranca : cobrancas) {
        if (cobranca.getId() == null && checkCobranca(cobranca)) {
          if (cobranca.getSetor() != null) {
            builder.append(String.format("Setor: %s", cobranca.getSetor().getLabelForSelectItem()));
          }
          builder.append(String.format("Cliente: %s", cobranca.getCliente().getLabelForSelectItem()));
          builder.append("\r\n");
        } else {
          toPersist.add(cobranca);
        }
      }


      if (builder.length() > 0) {
        throw new LanceBusinessException("Já existe cobranças para os seguintes items: \r\n" + builder.toString());
      }


      for (Cobranca cobranca : toPersist) {
        if (cobranca.getId() == null) {
          attachedCobrancas.add(create(cobranca));
        } else {
          attachedCobrancas.add(update(cobranca));
        }
      }
    }
    return attachedCobrancas;
  }

  private Map<String, ContaReceber> contasLancamento(Lancamento lancamento) {
    Map<String, ContaReceber> contas = new HashMap<>();
    for (ContaReceber contaReceber : lancamento.getContasReceber()) {
      Fornecedor cliente = contaReceber.getCliente();
      Long cdSetor = contaReceber.getItensRateio().iterator().next().getCdSetor();
      if (!lancamento.getServico().getAgrupavel()) {
        cdSetor = 0l;
      }
      String chave = String.format("%d - %d - %d", cliente.getId(), cdSetor, lancamento.getServico().getDiaVencimento());
      contas.put(chave, contaReceber);
    }
    return contas;
  }

  public Cobranca cancelarCobranca(Cobranca cobranca) {
    cobranca.setCancelado(true);
    return update(cobranca);
  }

  public Map<String, Cobranca> recuperarCobrancas(Date[] vigencia, Integer diaVencimento) {
    Criteria criteria = createCriteria();
    if (diaVencimento == null) {
      criteria.add(Restrictions.between("vigencia", vigencia[0], vigencia[1]));
    } else {
      Calendar cld = Calendar.getInstance();
      cld.setTime(vigencia[0]);
      cld.add(Calendar.MONTH, 1);
      cld.set(Calendar.DAY_OF_MONTH, diaVencimento);
      criteria.add(Restrictions.eq("vencimento", cld.getTime()));
    }
    criteria.add(Restrictions.eq("cancelado", false));
    List<Cobranca> cobrancas = criteria.list();
    Map<String, Cobranca> maps = new HashMap<>();
    for (Cobranca cobranca : cobrancas) {
      Long cdSetor = cobranca.getSetor() == null ? 0l : cobranca.getSetor().getId();
      Integer vencimento = Integer.valueOf(ArquivoUtils.formatDate(cobranca.getVencimento(), "dd"));
      String chave = String.format("%d - %d - %d", cobranca.getCliente().getId(), cdSetor, vencimento);
      maps.put(chave, cobranca);
    }
    return maps;
  }

  /**
   * Realiza uma varredura nos lançamentos verificando
   *
   * @param vigencia
   * @param vencimento
   * @return
   */
  public List<Cobranca> gerarCobrancas(Date[] vigencia, Integer vencimento) throws LanceBusinessException {

    List<Lancamento> lancamentos = lancamentoService.recuperarLancamentosValidados(vigencia, vencimento);

    Calendar cld = Calendar.getInstance();
    cld.setTime(vigencia[0]);
    cld.add(Calendar.MONTH, 1);

    Map<String, Cobranca> cobrancasMap = new HashMap<>();

    Map<String, Cobranca> attachedCobrancas = recuperarCobrancas(vigencia, vencimento);

    for (Lancamento lancamento : lancamentos) {

      Map<String, ContaReceber> contas = contasLancamento(lancamento);

      Servico servico = lancamento.getServico();

      cld.set(Calendar.DAY_OF_MONTH, servico.getDiaVencimento());

      Date dataVencimento = cld.getTime();

      lancamento.getContasReceber();

      Boolean cobraPorSetor = lancamento.getServico().getAgrupavel();

      for (LancamentoValor valor : lancamento.getValues()) {
        if (BigDecimal.ZERO.equals(valor.getValue())) {
          continue;
        }
        ContratoSetor contratoSetor = valor.getContratoSetor();
        Fornecedor cliente = contratoSetor.getContrato().getContratante();
        Setor setor = contratoSetor.getSetor();

        Long cdSetor = setor.getId();

        if (!lancamento.getServico().getAgrupavel()) {
          cdSetor = 0l;
        }

        String chave = String.format("%d - %d - %d", cliente.getId(), cdSetor, lancamento.getServico().getDiaVencimento());

        Cobranca attachedCobranca = attachedCobrancas.get(chave);

        if (attachedCobranca != null) {
          continue;
        }

        Cobranca cobranca = cobrancasMap.get(chave);

        if (cobranca == null) {
          cobranca = new Cobranca();
          cobranca.setVigencia(vigencia[0]);
          cobranca.setVencimento(dataVencimento);
          if (cobraPorSetor) {
            cobranca.setSetor(setor);
          }
          cobranca.setCliente(cliente);
          cobrancasMap.put(chave, cobranca);
        }

        ContaReceber conta = contas.get(chave);

        if (conta != null) {
          cobranca.getContas().add(conta);
        }

        ItemCobranca item = new ItemCobranca();
        item.setCobranca(cobranca);
        item.setServico(lancamento.getServico());
        item.setTotal(lancamento.getTotalValue());
        item.setValor(valor.getValue());
        cobranca.getDescritivo().add(item);
        cobranca.setValor(cobranca.getValor().add(valor.getValue()));
      }
    }

    List<Cobranca> cobrancas = new ArrayList<>();

    // Remover itens já persistidos
    for (Cobranca cobranca : cobrancasMap.values()) {
      if (!BigDecimal.ZERO.equals(cobranca.getValor()) && cobranca.getId() == null) {
        cobrancas.add(cobranca);
      }
    }

    Collections.sort(cobrancas, new Comparator<Cobranca>() {
      @Override
      public int compare(Cobranca o1, Cobranca o2) {
        return o1.getCliente().getTitle().compareTo(o2.getCliente().getTitle());
      }
    });

    return cobrancas;
  }

  /**
   * Verifica se já existe cobrança com o mesmo vencimento para o cliente/setor informado na nova cobrança.
   *
   * @param cobranca
   * @return {@true} se houver {@false} se não.
   */
  public Boolean checkCobranca(Cobranca cobranca) {
    return recuperarCobranca(cobranca) != null;

  }

  public Cobranca recuperarCobranca(Cobranca cobranca) {
    Criteria criteria = createCriteria();
    if (cobranca.getSetor() != null) {
      criteria.add(Restrictions.eq("setor", cobranca.getSetor()));
    }
    criteria.add(Restrictions.eq("cliente", cobranca.getCliente()));
    criteria.add(Restrictions.eq("vencimento", cobranca.getVencimento()));
    criteria.add(Restrictions.eq("cancelado", false));
    if (criteria.list().size() == 0) {
      return null;
    } else {
      return (Cobranca) criteria.list().iterator().next();
    }
  }

  public void salvarNotaFiscal(List<ContaReceber> contas) throws LanceBusinessException {
    for (ContaReceber contaReceber : contas) {
      if (contaReceber.getNumeroDocumento() == null) {
        throw new LanceBusinessException("Número de documento obrigatório");
      }
    }
    for (ContaReceber contaReceber : contas) {
      contaReceber.setDescricao(String.format("%s - %s", contaReceber.getNumeroDocumento(), contaReceber.getCliente().getTitle()));
      contaReceberService.update(contaReceber);
    }
  }

  public void salvarNotaFiscal(Lancamento lancamento) throws LanceBusinessException {
    salvarNotaFiscal(lancamento.getContasReceber());
  }

  public void salvarNotaFiscal(Cobranca cobranca) throws LanceBusinessException {
    salvarNotaFiscal(new ArrayList<ContaReceber>(cobranca.getContas()));
  }

  public void validarDataBaixaCobranca(Date data) throws LanceBusinessException {
    if (data == null) {
      throw new LanceBusinessException("Data de baixa obrigatória.");
    }
    if (data.compareTo(new Date()) > 0) {
      throw new LanceBusinessException("Data de baixa maior que data atual.");
    }
  }

  public void validarValorBaixa(Cobranca cobranca, BigDecimal valor) throws LanceBusinessException {

    if (valor == null) {
      throw new LanceBusinessException("Valor de baixa obrigatório");
    }

    if (!cobranca.getValor().equals(valor)) {
      throw new LanceBusinessException("Valores de cobrança e de baixa divergentes");
    }
  }

  public void receberCobrancas(List<Cobranca> cobrancas, Map<String, Object> params, Boolean validar) throws LanceBusinessException {
    for (Cobranca cobranca : cobrancas) {
      receberCobranca(cobranca, params, validar);
    }
  }

  public void receberCobrancasDTOs(CobrancaDTO[] dtos, String usuario, Date dataCompensado) throws LanceBusinessException {
    for (CobrancaDTO dto : dtos) {
      Map<String, Object> params = new HashMap<>();
      params.put(DATA_BAIXA, dataCompensado);
      params.put(USUARIO, usuario);
      if (!BigDecimal.ZERO.equals(dto.getMulta())) {
        params.put(MULTA_ACRESCIMO, dto.getMulta());
      }
      receberCobranca(dto.getCobranca(), params, false);
    }

  }

  public void receberCobranca(Cobranca dettach, Map<String, Object> params, Boolean validar) throws LanceBusinessException {
    Cobranca cobranca = initializeContas(dettach.getId());
    Date dataBaixa = (Date) params.get(DATA_BAIXA);
    String usuario = (String) params.get(USUARIO);
    BigDecimal multaAcrescimo = (BigDecimal) params.get(MULTA_ACRESCIMO);
    DescontoAcrescimo tipoDesconto = new DescontoAcrescimo();
    tipoDesconto.setId(20l);
    tipoDesconto.setDescricao("JUROS CLIENTES");
    tipoDesconto.setTipo("A");


    if (validar) {
      validarDataBaixaCobranca(dataBaixa);
    }

    if (cobranca.getBaixa()) {
      throw new LanceBusinessException("Cobrança com baixa já realizada pelo Lance.");
    }

    Recebimento aDescontarAcrescer = null;

    // Para cada conta a receber que compor a cobrança, crie um recebimento

    List<Recebimento> recebimentosToPersist = new ArrayList<>();
    List<Movimentacao> movimentacaosToPersist = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    String docIdentificacao = String.format("DOC - %s", sdf.format(dataBaixa));

    BigDecimal totalProporcional = BigDecimal.ZERO;

    for (ContaReceber conta : cobranca.getContas()) {

      ContaReceberItem item = conta.getItensConta().iterator().next();
      ContaReceberItem attachedItem = contaReceberItemService.find(item.getId());

      if (attachedItem.getRecebimentos().size() > 0) {
        throw new LanceBusinessException(String.format("Cobrança [%d] já foi recebido.", cobranca.getId()));
      }

      Recebimento recebimento = new Recebimento();

      String descricao = String.format("RECEBIMENTO BOLETO %d - %s", cobranca.getId(), cobranca.getCliente().getTitle());

      recebimento.setItem(item);
      recebimento.setDataRecebimento(dataBaixa);
      recebimento.setDataCheque(dataBaixa);
      recebimento.setValorRecebido(conta.getValorBruto());
      recebimento.setValorMoeda(conta.getValorBruto());
      recebimento.setNomeUsuario(usuario);
      recebimento.setDescricao(descricao);

      if (aDescontarAcrescer == null
          || aDescontarAcrescer.getValorRecebido().compareTo(recebimento.getValorRecebido()) < 0) {
        aDescontarAcrescer = recebimento;
      }

      if (descricao.length() > 50) {
        descricao = descricao.substring(0, 50);
      }

      Movimentacao movimentacao = new Movimentacao();
      RecebimentoMovimentacao recebimentoMov = new RecebimentoMovimentacao();
      recebimentoMov.setMovimentacao(movimentacao);
      recebimentoMov.setRecebimento(recebimento);
      movimentacao.getRecebimentos().add(recebimentoMov);

      movimentacao.setValor(conta.getValorBruto());
      movimentacao.setValorMoeda(conta.getValorBruto());
      movimentacao.setData(dataBaixa);
      movimentacao.setNumeroIdentificacao(docIdentificacao);
      movimentacao.setDescricao(descricao);
      movimentacao.setDescricaoPadrao(descricao);
      movimentacao.setDescricaoProcesso(descricao);

      movimentacaosToPersist.add(movimentacao);
      recebimentosToPersist.add(recebimento);

      // MULTA/ACRESCIMO

      if (multaAcrescimo != null) {
        BigDecimal proporcional = multaAcrescimo.multiply(conta.getValorBruto()).divide(cobranca.getValor(), 2, BigDecimal.ROUND_HALF_UP);
        totalProporcional = totalProporcional.add(proporcional);
        RecebimentoDescAcres acrescimo = new RecebimentoDescAcres();
        acrescimo.setRecebimento(recebimento);
        acrescimo.setValor(proporcional);
        acrescimo.setValorMoeda(proporcional);
        acrescimo.setDescontoAcrescimo(tipoDesconto);
        acrescimo.setDescricaoDescAcres(String.format("JUROS INCORRIDOS - %s", cobranca.getCliente().getTitle()));
        acrescimo.setTipoDescAcres("A");

        recebimento.getDescontosAcrescimos().add(acrescimo);
        recebimento.setValorAcrescimo(proporcional);
        // JUROS FORNECEDOR

        // Removido dia 19/04/2017
        // Verificou-se que o acrescimo informado no lance
        // não deve ser inserido ao valor da conta, mas somente
        // às suas parcelas.

        // conta.setValorAcrescimo(proporcional);
        // conta.setCodigoAcrescimo(1);
        BigDecimal valorLiquido = conta.getValorBruto().add(proporcional);

        recebimento.setValorRecebido(valorLiquido);
        movimentacao.setValor(valorLiquido);
        movimentacao.setValorMoeda(valorLiquido);

      }

    }

    if (multaAcrescimo != null) {
      BigDecimal delta = multaAcrescimo.subtract(totalProporcional);
      if (!BigDecimal.ZERO.equals(delta)) {
        Movimentacao mov = movimentacaosToPersist.iterator().next();
        Recebimento receb = recebimentosToPersist.iterator().next();
        mov.setValor(mov.getValor().add(delta));
        receb.setValorAcrescimo(receb.getValorAcrescimo().add(delta));
        receb.setValorMoedaAcrescimo(receb.getValorMoedaAcrescimo().add(delta));
        RecebimentoDescAcres acresc = receb.getDescontosAcrescimos().iterator().next();
        acresc.setValor(acresc.getValor().add(delta));
        acresc.setValorMoeda(acresc.getValorMoeda().add(delta));
      }
    }

    cobranca.setBaixa(true);
    cobranca.setDataBaixa(new Date());

    try {
      update(cobranca);
      recebimentoService.persistList(recebimentosToPersist);
      movimentacaoService.persistList(movimentacaosToPersist);
    } catch (Exception e) {
      throw new LanceBusinessException(e.getMessage());
    }

  }

  public void cancelarRecebimento(Cobranca dettached) {

    Cobranca cobranca = initializeContas(dettached.getId());

    Set<Recebimento> recebimentosToRemove = new HashSet<>();
    Set<Movimentacao> movimentacoesToRemove = new HashSet<>();
    Set<RecebimentoMovimentacao> recebMovToRemove = new HashSet<>();

    for (ContaReceber conta : cobranca.getContas()) {
      for (ContaReceberItem item : conta.getItensConta()) {
        List<Recebimento> recebimentos = recebimentoService.recebimentosByItem(item);
        for (Recebimento recebimento : recebimentos) {
          recebimentosToRemove.add(recebimento);
          List<RecebimentoMovimentacao> movimentacoes = recebMovService.movimentacaoByRecebimento(recebimento);
          for (RecebimentoMovimentacao recebMov : movimentacoes) {
            movimentacoesToRemove.add(recebMov.getMovimentacao());
            recebMovToRemove.add(recebMov);
          }
        }
      }
    }

    for (RecebimentoMovimentacao recebMov : recebMovToRemove) {
      recebMovService.delete(recebMov.getId());
    }

    for (Movimentacao mov : movimentacoesToRemove) {
      movimentacaoService.delete(mov.getId());
    }

    for (Recebimento receb : recebimentosToRemove) {
      recebimentoService.delete(receb.getId());
    }

    cobranca.setBaixa(false);

    update(cobranca);

  }

  public void criarMovimentacoes(Cobranca cobranca) {
    Cobranca attached = find(cobranca.getId());
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    List<Movimentacao> movimentacaosToPersist = new ArrayList<>();
    for (ContaReceber conta : attached.getContas()) {
      for (ContaReceberItem item : conta.getItensConta()) {
        for (Recebimento recebimento : item.getRecebimentos()) {

          String descricao = recebimento.getDescricao();

          if (descricao.length() > 50) {
            descricao = descricao.substring(0, 50);
          }

          Movimentacao movimentacao = new Movimentacao();
          RecebimentoMovimentacao recebimentoMov = new RecebimentoMovimentacao();
          recebimentoMov.setMovimentacao(movimentacao);
          recebimentoMov.setRecebimento(recebimento);
          movimentacao.getRecebimentos().add(recebimentoMov);

          movimentacao.setValor(conta.getValorBruto());
          movimentacao.setData(recebimento.getDataRecebimento());
          movimentacao.setNumeroIdentificacao(String.format("DOC - %s", sdf.format(recebimento.getDataRecebimento())));
          movimentacao.setDescricao(descricao);
          movimentacao.setDescricaoPadrao(descricao);
          movimentacao.setDescricaoProcesso(descricao);
          movimentacaosToPersist.add(movimentacao);
        }
      }
    }
    movimentacaoService.persistList(movimentacaosToPersist);

  }


  public static final String firstLine0To26 = "02RETORNO01COBRANCA       ";

  private void validarPrimeiraLinha(String line) throws LanceBusinessException {
    if (line == null) {
      throw new LanceBusinessException("Primeira linha do arquivo é nula");
    }
    if (!firstLine0To26.equals(line.substring(0, 26))) {
      throw new LanceBusinessException("Posição 0 a 26 da primeira linha inválida");
    }

  }

  public static final String MENSAGENS = "MENSAGENS";
  public static final String COBRANCAS = "COBRANCAS";
  public static final String CONFIRMADAS = "CONFIRMADAS";

  public Map<String, Object> processarArquivoRetorno(byte[] content) throws LanceBusinessException {
    Map<String, Object> resultadoProcessamento = new HashMap<>();

    InputStream is = null;

    List<String> mensagens = new ArrayList<>();
    List<CobrancaDTO> cobrancas = new ArrayList<>();
    List<Cobranca> confirmadas = new ArrayList<>();

    resultadoProcessamento.put(MENSAGENS, mensagens);
    resultadoProcessamento.put(COBRANCAS, cobrancas);
    resultadoProcessamento.put(CONFIRMADAS, confirmadas);

    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");

    try {
      is = new ByteArrayInputStream(content);
      BufferedReader bf = new BufferedReader(new InputStreamReader(is));
      // Primeira linha
      Integer lCount = 1;
      Integer ignorados = 0;
      String line = bf.readLine();
      validarPrimeiraLinha(line);
      String seqArRet = line.substring(108, 113);
      String seqReg = line.substring(394);
      while ((line = bf.readLine()) != null) {
        lCount++;
        String tipoRegistro = line.substring(0, 1);
        if (tipoRegistro.equals("1")) {
          String usoEmpresa = line.substring(37, 62).trim();
          String nossoNumero = line.substring(62, 70);
          String valorTitulo = line.substring(152, 165);
          String tarifaCobranca = line.substring(175, 188);
          String iof = line.substring(214, 227);
          String valorAbatimento = line.substring(227, 240);
          String descontos = line.substring(240, 253);
          String valorPrincipal = line.substring(253, 266);
          String jurosMoraMulta = line.substring(266, 279);
          String outrosCreditos = line.substring(279, 292);
          String mensagem = line.substring(377, 385).trim();

          BigDecimal valor = new BigDecimal(valorTitulo.substring(0, 11) + "." + valorTitulo.substring(11));
          BigDecimal multa = new BigDecimal(jurosMoraMulta.substring(0, 11) + "." + jurosMoraMulta.substring(11));

          Date dataOcorrencia = sdf.parse(line.substring(110, 116));

          OcorrenciasRecebimentoEnum codigoOcorrencia = OcorrenciasRecebimentoEnum.byValue(line.substring(108, 110));

          // OCORRÊNCIA 02 - ENTRADA CONFIRMADA
          if (codigoOcorrencia.equals(OcorrenciasRecebimentoEnum._02)) {
            try {
              Cobranca cobranca = find(Long.valueOf(usoEmpresa));
              if (cobranca.getLiquidado()) {
                mensagens.add(String.format("Linha %03d: Cobrança [%d] já confirmada.", lCount, cobranca.getId()));
                ignorados++;
                continue;
              } else {
                confirmadas.add(cobranca);
              }
            } catch (Exception e) {
              mensagens.add(String.format("Linha %03d: Registro sem correspondência no Lance (%s).", lCount, nossoNumero));
              continue;
            }
          } else if (codigoOcorrencia.equals(OcorrenciasRecebimentoEnum._03)) { // OCORRÊNCIA 03 - REJEITADA
            EntradasRejeitadasEnum motivoRejeicao = EntradasRejeitadasEnum.getByCodigo(mensagem.substring(0, 2));
            if (motivoRejeicao == null) {
              mensagens.add(String.format("Linha %03d: Cobrança [%s] rejeitada.", lCount, usoEmpresa));
              continue;
            } else {
              mensagens.add(String.format("Linha %03d: Cobrança [%s] rejeitada. Motivo: %s", lCount, usoEmpresa, motivoRejeicao.getLabel()));
              if (motivoRejeicao.equals(EntradasRejeitadasEnum._14)) {
                try {
                  Cobranca cobranca = find(Long.valueOf(usoEmpresa));
                  confirmadas.add(cobranca);
                } catch (Exception e) {
                  mensagens.add(String.format("Linha %03d: Registro rejeitado sem correspondência no Lance (%s): %s", lCount, nossoNumero, motivoRejeicao.getLabel()));
                }
              }
              continue;
            }
            // OCORRÊNCIA 06 - LIQUIDAÇÃO NORMAL ou 08 - LIQUIDAÇÃO EM CARTÓRIO
          } else if (codigoOcorrencia.equals(OcorrenciasRecebimentoEnum._06)
              || codigoOcorrencia.equals(OcorrenciasRecebimentoEnum._08)) {

            try {
              Cobranca cobranca = find(Long.valueOf(usoEmpresa));
              if (cobranca.getBaixa()) {
                mensagens.add(String.format("Linha %03d: Cobrança [%d] já recebida.", lCount, cobranca.getId()));
                ignorados++;
                continue;
              } else {
                CobrancaDTO dto  = new CobrancaDTO(cobranca, dataOcorrencia, multa);
                cobrancas.add(dto);
              }
            } catch (Exception e) {
              mensagens.add(String.format("Linha %03d: Liquidação de registro sem correspondência no Lance (%s).", lCount, nossoNumero));
              continue;
            }
          } else {
            mensagens.add(String.format("Linha %03d: Cobrança [%s], ocorrência [%s]: %s.", lCount, usoEmpresa, codigoOcorrencia.getValue(), codigoOcorrencia.getLabel()));
          }

        } else if (tipoRegistro.equals("9")) {
          break;
        }
      }
      String qtdTitulos = line.substring(17, 25);
      String valorTotalCobranca = line.substring(25, 39);
    } catch (ParseException pe) {
      throw new LanceBusinessException("Erro na leitura da data");
    } catch (IOException e) {
      throw new LanceBusinessException("Erro na leitura do arquivo");
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {

        }
      }
    }


    return resultadoProcessamento;
  }

  public Cobranca initializeLists(Long id) {
    Cobranca cobranca = find(id);
    new HashSet<>(cobranca.getDescritivo());
    new HashSet<>(cobranca.getContas());
    return cobranca;
  }

  public Cobranca initializeDescritivo(Long id) {
    Cobranca cobranca = find(id);
    new HashSet<>(cobranca.getDescritivo());
    return cobranca;
  }

  public Cobranca initializeContas(Long id) {
    Cobranca cobranca = find(id);
    new HashSet<>(cobranca.getContas());
    return cobranca;
  }

}
