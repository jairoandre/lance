package br.com.vah.lance.reports;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jairoportela on 06/07/2016.
 */
public class DescritivoCondominioDTO implements Serializable {

  private String nomeCliente;

  private String numeroDocumento;

  private String servico;

  private BigDecimal total;

  private BigDecimal rateio;

  private String nomeSetor;

  private String vencimento;

  public String getNomeCliente() {
    return nomeCliente;
  }

  public void setNomeCliente(String nomeCliente) {
    this.nomeCliente = nomeCliente;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getServico() {
    return servico;
  }

  public void setServico(String servico) {
    this.servico = servico;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getRateio() {
    return rateio;
  }

  public void setRateio(BigDecimal rateio) {
    this.rateio = rateio;
  }

  public String getNomeSetor() {
    return nomeSetor;
  }

  public void setNomeSetor(String nomeSetor) {
    this.nomeSetor = nomeSetor;
  }

  public String getVencimento() {
    return vencimento;
  }

  public void setVencimento(String vencimento) {
    this.vencimento = vencimento;
  }
}
