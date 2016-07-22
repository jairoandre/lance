package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 22/07/2016.
 */
@Entity
@Table(name = "TB_LANCA_ARQUIVO", schema = "USRDBVAH")
public class ArquivoRemessa extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqItemCobrancaGenerator", sequenceName = "SEQ_LANCA_ITEM_COBRANCA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqItemCobrancaGenerator")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_AUTOR")
  private User autor;

  @Column(name = "DT_CRIACAO")
  private Date criacao;

  @OneToMany(mappedBy = "arquivo")
  private List<Cobranca> cobrancas;

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Date getCriacao() {
    return criacao;
  }

  public void setCriacao(Date criacao) {
    this.criacao = criacao;
  }

  public User getAutor() {
    return autor;
  }

  public void setAutor(User autor) {
    this.autor = autor;
  }

  public List<Cobranca> getCobrancas() {
    return cobrancas;
  }

  public void setCobrancas(List<Cobranca> cobrancas) {
    this.cobrancas = cobrancas;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
