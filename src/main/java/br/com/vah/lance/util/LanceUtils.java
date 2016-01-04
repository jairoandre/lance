package br.com.vah.lance.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.vah.lance.entity.BaseEntity;

public class LanceUtils {

	public static List<SelectItem> createSelectItem(List<BaseEntity> list, Boolean nullOption) {
		List<SelectItem> selectItems = new ArrayList<>();
		if (nullOption) {
			selectItems.add(new SelectItem(null, "Selecione..."));
		}
		for (BaseEntity entity : list) {
			selectItems.add(new SelectItem(entity.getId(), entity.getLabelForSelectItem()));
		}
		return selectItems;
	}

	public static List<SelectItem> splice(List<SelectItem> items, Object id) {
		List<SelectItem> newItems = new ArrayList<>();
		for (SelectItem item : items) {
			if (!id.equals(item.getValue())) {
				newItems.add(item);
			}
		}
		return newItems;
	}

}
