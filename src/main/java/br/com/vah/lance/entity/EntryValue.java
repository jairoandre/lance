package br.com.vah.lance.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entidade que representa um lançamento.
 *
 * @author jairoportela
 */
@Entity
@Table(name = "TB_LANCA_LANCAMENTO_VALORES", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = EntryValue.ALL, query = "SELECT e FROM EntryValue e"),
    @NamedQuery(name = EntryValue.COUNT, query = "SELECT COUNT(e) FROM EntryValue e")})
public class EntryValue extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "EntryValue.populatedItems";
  public static final String COUNT = "EntryValue.countTotal";

  @Id
  @SequenceGenerator(name = "seqEntryValueGenerator", sequenceName = "SEQ_TB_LANCA_LANCAMENTO_VALORES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEntryValueGenerator")
  @Column(name = "ID")
  private Long id;

  /**
   * Agrupador de lançamento
   */
  @ManyToOne
  @JoinColumn(name = "ID_LANCAMENTO", nullable = false)
  private Entry entry;

  /**
   * Contrato
   */
  @ManyToOne
  @JoinColumn(name = "ID_CONTRATO_SERVICO", nullable = false)
  private ContractSector contractSector;

  /**
   * Valor do lançamento (calculado)
   */
  @Column(name = "VL_LANCAMENTO", nullable = false)
  private BigDecimal value;

  /**
   * Valor de contrato do serviço (fixo)
   */
  @Column(name = "VL_CONTRATO", nullable = true)
  private BigDecimal contractValue;

  /**
   * Alíquota do lançamento (para lançamentos baseados em consumo)
   */
  @Column(name = "VL_ALIQUOTA", nullable = true)
  private BigDecimal aliquot;

  /**
   * Base de cálculo do lançamento
   */
  @Column(name = "VL_BASE_CALCULO", nullable = true)
  private BigDecimal taxBase;

  /**
   * Valor complementar do lançamento (exemplo: leitura anterior de energia)
   */
  @Column(name = "VL_COMPLEMENTAR_A", nullable = true)
  private BigDecimal complementaryValueA;

  /**
   * Valor complementar do lançamento (exemplo: leitura atual de energia)
   */
  @Column(name = "VL_COMPLEMENTAR_B", nullable = true)
  private BigDecimal complementaryValueB;

  /**
   * Data de criação do lançamento
   */
  @Column(name = "DT_LANCAMENTO")
  private Date createdOn;

  /**
   * Data de atualização do lançamento
   */
  @Column(name = "DT_ATUALIZACAO")
  private Date updatedOn;

  public EntryValue() {
    this.createdOn = new Date();
    this.updatedOn = new Date();
    this.value = BigDecimal.ZERO;

  }

  public EntryValue(Entry entry, ContractSector contractSector) {
    this();
    this.entry = entry;
    this.contractSector = contractSector;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Entry getEntry() {
    return entry;
  }

  public void setEntry(Entry entry) {
    this.entry = entry;
  }

  public ContractSector getContractSector() {
    return contractSector;
  }

  public void setContractSector(ContractSector contractSector) {
    this.contractSector = contractSector;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getContractValue() {
    return contractValue;
  }

  public void setContractValue(BigDecimal contractValue) {
    this.contractValue = contractValue;
  }

  public BigDecimal getAliquot() {
    return aliquot;
  }

  public void setAliquot(BigDecimal aliquot) {
    this.aliquot = aliquot;
  }

  public BigDecimal getTaxBase() {
    return taxBase;
  }

  public void setTaxBase(BigDecimal taxBase) {
    this.taxBase = taxBase;
  }

  public BigDecimal getComplementaryValueA() {
    return complementaryValueA;
  }

  public void setComplementaryValueA(BigDecimal complementaryValueA) {
    this.complementaryValueA = complementaryValueA;
  }

  public BigDecimal getComplementaryValueB() {
    return complementaryValueB;
  }

  public void setComplementaryValueB(BigDecimal complementaryValueB) {
    this.complementaryValueB = complementaryValueB;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

}
