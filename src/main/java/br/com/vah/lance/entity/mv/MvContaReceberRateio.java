package br.com.vah.lance.entity.mv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 03/05/2016.
 */
@Entity
@Table(name = "RAT_CONREC", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvContaReceberRateio.ALL, query = "SELECT c FROM MvContaReceberRateio c"),
    @NamedQuery(name = MvContaReceberRateio.COUNT, query = "SELECT COUNT(c) FROM MvContaReceberRateio c") })
public class MvContaReceberRateio extends BaseEntity {

  public final static String ALL = "MvContaReceberRateio.populatedItems";
  public final static String COUNT = "MvContaReceberRateio.countTotal";


  @EmbeddedId
  private ContaReceberRateioPK pk;

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO")
  private MvPlanoConta contaContabil;

  @ManyToOne
  @JoinColumn(name = "CD_ITEM_RES")
  private MvResultItem contaResultado;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR")
  private MvSector setor;


  @Column(name = "VL_RATEIO", nullable = false)
  private BigDecimal valorRateio;

  @Override
  public Long getId() {
    return null;
  }

  @Override
  public void setId(Long id) {

  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

  public ContaReceberRateioPK getPk() {
    return pk;
  }

  public void setPk(ContaReceberRateioPK pk) {
    this.pk = pk;
  }

  public MvPlanoConta getContaContabil() {
    return contaContabil;
  }

  public void setContaContabil(MvPlanoConta contaContabil) {
    this.contaContabil = contaContabil;
  }

  public MvResultItem getContaResultado() {
    return contaResultado;
  }

  public void setContaResultado(MvResultItem contaResultado) {
    this.contaResultado = contaResultado;
  }

  public MvSector getSetor() {
    return setor;
  }

  public void setSetor(MvSector setor) {
    this.setor = setor;
  }

  public BigDecimal getValorRateio() {
    return valorRateio;
  }

  public void setValorRateio(BigDecimal valorRateio) {
    this.valorRateio = valorRateio;
  }
}
