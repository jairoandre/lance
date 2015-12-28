package br.com.vah.lance.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 
 * @author Emre Simtay <emre@simtay.com>
 */
@SuppressWarnings("serial")
@Entity
@NamedQueries({
    @NamedQuery(name = User.ALL, query = "SELECT u FROM User u "),
    @NamedQuery(name = User.TOTAL, query = "SELECT COUNT(u) FROM User u")})
public class User extends BaseEntity implements Serializable {

    public final static String ALL = "User.populateUsers";
    public final static String TOTAL = "User.countUsersTotal";
       
    @Column(nullable = false, length = 50)
    private String username;
    
    @Column(length = 50)
    private String firstname;
    
    @Column(length = 50)
    private String lastname;
    
    @Column(length = 50)
    private String email;
    
    @Column(length = 64)
    private String password;
    
    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = {
        @JoinColumn(name = "User_userid")}, inverseJoinColumns = {
        @JoinColumn(name = "Role_roleid")})
    private List<Role> roles;

    public User() {
        roles = new ArrayList<Role>();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}