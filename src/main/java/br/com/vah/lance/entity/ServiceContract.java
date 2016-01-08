package br.com.vah.lance.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_LANCA_SERVICO_CONTRATO", schema = "USRDBVAH")
public class ServiceContract {
	
	@Id
	@SequenceGenerator(name = "seqServiceContractGenerator", sequenceName = "SEQ_TB_LANCA_SERVICO_CONTRATO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceContractGenerator")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="ID_SERVICO", nullable=false)
	private Service service;
	
	@ManyToOne
	@JoinColumn(name="ID_CONTRATO", insertable=true, updatable=true, nullable=false)
	private Contract contract;
	
	@Column(name = "VL_SERVICO")
	private BigDecimal amount;

	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return the contract
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
