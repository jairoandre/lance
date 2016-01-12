package br.com.vah.lance.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.constant.RolesEnum;

@Entity
@Table(name = "TB_PTC_USUARIO_PUSER", schema = "USRDBVAH")
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
			@JoinColumn(name = "ID_PUSER") }, inverseJoinColumns = {
					@JoinColumn(name = "ID_SERVICO") }, schema = "USRDBVAH")
	private Set<Service> services;

	@ElementCollection(targetClass = RolesEnum.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "TB_LANCA_USUARIO_ROLE", joinColumns = @JoinColumn(name = "ID_PUSER") , schema = "USRDBVAH")
	@Column(name = "CD_ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<RolesEnum> roles;

	public User() {
		services = new LinkedHashSet<Service>();
		roles = new LinkedHashSet<RolesEnum>();
	}

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

	/**
	 * @return the roles
	 */
	public Set<RolesEnum> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Set<RolesEnum> roles) {
		this.roles = roles;
	}

}
