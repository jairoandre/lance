package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.ContaReceber;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jairoportela on 20/07/2016.
 */
@Entity
@Table(name = "TB_LANCA_COBRANCA", schema = "USRDBVAH")
public class Cobranca extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqCobrancaGenerator", sequenceName = "SEQ_LANCA_COBRANCA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCobrancaGenerator")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_SETOR")
  private Setor setor;

  @ManyToOne
  @JoinColumn(name = "ID_CLIENTE")
  private Fornecedor cliente;

  @ManyToOne
  @JoinColumn(name = "ID_ARQUIVO")
  private ArquivoRemessa arquivo;

  @Column(name = "VL_VALOR")
  private BigDecimal valor = BigDecimal.ZERO;

  @Column(name = "DT_VIGENCIA")
  private Date vigencia;

  @Column(name = "DT_VENCIMENTO")
  private Date vencimento;

  @Column(name = "SN_LIQUIDADO")
  private Boolean liquidado = false;

  @Column(name = "SN_BAIXA_MV")
  private Boolean baixa = false;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "TB_LANCA_COBRANCA_CONTA", joinColumns = {
      @JoinColumn(name = "ID_COBRANCA")}, inverseJoinColumns = {
      @JoinColumn(name = "CD_SETOR")}, schema = "USRDBVAH")
  private Set<ContaReceber> contas = new HashSet<>();

  @OneToMany(mappedBy = "cobranca")
  private Set<ItemCobranca> descritivo = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  public Fornecedor getCliente() {
    return cliente;
  }

  public ArquivoRemessa getArquivo() {
    return arquivo;
  }

  public void setArquivo(ArquivoRemessa arquivo) {
    this.arquivo = arquivo;
  }

  public void setCliente(Fornecedor cliente) {
    this.cliente = cliente;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Date getVigencia() {
    return vigencia;
  }

  public void setVigencia(Date vigencia) {
    this.vigencia = vigencia;
  }

  public Date getVencimento() {
    return vencimento;
  }

  public void setVencimento(Date vencimento) {
    this.vencimento = vencimento;
  }

  public Boolean getLiquidado() {
    return liquidado;
  }

  public void setLiquidado(Boolean liquidado) {
    this.liquidado = liquidado;
  }

  public Boolean getBaixa() {
    return baixa;
  }

  public void setBaixa(Boolean baixa) {
    this.baixa = baixa;
  }

  public Set<ContaReceber> getContas() {
    return contas;
  }

  public void setContas(Set<ContaReceber> contas) {
    this.contas = contas;
  }

  public Set<ItemCobranca> getDescritivo() {
    return descritivo;
  }

  public void setDescritivo(Set<ItemCobranca> descritivo) {
    this.descritivo = descritivo;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

  public String getRowKey() {
    return id == null ? String.format("%d - %d", cliente.getId(), setor.getId()) : id.toString();
  }


}