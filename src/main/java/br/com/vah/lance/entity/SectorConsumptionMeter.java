package br.com.vah.lance.entity;

import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.mv.MvSector;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jairoportela on 07/03/2016.
 */
@Entity
@Table(name = "TB_LANCA_SETOR_MEDIDOR", schema = "USRDBVAH")
@NamedQueries({ @NamedQuery(name = SectorConsumptionMeter.ALL, query = "SELECT c FROM SectorConsumptionMeter c"),
    @NamedQuery(name = SectorConsumptionMeter.COUNT, query = "SELECT COUNT(c) FROM SectorConsumptionMeter c") })
public class SectorConsumptionMeter extends BaseEntity {

  private static final long serialVersionUID = 1L;
  public static final String ALL = "SectorConsumptionMeter.populatedItems";
  public static final String COUNT = "SectorConsumptionMeter.countTotal";

  @Id
  @SequenceGenerator(name = "seqSectorConsumptionMeterGenerator", sequenceName = "SEQ_LANCA_SETOR_MEDIDOR", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSectorConsumptionMeterGenerator")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR", nullable = true)
  private MvSector sector;

  @ManyToOne
  @JoinColumn(name = "ID_MEDIDOR", nullable = true)
  private ConsumptionMeter consumptionMeter;

  @Column(name = "VL_PERCENTUAL")
  private BigDecimal percent = BigDecimal.ZERO;

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public ConsumptionMeter getConsumptionMeter() {
    return consumptionMeter;
  }

  public void setConsumptionMeter(ConsumptionMeter consumptionMeter) {
    this.consumptionMeter = consumptionMeter;
  }

  public BigDecimal getPercent() {
    return percent;
  }

  public void setPercent(BigDecimal percent) {
    this.percent = percent;
  }

  public MvSector getSector() {
    return sector;
  }

  public void setSector(MvSector sector) {
    this.sector = sector;
  }

  @Override
  public String getLabelForSelectItem() {
    return this.sector.getTitle() + " :: " + this.consumptionMeter.getTitle() + " :: " + this.percent;
  }
}
