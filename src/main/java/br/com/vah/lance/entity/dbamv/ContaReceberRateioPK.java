package br.com.vah.lance.entity.dbamv;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by jairoportela on 03/05/2016.
 */
@Embeddable
public class ContaReceberRateioPK implements Serializable {

  @ManyToOne
  @JoinColumn(name = "CD_CON_REC")
  private ContaReceber contaReceber;

  @Column(name = "NR_LINHA")
  private Integer numeroLinha = 1;

  public ContaReceberRateioPK() {
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ContaReceberRateioPK) {
      ContaReceberRateioPK other = (ContaReceberRateioPK) obj;

      if (!contaReceber.equals(other.getContaReceber())) {
        return false;
      }

      if (!numeroLinha.equals(other.getNumeroLinha())) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int contaReceberHC = contaReceber == null ? 0 : contaReceber.hashCode();
    int numeroLinhaHC = numeroLinha == null ? 0 : numeroLinha.hashCode();

    return contaReceberHC + numeroLinhaHC;
  }

  public ContaReceber getContaReceber() {
    return contaReceber;
  }

  public void setContaReceber(ContaReceber contaReceber) {
    this.contaReceber = contaReceber;
  }

  public Integer getNumeroLinha() {
    return numeroLinha;
  }

  public void setNumeroLinha(Integer numeroLinha) {
    this.numeroLinha = numeroLinha;
  }
}
