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
	private String ledgerAccount;

	@Column(name = "DS_CONTA")
	private String title;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ledgerAccount
	 */
	public String getLedgerAccount() {
		return ledgerAccount;
	}

	/**
	 * @param ledgerAccount the ledgerAccount to set
	 */
	public void setLedgerAccount(String ledgerAccount) {
		this.ledgerAccount = ledgerAccount;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getLabelForSelectItem() {
		return title;
	}

}
