package br.com.vah.lance.entity.dbamv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "PLANO_CONTAS", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = PlanoConta.ALL, query = "SELECT s FROM PlanoConta s"),
		@NamedQuery(name = PlanoConta.COUNT, query = "SELECT COUNT(s) FROM PlanoConta s") })
public class PlanoConta extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String ALL = "PlanoConta.populatedItems";
	public final static String COUNT = "PlanoConta.countTotal";

	@Id
	@Column(name = "CD_REDUZIDO")
	private Long id;

	@Column(name = "CD_CONTABIL")
	private String codigoContabil;

	@Column(name = "DS_CONTA")
	private String title;

	@Column(name = "CD_ITEM_RES")
	private Integer codigoItemResultado;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the codigoContabil
	 */
	public String getCodigoContabil() {
		return codigoContabil;
	}

	/**
	 * @param codigoContabil
	 *            the codigoContabil to set
	 */
	public void setCodigoContabil(String codigoContabil) {
		this.codigoContabil = codigoContabil;
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

	/**
	 * @return the resultItemCode
	 */
	public Integer getCodigoItemResultado() {
		return codigoItemResultado;
	}

	/**
	 * @param resultItemCode
	 *            the resultItemCode to set
	 */
	public void setCodigoItemResultado(Integer resultItemCode) {
		this.codigoItemResultado = resultItemCode;
	}

}
