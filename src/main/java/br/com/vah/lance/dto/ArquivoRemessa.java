package br.com.vah.lance.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jairoportela on 12/07/2016.
 */
public class ArquivoRemessa {


  // 9(01)
  public static final String TIPO_REGISTRO = "0";
  // 9(01)
  public static final String OPERACAO = "1";
  // X(07)
  public static final String LITERAL_REMESSA = "REMESSA";
  // 9(02)
  public static final String CODIGO_SERVICO = "01";
  // X(15)
  public static final String LITERAL_COBRANCA = "COBRANCA       ";
  // 9(04)
  public static final String AGENCIA = "9363";
  // 9(02)
  public static final String ZEROS_0 = "00";
  // 9(05)
  public static final String CONTA_CORRENTE = "14019";
  // 9(01)
  public static final String DAC_0 = "6";
  // X(08)
  public static final String BRANCOS_0 = ArquivoUtils.rightSpace("", 8);
  // X(30)
  public static final String NOME_EMPRESA = ArquivoUtils.rightSpace("VITORIA APART HOSPITAL AS", 30);
  // 9(03)
  public static final String COD_BANCO = "341";
  // X(15)
  public static final String NOME_BANCO = ArquivoUtils.rightSpace("BANCO ITAU SA", 15);
  // 9(06)
  private String dataGeracao;
  // X(294)
  public static final String BRANCOS_1 = ArquivoUtils.rightSpace("", 294);
  // 9(06)
  public static final String NUMERO_SEQUENCIAL = "000001";

  private List<Detalhe> detalhes = new ArrayList<>();

  public ArquivoRemessa(String dataGeracao) {
    this.dataGeracao = dataGeracao;
    for (int i = 0 ; i < 30 ; i++) {
      detalhes.add(new Detalhe());
    }
  }

  public String header() {
    return TIPO_REGISTRO +
        OPERACAO +
        LITERAL_REMESSA +
        CODIGO_SERVICO +
        LITERAL_COBRANCA +
        AGENCIA +
        ZEROS_0 +
        CONTA_CORRENTE +
        DAC_0 +
        BRANCOS_0 +
        NOME_EMPRESA +
        COD_BANCO +
        NOME_BANCO +
        dataGeracao +
        BRANCOS_1 +
        NUMERO_SEQUENCIAL +
        "\n";
  }



  public String print() {
    StringBuilder builder = new StringBuilder();
    builder.append(header());
    for (Detalhe d : detalhes) {
      builder.append(d.print());
    }
    builder.append(footer());
    return builder.toString();
  }

  public String footer() {
    return
        // 9(01)
        "9" +
            // X(393)
            ArquivoUtils.rightSpace("", 393) +
            // 9(06)
            ArquivoUtils.leftZeros("12", 6);
  }


}
