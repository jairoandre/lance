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
  @Column(name = "VL_LANCAMENTO", nullable = false, precision = 4)
  private BigDecimal value;

  /**
   * Valor complementar de lançamento:
   * - Leitura de energia
   * - Venda de produto
   * - etc...
   */
  @Column(name = "VL_VALOR_A", nullable = true, precision = 4)
  private BigDecimal valueA;

  /**
   * Valor complementar de lançamento:
   * - Leitura de energia
   * - Venda de produto
   * - etc...
   */
  @Column(name = "VL_VALOR_B", nullable = true, precision = 4)
  private BigDecimal valueB;

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
    this.valueA = BigDecimal.ZERO;
    this.valueB = BigDecimal.ZERO;
  }

  public EntryValue(Entry entry, ContractSector contractSector) {
    this();
    this.entry = entry;
    this.contractSector = contractSector;
  }

  /**
   *
   * @return
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   *
   * @param id
   */
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

  public BigDecimal getValueA() {
    return valueA;
  }

  public void setValueA(BigDecimal valueA) {
    this.valueA = valueA;
  }

  public BigDecimal getValueB() {
    return valueB;
  }

  public void setValueB(BigDecimal valueB) {
    this.valueB = valueB;
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

  /**
   *
   * @return
   */
  @Override
  public String getLabelForSelectItem() {
    return null;
  }

}
