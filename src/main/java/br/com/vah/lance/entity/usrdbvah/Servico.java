
package br.com.vah.lance.entity.usrdbvah;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.HistoricoPadrao;
import br.com.vah.lance.entity.dbamv.PlanoConta;
import br.com.vah.lance.entity.dbamv.TipoDocumento;
import br.com.vah.lance.entity.dbamv.ItemResultado;

@Entity
@Table(name = "TB_LANCA_SERVICO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Servico.ALL, query = "SELECT s FROM Servico s"),
    @NamedQuery(name = Servico.COUNT, query = "SELECT COUNT(s) FROM Servico s")})
public class Servico extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public final static String ALL = "Servico.all";
  public final static String COUNT = "Servico.total";

  @Id
  @SequenceGenerator(name = "seqServiceGenerator", sequenceName = "SEQ_LANCA_SERVICO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String title;

  @ManyToOne
  @JoinColumn(name = "CD_HISTORICO_PADRAO", nullable = false)
  private HistoricoPadrao historicoPadrao = new HistoricoPadrao();

  @ManyToOne
  @JoinColumn(name = "CD_TP_DOCUMENTO", nullable = false)
  private TipoDocumento tipoDocumento = new TipoDocumento();

  @ManyToOne
  @JoinColumn(name = "CD_CONTA_CONTABIL", nullable = true)
  private PlanoConta contaContabil = new PlanoConta();

  @ManyToOne
  @JoinColumn(name = "CD_CONTA_RESULTADO", nullable = false)
  private PlanoConta contaResultado = new PlanoConta();

  @ManyToOne
  @JoinColumn(name = "CD_CONTA_CUSTO", nullable = false)
  private ItemResultado contaCusto = new ItemResultado();

  @Column(name = "SN_FATURAVEL")
  private Boolean faturavel;

  @Column(name = "SN_AGRUPAVEL")
  private Boolean agrupavel;

  @Column(name = "SN_COMPULSORIO")
  private Boolean compulsorio;

  @Column(name = "SN_CONTA_SETOR")
  private Boolean contaPorSetor = false;

  @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<ServicoValor> values = new LinkedHashSet<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "CD_TIPO")
  private TipoServicoEnum type;

  @Column(name = "VL_DIA_LIMITE")
  private Integer diaLimite;

  @Column(name = "VL_DIA_VENCIMENTO")
  private Integer diaVencimento;

  @Transient
  private Boolean systemAdded = false;

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
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
  public HistoricoPadrao getHistoricoPadrao() {
    return historicoPadrao;
  }

  /**
   * @param defaultHistory the defaultHistory to set
   */
  public void setHistoricoPadrao(HistoricoPadrao defaultHistory) {
    this.historicoPadrao = defaultHistory;
  }

  /**
   * @return the documentType
   */
  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  /**
   * @param documentType the documentType to set
   */
  public void setTipoDocumento(TipoDocumento documentType) {
    this.tipoDocumento = documentType;
  }

  /**
   * @return the contaContabil
   */
  public PlanoConta getContaContabil() {
    return contaContabil;
  }

  /**
   * @param contaContabil the contaContabil to set
   */
  public void setContaContabil(PlanoConta contaContabil) {
    this.contaContabil = contaContabil;
  }

  /**
   * @return the resultAccount
   */
  public PlanoConta getContaResultado() {
    return contaResultado;
  }

  /**
   * @param resultAccount the resultAccount to set
   */
  public void setContaResultado(PlanoConta resultAccount) {
    this.contaResultado = resultAccount;
  }

  /**
   * @return the costAccount
   */
  public ItemResultado getContaCusto() {
    return contaCusto;
  }

  /**
   * @param costAccount the costAccount to set
   */
  public void setContaCusto(ItemResultado costAccount) {
    this.contaCusto = costAccount;
  }

  /**
   * @return the billable
   */
  public Boolean getFaturavel() {
    return faturavel;
  }

  /**
   * @param billable the billable to set
   */
  public void setFaturavel(Boolean billable) {
    this.faturavel = billable;
  }


  public Boolean getAgrupavel() {
    return agrupavel;
  }

  public void setAgrupavel(Boolean agrupavel) {
    this.agrupavel = agrupavel;
  }

  /**
   * @return the compulsory
   */
  public Boolean getCompulsorio() {
    return compulsorio;
  }

  /**
   * @param compulsory the compulsory to set
   */
  public void setCompulsorio(Boolean compulsory) {
    this.compulsorio = compulsory;
  }

  /**
   * @return the values
   */
  public Set<ServicoValor> getValues() {
    return values;
  }

  /**
   * @param values the values to set
   */
  public void setValues(Set<ServicoValor> values) {
    this.values = values;
  }

  /**
   * @return the type
   */
  public TipoServicoEnum getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(TipoServicoEnum type) {
    this.type = type;
  }

  public Boolean getContaPorSetor() {
    return contaPorSetor;
  }

  public void setContaPorSetor(Boolean contaPorSetor) {
    this.contaPorSetor = contaPorSetor;
  }

  public Integer getDiaLimite() {
    return diaLimite;
  }

  public void setDiaLimite(Integer diaLimite) {
    this.diaLimite = diaLimite;
  }

  public Integer getDiaVencimento() {
    return diaVencimento;
  }

  public void setDiaVencimento(Integer diaVencimento) {
    this.diaVencimento = diaVencimento;
  }

  public Boolean getSystemAdded() {
    return systemAdded;
  }

  public void setSystemAdded(Boolean systemAdded) {
    this.systemAdded = systemAdded;
  }

  @Override
  public String getLabelForSelectItem() {
    return getTitle();
  }
}
