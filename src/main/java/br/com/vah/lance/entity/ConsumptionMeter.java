package br.com.vah.lance.entity;

import br.com.vah.lance.constant.ServiceTypesEnum;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TB_LANCA_MEDIDOR", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = ConsumptionMeter.ALL, query = "SELECT c FROM ConsumptionMeter c"),
    @NamedQuery(name = ConsumptionMeter.COUNT, query = "SELECT COUNT(c) FROM ConsumptionMeter c"),
    @NamedQuery(name = ConsumptionMeter.BY_TYPE, query = "SELECT c FROM ConsumptionMeter c WHERE c.type = :type")})
public class ConsumptionMeter extends BaseEntity {

  private static final long serialVersionUID = 1L;
  public static final String ALL = "ConsumptionMeter.populatedItems";
  public static final String COUNT = "ConsumptionMeter.countTotal";
  public static final String BY_TYPE = "ConsumptionMeter.byType";

  @Id
  @SequenceGenerator(name = "seqConsumptionMeterGenerator", sequenceName = "SEQ_LANCA_MEDIDOR", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqConsumptionMeterGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_CODIGO")
  private String code;

  @Column(name = "NM_TITULO")
  private String title;

  @Column(name = "NM_DETALHES")
  private String detail;

  @Enumerated(EnumType.STRING)
  @Column(name = "CD_TIPO")
  private ServiceTypesEnum type;

  @OneToMany(mappedBy = "consumptionMeter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<SectorConsumptionMeter> sectors;

  public ConsumptionMeter() {
    this.sectors = new LinkedHashSet<>();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public Set<SectorConsumptionMeter> getSectors() {
    return sectors;
  }

  public void setSectors(Set<SectorConsumptionMeter> sectors) {
    this.sectors = sectors;
  }

  public ServiceTypesEnum getType() {
    return type;
  }

  public void setType(ServiceTypesEnum type) {
    this.type = type;
  }

  @Override
  public String getLabelForSelectItem() {
    return this.title;
  }
}