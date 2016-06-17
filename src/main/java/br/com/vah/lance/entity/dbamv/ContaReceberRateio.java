package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 03/05/2016.
 */
@Entity
@Table(name = "RAT_CONREC", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = ContaReceberRateio.ALL, query = "SELECT c FROM ContaReceberRateio c"),
    @NamedQuery(name = ContaReceberRateio.COUNT, query = "SELECT COUNT(c) FROM ContaReceberRateio c")})
public class ContaReceberRateio extends BaseEntity {

  public final static String ALL = "ContaReceberRateio.populatedItems";
  public final static String COUNT = "ContaReceberRateio.countTotal";


  @EmbeddedId
  private ContaReceberRateioPK pk = new ContaReceberRateioPK();

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO")
  private PlanoConta contaContabil;

  @ManyToOne
  @JoinColumn(name = "CD_ITEM_RES")
  private ItemResultado contaResultado;

  @Column(name = "VL_RATEIO", nullable = false)
  private BigDecimal valorRateio;

  @Column(name = "CD_SETOR")
  private Long cdSetor;

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

  public PlanoConta getContaContabil() {
    return contaContabil;
  }

  public void setContaContabil(PlanoConta contaContabil) {
    this.contaContabil = contaContabil;
  }

  public ItemResultado getContaResultado() {
    return contaResultado;
  }

  public void setContaResultado(ItemResultado contaResultado) {
    this.contaResultado = contaResultado;
  }

  public BigDecimal getValorRateio() {
    return valorRateio;
  }

  public void setValorRateio(BigDecimal valorRateio) {
    this.valorRateio = valorRateio;
  }

  public Long getCdSetor() {
    return cdSetor;
  }

  public void setCdSetor(Long cdSetor) {
    this.cdSetor = cdSetor;
  }
}
