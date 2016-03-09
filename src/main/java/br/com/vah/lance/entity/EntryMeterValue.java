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
@Table(name = "TB_LANCA_LEITURAS", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = EntryMeterValue.ALL, query = "SELECT e FROM EntryMeterValue e"),
    @NamedQuery(name = EntryMeterValue.COUNT, query = "SELECT COUNT(e) FROM EntryMeterValue e")})
public class EntryMeterValue extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "EntryMeterValue.populatedItems";
  public static final String COUNT = "EntryMeterValue.countTotal";

  @Id
  @SequenceGenerator(name = "seqEntryMeterValueGenerator", sequenceName = "SEQ_LANCA_LEITURAS", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEntryMeterValueGenerator")
  @Column(name = "ID")
  private Long id;

  /**
   * Agrupador de lançamento
   */
  @ManyToOne
  @JoinColumn(name = "ID_LANCAMENTO", nullable = false)
  private Entry entry;

  /**
   * Medidor de consumo
   */
  @ManyToOne
  @JoinColumn(name = "ID_MEDIDOR", nullable = false)
  private ConsumptionMeter consumptionMeter;

  /**
   * Valor de leitura (atual)
   */
  @Column(name = "VL_LEITURA_ATUAL", nullable = false, precision = 4)
  private BigDecimal currentValue;

  /**
   * Valor de leitura (anterior)
   */
  @Column(name = "VL_LEITURA_ANTERIOR", nullable = true, precision = 4)
  private BigDecimal previousValue;

  public EntryMeterValue() {
    this.currentValue = BigDecimal.ZERO;
    this.previousValue = BigDecimal.ZERO;
  }

  public EntryMeterValue(Entry entry, ConsumptionMeter consumptionMeter) {
    this();
    this.entry = entry;
    this.consumptionMeter = consumptionMeter;
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

  public ConsumptionMeter getConsumptionMeter() {
    return consumptionMeter;
  }

  public void setConsumptionMeter(ConsumptionMeter consumptionMeter) {
    this.consumptionMeter = consumptionMeter;
  }

  public BigDecimal getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(BigDecimal currentValue) {
    this.currentValue = currentValue;
  }

  public BigDecimal getPreviousValue() {
    return previousValue;
  }

  public void setPreviousValue(BigDecimal previousValue) {
    this.previousValue = previousValue;
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
