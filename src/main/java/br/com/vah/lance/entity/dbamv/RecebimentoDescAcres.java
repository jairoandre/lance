package br.com.vah.lance.entity.dbamv;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Jairoportela on 08/08/2016.
 */
@Entity
@Table(name = "REC_DESC_ACRES", schema = "DBAMV")
public class RecebimentoDescAcres {

  @Id
  @SequenceGenerator(name = "seqRecDescAcres", sequenceName = "SEQ_REC_DESC_ACRES", schema = "DBAMV", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecDescAcres")
  @Column(name = "CD_REC_DESC_ACRES", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_RECCON_REC")
  private Recebimento recebimento;

  @ManyToOne
  @JoinColumn(name = "CD_DESC_ACRES")
  private DescontoAcrescimo descontoAcrescimo;

  @Column(name = "VL_DESC_ACRES")
  private BigDecimal valor;

  @Column(name = "VL_MOEDA_DESC_ACRES")
  private BigDecimal valorMoeda;

  @Column(name = "TP_DESC_ACRES")
  private String tipoDescAcres;

  @Column(name = "DS_DESC_ACRES")
  private String descricaoDescAcres;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Recebimento getRecebimento() {
    return recebimento;
  }

  public void setRecebimento(Recebimento recebimento) {
    this.recebimento = recebimento;
  }

  public DescontoAcrescimo getDescontoAcrescimo() {
    return descontoAcrescimo;
  }

  public void setDescontoAcrescimo(DescontoAcrescimo descontoAcrescimo) {
    this.descontoAcrescimo = descontoAcrescimo;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public BigDecimal getValorMoeda() {
    return valorMoeda;
  }

  public void setValorMoeda(BigDecimal valorMoeda) {
    this.valorMoeda = valorMoeda;
  }

  public String getTipoDescAcres() {
    return tipoDescAcres;
  }

  public void setTipoDescAcres(String tipoDescAcres) {
    this.tipoDescAcres = tipoDescAcres;
  }

  public String getDescricaoDescAcres() {
    return descricaoDescAcres;
  }

  public void setDescricaoDescAcres(String descricaoDescAcres) {
    this.descricaoDescAcres = descricaoDescAcres;
  }
}
