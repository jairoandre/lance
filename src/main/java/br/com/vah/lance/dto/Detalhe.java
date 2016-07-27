package br.com.vah.lance.dto;

import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.usrdbvah.Cobranca;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jairoportela on 19/07/2016.
 */
public class Detalhe {

  // 9(01)
  private String tipoRegistro = "1";
  // 9(02)
  private String codigoInscricaoEmpresa = "02";
  // 9(14)
  private String numeroInscricaoEmpresa = ArquivoUtils.leftZeros("2209094000139", 14);
  // 9(04)
  private String agenciaConta = "9363";
  // 9(02)
  private String zeros01 = "00";
  // 9(05)
  private String contaCorrente = "14019";
  // 9(01)
  private String dac01 = "6";
  // X(04)
  private String spaces01 = ArquivoUtils.rightSpace("", 4);
  // 9(04)
  private String alegacao = ArquivoUtils.leftZeros("", 4);
  // X(25)
  private String usoEmpresa = ArquivoUtils.rightSpace("", 25);
  // 9(08) - Número sequencial Lance
  private String nossoNumero = ArquivoUtils.leftZeros("", 8);
  // 9(08)V9(05)
  private String moedaVariavel = ArquivoUtils.leftZeros("", 13);
  // 9(03) - Direta Simples Sem Emissão
  private String carteira = "109";
  // X(21) - Uso do Banco
  private String identificacaoOperacao = ArquivoUtils.rightSpace("", 21);
  // X(01)
  private String codigoCarteira = "I";
  // 9(02) : 01 - Remessa ; 09 - Protestar ????
  private String codigoOcorrencia = "01";
  // X(10) - Nota Fiscal
  private String numeroDocumento = ArquivoUtils.rightSpace("", 10);
  // 9(06)
  private String vencimento = ArquivoUtils.leftZeros("", 6);
  // 9(11)V9(2)
  private String valor = ArquivoUtils.leftZeros("", 13);
  // 9(03)
  private String codigoBanco = "341";
  // 9(05)
  private String agenciaCobradora = ArquivoUtils.leftZeros("", 5);
  // 9(01) : 01 - Duplicata Mercantil
  private String especie = "01";
  // X(01) : A - Aceite ou N - Não aceite
  private String aceite = "A";
  // 9(06)
  private String dataEmissao = ArquivoUtils.leftZeros("", 6);
  // X(02) : 09 - Protestar ; 02 - Devolver 05 dias ; 03 - Devolver 30 dias ; 05 - Conforme instruções ; 06 - 10 dias ; 07 - 15 dias
  private String instrucao01 = "35";
  // X(02)
  private String instrucao02 = "09";
  // 9(11)V9(02)
  private String moraDiaria = ArquivoUtils.leftZeros("", 13);
  // 9(06)
  private String descontoAte = ArquivoUtils.leftZeros("", 6);
  // 9(11)V9(02)
  private String valorDesconto = ArquivoUtils.leftZeros("", 13);
  // 9(11)V9(02)
  private String valorIOF = ArquivoUtils.leftZeros("", 13);
  // 9(11)V9(02)
  private String abatimento = ArquivoUtils.leftZeros("", 13);
  // 9(02) - 01 - CPF ; 02 - CNPJ
  private String codigoInscricaoPagador = "02";
  // 9(14)
  private String numeroInscricaoPagador = ArquivoUtils.leftZeros("", 14);
  // X(30)
  private String nomePagador = ArquivoUtils.rightSpace("", 30);
  // X(10)
  private String spaces02 = ArquivoUtils.rightSpace("", 10);
  // X(40)
  private String logradouro = ArquivoUtils.rightSpace("", 40);
  // X(12)
  private String bairro = ArquivoUtils.rightSpace("", 12);
  // 9(08)
  private String cep = ArquivoUtils.leftZeros("", 8);
  // X(15)
  private String cidade = ArquivoUtils.rightSpace("", 15);
  // X(02)
  private String uf = ArquivoUtils.rightSpace("", 2);
  // X(30)
  private String sacadorAvalista = ArquivoUtils.rightSpace("", 30);
  // X(04)
  private String spaces03 = ArquivoUtils.rightSpace("", 4);
  // 9(06)
  private String dataMora = ArquivoUtils.leftZeros("", 6);
  // 9(02)
  private String prazo = "05";
  // X(01)
  private String spaces04 = " ";
  // 9(06)
  private String sequencial = ArquivoUtils.leftZeros("", 6);

  private Multa multa;

