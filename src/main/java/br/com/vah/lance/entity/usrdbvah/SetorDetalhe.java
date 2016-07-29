package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.constant.TipoSetorEnum;
import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.PlanoConta;
import br.com.vah.lance.entity.dbamv.Setor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 29/02/2016.
 */
@Entity
@Table(name = "TB_LANCA_SETOR_DETALHE", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = SetorDetalhe.ALL, query = "SELECT s FROM SetorDetalhe s"),
    @NamedQuery(name = SetorDetalhe.COUNT, query = "SELECT COUNT(s) FROM SetorDetalhe s")})
public class SetorDetalhe extends BaseEntity {

  public final static String ALL = "SetorDetalhe.populatedItems";
  public final static String COUNT = "SetorDetalhe.countTotal";

  @Id
  @SequenceGenerator(name = "seqSetorDetailGenerator", sequenceName = "SEQ_LANCA_SETOR_DETALHE", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSetorDetailGenerator")
  @Column(name = "ID")
  private Long id;

  @OneToOne
  @JoinColumn(name = "CD_SETOR", nullable = true)
  private Setor setor;

  @Column(name = "VL_AREA_M2")
  private BigDecimal area;

  @Column(name = "VL_QTD_TR")
  private BigDecimal rtQuantity;

  @ManyToOne
  @JoinColumn(name = "CD_REDUZIDO")
  private PlanoConta contaContabil;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "CD_TIPO")
  private TipoSetorEnum type;

  @Column(name = "CD_INSC_IMOBI")
  private String inscricaoImobiliaria;

  public SetorDetalhe() {
  }

  public SetorDetalhe(Setor setor) {
    this.setor = setor;
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

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
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

  public PlanoConta getContaContabil() {
    return contaContabil;
  }

  public void setContaContabil(PlanoConta contaContabil) {
    this.contaContabil = contaContabil;
  }

  public TipoSetorEnum getType() {
    return type;
  }

  public void setType(TipoSetorEnum type) {
    this.type = type;
  }

  public String getInscricaoImobiliaria() {
    return inscricaoImobiliaria;
  }

  public void setInscricaoImobiliaria(String inscricaoImobiliaria) {
    this.inscricaoImobiliaria = inscricaoImobiliaria;
  }

  @Override
  public String getLabelForSelectItem() {
    return setor.getTitle() + " - " + getContaContabil().getTitle();
  }
}
