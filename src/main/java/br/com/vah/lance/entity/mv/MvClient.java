package br.com.vah.lance.entity.mv;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vah.lance.entity.BaseEntity;

@Entity
@Table(name = "FORNECEDOR", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = MvClient.ALL, query = "SELECT c FROM MvClient c"),
		@NamedQuery(name = MvClient.COUNT, query = "SELECT COUNT(c) FROM MvClient c") })
public class MvClient extends BaseEntity {

	public final static String ALL = "MvClient.populatedItems";
	public final static String COUNT = "MvClient.countTotal";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CD_FORNECEDOR")
	private Long id;

	@Column(name = "NM_FORNECEDOR")
	private String title;

	@ElementCollection(targetClass = MvSector.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "TB_LANCA_CLIENTE_SETOR", joinColumns = @JoinColumn(name = "CD_FORNECEDOR"), schema = "USRDBVAH" )
	@Column(name = "CD_PRESTADOR", nullable = false)
	private Set<MvSector> sectors;

	public MvClient() {
		sectors = new LinkedHashSet<>();
	}

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

	/**
	 * @return the sectors
	 */
	public Set<MvSector> getSectors() {
		return sectors;
	}

	/**
	 * @param sectors
	 *            the sectors to set
	 */
	public void setSectors(Set<MvSector> sectors) {
		this.sectors = sectors;
	}

	@Override
	public String getLabelForSelectItem() {
		return getTitle();
	}

}
