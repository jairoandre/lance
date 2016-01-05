package br.com.vah.lance.constant;

/**
 * Estados do lançamento.
 * 
 * @author jairoportela
 *
 */
public enum EntryStatusEnum {

	/**
	 * Não lançado
	 */
	N("Não lançado"),
	/**
	 * Lançado
	 */
	L("Lançado"),
	/**
	 * Validado
	 */
	V("Validado"),
	/**
	 * Pendente
	 */
	P("Pendente"),
	/**
	 * Corrigido
	 */
	F("Corrigido"),
	/**
	 * Transmitido
	 */
	S("Transmitido"),
	/**
	 * Excluído
	 */
	E("Excluído");

	private String label;

	private EntryStatusEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
