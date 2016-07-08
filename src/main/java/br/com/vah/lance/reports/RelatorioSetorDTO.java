package br.com.vah.lance.reports;

import java.io.Serializable;

/**
 * Created by jairoportela on 07/07/2016.
 */
public class RelatorioSetorDTO implements Serializable {

  private Long codigoSetor;

  private String nomeSetor;

  private Long codigoCliente;

  private String nomeCliente;

  private String tipo = "C";

  private String servicos;

  public Long getCodigoSetor() {
    return codigoSetor;
  }

  public void setCodigoSetor(Long codigoSetor) {
    this.codigoSetor = codigoSetor;
  }

  public String getNomeSetor() {
    return nomeSetor;
  }

  public void setNomeSetor(String nomeSetor) {
    this.nomeSetor = nomeSetor;
  }

  public Long getCodigoCliente() {
    return codigoCliente;
  }

  public void setCodigoCliente(Long codigoCliente) {
    this.codigoCliente = codigoCliente;
  }

  public String getNomeCliente() {
    return nomeCliente;
  }

  public void setNomeCliente(String nomeCliente) {
    this.nomeCliente = nomeCliente;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getServicos() {
    return servicos;
  }

  public void setServicos(String servicos) {
    this.servicos = servicos;
  }
}
