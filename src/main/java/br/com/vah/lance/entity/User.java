package br.com.vah.lance.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "TB_PTC_USUARIO_PUSER")
@NamedQueries({ @NamedQuery(name = User.ALL, query = "SELECT u FROM User u"),
		@NamedQuery(name = User.COUNT, query = "SELECT COUNT(u) FROM User u"),
		@NamedQuery(name = User.FIND_BY_LOGIN, query = "SELECT u FROM User u where u.login = :login") })
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ALL = "User.populatedItems";
	public static final String COUNT = "User.countTotal";
	public static final String FIND_BY_LOGIN = "User.findByLogin";

	@Id
	@Column(name = "ID_PUSER")
	private Long id;

	@Column(name = "DS_LOGIN", nullable = false)
	private String login;

	@Column(name = "DS_SENHA", nullable = false)
	private String password;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "TB_LANCA_USUARIO_SERVICO", joinColumns = {
			@JoinColumn(name = "ID_PUSER") }, inverseJoinColumns = { @JoinColumn(name = "ID_SERVICO") })
	private List<Service> services;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getLabelForSelectItem() {
		return login;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	public String getAllNamedQuery() {
		return ALL;
	}

	@Override
	public String getCountNamedQuery() {
		return COUNT;
	}

}