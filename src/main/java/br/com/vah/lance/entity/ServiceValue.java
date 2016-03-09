package br.com.vah.lance.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_LANCA_SERVICO_VALORES", schema = "USRDBVAH")
public class ServiceValue extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqServiceValuesGenerator", sequenceName = "SEQ_LANCA_SERV_VALORES", schema = "USRDBVAH", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceValuesGenerator")
	@Column(name = "ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FINAL")
	private Date endDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INICIO")
	private Date beginDate;

	@Column(name = "VL_VALOR_A", precision = 4)
	private BigDecimal valueA;

	@Column(name = "VL_VALOR_B", precision = 4)
	private BigDecimal valueB;

	@Column(name = "VL_VALOR_C", precision = 4)
	private BigDecimal valueC;

	@Column(name = "VL_VALOR_D", precision = 4)
	private BigDecimal valueD;
	
	@ManyToOne
	@JoinColumn(name = "ID_SERVICO", nullable = false)
	private Service service;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public BigDecimal getValueA() {
		return valueA;
	}

	public void setValueA(BigDecimal valueA) {
		this.valueA = valueA;
	}

	public BigDecimal getValueB() {
		return valueB;
	}

	public void setValueB(BigDecimal valueB) {
		this.valueB = valueB;
	}

	public BigDecimal getValueC() {
		return valueC;
	}

	public void setValueC(BigDecimal valueC) {
		this.valueC = valueC;
	}

	public BigDecimal getValueD() {
		return valueD;
	}

	public void setValueD(BigDecimal valueD) {
		this.valueD = valueD;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@Override
	public String getLabelForSelectItem() {
		return String.valueOf(id);
	}

}
