package br.com.vah.lance.entity.mv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "ITEM_RES", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvResultItem.ALL, query = "SELECT s FROM MvResultItem s"),
		@NamedQuery(name = MvResultItem.COUNT, query = "SELECT COUNT(s) FROM MvResultItem s") })
public class MvResultItem extends BaseEntity {

	public final static String ALL = "MvResultItem.populatedItems";
	public final static String COUNT = "MvResultItem.countTotal";

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
		return title;
	}

}
