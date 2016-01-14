package br.com.vah.lance.entity.mv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "PLANO_CONTAS", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvAccountChart.ALL, query = "SELECT s FROM MvAccountChart s"),
		@NamedQuery(name = MvAccountChart.COUNT, query = "SELECT COUNT(s) FROM MvAccountChart s") })
public class MvAccountChart extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String ALL = "MvAccountChart.populatedItems";
	public final static String COUNT = "MvAccountChart.countTotal";

	@Id
	@Column(name = "CD_REDUZIDO")
	private Long id;

	@Column(name = "CD_CONTABIL")
	private String accountingCode;

	@Column(name = "DS_CONTA")
	private String title;

	@Column(name = "CD_ITEM_RES")
	private Integer resultItemCode;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the accountingCode
	 */
	public String getAccountingCode() {
		return accountingCode;
	}

	/**
	 * @param accountingCode
	 *            the accountingCode to set
	 */
	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
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

	/**
	 * @return the resultItemCode
	 */
	public Integer getResultItemCode() {
		return resultItemCode;
	}

	/**
	 * @param resultItemCode
	 *            the resultItemCode to set
	 */
	public void setResultItemCode(Integer resultItemCode) {
		this.resultItemCode = resultItemCode;
	}

}
