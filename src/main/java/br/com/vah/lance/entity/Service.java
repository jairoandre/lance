package br.com.vah.lance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.vah.lance.entity.mv.MvAccountChart;
import br.com.vah.lance.entity.mv.MvDefaultHistory;
import br.com.vah.lance.entity.mv.MvDocumentType;

@Entity
@Table(name = "TB_LANCA_SERVICO", schema = "USRDBVAH")
@NamedQueries({ @NamedQuery(name = Service.ALL, query = "SELECT s FROM Service s"),
		@NamedQuery(name = Service.COUNT, query = "SELECT COUNT(s) FROM Service s") })
public class Service extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String ALL = "Service.all";
	public final static String COUNT = "Service.total";

	@Id
	@SequenceGenerator(name = "seqServiceGenerator", sequenceName = "SEQ_TB_LANCA_SERVICO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceGenerator")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NM_TITULO")
	private String title;

	@ManyToOne
	@JoinColumn(name = "CD_HISTORICO_PADRAO", nullable = false)
	private MvDefaultHistory defaultHistory = new MvDefaultHistory();

	@ManyToOne
	@JoinColumn(name = "CD_TP_DOCUMENTO", nullable = false)
	private MvDocumentType documentType;

	@ManyToOne
	@JoinColumn(name = "CD_CONTA_CONTABIL", nullable = false)
	private MvAccountChart ledgerAccount;

	@Column(name = "SN_FATURAVEL")
	private Boolean billable;

	@Column(name = "SN_AGRUPAVEL")
	private Boolean groupable;

	@Override
	public Long getId() {
		return id;
	}

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

	/**
	 * @return the defaultHistory
	 */
	public MvDefaultHistory getDefaultHistory() {
		return defaultHistory;
	}

	/**
	 * @param defaultHistory
	 *            the defaultHistory to set
	 */
	public void setDefaultHistory(MvDefaultHistory defaultHistory) {
		this.defaultHistory = defaultHistory;
	}

	/**
	 * @return the documentType
	 */
	public MvDocumentType getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType
	 *            the documentType to set
	 */
	public void setDocumentType(MvDocumentType documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the ledgerAccount
	 */
	public MvAccountChart getLedgerAccount() {
		return ledgerAccount;
	}

	/**
	 * @param ledgerAccount
	 *            the ledgerAccount to set
	 */
	public void setLedgerAccount(MvAccountChart ledgerAccount) {
		this.ledgerAccount = ledgerAccount;
	}

	/**
	 * @return the billable
	 */
	public Boolean getBillable() {
		return billable;
	}

	/**
	 * @param billable
	 *            the billable to set
	 */
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	/**
	 * @return the groupable
	 */
	public Boolean getGroupable() {
		return groupable;
	}

	/**
	 * @param groupable
	 *            the groupable to set
	 */
	public void setGroupable(Boolean groupable) {
		this.groupable = groupable;
	}

	@Override
	public String getLabelForSelectItem() {
		return getTitle();
	}
}
