package br.com.vah.lance.entity.dbamv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "HISTORICO_PADRAO", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = HistoricoPadrao.ALL, query = "SELECT s FROM HistoricoPadrao s"),
		@NamedQuery(name = HistoricoPadrao.COUNT, query = "SELECT COUNT(s) FROM HistoricoPadrao s") })
public class HistoricoPadrao extends BaseEntity {

	public final static String ALL = "HistoricoPadrao.populatedItems";
	public final static String COUNT = "HistoricoPadrao.countTotal";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CD_HISTORICO_PADRAO")
	private Long id;

	@Column(name = "DS_HISTORICO_PADRAO")
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
		return getTitle();
	}

}
