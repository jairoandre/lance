package br.com.vah.lance.dto;

import java.util.Map;

/**
 * Created by jairoportela on 12/07/2016.
 */
public class ArquivoRemessa {

  // Formatação arquivo CNAB 240

  public static final String ZERO = "0";
  public static final String SPACE = " ";
  public static final String BANCO = "341";
  public static final String CODIGO_LOTE = "CODIGO_LOTE";
  public static final String BANCO_ITAU = "BANCO ITAU";
  public static final String LAYOUT = "040";
  public static final String TOTAL_REGISTROS = "TOTAL_REGISTROS";
  public static final String TOTAL_DEBITOS = "TOTAL_DEBITOS";
  public static final String TOTAL_MOEDAS = "TOTAL_MOEDAS";
  public static final String TOTAL_LOTES = "TOTAL_LOTES";


  private String brancos(int size) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < size; i++) {
      s.append(SPACE);
    }
    return s.toString();
  }

  private String zeros(int size) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < size; i++) {
      s.append(ZERO);
    }
    return s.toString();
  }

  private String leftZero(String value, int size) {
    return symbol(value, ZERO, size, false);
  }

  private String rightSpace(String value, int size) {
    return symbol(value, SPACE, size, true);
  }

  private String symbol(String value, String symbol, int size, boolean right) {
    StringBuilder buffer = new StringBuilder();
    if (right) {
      buffer.append(value);
    }
    for (int i = 0; i < size - value.length(); i++) {
      buffer.append(symbol);
    }
    if (!right) {
      buffer.append(value);
    }
    return buffer.toString();
  }


  public String headerArquivo(RegistroHead h) {
    StringBuilder s = new StringBuilder();

    s.append(BANCO);                                  // 9(03)
    s.append(zeros(4));                               // 9(04)
    s.append("0");                                    // 9(01)
    s.append(brancos(9));                             // X(09)
    s.append("2");                                    // 9(01)
    s.append("02209094000139");                       // 9(14)
    s.append(rightSpace(h.getConvenio(), 13));        // X(13)
    s.append(brancos(7));                             // X(07)
    s.append("0");                                    // 9(01)
    s.append(leftZero(h.getAgencia(), 4));            // 9(04)
    s.append(SPACE);                                  // X(01)
    s.append(zeros(7));                               // 9(07)
    s.append(leftZero(h.getConta(), 5));              // 9(05)
    s.append(SPACE);                                  // X(01)
    s.append(h.getDac());                             // 9(01)
    s.append(rightSpace(h.getNomeEmpresa(), 30));     // X(30)
    s.append(rightSpace(BANCO_ITAU, 30));             // X(30)
    s.append(brancos(10));                            // X(10)
    s.append("1");                                    // 9(01)
    s.append(h.getDataGeracao());                     // 9(08)
    s.append(h.getHoraGeracao());                     // 9(06)
    s.append(leftZero(h.getSequencial(), 6));         // 9(06)
    s.append(LAYOUT);                                 // 9(03)
    s.append(zeros(5));                               // 9(05)
    s.append(brancos(20));                            // X(20)
    s.append(brancos(49));                            // X(49)

    return s.toString();

  }

  public String headerLote(LoteHead p) {
    StringBuilder s = new StringBuilder();

    s.append(BANCO);                                  // 9(03)
    s.append(leftZero(p.getCodigoLote(), 4));         // 9(04)
    s.append("1");                                    // 9(01)
    s.append("D");                                    // X(01)
    s.append("05");                                   // 9(02)
    s.append("50");                                   // 9(02)
    s.append("030");                                  // X(03)
    s.append(SPACE);                                  // X(01)
    s.append(p.getCpf() ? 1 : 2);                     // 9(01)
    s.append(leftZero(p.getNumeroInscricao(), 14));   // 9(14)
    s.append(rightSpace(p.getConvenio(), 13));        // X(13)
    s.append(brancos(7));                             // X(07)
    s.append("0");                                    // 9(01)
    s.append(leftZero(p.getAgencia(), 4));            // 9(04)
    s.append(SPACE);                                  // X(01)
    s.append(zeros(7));                               // 9(07)
    s.append(leftZero(p.getConta(), 5));              // 9(05)
    s.append(SPACE);                                  // X(01)
    s.append(p.getDac());                             // 9(01)
    s.append(rightSpace(p.getNomeEmpresa(), 30));     // X(30)
    s.append(brancos(40));                            // X(40)
    s.append(rightSpace(p.getEndereco(), 30));        // X(30)
    s.append(leftZero(p.getNumero(), 5));             // 9(05)
    s.append(rightSpace(p.getComplemento(), 15));     // X(15)
    s.append(rightSpace(p.getCidade(), 20));          // X(20)
    s.append(leftZero(p.getCep(), 8));                // 9(08)
    s.append(rightSpace(p.getEstado(), 2));           // X(02)
    s.append(brancos(8));                             // X(08)
    s.append(brancos(10));                            // X(10)

    return s.toString();
  }

  public String detalheLote(LoteDetalhe p) {
    StringBuilder s = new StringBuilder();

    s.append(BANCO);                                  // 9(03)
    s.append(leftZero(p.getCodigoLote(), 4));         // 9(04)
    s.append("3");                                    // 9(01)
    s.append(leftZero(p.getSequencial(), 5));         // 9(05)
    s.append("A");                                    // X(01)
    s.append(leftZero(p.getCodigoInstrucao(), 3));    // 9(03)
    s.append("000");                                  // 9(03)
    s.append("341");                                  // 9(03)
    s.append("0");                                    // 9(01)
    s.append(leftZero(p.getAgencia(), 4));            // 9(04)
    s.append(SPACE);                                  // X(01)
    s.append(zeros(7));                               // 9(07)
    s.append(leftZero(p.getConta(), 5));              // 9(05)
    s.append(SPACE);                                  // X(01)
    s.append(leftZero(p.getDac(), 1));                // 9(01)
    s.append(rightSpace(p.getNomeDebitado(), 30));    // X(30)
    s.append(rightSpace(p.getMeuNumero(), 15));       // X(15)
    s.append(brancos(5));                             // X(05)
    s.append(p.getDataAgendada());                    // 9(08)
    s.append("REA");                                  // X(03)
    s.append(leftZero(p.getQuantidadeMoeda(), 15));   // 9(10)V9(05)
    s.append(leftZero(p.getValorAgendado(), 15));     // 9(13)V9(02)
    s.append(brancos(20));                            // X(20)
    s.append(brancos(8));                             // 9(08)
    s.append(brancos(15));                            // 9(15)
    s.append(leftZero(p.getTipoMora(), 2));           // 9(02)
    s.append(leftZero(p.getValorMora(), 17));         // 9(17)
    s.append(rightSpace(p.getComplemento(), 16));     // X(16)
    s.append(brancos(4));                             // X(04)
    s.append(leftZero(p.getInscricaoDebitado(), 14)); // 9(14)
    s.append(brancos(10));                            // X(10)

    return s.toString();
  }

  public String trailerLote(Map<String, String> p) {
    StringBuilder s = new StringBuilder();

    s.append("341");                                  // 9(03)
    s.append(leftZero(p.get(CODIGO_LOTE), 4));        // 9(04)
    s.append("5");                                    // 9(01)
    s.append(brancos(9));                             // X(09)
    s.append(leftZero(p.get(TOTAL_REGISTROS), 6));    // 9(06)
    s.append(leftZero(p.get(TOTAL_DEBITOS), 18));     // 9(16)V9(02)
    s.append(leftZero(p.get(TOTAL_MOEDAS), 18));      // 9(13)V9(05)
    s.append(brancos(171));                           // X(171)
    s.append(brancos(10));                            // X(10)

    return s.toString();
  }

  public String trailerArquivo(Map<String, String> p) {
    StringBuilder s = new StringBuilder();

    s.append("341");                                  // 9(03)
    s.append("9999");                                 // 9(04)
    s.append("9");                                    // 9(01)
    s.append(brancos(9));                             // X(09)
    s.append(leftZero(p.get(TOTAL_LOTES), 6));        // 9(06)
    s.append(leftZero(p.get(TOTAL_REGISTROS), 6));    // 9(06)
    s.append(brancos(211));                           // X(211)

    return s.toString();
  }
}
