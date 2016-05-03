package br.com.vah.lance.entity.mv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jairoportela on 03/05/2016.
 */
@Entity
@Table(name = "ITCON_REC", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = MvContaReceberItem.ALL, query = "SELECT c FROM MvContaReceberItem c"),
    @NamedQuery(name = MvContaReceberItem.COUNT, query = "SELECT COUNT(c) FROM MvContaReceberItem c")})
public class MvContaReceberItem extends BaseEntity {

  public final static String ALL = "MvContaReceberItem.populatedItems";
  public final static String COUNT = "MvContaReceberItem.countTotal";

  @Id
  @SequenceGenerator(name = "seqItConRecGen", sequenceName = "SEQ_ITCON_REC", schema = "DBAMV", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqItConRecGen")
  @Column(name = "CD_ITCON_REC", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_CON_REC", nullable = false)
  private MvContaReceber contaReceber;

  @Column(name = "VL_MOEDA", nullable = false)
  private BigDecimal valorMoeda;

  @Column(name = "VL_SOMA_RECEBIDO")
  private BigDecimal valorSomaRecebido;

  @Column(name = "VL_DUPLICATA", nullable = false)
  private BigDecimal valorDuplicata;

  @Column(name = "CD_MOEDA")
  private String codigoMoeda = "1";

  @Column(name = "TP_QUITACAO", nullable = false)
  private String tipoQuitacao = "C";

  @Column(name = "DT_VENCIMENTO", nullable = false)
  private Date dataVencimento;

  @Column(name = "DT_PREVISTA_RECEBIMENTO", nullable = false)
  private Date dataPrevistaRecebimento;

  @Column(name = "NR_PARCELA", nullable = false)
  private Integer numeroParcela = 1;


  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public MvContaReceber getContaReceber() {
    return contaReceber;
  }

  public void setContaReceber(MvContaReceber contaReceber) {
    this.contaReceber = contaReceber;
  }

  public BigDecimal getValorMoeda() {
    return valorMoeda;
  }

  public void setValorMoeda(BigDecimal valorMoeda) {
    this.valorMoeda = valorMoeda;
  }

  public BigDecimal getValorSomaRecebido() {
    return valorSomaRecebido;
  }

  public void setValorSomaRecebido(BigDecimal valorSomaRecebido) {
    this.valorSomaRecebido = valorSomaRecebido;
  }

  public BigDecimal getValorDuplicata() {
    return valorDuplicata;
  }

  public void setValorDuplicata(BigDecimal valorDuplicata) {
    this.valorDuplicata = valorDuplicata;
  }

  public String getCodigoMoeda() {
    return codigoMoeda;
  }

  public void setCodigoMoeda(String codigoMoeda) {
    this.codigoMoeda = codigoMoeda;
  }

  public String getTipoQuitacao() {
    return tipoQuitacao;
  }

  public void setTipoQuitacao(String tipoQuitacao) {
    this.tipoQuitacao = tipoQuitacao;
  }

  public Date getDataVencimento() {
    return dataVencimento;
  }

  public void setDataVencimento(Date dataVencimento) {
    this.dataVencimento = dataVencimento;
  }

  public Date getDataPrevistaRecebimento() {
    return dataPrevistaRecebimento;
  }

  public void setDataPrevistaRecebimento(Date dataPrevistaRecebimento) {
    this.dataPrevistaRecebimento = dataPrevistaRecebimento;
  }

  public Integer getNumeroParcela() {
    return numeroParcela;
  }

  public void setNumeroParcela(Integer numeroParcela) {
    this.numeroParcela = numeroParcela;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
