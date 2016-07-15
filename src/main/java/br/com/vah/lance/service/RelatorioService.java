package br.com.vah.lance.service;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.PlanoConta;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.*;
import br.com.vah.lance.reports.BalancoContabilDTO;
import br.com.vah.lance.reports.DescritivoCondominioDTO;
import br.com.vah.lance.reports.RelatorioSetorDTO;
import br.com.vah.lance.reports.ReportLoader;
import br.com.vah.lance.util.ViewUtils;
import javassist.runtime.Desc;
import org.primefaces.model.StreamedContent;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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

    Date[] range = ViewUtils.getDateRange(vigencia);

    List<Lancamento> lancamentos = lancamentoService.getLancamentosPorPeriodoStatus(status, range);

    List<DescritivoCondominioDTO> datasource = new ArrayList<>();

    for (Lancamento lancamento : lancamentos) {

      if (condominio && !lancamento.getServico().getAgrupavel()) {
        continue;
      }

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
          dto.setServico(lancamento.getServico().getTitle());
          dto.setTotal(lancamento.getTotalValue());
          dto.setRateio(BigDecimal.ZERO);
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
    return reportLoader.imprimeRelatorio("descritivoCondominio", parameters, datasource);

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
    return reportLoader.imprimeRelatorio("setorRelatorio", parameters, datasource);
  }

  public StreamedContent getRelatorioBalanco(Set<Servico> servicos, Date[] range) {
    Map<String, Object> parameters = new HashMap<>();

    parameters.put("begin", range[0]);
    parameters.put("end", range[1]);

    List<BalancoContabilDTO> datasource = new ArrayList<>();

    List<Lancamento> list;

    if (servicos == null || servicos.isEmpty()) {
      list = lancamentoService.getListByNamedQuery(Lancamento.BY_PERIOD, parameters);
    } else {
      parameters.put("services", servicos);
      list = lancamentoService.getListByNamedQuery(Lancamento.BY_PERIOD_AND_SERVICE, parameters);
    }

    Set<Lancamento> lancs = new HashSet<>(list);

    for (Lancamento lanc : lancs) {
      for (LancamentoValor val : lanc.getValues()) {
        Servico serv = lanc.getServico();
        PlanoConta conta = serv.getContaContabil();
        ContratoSetor contratoSetor = val.getContratoSetor();
        Setor setor = contratoSetor.getSetor();
        if (serv.getContaPorSetor()) {
          SetorDetalhe setorDetalhe = setor.getSetorDetalhe();
          if (setorDetalhe == null && setorDetalhe.getContaContabil() == null) {
            conta = setorDetalhe.getContaContabil();
          }
        }
        BalancoContabilDTO dto = new BalancoContabilDTO();
        dto.setSetor(setor.getId().toString());
        dto.setCliente(contratoSetor.getInquilino() == null ? contratoSetor.getContrato().getContratante().getTitle() : contratoSetor.getInquilino().getTitle());
        dto.setServico(serv.getTitle());
        dto.setContaContabil(conta.getCodigoContabil());
        dto.setContaReduzido(conta.getId().toString());
        dto.setVigencia(lanc.getEffectiveOn());
        dto.setValor(val.getValue());
        datasource.add(dto);
      }
    }

    Collections.sort(datasource, new Comparator<BalancoContabilDTO>() {
      @Override
      public int compare(BalancoContabilDTO o1, BalancoContabilDTO o2) {
        if (o1.getServico().equals(o2.getServico())) {
          if (o1.getContaReduzido().equals(o2.getContaReduzido())) {
            if (o1.getVigencia().equals(o2.getVigencia())) {
              if (o1.getCliente().equals(o2.getCliente())) {
                return o1.getSetor().compareTo(o2.getSetor());
              } else {
                return o1.getCliente().compareTo(o2.getCliente());
              }
            } else {
              return o1.getVigencia().compareTo(o2.getVigencia());
            }
          } else {
            return o1.getContaReduzido().compareTo(o2.getContaReduzido());
          }

        } else {
          return o1.getServico().compareTo(o2.getServico());
        }
      }
    });

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    parameters.put("REFERENCIA", String.format("%s à %s", sdf.format(range[0]), sdf.format(range[1])));

    return reportLoader.imprimeRelatorio("balancoContabil", parameters, datasource);
  }
}
