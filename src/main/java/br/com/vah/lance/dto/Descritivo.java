package br.com.vah.lance.dto;

import br.com.vah.lance.entity.usrdbvah.ItemCobranca;

/**
 * Created by jairoportela on 28/07/2016.
 */
public class Descritivo {

  // 9(01) - 0: rateado; 1: individual
  private String rateado;
  // 9(08)
  private String nossoNumero;
  // X(30)
  private String descricao;
  // 9(11)V9(02)
  private String valorTotal;
  // 9(11)V9(02)
  private String valorIndividual;

  public Descritivo(ItemCobranca item) {
  }



}
