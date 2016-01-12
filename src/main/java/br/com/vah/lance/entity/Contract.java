package br.com.vah.lance.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;

/**
 * The persistent class for the TB_LANCA_CONTRATO database table.
 **/
@Entity
@Table(name = "TB_LANCA_CONTRATO", schema = "USRDBVAH")
@NamedQueries({ @NamedQuery(name = Contract.ALL, query = "SELECT c FROM Contract c"),
		@NamedQuery(name = Contract.COUNT, query = "SELECT COUNT(c) FROM Contract c"),
		@NamedQuery(name = Contract.VALIDS_IN_DATE, query = "SELECT c from Contract c where c.beginDate <= :date and c.endDate >= :date") })
public class Contract extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public static final String ALL = "Contract.populatedItems";
	public static final String COUNT = "Contract.countTotal";
	public static final String VALIDS_IN_DATE = "Contract.validsInDate";

	@Id
	@SequenceGenerator(name = "seqContractGenerator", sequenceName = "SEQ_TB_LANCA_CONTRATO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqContractGenerator")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NM_TITULO")
	private String title;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FINAL")
	private Date endDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INICIO")
	private Date beginDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_REAJUSTE")
	private Date changeDate;

	@ManyToOne
	@JoinColumn(name = "ID_FORNECEDOR_PROPRIETARIO", nullable = true)
	private MvClient owner;

	@ManyToOne
	@JoinColumn(name = "ID_FORNECEDOR_CONTRATANTE", nullable = false)
	private MvClient subject;

	@ManyToOne
	@JoinColumn(name = "ID_SETOR", nullable = false)
	private MvSector sector;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "TB_LANCA_CONTRATO_SERVICO", joinColumns = {
			@JoinColumn(name = "ID_CONTRATO") }, inverseJoinColumns = {
					@JoinColumn(name = "ID_SERVICO") }, schema = "USRDBVAH")
	private List<Service> services;

	public Contract() {
		this.services = new ArrayList<>();
	}

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

	/**
	 * @return the finalDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param finalDate
	 *            the finalDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return changeDate;
	}

	/**
	 * @param changeDate
	 *            the changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @return the owner
	 */
	public MvClient getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(MvClient owner) {
		this.owner = owner;
	}

	/**
	 * @return the subject
	 */
	public MvClient getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(MvClient subject) {
		this.subject = subject;
	}

	/**
	 * @return the sector
	 */
	public MvSector getSector() {
		return sector;
	}

	/**
	 * @param sector
	 *            the sector to set
	 */
	public void setSector(MvSector sector) {
		this.sector = sector;
	}

	/**
	 * @return the services
	 */
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String getLabelForSelectItem() {
		return getTitle();
	}

}
