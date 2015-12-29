package br.com.vah.lance.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author jairoportela
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="TB_PTC_USUARIO_PUSER")
@NamedQueries({
    @NamedQuery(name = User.ALL, query = "SELECT u FROM User u "),
    @NamedQuery(name = User.TOTAL, query = "SELECT COUNT(u) FROM User u")})
public class User extends BaseEntity implements Serializable {

    public final static String ALL = "User.populatedItems";
    public final static String TOTAL = "User.totalCount";
    
    @Id
    @Column(name="ID_PUSER")
    private Long id;
       
    @Column(name="DS_LOGIN", nullable = false)
    private String login;
    
    @Column(name="DS_SENHA")
    private String password;
    
    @ManyToMany
    @JoinTable(name = "TB_PTC_PERFIL_ACESSO_USUARIO", joinColumns = {
        @JoinColumn(name = "ID_PUSER")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_PERFIL_ACESSO")})
    private List<Role> roles;

    public User() {
        roles = new ArrayList<Role>();
    }

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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String getAllNamedQuery() {
		return ALL;
	}

	@Override
	public String getCountNamedQuery() {
		return TOTAL;
	}
    
    
    
}