package br.com.vah.lance.dto;

/**
 * Created by jairoportela on 13/07/2016.
 */
public class RegistroHead {

  private Boolean cpf;
  private String numeroInscricao;
  private String codigoLote;
  private String convenio;
  private String agencia;
  private String dac;
  private String conta;
  private String nomeEmpresa;
  private String dataGeracao;
  private String horaGeracao;
  private String sequencial;

  public Boolean getCpf() {
    return cpf;
  }

  public void setCpf(Boolean cpf) {
    this.cpf = cpf;
  }

  public String getNumeroInscricao() {
    return numeroInscricao;
  }

  public void setNumeroInscricao(String numeroInscricao) {
    this.numeroInscricao = numeroInscricao;
  }

  public String getCodigoLote() {
    return codigoLote;
  }

  public void setCodigoLote(String codigoLote) {
    this.codigoLote = codigoLote;
  }

  public String getConvenio() {
    return convenio;
  }

  public void setConvenio(String convenio) {
    this.convenio = convenio;
  }

  public String getAgencia() {
    return agencia;
  }

  public void setAgencia(String agencia) {
    this.agencia = agencia;
  }

  public String getDac() {
    return dac;
  }

  public void setDac(String dac) {
    this.dac = dac;
  }

  public String getConta() {
    return conta;
  }

  public void setConta(String conta) {
    this.conta = conta;
  }

  public String getNomeEmpresa() {
    return nomeEmpresa;
  }

  public void setNomeEmpresa(String nomeEmpresa) {
    this.nomeEmpresa = nomeEmpresa;
  }

  public String getDataGeracao() {
    return dataGeracao;
  }

  public void setDataGeracao(String dataGeracao) {
    this.dataGeracao = dataGeracao;
  }

  public String getHoraGeracao() {
    return horaGeracao;
  }

  public void setHoraGeracao(String horaGeracao) {
    this.horaGeracao = horaGeracao;
  }

  public String getSequencial() {
    return sequencial;
  }

  public void setSequencial(String sequencial) {
    this.sequencial = sequencial;
  }
}
