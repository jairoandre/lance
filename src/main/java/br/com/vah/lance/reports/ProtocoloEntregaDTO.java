package br.com.vah.lance.reports;

import java.io.Serializable;

/**
 * Created by jairoportela on 30/08/2016.
 */
public class ProtocoloEntregaDTO implements Serializable {

  private Long id;

  private String cliente;

  private String setor;

  private String vencimento;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getVencimento() {
    return vencimento;
  }

  public void setVencimento(String vencimento) {
    this.vencimento = vencimento;
  }
}
