
package br.com.vah.lance.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.mv.MvAccountChart;
import br.com.vah.lance.entity.mv.MvDefaultHistory;
import br.com.vah.lance.entity.mv.MvDocumentType;
import br.com.vah.lance.entity.mv.MvResultItem;

@Entity
@Table(name = "TB_LANCA_SERVICO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Service.ALL, query = "SELECT s FROM Service s"),
    @NamedQuery(name = Service.COUNT, query = "SELECT COUNT(s) FROM Service s")})
public class Service extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public final static String ALL = "Service.all";
  public final static String COUNT = "Service.total";

  @Id
  @SequenceGenerator(name = "seqServiceGenerator", sequenceName = "SEQ_LANCA_SERVICO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String title;

  @ManyToOne
  @JoinColumn(name = "CD_HISTORICO_PADRAO", nullable = false)
  private MvDefaultHistory defaultHistory = new MvDefaultHistory();

  @ManyToOne
  @JoinColumn(name = "CD_TP_DOCUMENTO", nullable = false)
  private MvDocumentType documentType = new MvDocumentType();

  @ManyToOne
  @JoinColumn(name = "CD_CONTA_CONTABIL", nullable = true)
  private MvAccountChart ledgerAccount = new MvAccountChart();

  @ManyToOne
  @JoinColumn(name = "CD_CONTA_RESULTADO", nullable = false)
  private MvAccountChart resultAccount = new MvAccountChart();

  @ManyToOne
  @JoinColumn(name = "CD_CONTA_CUSTO", nullable = false)
  private MvResultItem costAccount = new MvResultItem();

  @Column(name = "SN_FATURAVEL")
  private Boolean billable;

  @Column(name = "SN_AGRUPAVEL")
  private Boolean clusterable;

  @Column(name = "SN_COMPULSORIO")
  private Boolean compulsory;

  @Column(name = "SN_CONTA_SETOR")
  private Boolean accountBySector = false;

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<ServiceValue> values = new LinkedHashSet<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "CD_TIPO")
  private ServiceTypesEnum type;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the defaultHistory
   */
  public MvDefaultHistory getDefaultHistory() {
    return defaultHistory;
  }

  /**
   * @param defaultHistory the defaultHistory to set
   */
  public void setDefaultHistory(MvDefaultHistory defaultHistory) {
    this.defaultHistory = defaultHistory;
  }

  /**
   * @return the documentType
   */
  public MvDocumentType getDocumentType() {
    return documentType;
  }

  /**
   * @param documentType the documentType to set
   */
  public void setDocumentType(MvDocumentType documentType) {
    this.documentType = documentType;
  }

  /**
   * @return the ledgerAccount
   */
  public MvAccountChart getLedgerAccount() {
    return ledgerAccount;
  }

  /**
   * @param ledgerAccount the ledgerAccount to set
   */
  public void setLedgerAccount(MvAccountChart ledgerAccount) {
    this.ledgerAccount = ledgerAccount;
  }

  /**
   * @return the resultAccount
   */
  public MvAccountChart getResultAccount() {
    return resultAccount;
  }

  /**
   * @param resultAccount the resultAccount to set
   */
  public void setResultAccount(MvAccountChart resultAccount) {
    this.resultAccount = resultAccount;
  }

  /**
   * @return the costAccount
   */
  public MvResultItem getCostAccount() {
    return costAccount;
  }

  /**
   * @param costAccount the costAccount to set
   */
  public void setCostAccount(MvResultItem costAccount) {
    this.costAccount = costAccount;
  }

  /**
   * @return the billable
   */
  public Boolean getBillable() {
    return billable;
  }

  /**
   * @param billable the billable to set
   */
  public void setBillable(Boolean billable) {
    this.billable = billable;
  }


  public Boolean getClusterable() {
    return clusterable;
  }

  public void setClusterable(Boolean clusterable) {
    this.clusterable = clusterable;
  }

  /**
   * @return the compulsory
   */
  public Boolean getCompulsory() {
    return compulsory;
  }

  /**
   * @param compulsory the compulsory to set
   */
  public void setCompulsory(Boolean compulsory) {
    this.compulsory = compulsory;
  }

  /**
   * @return the values
   */
  public Set<ServiceValue> getValues() {
    return values;
  }

  /**
   * @param values the values to set
   */
  public void setValues(Set<ServiceValue> values) {
    this.values = values;
  }

  /**
   * @return the type
   */
  public ServiceTypesEnum getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(ServiceTypesEnum type) {
    this.type = type;
  }

  public Boolean getAccountBySector() {
    return accountBySector;
  }

  public void setAccountBySector(Boolean accountBySector) {
    this.accountBySector = accountBySector;
  }

  @Override
  public String getLabelForSelectItem() {
    return getTitle();
  }
}
