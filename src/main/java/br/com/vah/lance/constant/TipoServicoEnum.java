package br.com.vah.lance.constant;

public enum TipoServicoEnum {
  T("Tabelado"),
  V("Venda"),
  C("Cobrança"),
  E("Cond. Energia Privativa"),
  G("Cond. Gás Privativo"),
  CI("Cond. Individual"),
  CR("Cond. Rateio"),
  CP("Cond. Rateio Parcial"),
  CTP("Cond. Peso"),
  CTR("Cond. Tx. Refri."),
  CRE("Cond. Energia Comum");

  public static final TipoServicoEnum TABELADO = T;
  public static final TipoServicoEnum VENDA = V;
  public static final TipoServicoEnum COBRANCA = C;
  public static final TipoServicoEnum ENERGIA_PRIVADA = E;
  public static final TipoServicoEnum GAS_PRIVADO = G;
  public static final TipoServicoEnum CONDOMINIO_INDIVIDUAL = T;
  public static final TipoServicoEnum CONDOMINIO_RATEIO = CR;
  public static final TipoServicoEnum CONDOMINIO_RATEIO_PARCIAL = CP;
  public static final TipoServicoEnum COLETA_INFECTANTE = CTP;
  public static final TipoServicoEnum TAXA_REFRIGERACAO = CTR;
  public static final TipoServicoEnum ENERGIA_COMUM = CRE;

  private String label;

  TipoServicoEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public String getCompALabel() {
    switch (this) {
      case CTP:
        return "Peso (Kg)";
      case V:
        return "Valor de venda";
      case T:
      case CTR:
      case E:
      case C:
      case CI:
        return "Valor";
      case CP:
      case CR:
      case CRE:
        return "Valor rateado";
      default:
        return "N/A";
    }
  }

  public Boolean getRenderCompB() {
    switch (this) {
      case T:
        return false;
      case V:
        return true;
      case E:
        return false;
      case C:
      case CR:
      case CP:
      case CI:
      case CRE:
      case CTR:
      case CTP:
        return false;
      default:
        return true;
    }
  }

  public String getCompBLabel() {
    switch (this) {
      case T:
        return "N/A";
      case V:
        return "% Comissão";
      case E:
        return "Leitura atual";
      case C:
        return "N/A";
      default:
        return "N/A";
    }
  }

}
