package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade que representa um lançamento.
 *
 * @author jairoportela
 */
@Entity
@Table(name = "TB_LANCA_LEITURAS", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = LancamentoMedidorValor.ALL, query = "SELECT e FROM LancamentoMedidorValor e"),
    @NamedQuery(name = LancamentoMedidorValor.COUNT, query = "SELECT COUNT(e) FROM LancamentoMedidorValor e")})
public class LancamentoMedidorValor extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "LancamentoMedidorValor.populatedItems";
  public static final String COUNT = "LancamentoMedidorValor.countTotal";

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
  private Lancamento lancamento;

  /**
   * Medidor de consumo
   */
  @ManyToOne
  @JoinColumn(name = "ID_MEDIDOR", nullable = false)
  private MedidorConsumo medidorConsumo;

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

  public LancamentoMedidorValor() {
    this.currentValue = BigDecimal.ZERO;
    this.previousValue = BigDecimal.ZERO;
  }

  public LancamentoMedidorValor(Lancamento lancamento, MedidorConsumo medidorConsumo) {
    this();
    this.lancamento = lancamento;
    this.medidorConsumo = medidorConsumo;
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

  public Lancamento getLancamento() {
    return lancamento;
  }

  public void setLancamento(Lancamento lancamento) {
    this.lancamento = lancamento;
  }

  public MedidorConsumo getMedidorConsumo() {
    return medidorConsumo;
  }

  public void setMedidorConsumo(MedidorConsumo medidorConsumo) {
    this.medidorConsumo = medidorConsumo;
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
