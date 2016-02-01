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

  public Entry() {
    this.status = EntryStatusEnum.N;
    this.createdOn = new Date();
    this.effectiveOn = new Date();
    this.totalValue = BigDecimal.ZERO;
    this.comments = new LinkedHashSet<>();
    this.values = new LinkedHashSet<>();
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

  /**
   * @return the userForEntry
   */
  public User getUserForEntry() {
    return userForEntry;
  }

  /**
   * @param userForEntry the userForEntry to set
   */
  public void setUserForEntry(User userForEntry) {
    this.userForEntry = userForEntry;
  }

  /**
   * @return the comments
   */
  public Set<Comment> getComments() {
    return comments;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  /**
   * @return
   */
  public Service getService() {
    return service;
  }

  /**
   * @param service
   */
  public void setService(Service service) {
    this.service = service;
  }

  /**
   *
   * @return
   */
  public ServiceValue getServiceValue() {
    return serviceValue;
  }

  /**
   *
   * @param serviceValue
   */
  public void setServiceValue(ServiceValue serviceValue) {
    this.serviceValue = serviceValue;
  }

  /**
   * @return the value
   */
  public BigDecimal getTotalValue() {
    return totalValue;
  }

  /**
   * @param value the value to set
   */
  public void setTotalValue(BigDecimal value) {
    this.totalValue = value;
  }

  /**
   * @return the createdOn
   */
  public Date getCreatedOn() {
    return createdOn;
  }

  /**
   * @param createdOn the createdOn to set
   */
  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  /**
   * @return the effectiveOn
   */
  public Date getEffectiveOn() {
    return effectiveOn;
  }

  /**
   * @param effectiveOn the effectiveOn to set
   */
  public void setEffectiveOn(Date effectiveOn) {
    this.effectiveOn = effectiveOn;
  }

  /**
   * @return the status
   */
  public EntryStatusEnum getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(EntryStatusEnum status) {
    this.status = status;
  }

  /**
   * @return
   */
  public Set<EntryValue> getValues() {
    return values;
  }

  /**
   * @param values
   */
  public void setValues(Set<EntryValue> values) {
    this.values = values;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

}
