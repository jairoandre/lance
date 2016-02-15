package br.com.vah.lance.entity;

import br.com.vah.lance.entity.mv.MvAccountChart;
import br.com.vah.lance.entity.mv.MvSector;

import javax.persistence.*;

/**
 * Created by jairoportela on 15/02/2016.
 */
@Entity
@Table(name = "TB_LANCA_SER_SEC_CON", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = ServiceSectorAccount.ALL, query = "SELECT s FROM ServiceSectorAccount s"),
    @NamedQuery(name = ServiceSectorAccount.COUNT, query = "SELECT COUNT(s) FROM ServiceSectorAccount s")})
public class ServiceSectorAccount extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public final static String ALL = "ServiceSectorAccount.all";
  public final static String COUNT = "ServiceSectorAccount.total";

  @Id
  @SequenceGenerator(name = "seqServSecCon", sequenceName = "SEQ_LANCA_SER_SEC_CON", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServSecCon")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_SERVICO", nullable = false)
  private Service service;

  @ManyToOne
  @JoinColumn(name = "ID_SETOR", nullable = false)
  private MvSector sector = new MvSector();

  @ManyToOne
  @JoinColumn(name = "ID_CONTA", nullable = false)
  private MvAccountChart account = new MvAccountChart();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public MvSector getSector() {
    return sector;
  }

  public void setSector(MvSector sector) {
    this.sector = sector;
  }

  public MvAccountChart getAccount() {
    return account;
  }

  public void setAccount(MvAccountChart account) {
    this.account = account;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
