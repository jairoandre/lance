package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jairoportela on 11/08/2016.
 */
@Entity
@Table(name = "MOV_CONCOR", schema = "DBAMV")
public class Movimentacao extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqMovConCor", sequenceName = "SEQ_MOV_CONCOR", schema = "DBAMV", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMovConCor")
  @Column(name = "CD_MOV_CONCOR", nullable = false)
  private Long id;

  @Column(name = "CD_PROCESSO")
  private Integer processo = 155;

  @Column(name = "NR_DOCUMENTO_IDENTIFICACAO")
  private String numeroIdentificacao;

  @Column(name = "DT_MOVIMENTACAO")
  private Date data;

  @Column(name = "VL_MOVIMENTACAO")
  private BigDecimal valor;

  @Column(name = "CD_MULTI_EMPRESA")
  private Integer multiEmpresa = 1;

  @Column(name = "CD_MULTI_EMPRESA_ORIGEM")
  private Integer multiEmpresaOrigem = 1;

  @Column(name = "SN_CONCILIADO")
  private String conciliado = "N";

  @Column(name = "SN_RECURSO")
  private String recurso = "N";

  @Column(name = "CD_MOEDA")
  private String cdMoeda = "1";

  @Column(name = "CD_CON_COR")
  private Integer contaCorrente = 110;

  @Column(name = "VL_MOEDA")
  private BigDecimal valorMoeda;

  @Column(name = "CD_LAN_CONCOR")
  private Integer cdLanConcor = 73;

  @Column(name = "DS_MOVIMENTACAO")
  private String descricao;

  @Column(name = "DS_MOVIMENTACAO_PADRAO")
  private String descricaoPadrao;

  @Column(name = "DS_MOVIMENTACAO_PROCESSO")
  private String descricaoProcesso;

  @OneToMany(mappedBy = "movimentacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<RecebimentoMovimentacao> recebimentos = new HashSet<>();

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Integer getProcesso() {
    return processo;
  }

  public void setProcesso(Integer processo) {
    this.processo = processo;
  }

  public String getNumeroIdentificacao() {
    return numeroIdentificacao;
  }

  public void setNumeroIdentificacao(String numeroIdentificacao) {
    this.numeroIdentificacao = numeroIdentificacao;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Integer getMultiEmpresa() {
    return multiEmpresa;
  }

  public void setMultiEmpresa(Integer multiEmpresa) {
    this.multiEmpresa = multiEmpresa;
  }

  public Integer getMultiEmpresaOrigem() {
    return multiEmpresaOrigem;
  }

  public void setMultiEmpresaOrigem(Integer multiEmpresaOrigem) {
    this.multiEmpresaOrigem = multiEmpresaOrigem;
  }

  public String getConciliado() {
    return conciliado;
  }

  public void setConciliado(String conciliado) {
    this.conciliado = conciliado;
  }

  public String getRecurso() {
    return recurso;
  }

  public void setRecurso(String recurso) {
    this.recurso = recurso;
  }

  public String getCdMoeda() {
    return cdMoeda;
  }

  public void setCdMoeda(String cdMoeda) {
    this.cdMoeda = cdMoeda;
  }

  public Integer getContaCorrente() {
    return contaCorrente;
  }

  public void setContaCorrente(Integer contaCorrente) {
    this.contaCorrente = contaCorrente;
  }

  public BigDecimal getValorMoeda() {
    return valorMoeda;
  }

  public void setValorMoeda(BigDecimal valorMoeda) {
    this.valorMoeda = valorMoeda;
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

  public String getDescricaoPadrao() {
    return descricaoPadrao;
  }

  public void setDescricaoPadrao(String descricaoPadrao) {
    this.descricaoPadrao = descricaoPadrao;
  }

  public String getDescricaoProcesso() {
    return descricaoProcesso;
  }

  public Set<RecebimentoMovimentacao> getRecebimentos() {
    return recebimentos;
  }

  public void setRecebimentos(Set<RecebimentoMovimentacao> recebimentos) {
    this.recebimentos = recebimentos;
  }

  public void setDescricaoProcesso(String descricaoProcesso) {
    this.descricaoProcesso = descricaoProcesso;
  }

  @Override
  public String getLabelForSelectItem() {
    return descricaoPadrao;
  }
}
