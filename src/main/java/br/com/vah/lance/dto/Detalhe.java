package br.com.vah.lance.dto;

/**
 * Created by jairoportela on 19/07/2016.
 */
public class Detalhe {

  // 9(01)
  private String tipoRegistro = "1";
  // 9(02)
  private String codigoInscricaoEmpresa = "00";
  // 9(14)
  private String numeroInscricaoEmpresa = ArquivoUtils.leftZeros("", 14);
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
  private String codigoCarteira = "1";
  // 9(02) : 01 - Remessa ; 09 - Protestar ????
  private String codigoOcorrencia = "01";
  // X(10) - Nota Fiscal - Sequencial Lance?
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
  private String instrucao01 = "09";
  // X(02)
  private String instrucao02 = "02";
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
  private String prazo = "00";
  // X(01)
  private String spaces04 = " ";
  // 9(06)
  private String sequencial = ArquivoUtils.leftZeros("", 6);

  public String print() {
    return tipoRegistro
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
        + "\n";
  }


}
