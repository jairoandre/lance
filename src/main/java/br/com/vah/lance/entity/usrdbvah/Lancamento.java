package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.ContaReceber;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Entidade que representa um conjunto de lançamentos.
 *
 * @author jairoportela
 */
@Entity
@Table(name = "TB_LANCA_LANCAMENTO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Lancamento.ALL, query = "SELECT e FROM Lancamento e"),
    @NamedQuery(name = Lancamento.COUNT, query = "SELECT COUNT(e) FROM Lancamento e"),
    @NamedQuery(name = Lancamento.CONDOMINIAL, query = "SELECT e FROM Lancamento e where e.servico.agrupavel = true and e.status = 'L' order by e.effectiveOn"),
    @NamedQuery(name = Lancamento.BY_PERIOD_AND_SERVICE, query = "SELECT e FROM Lancamento e where e.effectiveOn between :begin and :end and e.servico in :services"),
    @NamedQuery(name = Lancamento.BY_SERVICE_DATE_STATUS, query = "SELECT e FROM Lancamento e where e.effectiveOn >= :date and e.servico = :service and e.status = :status"),
    @NamedQuery(name = Lancamento.BY_PERIOD, query = "SELECT e FROM Lancamento e where e.effectiveOn between :begin and :end"),
    @NamedQuery(name = Lancamento.BY_ID, query = "SELECT e FROM Lancamento e where e.id = :id")})