  public Detalhe(Cobranca cobranca, Integer sequencial) {
    Fornecedor cliente = cobranca.getCliente();
    if (cobranca.getId() == null) {
      usoEmpresa = ArquivoUtils.rightSpace(sequencial, 25);
      nossoNumero = ArquivoUtils.leftZeros(sequencial, 8);
    } else {
      usoEmpresa = ArquivoUtils.rightSpace(cobranca.getId(), 25);
      nossoNumero = ArquivoUtils.leftZeros(cobranca.getId(), 8);
    }
    numeroDocumento = ArquivoUtils.rightSpace(cobranca.getDocumento(), 10);
    vencimento = ArquivoUtils.formatDate(cobranca.getVencimento(), "ddMMyy");
    valor = ArquivoUtils.formatNumber(cobranca.getValor(), 13);
    dataEmissao = ArquivoUtils.formatDate(new Date(), "ddMMyy");
    moraDiaria = ArquivoUtils.leftZeros("1", 13);
    if (cliente.getCgcCpf().length() > 11) {
      codigoInscricaoPagador = "02";
    } else {
      codigoInscricaoPagador = "01";
    }
    numeroInscricaoPagador = ArquivoUtils.leftZeros(cliente.getCgcCpf(), 14);
    nomePagador = ArquivoUtils.rightSpace(cliente.getTitle(), 30);
    logradouro = ArquivoUtils.rightSpace(cliente.getEndereco(), 40);
    bairro = ArquivoUtils.rightSpace(cliente.getBairro(), 12);
    cep = ArquivoUtils.rightSpace(cliente.getCep(), 8);
    cidade = ArquivoUtils.rightSpace(cliente.getCidade().getNome(), 15);
    uf = ArquivoUtils.rightSpace(cliente.getCidade().getUf(), 2);
    this.sequencial = ArquivoUtils.leftZeros(sequencial, 6);

  }

  public String print() {
    String output = tipoRegistro
        + codigoInscricaoEmpresa
        + numeroInscricaoEmpresa
        + agenciaConta
        + zeros01
        + contaCorrente
        + dac01
        + spaces01
        + alegacao
        + usoEmpresa
        + nossoNumero
        + moedaVariavel
        + carteira
        + identificacaoOperacao
        + codigoCarteira
        + codigoOcorrencia
        + numeroDocumento
        + vencimento
        + valor
        + codigoBanco
        + agenciaCobradora
        + especie
        + aceite
        + dataEmissao
        + instrucao01
        + instrucao02
        + moraDiaria
        + descontoAte
        + valorDesconto
        + valorIOF
        + abatimento
        + codigoInscricaoPagador
        + numeroInscricaoPagador
        + nomePagador
        + spaces02
        + logradouro
        + bairro
        + cep
        + cidade
        + uf
        + sacadorAvalista
        + spaces03
        + dataMora
        + prazo
        + spaces04
        + sequencial
        + "\r\n";

    if (multa != null) {
      output += multa.print();
    }

    return output;
  }

  public String getTipoRegistro() {
    return tipoRegistro;
  }

  public void setTipoRegistro(String tipoRegistro) {
    this.tipoRegistro = tipoRegistro;
  }

  public String getCodigoInscricaoEmpresa() {
    return codigoInscricaoEmpresa;
  }

  public void setCodigoInscricaoEmpresa(String codigoInscricaoEmpresa) {
    this.codigoInscricaoEmpresa = codigoInscricaoEmpresa;
  }

  public String getNumeroInscricaoEmpresa() {
    return numeroInscricaoEmpresa;
  }

  public void setNumeroInscricaoEmpresa(String numeroInscricaoEmpresa) {
    this.numeroInscricaoEmpresa = numeroInscricaoEmpresa;
  }

  public String getAgenciaConta() {
    return agenciaConta;
  }

  public void setAgenciaConta(String agenciaConta) {
    this.agenciaConta = agenciaConta;
  }

  public String getContaCorrente() {
    return contaCorrente;
  }

  public void setContaCorrente(String contaCorrente) {
    this.contaCorrente = contaCorrente;
  }

  public String getDac01() {
    return dac01;
  }

  public void setDac01(String dac01) {
    this.dac01 = dac01;
  }

  public String getAlegacao() {
    return alegacao;
  }

  public void setAlegacao(String alegacao) {
    this.alegacao = alegacao;
  }

  public String getUsoEmpresa() {
    return usoEmpresa;
  }

  public void setUsoEmpresa(String usoEmpresa) {
    this.usoEmpresa = usoEmpresa;
  }

  public String getNossoNumero() {
    return nossoNumero;
  }

