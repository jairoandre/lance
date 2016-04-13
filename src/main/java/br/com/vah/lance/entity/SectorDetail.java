package br.com.vah.lance.entity;

import br.com.vah.lance.constant.SectorTypeEnum;
import br.com.vah.lance.entity.mv.MvAccountChart;
import br.com.vah.lance.entity.mv.MvSector;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 29/02/2016.
 */
@Entity
@Table(name = "TB_LANCA_SETOR_DETALHE", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = SectorDetail.ALL, query = "SELECT s FROM SectorDetail s"),
    @NamedQuery(name = SectorDetail.COUNT, query = "SELECT COUNT(s) FROM SectorDetail s")})
public class SectorDetail extends BaseEntity {

  public final static String ALL = "SectorDetail.populatedItems";
  public final static String COUNT = "SectorDetail.countTotal";

  @Id
  @SequenceGenerator(name = "seqSectorDetailGenerator", sequenceName = "SEQ_LANCA_SETOR_DETALHE", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSectorDetailGenerator")
  @Column(name = "ID")
  private Long id;

  @OneToOne
  @JoinColumn(name = "CD_SETOR", nullable = true)
  private MvSector sector;

  @Column(name = "VL_AREA_M2")
  private BigDecimal area;

  @Column(name = "VL_QTD_TR")
  private BigDecimal rtQuantity;

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO", nullable = true)
  private MvAccountChart ledgerAccount;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "CD_TIPO")
  private SectorTypeEnum type;

  public SectorDetail() {
  }

  public SectorDetail(MvSector sector) {
    this.sector = sector;
  }

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

  public BigDecimal getRtQuantity() {
    return rtQuantity;
  }

  public void setRtQuantity(BigDecimal rtQuantity) {
    this.rtQuantity = rtQuantity;
  }

  public MvAccountChart getLedgerAccount() {
    return ledgerAccount;
  }

  public void setLedgerAccount(MvAccountChart ledgerAccount) {
    this.ledgerAccount = ledgerAccount;
  }

  public SectorTypeEnum getType() {
    return type;
  }

  public void setType(SectorTypeEnum type) {
    this.type = type;
  }

  @Override
  public String getLabelForSelectItem() {
    return sector.getTitle() + " - " + getLedgerAccount().getTitle();
  }
}
