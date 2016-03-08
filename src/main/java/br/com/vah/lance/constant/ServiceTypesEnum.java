package br.com.vah.lance.constant;

public enum ServiceTypesEnum {
  T("Tabelado"), V("Venda"), E("Condomínio - Indiv. Energia"), CR("Condomínio - Rateio"), CRE("Condomínio - Energia Área Comum"), CP("Condomínio - Rateio Parcial"), CI("Condomínio - Individual"), G("Condomínio - Indiv. Gás"), C("Cobrança");

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
        return "Variável";
      case V:
        return "Valor de venda";
      case E:
        return "Leitura anterior";
      case C:
        return "Valor de cobrança";
      case CRE:
      case CR:
        return "Valor rateado";
      case CP:
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
        return true;
      case C:
      case CR:
      case CRE:
      case CP:
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
