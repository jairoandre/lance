package br.com.vah.lance.entity;

import java.util.LinkedHashSet;
import java.util.Set;

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

import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;

@Entity
@Table(name = "TB_LANCA_SERVICO_CONTRATO", schema = "USRDBVAH")
@NamedQueries({ @NamedQuery(name = ServiceContract.ALL, query = "SELECT s FROM ServiceContract s"),
		@NamedQuery(name = ServiceContract.COUNT, query = "SELECT COUNT(s) FROM ServiceContract s") })
public class ServiceContract extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ALL = "ServiceContract.populatedItems";
	public static final String COUNT = "ServiceContract.countTotal";

	@Id
	@SequenceGenerator(name = "seqServiceContractGenerator", sequenceName = "SEQ_TB_LANCA_SERVICE_CONTRACT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceContractGenerator")
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_CONTRATO", nullable = false)
	private Contract contract;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE", nullable = true)
	private MvClient tenant;

	@ManyToOne
	@JoinColumn(name = "ID_SETOR", nullable = true)
	private MvSector sector;

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "TB_LANCA_SERVICO_CONTRATO_SERVICO", joinColumns = {
			@JoinColumn(name = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ID_SERVICO") }, schema = "USRDBVAH")
	private Set<Service> services = new LinkedHashSet<>();

	public ServiceContract() {
	}

	public ServiceContract(Contract contract, MvSector sector) {
		this.contract = contract;
		this.sector = sector;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/**
	 * @return the contract
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the tenant
	 */
	public MvClient getTenant() {
		return tenant;
	}

	/**
	 * @param tenant
	 *            the tenant to set
	 */
	public void setTenant(MvClient tenant) {
		this.tenant = tenant;
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
	public Set<Service> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(Set<Service> services) {
		this.services = services;
	}

	@Override
	public String getLabelForSelectItem() {
		return "Serviço do contrato";
	}

}
