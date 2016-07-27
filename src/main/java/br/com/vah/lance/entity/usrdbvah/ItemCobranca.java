package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 20/07/2016.
 */
@Entity
@Table(name = "TB_LANCA_ITEM_COBRANCA", schema = "USRDBVAH")
public class ItemCobranca extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqItemCobrancaGenerator", sequenceName = "SEQ_LANCA_ITEM_COBRANCA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqItemCobrancaGenerator")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_SERVICO")
  private Servico servico;

  @Column(name = "VL_TOTAL")
  private BigDecimal total;

  @Column(name = "VL_VALOR")
  private BigDecimal valor;

  @ManyToOne
  @JoinColumn(name = "ID_COBRANCA")
  private Cobranca cobranca;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Servico getServico() {
    return servico;
  }

  public void setServico(Servico servico) {
    this.servico = servico;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Cobranca getCobranca() {
    return cobranca;
  }

  public void setCobranca(Cobranca cobranca) {
    this.cobranca = cobranca;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
