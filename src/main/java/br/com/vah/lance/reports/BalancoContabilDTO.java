package br.com.vah.lance.reports;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jairoportela on 14/07/2016.
 */
public class BalancoContabilDTO implements Serializable {

  private String servico;

  private String reduzido;

  private String contaCusto;

  private String conta;

  private String nomeConta;

  private String reduzidoCompart;

  private String contaCompart;

  private String nomeContaCompart;

  private String setor;

  private String cliente;

  private Date competencia;

  private BigDecimal valor = BigDecimal.ZERO;

  public String getServico() {
    return servico;
  }

  public void setServico(String servico) {
    this.servico = servico;
  }

  public String getReduzido() {
    return reduzido;
  }

  public void setReduzido(String reduzido) {
    this.reduzido = reduzido;
  }

  public String getContaCusto() {
    return contaCusto;
  }

  public void setContaCusto(String contaCusto) {
    this.contaCusto = contaCusto;
  }

  public String getConta() {
    return conta;
  }

  public void setConta(String conta) {
    this.conta = conta;
  }

  public String getNomeConta() {
    return nomeConta;
  }

  public void setNomeConta(String nomeConta) {
    this.nomeConta = nomeConta;
  }

  public String getReduzidoCompart() {
    return reduzidoCompart;
  }

  public void setReduzidoCompart(String reduzidoCompart) {
    this.reduzidoCompart = reduzidoCompart;
  }

  public String getContaCompart() {
    return contaCompart;
  }

  public void setContaCompart(String contaCompart) {
    this.contaCompart = contaCompart;
  }

  public String getNomeContaCompart() {
    return nomeContaCompart;
  }

  public void setNomeContaCompart(String nomeContaCompart) {
    this.nomeContaCompart = nomeContaCompart;
  }

  public String getSetor() {
    return setor;
  }

  public void setSetor(String setor) {
    this.setor = setor;
  }

  public String getCliente() {
    return cliente;
  }

  public void setCliente(String cliente) {
    this.cliente = cliente;
  }

  public Date getCompetencia() {
    return competencia;
  }

  public void setCompetencia(Date competencia) {
    this.competencia = competencia;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BalancoContabilDTO) {
      BalancoContabilDTO dto = (BalancoContabilDTO) obj;
      if (this.setor != null && dto.setor != null) {
        if (this.setor.equals(dto.setor)) {
          if (this.contaCusto != null && dto.contaCusto != null) {
            return this.contaCusto.equals(dto.contaCusto);
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    }
    return super.equals(obj);
  }
}