public class Lancamento extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "Lancamento.populatedItems";
  public static final String COUNT = "Lancamento.countTotal";

  public static final String CONDOMINIAL = "Lancamento.condominial";
  public static final String BY_PERIOD_AND_SERVICE = "Lancamento.byPeriodAndService";
  public static final String BY_SERVICE_DATE_STATUS = "Lancamento.byServiceDateStatus";
  public static final String BY_PERIOD = "Lancamento.byPeriod";
  public static final String BY_ID = "Lancamento.byID";

  @Id
  @SequenceGenerator(name = "seqEntryGenerator", sequenceName = "SEQ_LANCA_LANCAMENTO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEntryGenerator")
  @Column(name = "ID")
  private Long id;

  /**
   * Usuário autor do lançamento
   */
  @ManyToOne
  @JoinColumn(name = "ID_USU_LANCADOR", nullable = false)
  private User autor;

  /**
   * Comentários do lançamento
   */
  @OneToMany(mappedBy = "lancamento", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Comentario> comentarios;

  /**
   * Serviço ao qual o lançamento se refere
   */
  @ManyToOne
  @JoinColumn(name = "ID_SERVICO", nullable = false)
  private Servico servico;

  /**
   * Valor do serviço na vigência do lançamento
   */
  @ManyToOne
  @JoinColumn(name = "ID_SERVICO_VALORES")
  private ServicoValor servicoValor;

  /**
   * Valor total do lançamento
   */
  @Column(name = "VL_TOTAL", precision = 4)
  private BigDecimal totalValue = BigDecimal.ZERO;

  /**
   * Data de criação do lançamento
   */
  @Column(name = "DT_LANCAMENTO")
  private Date createdOn;

  /**
   * Data de vigência
   */
  @Column(name = "DT_VIGENCIA")
  private Date effectiveOn;

  @Column(name = "VL_MONTANTE")
  private BigDecimal ammountToShare = BigDecimal.ZERO;

  @Column(name = "VL_AREA_A")
  private BigDecimal totalAreaA = BigDecimal.ZERO;

  @Column(name = "VL_AREA_B")
  private BigDecimal totalAreaB = BigDecimal.ZERO;

  @Column(name = "VL_AREA_C")
  private BigDecimal totalAreaC = BigDecimal.ZERO;

  @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_LANCA_LAN_CON_REC", joinColumns = {
      @JoinColumn(name = "ID")}, inverseJoinColumns = {@JoinColumn(name = "CD_CON_REC")}, schema = "USRDBVAH")
  private List<ContaReceber> contasReceber = new ArrayList<>();

  /**
   * Estado do lançamento, valores possíveis:
   * <ul>
   * <li>Não lançado</li>
   * <li>Lançado</li>
   * <li>Validado</li>
   * <li>Pendente</li>
   * <li>Corrigido</li>
   * <li>Transmitido</li>
   * <li>Excluído</li>
   * </ul>
   */
  @Column(name = "CD_STATUS", nullable = false)
  @Enumerated(EnumType.STRING)
  private EstadoLancamentoEnum status;

  /**
   * Os valores individuais de lançamento (por cliente/contrato)
   */
  @OneToMany(mappedBy = "lancamento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<LancamentoValor> values;

  /**
   * Os valores individuais de leituras de medidores (para os caso de Energia e Gás)
   */
  @OneToMany(mappedBy = "lancamento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<LancamentoMedidorValor> meterValues;

  public Lancamento() {
    this.status = EstadoLancamentoEnum.N;
    this.createdOn = new Date();
    this.effectiveOn = new Date();
    this.totalValue = BigDecimal.ZERO;
    this.comentarios = new LinkedHashSet<>();
    this.values = new HashSet<>();
    this.meterValues = new LinkedHashSet<>();
  }

  public Lancamento(Servico servico) {
    this();
    this.servico = servico;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public User getAutor() {
    return autor;
  }

  public void setAutor(User userForEntry) {
    this.autor = userForEntry;
  }

  public Set<Comentario> getComentarios() {
    return comentarios;
  }

  public void setComentarios(Set<Comentario> comentarios) {
    this.comentarios = comentarios;
  }

  public Servico getServico() {
    return servico;
  }

  public void setServico(Servico servico) {
    this.servico = servico;
  }

  public ServicoValor getServicoValor() {
    return servicoValor;
  }

  public void setServicoValor(ServicoValor servicoValor) {
    this.servicoValor = servicoValor;
  }

  public BigDecimal getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(BigDecimal totalValue) {
    this.totalValue = totalValue;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Date getEffectiveOn() {
    return effectiveOn;
  }

  public void setEffectiveOn(Date effectiveOn) {
    this.effectiveOn = effectiveOn;
  }

  public BigDecimal getAmmountToShare() {
    return ammountToShare;
  }

  public void setAmmountToShare(BigDecimal ammountToShare) {
    this.ammountToShare = ammountToShare;
  }

  public BigDecimal getTotalAreaA() {
    return totalAreaA;
  }

  public void setTotalAreaA(BigDecimal totalAreaA) {
    this.totalAreaA = totalAreaA;
  }

  public BigDecimal getTotalAreaB() {
    return totalAreaB;
  }

  public void setTotalAreaB(BigDecimal totalAreaB) {
    this.totalAreaB = totalAreaB;
  }

  public BigDecimal getTotalAreaC() {
    return totalAreaC;
  }

  public void setTotalAreaC(BigDecimal totalAreaC) {
    this.totalAreaC = totalAreaC;
  }

  public EstadoLancamentoEnum getStatus() {
    return status;
  }

  public void setStatus(EstadoLancamentoEnum status) {
    this.status = status;
  }

  public Set<LancamentoValor> getValues() {
    return values;
  }

  public void setValues(Set<LancamentoValor> values) {
    this.values = values;
  }

  public Set<LancamentoMedidorValor> getMeterValues() {
    return meterValues;
  }

  public void setMeterValues(Set<LancamentoMedidorValor> meterValues) {
    this.meterValues = meterValues;
  }

  public List<ContaReceber> getContasReceber() {
    return contasReceber;
  }

  public void setContasReceber(List<ContaReceber> contasReceber) {
    this.contasReceber = contasReceber;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

  public List<ContaReceber> getListContasReceber() {
    return new ArrayList<>(contasReceber);
  }

  @Transient
  private List<LancamentoValor> valuesAsList;

  public List<LancamentoValor> getValuesAsList() {
    valuesAsList = valuesAsList == null ? new ArrayList<>(this.values) : valuesAsList;
    return valuesAsList;
  }

}
