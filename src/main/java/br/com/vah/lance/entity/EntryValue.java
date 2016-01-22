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
   * Valor do lançamento (calculado): valor base + valor variável
   */
  @Column(name = "VL_LANCAMENTO", nullable = false)
  private BigDecimal value;

  /**
   * Valor do serviço na vigência do lançamento
   */
  @Column(name = "VL_BASE_SERVICO", nullable = true)
  private BigDecimal baseValue;

  /**
   * Valor complementar de lançamento:
   * - Leitura de energia
   * - Venda de produto
   * - etc...
   */
  @Column(name = "VL_COMPLEMENTAR_A", nullable = true)
  private BigDecimal compValueA;

  /**
   * Valor complementar de lançamento:
   * - Leitura de energia
   * - Venda de produto
   * - etc...
   */
  @Column(name = "VL_COMPLEMENTAR_B", nullable = true)
  private BigDecimal compValueB;

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
    this.baseValue = BigDecimal.ZERO;
    this.compValueA = BigDecimal.ZERO;
    this.compValueB = BigDecimal.ZERO;
  }

  public EntryValue(Entry entry, ContractSector contractSector) {
    this();
    this.entry = entry;
    this.contractSector = contractSector;
    this.value = entry.getServiceValue();
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

  public BigDecimal getBaseValue() {
    return baseValue;
  }

  public void setBaseValue(BigDecimal contractValue) {
    this.baseValue = contractValue;
  }

  public BigDecimal getCompValueA() {
    return compValueA;
  }

  public void setCompValueA(BigDecimal complementaryValueA) {
    this.compValueA = complementaryValueA;
  }

  public BigDecimal getCompValueB() {
    return compValueB;
  }

  public void setCompValueB(BigDecimal complementaryValueB) {
    this.compValueB = complementaryValueB;
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
