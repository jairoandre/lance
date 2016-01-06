package br.com.vah.lance.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PaginatedSearchParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> params;
	
	private Integer first;
	
	private Integer pageSize;
	
	private String orderBy;
	
	private Boolean asc;
	
	public PaginatedSearchParam() {
		params = new HashMap<>();
		first = 0;
		pageSize = 0;
		asc = true;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * @return the first
	 */
	public Integer getFirst() {
		return first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(Integer first) {
		this.first = first;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the total to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the asc
	 */
	public Boolean getAsc() {
		return asc;
	}

	/**
	 * @param asc the asc to set
	 */
	public void setAsc(Boolean asc) {
		this.asc = asc;
	}

}