  public void setNossoNumero(String nossoNumero) {
    this.nossoNumero = nossoNumero;
  }

  public String getMoedaVariavel() {
    return moedaVariavel;
  }

  public void setMoedaVariavel(String moedaVariavel) {
    this.moedaVariavel = moedaVariavel;
  }

  public String getCarteira() {
    return carteira;
  }

  public void setCarteira(String carteira) {
    this.carteira = carteira;
  }

  public String getIdentificacaoOperacao() {
    return identificacaoOperacao;
  }

  public void setIdentificacaoOperacao(String identificacaoOperacao) {
    this.identificacaoOperacao = identificacaoOperacao;
  }

  public String getCodigoCarteira() {
    return codigoCarteira;
  }

  public void setCodigoCarteira(String codigoCarteira) {
    this.codigoCarteira = codigoCarteira;
  }

  public String getCodigoOcorrencia() {
    return codigoOcorrencia;
  }

  public void setCodigoOcorrencia(String codigoOcorrencia) {
    this.codigoOcorrencia = codigoOcorrencia;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getVencimento() {
    return vencimento;
  }

  public void setVencimento(String vencimento) {
    this.vencimento = vencimento;
  }

  public String getValor() {
    return valor;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public String getCodigoBanco() {
    return codigoBanco;
  }

  public void setCodigoBanco(String codigoBanco) {
    this.codigoBanco = codigoBanco;
  }

  public String getAgenciaCobradora() {
    return agenciaCobradora;
  }

  public void setAgenciaCobradora(String agenciaCobradora) {
    this.agenciaCobradora = agenciaCobradora;
  }

  public String getEspecie() {
    return especie;
  }

  public void setEspecie(String especie) {
    this.especie = especie;
  }

  public String getAceite() {
    return aceite;
  }

  public void setAceite(String aceite) {
    this.aceite = aceite;
  }

  public String getDataEmissao() {
    return dataEmissao;
  }

  public void setDataEmissao(String dataEmissao) {
    this.dataEmissao = dataEmissao;
  }

  public String getInstrucao01() {
    return instrucao01;
  }

  public void setInstrucao01(String instrucao01) {
    this.instrucao01 = instrucao01;
  }

  public String getInstrucao02() {
    return instrucao02;
  }

  public void setInstrucao02(String instrucao02) {
    this.instrucao02 = instrucao02;
  }

  public String getMoraDiaria() {
    return moraDiaria;
  }

  public void setMoraDiaria(String moraDiaria) {
    this.moraDiaria = moraDiaria;
  }

  public String getDescontoAte() {
    return descontoAte;
  }

  public void setDescontoAte(String descontoAte) {
    this.descontoAte = descontoAte;
  }

  public String getValorDesconto() {
    return valorDesconto;
  }

  public void setValorDesconto(String valorDesconto) {
    this.valorDesconto = valorDesconto;
  }

  public String getValorIOF() {
    return valorIOF;
  }

  public void setValorIOF(String valorIOF) {
    this.valorIOF = valorIOF;
  }

  public String getAbatimento() {
    return abatimento;
  }

  public void setAbatimento(String abatimento) {
    this.abatimento = abatimento;
  }

  public String getCodigoInscricaoPagador() {
    return codigoInscricaoPagador;
  }

  public void setCodigoInscricaoPagador(String codigoInscricaoPagador) {
    this.codigoInscricaoPagador = codigoInscricaoPagador;
  }

  public String getNumeroInscricaoPagador() {
    return numeroInscricaoPagador;
  }

  public void setNumeroInscricaoPagador(String numeroInscricaoPagador) {
    this.numeroInscricaoPagador = numeroInscricaoPagador;
  }

  public String getNomePagador() {
    return nomePagador;
  }

  public void setNomePagador(String nomePagador) {
    this.nomePagador = nomePagador;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getSacadorAvalista() {
    return sacadorAvalista;
  }

  public void setSacadorAvalista(String sacadorAvalista) {
    this.sacadorAvalista = sacadorAvalista;
  }

  public String getDataMora() {
    return dataMora;
  }

  public void setDataMora(String dataMora) {
    this.dataMora = dataMora;
  }

  public String getPrazo() {
    return prazo;
  }

  public void setPrazo(String prazo) {
    this.prazo = prazo;
  }

  public String getSequencial() {
    return sequencial;
  }

  public void setSequencial(String sequencial) {
    this.sequencial = sequencial;
  }

  public Multa getMulta() {
    return multa;
  }

  public void setMulta(Multa multa) {
    this.multa = multa;
  }
}
