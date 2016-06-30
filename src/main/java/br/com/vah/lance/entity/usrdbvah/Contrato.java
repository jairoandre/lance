package br.com.vah.lance.entity.usrdbvah;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.Fornecedor;

/**
 * The class for the TB_LANCA_CONTRATO database table.
 **/
@Entity
@Table(name = "TB_LANCA_CONTRATO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Contrato.ALL, query = Contrato.ALL_QUERY),
    @NamedQuery(name = Contrato.COUNT, query = "SELECT COUNT(c) FROM Contrato c"),
    @NamedQuery(name = Contrato.VALIDS_IN_DATE, query = "SELECT c FROM Contrato c WHERE c.beginDate <= :date AND c.endDate >= :date"),
    @NamedQuery(name = Contrato.BY_CONTRATANTE_PERIOD, query = "SELECT c FROM Contrato c WHERE c.contratante = :contratante AND ((c.beginDate BETWEEN :begin AND :end) OR (c.endDate BETWEEN :begin AND :end))")})
public class Contrato extends BaseEntity {
  private static final long serialVersionUID = 1L;

  public static final String ALL = "Contrato.populatedItems";
  public static final String ALL_QUERY = "SELECT c FROM Contrato c";
  public static final String COUNT = "Contrato.countTotal";
  public static final String VALIDS_IN_DATE = "Contrato.validsInDate";
  public static final String BY_CONTRATANTE_PERIOD = "Contrato.byContratantePeriod";

  @Id
  @SequenceGenerator(name = "seqContractGenerator", sequenceName = "SEQ_LANCA_CONTRATO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqContractGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String title;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_FINAL")
  private Date endDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INICIO")
  private Date beginDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_REAJUSTE")
  private Date changeDate;

  @ManyToOne
  @JoinColumn(name = "ID_FORNECEDOR_CONTRATANTE", nullable = false)
  private Fornecedor contratante;

  @OneToMany(mappedBy = "contrato", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<ContratoSetor> setores;

  @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_LANCA_CON_SER", joinColumns = {
      @JoinColumn(name = "ID_CONTRATO")}, inverseJoinColumns = {@JoinColumn(name = "ID_SERVICO")}, schema = "USRDBVAH")
  private Set<Servico> servicos = new LinkedHashSet<>();

  public Contrato() {
    this.setores = new LinkedHashSet<>();
    this.contratante = new Fornecedor();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Contrato other = (Contrato) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String getLabelForSelectItem() {
    return getTitle();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  public Fornecedor getContratante() {
    return contratante;
  }

  public void setContratante(Fornecedor contratante) {
    this.contratante = contratante;
  }

  public Set<ContratoSetor> getSetores() {
    return setores;
  }

  public void setSetores(Set<ContratoSetor> setores) {
    this.setores = setores;
  }

  public Set<Servico> getServicos() {
    return servicos;
  }

  public void setServicos(Set<Servico> servicos) {
    this.servicos = servicos;
  }
}
