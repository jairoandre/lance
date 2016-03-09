package br.com.vah.lance.entity;

import br.com.vah.lance.constant.EntryStatusEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Entidade que representa um conjunto de lançamentos.
 *
 * @author jairoportela
 */
@Entity
@Table(name = "TB_LANCA_LANCAMENTO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Entry.ALL, query = "SELECT e FROM Entry e"),
    @NamedQuery(name = Entry.COUNT, query = "SELECT COUNT(e) FROM Entry e"),
    @NamedQuery(name = Entry.BY_PERIOD_AND_SERVICE, query = "SELECT e FROM Entry e where e.effectiveOn between :begin and :end and e.service in :services"),
    @NamedQuery(name = Entry.BY_PERIOD, query = "SELECT e FROM Entry e where e.effectiveOn between :begin and :end")})
public class Entry extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "Entry.populatedItems";
  public static final String COUNT = "Entry.countTotal";

  public static final String BY_PERIOD_AND_SERVICE = "Entry.byPeriodAndService";
  public static final String BY_PERIOD = "Entry.byPeriod";

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
  private User userForEntry;

  /**
   * Comentários do lançamento
   */
  @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Comment> comments;

  /**
   * Serviço ao qual o lançamento se refere
   */
  @ManyToOne
  @JoinColumn(name = "ID_SERVICO", nullable = false)
  private Service service;

  /**
   * Valor do serviço na vigência do lançamento
   */
  @ManyToOne
  @JoinColumn(name = "ID_SERVICO_VALORES", nullable = false)
  private ServiceValue serviceValue;

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
  private EntryStatusEnum status;

  /**
   * Os valores individuais de lançamento (por cliente/contrato)
   */
  @OneToMany(mappedBy = "entry", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<EntryValue> values;

  /**
   * Os valores individuais de leituras de medidores (para os caso de Energia e Gás)
   */
  @OneToMany(mappedBy = "entry", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<EntryMeterValue> meterValues;

  public Entry() {
    this.status = EntryStatusEnum.N;
    this.createdOn = new Date();
    this.effectiveOn = new Date();
    this.totalValue = BigDecimal.ZERO;
    this.comments = new LinkedHashSet<>();
    this.values = new LinkedHashSet<>();
    this.meterValues = new LinkedHashSet<>();
  }

  public Entry(Service service) {
    this();
    this.service = service;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public User getUserForEntry() {
    return userForEntry;
  }

  public void setUserForEntry(User userForEntry) {
    this.userForEntry = userForEntry;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public ServiceValue getServiceValue() {
    return serviceValue;
  }

  public void setServiceValue(ServiceValue serviceValue) {
    this.serviceValue = serviceValue;
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

  public EntryStatusEnum getStatus() {
    return status;
  }

  public void setStatus(EntryStatusEnum status) {
    this.status = status;
  }

  public Set<EntryValue> getValues() {
    return values;
  }

  public void setValues(Set<EntryValue> values) {
    this.values = values;
  }

  public Set<EntryMeterValue> getMeterValues() {
    return meterValues;
  }

  public void setMeterValues(Set<EntryMeterValue> meterValues) {
    this.meterValues = meterValues;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

}
