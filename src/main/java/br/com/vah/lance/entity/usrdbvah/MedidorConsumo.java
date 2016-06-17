package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TB_LANCA_MEDIDOR", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = MedidorConsumo.ALL, query = "SELECT c FROM MedidorConsumo c"),
    @NamedQuery(name = MedidorConsumo.COUNT, query = "SELECT COUNT(c) FROM MedidorConsumo c"),
    @NamedQuery(name = MedidorConsumo.BY_TYPE, query = "SELECT c FROM MedidorConsumo c WHERE c.type = :type")})
public class MedidorConsumo extends BaseEntity {

  private static final long serialVersionUID = 1L;
  public static final String ALL = "MedidorConsumo.populatedItems";
  public static final String COUNT = "MedidorConsumo.countTotal";
  public static final String BY_TYPE = "MedidorConsumo.byType";

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
  private TipoServicoEnum type;

  @Column(name = "VL_FATOR")
  private BigDecimal factor;

  @OneToMany(mappedBy = "medidorConsumo", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<SetorMedidorConsumo> setores;

  public MedidorConsumo() {
    this.setores = new LinkedHashSet<>();
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

  public Set<SetorMedidorConsumo> getSetores() {
    return setores;
  }

  public void setSetores(Set<SetorMedidorConsumo> setores) {
    this.setores = setores;
  }

  public TipoServicoEnum getType() {
    return type;
  }

  public void setType(TipoServicoEnum type) {
    this.type = type;
  }

  public BigDecimal getFactor() {
    return factor;
  }

  public void setFactor(BigDecimal factor) {
    this.factor = factor;
  }

  @Override
  public String getLabelForSelectItem() {
    return this.title;
  }
}