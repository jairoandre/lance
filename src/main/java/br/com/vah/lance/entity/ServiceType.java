package br.com.vah.lance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_LANCA_TIPO_SERVICO")
@NamedQueries({ @NamedQuery(name = ServiceType.ALL, query = "SELECT st FROM ServiceType st"),
		@NamedQuery(name = ServiceType.TOTAL, query = "SELECT COUNT(st) FROM ServiceType st") })
public class ServiceType extends BaseEntity {

	public final static String ALL = "ServiceType.populatedItems";
	public final static String TOTAL = "ServiceType.countTotal";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seqServiceTypeGenerator", sequenceName = "SEQ_TB_LANCA_SERVICO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqServiceTypeGenerator")
	@Column(name = "ID")
	private Long id;
	@Column(name = "NM_TITULO")
	private String title;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getAllNamedQuery() {
		return ALL;
	}

	@Override
	public String getCountNamedQuery() {
		return TOTAL;
	}

	@Override
	public String getLabelForSelectItem() {
		return getTitle();
	}

}
