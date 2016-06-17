package br.com.vah.lance.entity.usrdbvah;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;

@Entity
@Table(name = "TB_LANCA_CONTRATO_SETOR", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = ContratoSetor.ALL, query = "SELECT s FROM ContratoSetor s"),
    @NamedQuery(name = ContratoSetor.COUNT, query = "SELECT COUNT(s) FROM ContratoSetor s")})
public class ContratoSetor extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "ContratoSetor.populatedItems";
  public static final String COUNT = "ContratoSetor.countTotal";

  @Id
  @SequenceGenerator(name = "seqContractSetorGenerator", sequenceName = "SEQ_LANCA_CONTRATO_SETOR", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqContractSetorGenerator")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_CONTRATO", nullable = false)
  private Contrato contrato;

  @ManyToOne
  @JoinColumn(name = "ID_CLIENTE", nullable = true)
  private Fornecedor inquilino;

  @ManyToOne
  @JoinColumn(name = "ID_SETOR", nullable = true)
  private Setor setor;

  @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_LANCA_SER_CON_SER", joinColumns = {
      @JoinColumn(name = "ID")}, inverseJoinColumns = {@JoinColumn(name = "ID_SERVICO")}, schema = "USRDBVAH")
  private Set<Servico> servicos = new LinkedHashSet<>();

  public ContratoSetor() {
  }

  public ContratoSetor(Contrato contrato, Setor setor) {
    this.contrato = contrato;
    this.setor = setor;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;

  }

  /**
   * @return the contrato
   */
  public Contrato getContrato() {
    return contrato;
  }

  /**
   * @param contrato the contrato to set
   */
  public void setContrato(Contrato contrato) {
    this.contrato = contrato;
  }

  /**
   * @return the inquilino
   */
  public Fornecedor getInquilino() {
    return inquilino;
  }

  /**
   * @param inquilino the inquilino to set
   */
  public void setInquilino(Fornecedor inquilino) {
    this.inquilino = inquilino;
  }

  /**
   * @return the setor
   */
  public Setor getSetor() {
    return setor;
  }

  /**
   * @param setor the setor to set
   */
  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  /**
   * @return the servicos
   */
  public Set<Servico> getServicos() {
    return servicos;
  }

  /**
   * @param servicos the servicos to set
   */
  public void setServicos(Set<Servico> servicos) {
    this.servicos = servicos;
  }

  @Override
  public String getLabelForSelectItem() {
    return "Serviço do contrato";
  }

}
