package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.Setor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 07/03/2016.
 */
@Entity
@Table(name = "TB_LANCA_SETOR_MEDIDOR", schema = "USRDBVAH")
@NamedQueries({ @NamedQuery(name = SetorMedidorConsumo.ALL, query = "SELECT c FROM SetorMedidorConsumo c"),
    @NamedQuery(name = SetorMedidorConsumo.COUNT, query = "SELECT COUNT(c) FROM SetorMedidorConsumo c") })
public class SetorMedidorConsumo extends BaseEntity {

  private static final long serialVersionUID = 1L;
  public static final String ALL = "SetorMedidorConsumo.populatedItems";
  public static final String COUNT = "SetorMedidorConsumo.countTotal";

  @Id
  @SequenceGenerator(name = "seqSetorConsumptionMeterGenerator", sequenceName = "SEQ_LANCA_SETOR_MEDIDOR", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSetorConsumptionMeterGenerator")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR", nullable = true)
  private Setor setor;

  @ManyToOne
  @JoinColumn(name = "ID_MEDIDOR", nullable = true)
  private MedidorConsumo medidorConsumo;

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

  public MedidorConsumo getMedidorConsumo() {
    return medidorConsumo;
  }

  public void setMedidorConsumo(MedidorConsumo medidorConsumo) {
    this.medidorConsumo = medidorConsumo;
  }

  public BigDecimal getPercent() {
    return percent;
  }

  public void setPercent(BigDecimal percent) {
    this.percent = percent;
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  @Override
  public String getLabelForSelectItem() {
    return this.setor.getTitle() + " :: " + this.medidorConsumo.getTitle() + " :: " + this.percent;
  }
}
