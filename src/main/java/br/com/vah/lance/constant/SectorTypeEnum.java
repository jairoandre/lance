package br.com.vah.lance.constant;

public enum SectorTypeEnum {
  VAH("VAH"),
  CONSULTORIOS("Consultórios"),
  TERCEIROS("Terceiros"),
  SHOPPING("Shopping");

  private String label;

  SectorTypeEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

}
