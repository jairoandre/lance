package br.com.vah.lance.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_LANCA_SETOR database table.
 * 
 */
@Entity
@Table(name = "TB_LANCA_SETOR")
@NamedQueries({
    @NamedQuery(name = Sector.ALL, query = "SELECT s FROM Sector s "),
    @NamedQuery(name = Sector.TOTAL, query = "SELECT COUNT(s) FROM Sector s")})
public class Sector implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static String ALL = "Sector.populateSectors";
    public final static String TOTAL = "Sector.countSectorsTotal";

	@Id
	@SequenceGenerator(name = "seqSectorGenerator", sequenceName = "SEQ_TB_LANCA_SETOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSectorGenerator")
	@Column(name = "ID")
	private Long id;
	@Column(name = "VL_AREA")
	private Double area;
	@Lob
	@Column(name = "LO_DETALHES")
	private String details;
	@Column(name = "NM_TITULO")
	private String name;
	@ManyToMany
	@JoinTable(name = "TB_LANCA_CLIENTE_SETOR", joinColumns = {
			@JoinColumn(name = "ID_SETOR", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID") })
	private List<Supplier> suppliers;

	public Sector() {}

	

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
	 * @return the area
	 */
	public Double getArea() {
		return area;
	}



	/**
	 * @param area the area to set
	 */
	public void setArea(Double area) {
		this.area = area;
	}



	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}



	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the suppliers
	 */
	public List<Supplier> getSuppliers() {
		return suppliers;
	}



	/**
	 * @param suppliers the suppliers to set
	 */
	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sector other = (Sector) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}