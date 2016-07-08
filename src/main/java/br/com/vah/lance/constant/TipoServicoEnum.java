package br.com.vah.lance.constant;

public enum TipoServicoEnum {
  T("Tabelado"),
  V("Venda"),
  C("Cobrança"),
  E("Cond. Energia Privativa"),
  G("Cond. Gás Privativo"),
  CTR("Cond. Taxado"),
  CI("Cond. Individual"),
  CR("Cond. Rateio"),
  CP("Cond. Rateio Parcial"),
  CRE("Cond. Energia Comum");

  private String label;

  TipoServicoEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public String getCompALabel() {
    switch (this) {
      case T:
        return "Valor";
      case CTR:
        return "Quantidade";
      case V:
        return "Valor de venda";
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
      case CRE:
      case CP:
      case CTR:
      case CI:
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
