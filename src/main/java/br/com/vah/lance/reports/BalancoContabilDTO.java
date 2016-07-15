package br.com.vah.lance.reports;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jairoportela on 14/07/2016.
 */
public class BalancoContabilDTO implements Serializable {

  private String servico;

  private String cliente;

  private String setor;

  private Date vigencia;

  private String contaReduzido;

  private String contaContabil;

  private BigDecimal valor;

  public String getServico() {
    return servico;
  }

  public void setServico(String servico) {
    this.servico = servico;
  }

  public String getCliente() {
    return cliente;
  }

  public void setCliente(String cliente) {
    this.cliente = cliente;
  }

  public String getSetor() {
    return setor;
  }

  public void setSetor(String setor) {
    this.setor = setor;
  }

  public Date getVigencia() {
    return vigencia;
  }

  public void setVigencia(Date vigencia) {
    this.vigencia = vigencia;
  }

  public String getContaReduzido() {
    return contaReduzido;
  }

  public void setContaReduzido(String contaReduzido) {
    this.contaReduzido = contaReduzido;
  }

  public String getContaContabil() {
    return contaContabil;
  }

  public void setContaContabil(String contaContabil) {
    this.contaContabil = contaContabil;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }
}
