package br.com.vah.lance.service;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.dto.ArquivoUtils;
import br.com.vah.lance.entity.dbamv.*;
import br.com.vah.lance.entity.usrdbvah.*;
import br.com.vah.lance.reports.BalancoContabilDTO;
import br.com.vah.lance.reports.DescritivoCondominioDTO;
import br.com.vah.lance.reports.RelatorioSetorDTO;
import br.com.vah.lance.reports.ReportLoader;
import br.com.vah.lance.util.VahUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.inject.Inject;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by jairoportela on 06/07/2016.
 */
public class RelatorioService implements Serializable {

  private
  @Inject
  ReportLoader reportLoader;

  private
  @Inject
  LancamentoService lancamentoService;

  private
  @Inject
  ContratoService contratoService;

  public StreamedContent getRelatorioDescritivoCondominio(Fornecedor cliente, Date vigencia, EstadoLancamentoEnum status, Boolean condominio) {

    Map<String, Object> parameters = new HashMap<>();

    Date[] range = VahUtils.getDateRange(vigencia);

    List<Lancamento> lancamentos = lancamentoService.getLancamentosPorPeriodoStatus(status, range);

    List<DescritivoCondominioDTO> datasource = new ArrayList<>();

    for (Lancamento lancamento : lancamentos) {

      if (!condominio) {
        continue;
      }

      Servico servico = lancamento.getServico();

      Calendar cld = Calendar.getInstance();
      cld.setTime(lancamento.getEffectiveOn());
      cld.add(Calendar.MONTH, 1);
      cld.set(Calendar.DAY_OF_MONTH, servico.getDiaVencimento());
      Date vencimento = cld.getTime();

      Map<Fornecedor, ContaReceber> mapContaReceber = new HashMap<>();
      Map<Fornecedor, DescritivoCondominioDTO> mapDTO = new HashMap<>();

      for (ContaReceber conta : lancamento.getContasReceber()) {
        mapContaReceber.put(conta.getCliente(), conta);
      }

      for (LancamentoValor valorLancamento : lancamento.getValues()) {

        Fornecedor contratante = valorLancamento.getContratoSetor().getContrato().getContratante();

        if (cliente != null && !cliente.equals(contratante)) {
          continue;
        }

        DescritivoCondominioDTO dto = mapDTO.get(contratante);

        if (dto == null) {
          dto = new DescritivoCondominioDTO();
          dto.setNomeCliente(contratante.getTitle());
          dto.setServico(servico.getTitle());
          dto.setTotal(lancamento.getTotalValue());
          dto.setRateio(BigDecimal.ZERO);
          dto.setNomeSetor(valorLancamento.getContratoSetor().getSetor().getTitle());
          dto.setVencimento(ArquivoUtils.formatDate(vencimento, "dd/MM/yyyy"));
          if (mapContaReceber.get(contratante) == null) {
            dto.setNumeroDocumento("-");
          } else {
            dto.setNumeroDocumento(mapContaReceber.get(contratante).getNumeroDocumento());
          }
          mapDTO.put(contratante, dto);
          datasource.add(dto);
        }

        dto.setRateio(dto.getRateio().add(valorLancamento.getValue()));
      }

    }

    Collections.sort(datasource, new Comparator<DescritivoCondominioDTO>() {
      @Override
      public int compare(DescritivoCondominioDTO o1, DescritivoCondominioDTO o2) {
        return o1.getNomeCliente().compareTo(o2.getNomeCliente());
      }
    });

    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    parameters.put("REFERENCIA", sdf.format(vigencia));
    return reportLoader.imprimeRelatorio("descritivoCondominio", parameters, datasource, null);

  }

  private void ordenarDescritivo(List<DescritivoCondominioDTO> dtos) {
    Collections.sort(dtos, new Comparator<DescritivoCondominioDTO>() {
      @Override
      public int compare(DescritivoCondominioDTO o1, DescritivoCondominioDTO o2) {
        return o1.getServico().compareTo(o2.getServico());
      }
    });
  }

