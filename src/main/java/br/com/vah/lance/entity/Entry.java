package br.com.vah.lance.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.vah.lance.constant.EntryStatusEnum;

/**
 * Entidade que representa um lançamento.
 * 
 * @author jairoportela
 *
 */
@Entity
@Table(name = "TB_LANCA_LANCAMENTO")
@NamedQueries({ @NamedQuery(name = Entry.ALL, query = "SELECT e FROM Entry e"),
		@NamedQuery(name = Entry.COUNT, query = "SELECT COUNT(e) FROM Entry e"),
		@NamedQuery(name = Entry.BY_DATE_AND_SERVICE, query = "SELECT e FROM Entry e where e.effectiveOn = :date and e.service in :services") })
public class Entry extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ALL = "Entry.populatedItems";
	public static final String COUNT = "Entry.countTotal";
	public static final String BY_DATE_AND_SERVICE = "Entry.byDateAndService";

	@Id
	@SequenceGenerator(name = "seqEntryGenerator", sequenceName = "SEQ_TB_LANCA_LANCAMENTO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEntryGenerator")
	@Column(name = "ID")
	private Long id;

	/**
	 * Contrato
	 */
	@ManyToOne
	@JoinColumn(name = "ID_CONTRATO", nullable = false)
	private Contract contract;

	/**
	 * Serviço referenciado
	 */
	@ManyToOne
	@JoinColumn(name = "ID_SERVICO", nullable = false)
	private Service service;

	/**
	 * Usuário do contrato?
	 */
	@ManyToOne
	@JoinColumn(name = "ID_USU_CONTRATO", nullable = false)
	private User userForContract;

	/**
	 * Usuário autor do lançamento
	 */
	@ManyToOne
	@JoinColumn(name = "ID_USU_LANCADOR", nullable = false)
	private User userForEntry;

	/**
	 * Comentários do lançamento
	 */
	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Comment> comments;

	/**
	 * Valor do lançamento
	 */
	@Column(name = "VL_LANCAMENTO")
	private BigDecimal value;

	/**
	 * Valor base do lançamento
	 */
	@Column(name = "VL_FIXO")
	private BigDecimal contractValue;

	/**
	 * Valor variável do lançamento
	 */
	@Column(name = "VL_VARIAVEL")
	private BigDecimal variableValue;

	/**
	 * Data de criação do lançamento
	 */
	@Column(name = "DT_LANCAMENTO")
	private Date createdOn;

	/**
	 * Data de vigência
	 */
	@Column(name = "DT_VIGENCIA")
	private Date effectiveOn;

	/**
	 * Estado do lançamento, valores possíveis:
	 * <ul>
	 * <li>Criado</li>
	 * <li>Validado</li>
	 * <li>Pendente</li>
	 * <li>Corrigido</li>
	 * <li>Transmitido</li>
	 * <li>Excluído</li>
	 * </ul>
	 */
	@Column(name = "CD_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntryStatusEnum status;

	public Entry() {
		this.status = EntryStatusEnum.C;
		this.createdOn = new Date();
	}

	public Entry(Contract contract) {
		this();
		this.contract = contract;
	}

	@Override
	public Long getId() {
		return id;
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
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return the userForContract
	 */
	public User getUserForContract() {
		return userForContract;
	}

	/**
	 * @param userForContract
	 *            the userForContract to set
	 */
	public void setUserForContract(User userForContract) {
		this.userForContract = userForContract;
	}

	/**
	 * @return the userForEntry
	 */
	public User getUserForEntry() {
		return userForEntry;
	}

	/**
	 * @param userForEntry
	 *            the userForEntry to set
	 */
	public void setUserForEntry(User userForEntry) {
		this.userForEntry = userForEntry;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the contractValue
	 */
	public BigDecimal getContractValue() {
		return contractValue;
	}

	/**
	 * @param contractValue
	 *            the contractValue to set
	 */
	public void setContractValue(BigDecimal contractValue) {
		this.contractValue = contractValue;
	}

	/**
	 * @return the variableValue
	 */
	public BigDecimal getVariableValue() {
		return variableValue;
	}

	/**
	 * @param variableValue
	 *            the variableValue to set
	 */
	public void setVariableValue(BigDecimal variableValue) {
		this.variableValue = variableValue;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the effectiveOn
	 */
	public Date getEffectiveOn() {
		return effectiveOn;
	}

	/**
	 * @param effectiveOn
	 *            the effectiveOn to set
	 */
	public void setEffectiveOn(Date effectiveOn) {
		this.effectiveOn = effectiveOn;
	}

	/**
	 * @return the status
	 */
	public EntryStatusEnum getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(EntryStatusEnum status) {
		this.status = status;
	}

	@Override
	public String getLabelForSelectItem() {
		return null;
	}

	@Override
	public String getAllNamedQuery() {
		return ALL;
	}

	@Override
	public String getCountNamedQuery() {
		return COUNT;
	}

}
