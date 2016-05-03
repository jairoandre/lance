package br.com.vah.lance.entity.mv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 02/05/2016.
 */
@Entity
@Table(name = "CON_REC", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvContaReceber.ALL, query = "SELECT c FROM MvContaReceber c"),
    @NamedQuery(name = MvContaReceber.COUNT, query = "SELECT COUNT(c) FROM MvContaReceber c") })
public class MvContaReceber extends BaseEntity {

  public final static String ALL = "MvContaReceber.populatedItems";
  public final static String COUNT = "MvContaReceber.countTotal";

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
  private MvClient cliente;

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO")
  private MvPlanoConta contaContabil;

  @Column(name = "TP_VENCIMENTO")
  private String tipoVencimento = "V";

  @Column(name = "CD_SEQ_INTEGRA")
  private Long numeroDocumento;

  @Column(name = "CD_MOEDA")
  private String moeda = "V";

  @Column(name = "VL_PREVISTO")
  private BigDecimal valorBruto;

  @ManyToOne
  @JoinColumn(name = "CD_HISTORICO_PADRAO")
  private MvDefaultHistory historicoPadrao;

  @Column(name = "DS_CON_REC")
  private String descricao;

  @Column(name = "DS_OBSERVACAO")
  private String observacao;

  @OneToMany(mappedBy = "pk.contaReceber", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<MvContaReceberRateio> itensRateio;

  @OneToMany(mappedBy = "contaReceber", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<MvContaReceberItem> itensConta;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
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

  public MvClient getCliente() {
    return cliente;
  }

  public void setCliente(MvClient cliente) {
    this.cliente = cliente;
  }

  public MvPlanoConta getContaContabil() {
    return contaContabil;
  }

  public void setContaContabil(MvPlanoConta contaContabil) {
    this.contaContabil = contaContabil;
  }

  public String getTipoVencimento() {
    return tipoVencimento;
  }

  public void setTipoVencimento(String tipoVencimento) {
    this.tipoVencimento = tipoVencimento;
  }

  public Long getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(Long numeroDocumento) {
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

  public MvDefaultHistory getHistoricoPadrao() {
    return historicoPadrao;
  }

  public void setHistoricoPadrao(MvDefaultHistory historicoPadrao) {
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

  public List<MvContaReceberRateio> getItensRateio() {
    return itensRateio;
  }

  public void setItensRateio(List<MvContaReceberRateio> itensRateio) {
    this.itensRateio = itensRateio;
  }

  public List<MvContaReceberItem> getItensConta() {
    return itensConta;
  }

  public void setItensConta(List<MvContaReceberItem> itensConta) {
    this.itensConta = itensConta;
  }
}
