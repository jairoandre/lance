package br.com.vah.lance.entity.dbamv;

import java.util.LinkedHashSet;
import java.util.Set;

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

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "FORNECEDOR", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Fornecedor.ALL, query = "SELECT c FROM Fornecedor c"),
    @NamedQuery(name = Fornecedor.COUNT, query = "SELECT COUNT(c) FROM Fornecedor c")})
public class Fornecedor extends BaseEntity {

  public final static String ALL = "Fornecedor.populatedItems";
  public final static String COUNT = "Fornecedor.countTotal";

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CD_FORNECEDOR")
  private Long id;

  @Column(name = "NM_FORNECEDOR")
  private String title;

  @Column(name = "NM_FANTASIA")
  private String nomeFantasia;

  @Column(name = "TP_CLIENTE_FORN")
  private String type;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_LANCA_CLIENTE_SETOR", joinColumns = {
      @JoinColumn(name = "CD_FORNECEDOR")}, inverseJoinColumns = {
      @JoinColumn(name = "CD_SETOR")}, schema = "USRDBVAH")
  private Set<Setor> setores;

  public Fornecedor() {
    setores = new LinkedHashSet<>();
  }

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

  public String getNomeFantasia() {
    return nomeFantasia;
  }

  public void setNomeFantasia(String nomeFantasia) {
    this.nomeFantasia = nomeFantasia;
  }

  public Set<Setor> getSetores() {
    return setores;
  }

  public void setSetores(Set<Setor> setores) {
    this.setores = setores;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getLabelForSelectItem() {
    if (id == null) {
      return null;
    } else {
      return String.format("%d - %s", getId(), getTitle());
    }
  }

}