  public StreamedContent descritivos(Cobranca[] cobrancas, User user) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    ZipOutputStream zos = new ZipOutputStream(baos);
    try {
      for (Cobranca cobranca : cobrancas) {
        String fileName = cobranca.getCliente().getId().toString();
        byte[] buffer = new byte[1024];
        if (cobranca.getSetor() != null) {
          fileName += "-" + cobranca.getSetor().getId();
        }
        fileName += ArquivoUtils.formatDate(cobranca.getVencimento(), "ddMMyy") + ".pdf";
        ZipEntry ze = new ZipEntry(fileName);
        zos.putNextEntry(ze);
        InputStream in;
        if (cobranca.getSetor() == null) {
          in = descritivoGeralIN(cobranca, user);
        } else {
          in = descritivoIN(cobranca, user);
        }
        int len;
        while ((len = in.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }
        in.close();
        zos.closeEntry();
      }

      zos.close();

      DefaultStreamedContent dsc = new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()));
      dsc.setContentType("application/zip");
      dsc.setName("DESCRITIVOS.zip");

      return dsc;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public InputStream descritivoGeralIN(Cobranca cobranca, User user) {
    Map<String, Object> parameters = new HashMap<>();
    List<DescritivoCondominioDTO> datasource = new ArrayList<>();

    for (ItemCobranca item : cobranca.getDescritivo()) {
      DescritivoCondominioDTO dto = new DescritivoCondominioDTO();
      dto.setNumeroDocumento(cobranca.getId() == null ? "-" : cobranca.getId().toString());
      dto.setTotal(item.getTotal());
      dto.setRateio(item.getValor());
      dto.setServico(item.getServico().getTitle());
      dto.setNomeSetor("-");
      dto.setVencimento(ArquivoUtils.formatDate(cobranca.getVencimento(), "dd/MM/yyyy"));
      dto.setNomeCliente(cobranca.getCliente().getTitle());
      datasource.add(dto);
    }

    parameters.put("AUTOR", user.getLogin());
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    parameters.put("REFERENCIA", sdf.format(cobranca.getVigencia()));
    parameters.put("TOTAL_CONDOMINIO", cobranca.getValor());

    return reportLoader.getReportAsInputStream("descritivoGeral", parameters, datasource);
  }

  public StreamedContent descritivoGeral(Cobranca cobranca, User user) {

    String fileName = cobranca.getCliente().getId().toString();

    if (cobranca.getSetor() != null) {
      fileName += " - " + cobranca.getSetor();
    }

    fileName += "-DESC-GERAL";

    return reportLoader.printReport(descritivoGeralIN(cobranca, user), fileName);
  }

  public InputStream descritivoIN(Cobranca cobranca, User user) {
    Map<String, Object> parameters = new HashMap<>();

    Calendar cld = Calendar.getInstance();
    cld.setTime(cobranca.getVigencia());
    cld.add(Calendar.MONTH, -1);

    String consumoEnergia = "-";
    String pesoColeta = "-";
    BigDecimal valorEnergia = BigDecimal.ZERO;
    BigDecimal valorColeta = BigDecimal.ZERO;
    BigDecimal valorTelefonia = BigDecimal.ZERO;
    BigDecimal valorTR = BigDecimal.ZERO;
    BigDecimal valorInternet = BigDecimal.ZERO;

    parameters.put("AUTOR", user.getLogin());
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    parameters.put("REFERENCIA", sdf.format(cld.getTime()));
    List<DescritivoCondominioDTO> datasource = new ArrayList<>();

    for (ItemCobranca item : cobranca.getDescritivo()) {

      Servico servico = item.getServico();

      if (TipoServicoEnum.ENERGIA_PRIVADA.equals(servico.getType())) {
        valorEnergia = item.getValor();
        continue;
      }

      if (TipoServicoEnum.TAXA_REFRIGERACAO.equals(servico.getType())) {
        valorTR = item.getValor();
        continue;
      }

      Long contaCusto = servico.getContaCusto().getId();

      if (contaCusto.equals(269l)) {
        valorInternet = item.getValor();
        continue;
      }

      if (contaCusto.equals(272l)) {
        valorTelefonia = item.getValor();
        continue;
      }

      DescritivoCondominioDTO dto = new DescritivoCondominioDTO();
      dto.setNumeroDocumento(cobranca.getId() == null ? "-" : cobranca.getId().toString());
      dto.setTotal(item.getTotal());
      dto.setRateio(item.getValor());
      dto.setServico(servico.getTitle());
      dto.setNomeSetor(cobranca.getSetor().getTitle());
      dto.setVencimento(ArquivoUtils.formatDate(cobranca.getVencimento(), "dd/MM/yyyy"));
      dto.setNomeCliente(cobranca.getCliente().getTitle());
      datasource.add(dto);
    }

    parameters.put("VALOR_ENERGIA", valorEnergia);
    parameters.put("VALOR_COLETA", valorColeta);
    parameters.put("VALOR_TR", valorTR);
    parameters.put("VALOR_INTERNET", valorInternet);
    parameters.put("VALOR_TELEFONIA", valorTelefonia);
    parameters.put("CONSUMO_ENERGIA", consumoEnergia);
    parameters.put("PESO_COLETA", pesoColeta);
    parameters.put("TOTAL_CONDOMINIO", cobranca.getValor());

    ordenarDescritivo(datasource);

    return reportLoader.getReportAsInputStream("descritivoCondominio", parameters, datasource);
  }

