package br.com.vah.lance.dto;

import br.com.vah.lance.entity.usrdbvah.Cobranca;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Jairoportela on 13/10/2016.
 */
public class CobrancaDTO implements Serializable {
  private Cobranca cobranca;
  private Date data;
  private BigDecimal multa;

  public CobrancaDTO(Cobranca cobranca, Date data, BigDecimal multa) {
    this.cobranca = cobranca;
    this.data = data;
    this.multa = multa;
  }

  public Cobranca getCobranca() {
    return cobranca;
  }

  public void setCobranca(Cobranca cobranca) {
    this.cobranca = cobranca;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public BigDecimal getMulta() {
    return multa;
  }

  public void setMulta(BigDecimal multa) {
    this.multa = multa;
  }

  public String getRowKey() {
    return cobranca.getRowKey();
  }
}
