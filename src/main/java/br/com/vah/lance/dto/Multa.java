package br.com.vah.lance.dto;

import br.com.vah.lance.entity.usrdbvah.Cobranca;

/**
 * Created by jairoportela on 26/07/2016.
 */
public class Multa {
  private String tipoRegistro = "2";
  private String codigoMulta = "2";
  private String dataMulta;
  private String multa = "0000000000200";
  private String spaces = ArquivoUtils.rightSpace("", 371);
  private String sequencial;

  public Multa(Cobranca cobranca, Integer sequencial) {
    this.dataMulta = ArquivoUtils.formatDate(cobranca.getVencimento(), "ddMMyyyy");
    this.sequencial = ArquivoUtils.leftZeros(sequencial, 6);
  }

  public String print() {
    return tipoRegistro
        + codigoMulta
        + dataMulta
        + multa
        + spaces
        + sequencial
        + "\r\n";
  }
}
