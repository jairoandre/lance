package br.com.vah.lance.constant;

public enum ServiceTypesEnum {
	T("Tabelado"), V("Venda"), E("Energia/Gás"), C("Cobrança");

	private String label;

	private ServiceTypesEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
