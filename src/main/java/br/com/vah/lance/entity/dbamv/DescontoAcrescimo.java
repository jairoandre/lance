package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by Jairoportela on 08/08/2016.
 */
@Entity
@Table(name = "DESCONTOS_ACRESCIMOS", schema = "DBAMV")
public class DescontoAcrescimo extends BaseEntity {
  @Override
  public String getLabelForSelectItem() {
    return String.format("%d - %s", id, descricao);
  }

  @Id
  @Column(name = "CD_DESC_ACRES", nullable = false)
  private Long id;

  @Column(name = "DS_DESC_ACRES")
  private String descricao;

  @Column(name = "TP_DESC_ACRES")
  private String tipo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
}
