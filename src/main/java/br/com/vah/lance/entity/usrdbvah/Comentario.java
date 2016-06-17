package br.com.vah.lance.entity.usrdbvah;

import br.com.vah.lance.entity.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_LANCA_COMENTARIOS", schema = "USRDBVAH")
@NamedQueries({ @NamedQuery(name = Comentario.ALL, query = "SELECT c FROM Comentario c"),
		@NamedQuery(name = Comentario.COUNT, query = "SELECT COUNT(c) FROM Comentario c") })
public class Comentario extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ALL = "Comentario.populatedItems";
	public static final String COUNT = "Comentario.countTotal";

	@Id
	@SequenceGenerator(name = "seqCommentGenerator", sequenceName = "SEQ_LANCA_COMENTARIOS", schema = "USRDBVAH", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCommentGenerator")
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_PUSER", nullable = false)
	private User author;

	@Column(name = "NM_DETALHES", nullable = false)
	private String details;

	@Column(name = "DT_CRIACAO", nullable = false)
	private Date createdOn;

	@ManyToOne
	@JoinColumn(name = "ID_LANCAMENTO", nullable = false)
	private Lancamento lancamento;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the lancamento
	 */
	public Lancamento getLancamento() {
		return lancamento;
	}

	/**
	 * @param lancamento
	 *            the lancamento to set
	 */
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	@Override
	public String getLabelForSelectItem() {
		return null;
	}

}
