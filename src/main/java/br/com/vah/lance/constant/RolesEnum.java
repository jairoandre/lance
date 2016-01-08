package br.com.vah.lance.constant;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum RolesEnum {
	ADMINISTRATOR("Administrador"), USER("Usuário"), MANAGER("Gestor"), SUPERVISOR("Controlador"), ACCOUNTANT(
			"Contabilidade");

	private String label;

	private RolesEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public static List<SelectItem> getSelectItems() {
		List<SelectItem> items = new ArrayList<>();
		items.add(new SelectItem(null, "Selecione..."));
		for (RolesEnum role : RolesEnum.values()) {
			items.add(new SelectItem(role, role.getLabel()));
		}
		return items;
	}
}