  public StreamedContent descritivo(Cobranca cobranca, User user) {

    String fileName = cobranca.getCliente().getId().toString();
    if (cobranca.getSetor() != null) {
      fileName += "-" + cobranca.getSetor().getId();
    }

    fileName += "-DESC";

    return reportLoader.printReport(descritivoIN(cobranca, user), fileName);
  }

  public StreamedContent getRelatorioSetor() {
    Map<String, Object> parameters = new HashMap<>();

    List<Contrato> contratos = contratoService.findWithNamedQuery(Contrato.ALL);

    List<RelatorioSetorDTO> datasource = new ArrayList<>();

    Date today = new Date();

    for (Contrato contrato : contratos) {

      if (today.after(contrato.getBeginDate()) && today.before(contrato.getEndDate())) {
        Fornecedor contratante = contrato.getContratante();
        for (ContratoSetor contratoSetor : contrato.getSetores()) {
          Setor setor = contratoSetor.getSetor();
          Fornecedor inquilino = contratoSetor.getInquilino();
          RelatorioSetorDTO dto = new RelatorioSetorDTO();
          dto.setCodigoSetor(setor.getId());
          dto.setNomeSetor(setor.getTitle());
          if (inquilino == null) {
            dto.setCodigoCliente(contratante.getId());
            dto.setNomeCliente(contratante.getNomeFantasia());
          } else {
            dto.setTipo("I");
            dto.setCodigoCliente(inquilino.getId());
            dto.setNomeCliente(inquilino.getNomeFantasia());
          }
          StringBuffer buffer = new StringBuffer();
          for (Servico servico : contratoSetor.getServicos()) {
            buffer.append("* ");
            buffer.append(servico.getTitle());
            buffer.append("\n");
          }
          dto.setServicos(buffer.toString());
          datasource.add(dto);
        }
      }
    }
    Collections.sort(datasource, new Comparator<RelatorioSetorDTO>() {
      @Override
      public int compare(RelatorioSetorDTO o1, RelatorioSetorDTO o2) {
        return o1.getNomeCliente().compareTo(o2.getNomeCliente());
      }
    });
    return reportLoader.imprimeRelatorio("setorRelatorio", parameters, datasource, null);
  }

  public StreamedContent relatorioBalancoCondominial(Date vigencia, User usuario) {

    Date[] range = VahUtils.getDateRange(vigencia);

    Map<String, Object> params = new HashMap<>();
    params.put("begin", range[0]);
    params.put("end", range[1]);
    List<BalancoContabilDTO> datasource = new ArrayList<>();

    List<Lancamento> lancamentos = lancamentoService.findWithNamedQuery(Lancamento.COND_VIGENCIA, params);

    for (Lancamento lancamento : lancamentos) {
      if (lancamento.getServico().getAgrupavel()) {
        lancamentoToDtos(lancamento, datasource, true);
      }

    }

    String fileName = "SERVCOND-" + ArquivoUtils.formatDate(vigencia, "ddMMyy");

    return relatorioBalanco(datasource, vigencia, 22, fileName, usuario, true);
  }

