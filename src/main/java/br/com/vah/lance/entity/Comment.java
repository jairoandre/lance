package br.com.vah.lance.entity;

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
@NamedQueries({ @NamedQuery(name = Comment.ALL, query = "SELECT c FROM Comment c"),
		@NamedQuery(name = Comment.COUNT, query = "SELECT COUNT(c) FROM Comment c") })
public class Comment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ALL = "Comment.populatedItems";
	public static final String COUNT = "Comment.countTotal";

	@Id
	@SequenceGenerator(name = "seqCommentGenerator", sequenceName = "SEQ_TB_LANCA_COMENTARIOS", allocationSize = 1)
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
	private Entry entry;

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
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public String getLabelForSelectItem() {
		return null;
	}

}
