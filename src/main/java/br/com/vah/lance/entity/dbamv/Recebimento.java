package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jairoportela on 04/08/2016.
 */
@Entity
@Table(name = "RECCON_REC", schema = "DBAMV")
public class Recebimento extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqRecConRecGen", sequenceName = "SEQ_RECCON_REC", schema = "DBAMV", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecConRecGen")
  @Column(name = "CD_RECCON_REC", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_ITCON_REC")
  private ContaReceberItem item;

  @Column(name = "DT_RECEBIMENTO")
  private Date dataRecebimento;

  @Column(name = "CD_PROCESSO")
  private Integer processo = 155;

  @Column(name = "CD_MULTI_EMPRESA")
  private Integer multiEmpresa = 1;

  @Column(name = "TP_RECEBIMENTO")
  private String tpRecebimento = "6";

  @Column(name = "CD_BANCO")
  private Integer cdBanco = 341;

  @Column(name = "CD_CON_COR")
  private Integer cdContaCorrente = 110;

  @Column(name = "CD_LAN_CONCOR")
  private Integer cdLanConcor = 73;

  @Column(name = "DS_RECCON_REC")
  private String descricao;

  @Column(name = "VL_RECEBIDO")
  private BigDecimal valorRecebido = BigDecimal.ZERO;

  @Column(name = "VL_ACRESCIMO")
  private BigDecimal valorAcrescimo = BigDecimal.ZERO;

  @Column(name = "VL_DESCONTO")
  private BigDecimal valorDesconto = BigDecimal.ZERO;

  @Column(name = "VL_MOEDA")
  private BigDecimal valorMoeda = BigDecimal.ZERO;

  @Column(name = "VL_MOEDA_ACRESCIMO")
  private BigDecimal valorMoedaAcrescimo = BigDecimal.ZERO;

  @Column(name = "VL_MOEDA_DESCONTO")
  private BigDecimal valorMoedaDesconto = BigDecimal.ZERO;

  @Column(name = "CD_MOEDA")
  private String cdMoeda = "1";

  @Column(name = "NM_USUARIO")
  private String nomeUsuario;

  @Column(name = "DT_CHEQUE")
  private Date dataCheque;

  @Column(name = "SN_RECURSO")
  private String recurso = "N";

  @Column(name = "DS_OBSERVACAO")
  private String observacao = " - O_REC";

  @OneToMany(mappedBy = "recebimento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<RecebimentoDescAcres> descontosAcrescimos = new HashSet<>();

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public ContaReceberItem getItem() {
    return item;
  }

  public void setItem(ContaReceberItem item) {
    this.item = item;
  }

  public Date getDataRecebimento() {
    return dataRecebimento;
  }

  public void setDataRecebimento(Date dataRecebimento) {
    this.dataRecebimento = dataRecebimento;
  }

  public Integer getProcesso() {
    return processo;
  }

  public void setProcesso(Integer processo) {
    this.processo = processo;
  }

  public Integer getMultiEmpresa() {
    return multiEmpresa;
  }

  public void setMultiEmpresa(Integer multiEmpresa) {
    this.multiEmpresa = multiEmpresa;
  }

  public String getTpRecebimento() {
    return tpRecebimento;
  }

  public void setTpRecebimento(String tpRecebimento) {
    this.tpRecebimento = tpRecebimento;
  }

  public Integer getCdBanco() {
    return cdBanco;
  }

  public void setCdBanco(Integer cdBanco) {
    this.cdBanco = cdBanco;
  }

  public Integer getCdContaCorrente() {
    return cdContaCorrente;
  }

  public void setCdContaCorrente(Integer cdContaCorrente) {
    this.cdContaCorrente = cdContaCorrente;
  }

  public Integer getCdLanConcor() {
    return cdLanConcor;
  }

  public void setCdLanConcor(Integer cdLanConcor) {
    this.cdLanConcor = cdLanConcor;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getValorRecebido() {
    return valorRecebido;
  }

  public void setValorRecebido(BigDecimal valorRecebido) {
    this.valorRecebido = valorRecebido;
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

  public BigDecimal getValorMoeda() {
    return valorMoeda;
  }

  public void setValorMoeda(BigDecimal valorMoeda) {
    this.valorMoeda = valorMoeda;
  }

  public BigDecimal getValorMoedaAcrescimo() {
    return valorMoedaAcrescimo;
  }

  public void setValorMoedaAcrescimo(BigDecimal valorMoedaAcrescimo) {
    this.valorMoedaAcrescimo = valorMoedaAcrescimo;
  }

  public BigDecimal getValorMoedaDesconto() {
    return valorMoedaDesconto;
  }

  public void setValorMoedaDesconto(BigDecimal valorMoedaDesconto) {
    this.valorMoedaDesconto = valorMoedaDesconto;
  }

  public String getCdMoeda() {
    return cdMoeda;
  }

  public void setCdMoeda(String cdMoeda) {
    this.cdMoeda = cdMoeda;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
  }

  public Date getDataCheque() {
    return dataCheque;
  }

  public void setDataCheque(Date dataCheque) {
    this.dataCheque = dataCheque;
  }

  public String getRecurso() {
    return recurso;
  }

  public void setRecurso(String recurso) {
    this.recurso = recurso;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public Set<RecebimentoDescAcres> getDescontosAcrescimos() {
    return descontosAcrescimos;
  }

  public void setDescontosAcrescimos(Set<RecebimentoDescAcres> descontosAcrescimos) {
    this.descontosAcrescimos = descontosAcrescimos;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
