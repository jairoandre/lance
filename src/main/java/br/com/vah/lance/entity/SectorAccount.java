package br.com.vah.lance.entity;

import br.com.vah.lance.entity.mv.MvAccountChart;
import br.com.vah.lance.entity.mv.MvSector;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 29/02/2016.
 */
@Entity
@Table(name = "TB_LANCA_SETOR_CONTA", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = SectorAccount.ALL, query = "SELECT s FROM SectorAccount s"),
    @NamedQuery(name = SectorAccount.COUNT, query = "SELECT COUNT(s) FROM SectorAccount s")})
public class SectorAccount extends BaseEntity {

  public final static String ALL = "SectorAccount.populatedItems";
  public final static String COUNT = "SectorAccount.countTotal";

  @Id
  @SequenceGenerator(name = "seqSectorValueGenerator", sequenceName = "SEQ_LANCA_SETOR_CONTA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSectorValueGenerator")
  @Column(name = "ID")
  private Long id;

  @OneToOne
  @JoinColumn(name = "CD_SETOR", nullable = true)
  private MvSector sector;

  @Column(name = "VL_AREA_M2")
  private BigDecimal area;

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO", nullable = true)
  private MvAccountChart ledgerAccount;

  public static String getALL() {
    return ALL;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public MvSector getSector() {
    return sector;
  }

  public void setSector(MvSector sector) {
    this.sector = sector;
  }

  public BigDecimal getArea() {
    return area;
  }

  public void setArea(BigDecimal area) {
    this.area = area;
  }

  public MvAccountChart getLedgerAccount() {
    return ledgerAccount;
  }

  public void setLedgerAccount(MvAccountChart ledgerAccount) {
    this.ledgerAccount = ledgerAccount;
  }

  @Override
  public String getLabelForSelectItem() {
    return sector.getTitle() + " - " + getLedgerAccount().getTitle();
  }
}
