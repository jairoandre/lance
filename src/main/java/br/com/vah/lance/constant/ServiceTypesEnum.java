package br.com.vah.lance.constant;

public enum ServiceTypesEnum {
  T("Tabelado"),
  V("Venda"),
  C("Cobrança"),
  E("Condomínio - Indiv. Energia"),
  G("Condomínio - Indiv. Gás"),
  CTR("Condomínio - Taxado"),
  CI("Condomínio - Individual"),
  CR("Condomínio - Rateio"),
  CP("Condomínio - Rateio Parcial"),
  CRE("Condomínio - Energia Área Comum");

  private String label;

  ServiceTypesEnum(String label) {
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
        return "Valor de cobrança";
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
