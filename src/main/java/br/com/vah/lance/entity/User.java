package br.com.vah.lance.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;

import br.com.vah.lance.constant.RolesEnum;

@Entity
@Table(name = "TB_LANCA_USUARIO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = User.ALL, query = "SELECT u FROM User u"),
    @NamedQuery(name = User.COUNT, query = "SELECT COUNT(u) FROM User u"),
    @NamedQuery(name = User.FIND_BY_LOGIN, query = "SELECT u FROM User u where u.login = :login")})
public class User extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "User.populatedItems";
  public static final String COUNT = "User.countTotal";
  public static final String FIND_BY_LOGIN = "User.findByLogin";

  @Id
  @SequenceGenerator(name = "seqUserGenerator", sequenceName = "SEQ_LANCA_USUARIO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUserGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "DS_LOGIN", nullable = false, unique = true)
  private String login;

  @Column(name = "NM_TITULO", nullable = true)
  private String title;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_LANCA_USUARIO_SERVICO", joinColumns = {
      @JoinColumn(name = "ID_PUSER")}, inverseJoinColumns = {
      @JoinColumn(name = "ID_SERVICO")}, schema = "USRDBVAH")
  private Set<Service> services;

  @ElementCollection(targetClass = RolesEnum.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "TB_LANCA_USUARIO_ROLE", joinColumns = @JoinColumn(name = "ID_PUSER"), schema = "USRDBVAH")
  @Column(name = "CD_ROLE", nullable = false)
  @Enumerated(EnumType.STRING)
  private Set<RolesEnum> roles;

  public User() {
    services = new LinkedHashSet<>();
    roles = new LinkedHashSet<>();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;

  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Set<Service> getServices() {
    return services;
  }

  public void setServices(Set<Service> services) {
    this.services = services;
  }

  public Set<RolesEnum> getRoles() {
    return roles;
  }

  public void setRoles(Set<RolesEnum> roles) {
    this.roles = roles;
  }

  @Override
  public String getLabelForSelectItem() {
    return login;
  }



}
