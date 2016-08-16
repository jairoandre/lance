package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by jairoportela on 15/08/2016.
 */
@Entity
@Table(name = "REC_MOV_CON", schema = "DBAMV")
public class RecebimentoMovimentacao extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqRecMovCon", sequenceName = "SEQ_REC_MOV_CON", schema = "DBAMV", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRecMovCon")
  @Column(name = "CD_REC_MOV_CON", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_RECCON_REC")
  private Recebimento recebimento;

  @ManyToOne
  @JoinColumn(name = "CD_MOV_CONCOR")
  private Movimentacao movimentacao;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

  public Recebimento getRecebimento() {
    return recebimento;
  }

  public void setRecebimento(Recebimento recebimento) {
    this.recebimento = recebimento;
  }

  public Movimentacao getMovimentacao() {
    return movimentacao;
  }

  public void setMovimentacao(Movimentacao movimentacao) {
    this.movimentacao = movimentacao;
  }
}
