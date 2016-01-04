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
@Table(name = "TB_LANCA_CLIENTE")
@NamedQueries({
    @NamedQuery(name = Client.ALL, query = "SELECT c FROM Client c"),
    @NamedQuery(name = Client.COUNT, query = "SELECT COUNT(c) FROM Client c")})
public class Client extends BaseEntity {
	
	public final static String ALL = "Client.populatedClients";
    public final static String COUNT = "Client.countClientsTotal";

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqClientGenerator", sequenceName = "SEQ_TB_LANCA_CLIENTE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqClientGenerator")
	@Column(name = "ID")
	private Long id;
	@Column(name = "CD_FORNECEDOR")
	private Integer clientCode;
	@Column(name = "NM_TITULO")
	private String title;

	public Client() {

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
	 * @return the clientCode
	 */
	public Integer getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(Integer clientCode) {
		this.clientCode = clientCode;
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
	public String getAllNamedQuery() {
		return ALL;
	}

	@Override
	public String getCountNamedQuery() {
		return COUNT;
	}

	@Override
	public String getLabelForSelectItem() {
		return getTitle();
	}

}