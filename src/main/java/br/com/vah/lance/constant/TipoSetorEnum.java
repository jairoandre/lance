package br.com.vah.lance.constant;

public enum TipoSetorEnum {
  VAH("VAH"),
  CONSULTORIOS("Consultórios"),
  TERCEIROS("Terceiros"),
  SHOPPING("Shopping");

  private String label;

  TipoSetorEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

}
