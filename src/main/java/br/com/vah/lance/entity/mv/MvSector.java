package br.com.vah.lance.entity.mv;

import br.com.vah.lance.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SETOR", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvSector.ALL, query = "SELECT s FROM MvSector s"),
		@NamedQuery(name = MvSector.COUNT, query = "SELECT COUNT(s) FROM MvSector s") })
public class MvSector extends BaseEntity {

	public final static String ALL = "MvSector.populatedItems";
	public final static String COUNT = "MvSector.countTotal";

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
	@JoinTable(name = "TB_LANCA_CLIENTE_SETOR", joinColumns = { @JoinColumn(name = "CD_SETOR") }, inverseJoinColumns = {
			@JoinColumn(name = "CD_FORNECEDOR") }, schema = "USRDBVAH")
	private Set<MvClient> clients;

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param title
	 *            the title to set
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
	public Set<MvClient> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 *            the clients to set
	 */
	public void setClient(Set<MvClient> clients) {
		this.clients = clients;
	}

}
