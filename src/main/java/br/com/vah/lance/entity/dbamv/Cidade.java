package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jairoportela on 26/07/2016.
 */
@Entity
@Table(name = "CIDADE", schema = "DBAMV")
public class Cidade extends BaseEntity {

  @Id
  @Column(name = "CD_CIDADE")
  private Long id;

  @Column(name = "NM_CIDADE")
  private String nome;

  @Column(name = "CD_UF")
  private String uf;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  @Override
  public String getLabelForSelectItem() {
    return nome;
  }
}
