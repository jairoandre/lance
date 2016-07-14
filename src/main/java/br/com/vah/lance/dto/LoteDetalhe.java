package br.com.vah.lance.dto;

/**
 * Created by jairoportela on 13/07/2016.
 */
public class LoteDetalhe extends LoteHead {

  private String codigoInstrucao = "000";
  private String nomeDebitado;
  private String meuNumero;
  private String dataAgendada;
  private String tipoMoeda = "REA";
  private String quantidadeMoeda;
  private String valorAgendado;
  private String tipoMora; // 00 - Isento ; 01 - Juros Simples ; 03 - IDA (Importância por dia de atraso)
  private String valorMora;
  private String complementoRegistro;
  private String inscricaoDebitado;

  public String getCodigoInstrucao() {
    return codigoInstrucao;
  }

  public void setCodigoInstrucao(String codigoInstrucao) {
    this.codigoInstrucao = codigoInstrucao;
  }

  public String getNomeDebitado() {
    return nomeDebitado;
  }

  public void setNomeDebitado(String nomeDebitado) {
    this.nomeDebitado = nomeDebitado;
  }

  public String getMeuNumero() {
    return meuNumero;
  }

  public void setMeuNumero(String meuNumero) {
    this.meuNumero = meuNumero;
  }

  public String getDataAgendada() {
    return dataAgendada;
  }

  public void setDataAgendada(String dataAgendada) {
    this.dataAgendada = dataAgendada;
  }

  public String getTipoMoeda() {
    return tipoMoeda;
  }

  public void setTipoMoeda(String tipoMoeda) {
    this.tipoMoeda = tipoMoeda;
  }

  public String getQuantidadeMoeda() {
    return quantidadeMoeda;
  }

  public void setQuantidadeMoeda(String quantidadeMoeda) {
    this.quantidadeMoeda = quantidadeMoeda;
  }

  public String getValorAgendado() {
    return valorAgendado;
  }

  public void setValorAgendado(String valorAgendado) {
    this.valorAgendado = valorAgendado;
  }

  public String getTipoMora() {
    return tipoMora;
  }

  public void setTipoMora(String tipoMora) {
    this.tipoMora = tipoMora;
  }

  public String getValorMora() {
    return valorMora;
  }

  public void setValorMora(String valorMora) {
    this.valorMora = valorMora;
  }

  public String getComplementoRegistro() {
    return complementoRegistro;
  }

  public void setComplementoRegistro(String complementoRegistro) {
    this.complementoRegistro = complementoRegistro;
  }

  public String getInscricaoDebitado() {
    return inscricaoDebitado;
  }

  public void setInscricaoDebitado(String inscricaoDebitado) {
    this.inscricaoDebitado = inscricaoDebitado;
  }
}