  private void lancamentoToDtos(Lancamento lancamento, List<BalancoContabilDTO> datasource, Boolean condominio) {
    Calendar cld = Calendar.getInstance();
    cld.setTime(lancamento.getEffectiveOn());
    cld.add(Calendar.MONTH, 1);
    cld.set(Calendar.DAY_OF_MONTH, 1);
    cld.add(Calendar.DAY_OF_MONTH, -1);
    for (LancamentoValor val : lancamento.getValues()) {

      if (BigDecimal.ZERO.equals(val.getValue())) {
        continue;
      }

      Servico serv = lancamento.getServico();
      PlanoConta conta = serv.getContaContabil();
      PlanoConta compartilhada = serv.getContaResultado();

      ContratoSetor contratoSetor = val.getContratoSetor();
      Setor setor = contratoSetor.getSetor();
      if (serv.getContaPorSetor()) {
        SetorDetalhe setorDetalhe = setor.getSetorDetalhe();
        if (setorDetalhe != null && setorDetalhe.getContaContabil() != null) {
          conta = setorDetalhe.getContaContabil();
        }
      }
      BalancoContabilDTO dto = new BalancoContabilDTO();
      dto.setSetor(setor.getId().toString());
      ItemResultado contaCusto = lancamento.getServico().getContaCusto();
      dto.setContaCusto(String.format("%d - %s", contaCusto.getId(), contaCusto.getTitle()));

      if (condominio && datasource.contains(dto)) {
        dto = datasource.get(datasource.indexOf(dto));
        dto.setValor(dto.getValor().add(val.getValue()));
      } else {
        dto.setCliente(contratoSetor.getInquilino() == null ? contratoSetor.getContrato().getContratante().getTitle() : contratoSetor.getInquilino().getTitle());

        if (condominio) {
          dto.setServico("SERVIÇOS CONDOMÍNIO");
        } else {
          dto.setServico(serv.getTitle());
        }

        dto.setReduzido(conta.getId().toString());
        dto.setConta(conta.getCodigoContabil());
        dto.setNomeConta(conta.getTitle());

        dto.setReduzidoCompart(compartilhada.getId().toString());
        dto.setContaCompart(compartilhada.getCodigoContabil());
        dto.setNomeContaCompart(compartilhada.getTitle());

        dto.setCompetencia(cld.getTime());
        dto.setValor(val.getValue());
        datasource.add(dto);
      }

    }
  }

  private void ordenarDtos(List<BalancoContabilDTO> datasource) {
    Collections.sort(datasource, new Comparator<BalancoContabilDTO>() {
      @Override
      public int compare(BalancoContabilDTO o1, BalancoContabilDTO o2) {
        if (o1.getServico().equals(o2.getServico())) {
          if (o1.getReduzido().equals(o2.getReduzido())) {
            if (o1.getContaCusto().equals(o2.getContaCusto())) {
              if (o1.getCompetencia().equals(o2.getCompetencia())) {
                if (o1.getCliente().equals(o2.getCliente())) {
                  return o1.getSetor().compareTo(o2.getSetor());
                } else {
                  return o1.getCliente().compareTo(o2.getCliente());
                }
              } else {
                return o1.getCompetencia().compareTo(o2.getCompetencia());
              }
            } else {
              return o1.getContaCusto().compareTo(o2.getContaCusto());
            }
          } else {
            return o1.getReduzido().compareTo(o2.getReduzido());
          }
        } else {
          return o1.getServico().compareTo(o2.getServico());
        }
      }
    });
  }

  public StreamedContent relatorioBalanco(Lancamento lancamento, User usuario) {
    List<BalancoContabilDTO> datasource = new ArrayList<>();
    lancamentoToDtos(lancamento, datasource, false);
    ordenarDtos(datasource);
    Date vigencia = lancamento.getEffectiveOn();
    Integer apuracao = lancamento.getServico().getDiaLimite();
    String fileName = lancamento.getServico().getContaContabil().getTitle();
    return relatorioBalanco(datasource, vigencia, apuracao, fileName, usuario, false);
  }

  public StreamedContent relatorioBalanco(List<BalancoContabilDTO> datasource, Date vigencia, Integer apuracao, String fileName, User usuario, Boolean condominio) {

    Map<String, Object> parameters = new HashMap<>();

    Calendar cld = Calendar.getInstance();
    cld.setTime(vigencia);
    cld.set(Calendar.DAY_OF_MONTH, apuracao);
    cld.add(Calendar.MONTH, -1);
    Date begin = cld.getTime();
    cld.add(Calendar.MONTH, 1);
    Date end = cld.getTime();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    parameters.put("AUTOR", usuario.getLogin());
    parameters.put("EMISSAO", sdf.format(new Date()));
    if (condominio) {
      parameters.put("POR_PERIODO", false);
      parameters.put("REFERENCIA", ArquivoUtils.formatDate(vigencia, "MM/yyyy"));
    } else {
      parameters.put("POR_PERIODO", true);
      parameters.put("REFERENCIA", String.format("%s à %s", sdf.format(begin), sdf.format(end)));
    }
    sdf = new SimpleDateFormat("MMyyyy");

    if (fileName.length() > 8) {
      fileName = fileName.substring(0, 8);
    }

    String downloadFileName = String.format("%s-%s", fileName, sdf.format(new Date()));

    ordenarDtos(datasource);

    return reportLoader.imprimeRelatorio("balancoContabil", parameters, datasource, downloadFileName);
  }

}
