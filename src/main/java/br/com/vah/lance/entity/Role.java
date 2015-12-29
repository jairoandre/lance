package br.com.vah.lance.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Emre Simtay <emre@simtay.com>
 */
@SuppressWarnings("serial")
@Entity
@Table(name="TB_PTC_PERFIL_ACESSO")
@NamedQueries({@NamedQuery(name = Role.ALL, query = "SELECT r FROM Role r")})
public class Role extends BaseEntity implements Serializable {

    public final static String ALL = "Role.populatedItems";
    
    @Id
    @Column(name="ID_PERFIL_ACESSO")
    private Long id;
    
    @Column(name="DS_PERFIL_ACESSO")
    private String rolename;

    public Role() {
    }   

    @ManyToMany(mappedBy = "roles", fetch=FetchType.LAZY)
    private List<User> users;

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
	 * @return the rolename
	 */
	public String getRolename() {
		return rolename;
	}

	/**
	 * @param rolename the rolename to set
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String getAllNamedQuery() {
		return ALL;
	}

	@Override
	public String getCountNamedQuery() {
		return null;
	}
    
    
    
}
