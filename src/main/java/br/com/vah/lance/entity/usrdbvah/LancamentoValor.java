package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;

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
@NamedQueries({@NamedQuery(name = LancamentoValor.ALL, query = "SELECT e FROM LancamentoValor e"),
    @NamedQuery(name = LancamentoValor.COUNT, query = "SELECT COUNT(e) FROM LancamentoValor e")})
public class LancamentoValor extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "LancamentoValor.populatedItems";
  public static final String COUNT = "LancamentoValor.countTotal";

  @Id
  @SequenceGenerator(name = "seqEntryValueGenerator", sequenceName = "SEQ_LANCA_LANC_VALORES", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEntryValueGenerator")
  @Column(name = "ID")
  private Long id;

  /**
   * Agrupador de lançamento
   */
  @ManyToOne
  @JoinColumn(name = "ID_LANCAMENTO", nullable = false)
  private Lancamento lancamento;

  /**
   * Contrato
   */
  @ManyToOne
  @JoinColumn(name = "ID_CONTRATO_SERVICO", nullable = false)
  private ContratoSetor contratoSetor;

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

  public LancamentoValor() {
    this.createdOn = new Date();
    this.updatedOn = new Date();
    this.value = BigDecimal.ZERO;
    this.valueA = BigDecimal.ZERO;
    this.valueB = BigDecimal.ZERO;
  }

  public LancamentoValor(Lancamento lancamento, ContratoSetor contratoSetor) {
    this();
    this.lancamento = lancamento;
    this.contratoSetor = contratoSetor;
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

  public ContratoSetor getContratoSetor() {
    return contratoSetor;
  }

  public void setContratoSetor(ContratoSetor contratoSetor) {
    this.contratoSetor = contratoSetor;
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
