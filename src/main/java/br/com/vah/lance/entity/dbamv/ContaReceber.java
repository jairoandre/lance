package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jairoportela on 02/05/2016.
 */
@Entity
@Table(name = "CON_REC", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = ContaReceber.ALL, query = "SELECT c FROM ContaReceber c"),
    @NamedQuery(name = ContaReceber.COUNT, query = "SELECT COUNT(c) FROM ContaReceber c") })
public class ContaReceber extends BaseEntity {

  public final static String ALL = "ContaReceber.populatedItems";
  public final static String COUNT = "ContaReceber.countTotal]";

  @Id
  @SequenceGenerator(name = "seqConReqGen", sequenceName = "SEQ_CONREC", schema = "DBAMV", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqConReqGen")
  @Column(name = "CD_CON_REC")
  private Long id;

  @Column(name = "CD_PROCESSO")
  private Long cdProcesso;

  @Column(name = "CD_MULTI_EMPRESA")
  private Integer cdMultiEmpresa;

  @Temporal(value = TemporalType.DATE)
  @Column(name = "DT_EMISSAO")
  private Date dataEmissao;

  @Temporal(value = TemporalType.DATE)
  @Column(name = "DT_LANCAMENTO")
  private Date dataLancamento;

  @Column(name = "TP_CON_REC")
  private String tipoDocumento;

  @ManyToOne
  @JoinColumn(name = "CD_FORNECEDOR")
  private Fornecedor cliente;

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO")
  private PlanoConta contaContabil;

  @Column(name = "TP_VENCIMENTO")
  private String tipoVencimento;

  @Column(name = "NR_DOCUMENTO")
  private String numeroDocumento;

  @Column(name = "NM_CLIENTE")
  private String nomeCliente;

  @Column(name = "CD_MOEDA")
  private String moeda;

  @Column(name = "CD_TIP_DOC")
  private Integer cdTipDoc = 1;

  @Column(name = "VL_PREVISTO")
  private BigDecimal valorBruto;

  @Column(name = "VL_ACRESCIMO")
  private BigDecimal valorAcrescimo;

  @Column(name = "VL_DESCONTO")
  private BigDecimal valorDesconto;

  @ManyToOne
  @JoinColumn(name = "CD_HISTORICO_PADRAO")
  private HistoricoPadrao historicoPadrao;

  @Column(name = "DS_CON_REC")
  private String descricao;

  @Column(name = "DS_OBSERVACAO")
  private String observacao;

  @OneToMany(mappedBy = "pk.contaReceber", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<ContaReceberRateio> itensRateio = new LinkedHashSet<>();

  @OneToMany(mappedBy = "contaReceber", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<ContaReceberItem> itensConta = new LinkedHashSet<>();

  @Column(name = "SN_GLOSA_ACEITA")
  private String glosaAceita;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getLabelForSelectItem() {
    return String.format("Cód.: %d", id);
  }

  public Long getCdProcesso() {
    return cdProcesso;
  }

  public void setCdProcesso(Long cdProcesso) {
    this.cdProcesso = cdProcesso;
  }

  public Integer getCdMultiEmpresa() {
    return cdMultiEmpresa;
  }

  public void setCdMultiEmpresa(Integer cdMultiEmpresa) {
    this.cdMultiEmpresa = cdMultiEmpresa;
  }

  public Date getDataEmissao() {
    return dataEmissao;
  }

  public void setDataEmissao(Date dataEmissao) {
    this.dataEmissao = dataEmissao;
  }

  public Date getDataLancamento() {
    return dataLancamento;
  }

  public void setDataLancamento(Date dataLancamento) {
    this.dataLancamento = dataLancamento;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public Fornecedor getCliente() {
    return cliente;
  }

  public void setCliente(Fornecedor cliente) {
    this.cliente = cliente;
  }

  public String getNomeCliente() {
    return nomeCliente;
  }

  public void setNomeCliente(String nomeCliente) {
    this.nomeCliente = nomeCliente;
  }

  public Integer getCdTipDoc() {
    return cdTipDoc;
  }

  public void setCdTipDoc(Integer cdTipDoc) {
    this.cdTipDoc = cdTipDoc;
  }

  public PlanoConta getContaContabil() {
    return contaContabil;
  }

  public void setContaContabil(PlanoConta contaContabil) {
    this.contaContabil = contaContabil;
  }

  public String getTipoVencimento() {
    return tipoVencimento;
  }

  public void setTipoVencimento(String tipoVencimento) {
    this.tipoVencimento = tipoVencimento;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getMoeda() {
    return moeda;
  }

  public void setMoeda(String moeda) {
    this.moeda = moeda;
  }

  public BigDecimal getValorBruto() {
    return valorBruto;
  }

  public void setValorBruto(BigDecimal valorBruto) {
    this.valorBruto = valorBruto;
  }

  public BigDecimal getValorAcrescimo() {
    return valorAcrescimo;
  }

  public void setValorAcrescimo(BigDecimal valorAcrescimo) {
    this.valorAcrescimo = valorAcrescimo;
  }

  public BigDecimal getValorDesconto() {
    return valorDesconto;
  }

  public void setValorDesconto(BigDecimal valorDesconto) {
    this.valorDesconto = valorDesconto;
  }

  public HistoricoPadrao getHistoricoPadrao() {
    return historicoPadrao;
  }

  public void setHistoricoPadrao(HistoricoPadrao historicoPadrao) {
    this.historicoPadrao = historicoPadrao;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public Set<ContaReceberRateio> getItensRateio() {
    return itensRateio;
  }

  public void setItensRateio(Set<ContaReceberRateio> itensRateio) {
    this.itensRateio = itensRateio;
  }

  public Set<ContaReceberItem> getItensConta() {
    return itensConta;
  }

  public void setItensConta(Set<ContaReceberItem> itensConta) {
    this.itensConta = itensConta;
  }

  public String getGlosaAceita() {
    return glosaAceita;
  }

  public void setGlosaAceita(String glosaAceita) {
    this.glosaAceita = glosaAceita;
  }

  public String getCodSetor() {
    if (itensRateio != null) {
      return itensRateio.iterator().next().getCdSetor().toString();
    }
    return "-";
  }
}
