package br.com.vah.lance.entity.mv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "TIP_DOC", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvDocumentType.ALL, query = "SELECT c FROM MvDocumentType c"),
		@NamedQuery(name = MvDocumentType.COUNT, query = "SELECT COUNT(c) FROM MvDocumentType c") })
public class MvDocumentType extends BaseEntity {

	public final static String ALL = "MvDocumentType.populatedItems";
	public final static String COUNT = "MvDocumentType.countTotal";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CD_TIP_DOC")
	private Long id;

	@Column(name = "DS_TIP_DOC")
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
