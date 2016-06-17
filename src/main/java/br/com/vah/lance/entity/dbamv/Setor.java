package br.com.vah.lance.entity.dbamv;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.usrdbvah.SetorDetalhe;
import br.com.vah.lance.entity.usrdbvah.SetorMedidorConsumo;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SETOR", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Setor.ALL, query = "SELECT s FROM Setor s"),
    @NamedQuery(name = Setor.COUNT, query = "SELECT COUNT(s) FROM Setor s")})
public class Setor extends BaseEntity {

  public final static String ALL = "Setor.populatedItems";
  public final static String COUNT = "Setor.countTotal";

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CD_SETOR")
  private Long id;

  @Column(name = "NM_SETOR")
  private String title;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "TB_LANCA_CLIENTE_SETOR", joinColumns = {@JoinColumn(name = "CD_SETOR")}, inverseJoinColumns = {
      @JoinColumn(name = "CD_FORNECEDOR")}, schema = "USRDBVAH")
  private Set<Fornecedor> clients;

  @OneToOne(mappedBy = "setor", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  private SetorDetalhe setorDetalhe = new SetorDetalhe();

  @OneToMany(mappedBy = "setor", fetch = FetchType.EAGER)
  private Set<SetorMedidorConsumo> meters;

  /**
   * @return the id
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  @Override
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getLabelForSelectItem() {
    return getTitle();
  }

  /**
   * @return the clients
   */
  public Set<Fornecedor> getClients() {
    return clients;
  }

  /**
   * @param clients the clients to set
   */
  public void setClient(Set<Fornecedor> clients) {
    this.clients = clients;
  }

  public void setClients(Set<Fornecedor> clients) {
    this.clients = clients;
  }

  public SetorDetalhe getSetorDetalhe() {
    return setorDetalhe;
  }

  public void setSetorDetalhe(SetorDetalhe setorDetalhe) {
    this.setorDetalhe = setorDetalhe;
  }

  public Set<SetorMedidorConsumo> getMeters() {
    return meters;
  }

  public void setMeters(Set<SetorMedidorConsumo> meters) {
    this.meters = meters;
  }
}
