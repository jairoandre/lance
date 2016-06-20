package br.com.vah.lance.entity.dbamv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "ITEM_RES", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = ItemResultado.ALL, query = "SELECT s FROM ItemResultado s"),
		@NamedQuery(name = ItemResultado.COUNT, query = "SELECT COUNT(s) FROM ItemResultado s") })
public class ItemResultado extends BaseEntity {

	public final static String ALL = "ItemResultado.populatedItems";
	public final static String COUNT = "ItemResultado.countTotal";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CD_ITEM_RES")
	private Long id;

	@Column(name = "DS_ITEM_RES")
	private String title;

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getLabelForSelectItem() {
		if (id == null) {
			return null;
		} else {
			return String.format("%d - %s", getId(), getTitle());
		}
	}

}
