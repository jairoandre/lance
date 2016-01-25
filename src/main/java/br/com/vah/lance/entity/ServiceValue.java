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
	@SequenceGenerator(name = "seqServiceValuesGenerator", sequenceName = "SEQ_TB_LANCA_SERVICO_VALORES", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceValuesGenerator")
	@Column(name = "ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FINAL")
	private Date endDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INICIO")
	private Date beginDate;

	@Column(name = "VL_VALOR", precision = 4)
	private BigDecimal value;

	@Column(name = "VL_VALOR_A", precision = 4)
	private BigDecimal valueA;

	@Column(name = "VL_VALOR_B", precision = 4)
	private BigDecimal valueB;

	@Column(name = "VL_VALOR_C", precision = 4)
	private BigDecimal valueC;
	
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

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
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
	 *
	 * @return
   */
	public BigDecimal getValueA() {
		return valueA;
	}

	/**
	 *
	 * @param compValueA
   */
	public void setValueA(BigDecimal compValueA) {
		this.valueA = compValueA;
	}

	/**
	 *
	 * @return
   */
	public BigDecimal getValueB() {
		return valueB;
	}

	/**
	 *
	 * @param compValueB
   */
	public void setValueB(BigDecimal compValueB) {
		this.valueB = compValueB;
	}

	/**
	 *
	 * @return
   */
	public BigDecimal getValueC() {
		return valueC;
	}

	/**
	 *
	 * @param compValueC
   */
	public void setValueC(BigDecimal compValueC) {
		this.valueC = compValueC;
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

	@Override
	public String getLabelForSelectItem() {
		return String.valueOf(id);
	}

}
