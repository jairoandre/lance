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

	public String getColor() {
		switch (this) {
		case N:
			return "#ff7700";
		case L:
			return "#002C57";
		case V:
			return "#005700";
		case P:
			return "#A30000";
		case F:
			return "#005700";
		case S:
			return "#0088ff";
		default:
			return "#000000";
		}
	}

}
