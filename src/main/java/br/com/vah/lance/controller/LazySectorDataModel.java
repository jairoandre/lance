package br.com.vah.lance.controller;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.vah.lance.entity.Sector;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.util.LazySorter;

/**
 * 
 * Custom Lazy Sector DataModel which extends PrimeFaces LazyDataModel.
 * For more information please visit http://www.primefaces.org/showcase-labs/ui/datatableLazy.jsf
 */

public class LazySectorDataModel extends LazyDataModel<Sector> implements Serializable{

    // Data Source for binding data to the DataTable
    private List<Sector> datasource;
    // Selected Page size in the DataTable
    private int pageSize;
    // Current row index number
    private int rowIndex;
    // Total row number
    private int rowCount;
    // Data Access Service for create read update delete operations
    private DataAccessService crudService;
    
    /**
     *
     * @param crudService
     */
    public LazySectorDataModel(DataAccessService crudService) {
        this.crudService = crudService;
    }
    
    @Override
    public List<Sector> load(int first, int pageSize, String sortField, SortOrder sortOrder,
    		Map<String, Object> filters) {
    	datasource = crudService.findWithNamedQuery(Sector.ALL, first, first + pageSize);
    	if(sortField != null){
    		Collections.sort(datasource, new LazySorter<>(sortField, sortOrder));
    	}
    	setRowCount(crudService.countTotalRecord(Sector.TOTAL));
    	return datasource;
    }
    
    
    /**
     * Checks if the row is available
     * @return boolean
     */
    @Override
    public boolean isRowAvailable() {
        if(datasource == null) 
            return false;
        int index = rowIndex % pageSize ; 
        return index >= 0 && index < datasource.size();
    }
    
    /**
     * Gets the sector object's primary key
     * @param sector
     * @return Object
     */
    @Override
    public Object getRowKey(Sector sector) {
        return sector.getId().toString();
    }

    /**
     * Returns the sector object at the specified position in datasource.
     * @return 
     */
    @Override
    public Sector getRowData() {
        if(datasource == null)
            return null;
        int index =  rowIndex % pageSize;
        if(index > datasource.size()){
            return null;
        }
        return datasource.get(index);
    }
    
    /**
     * Returns the sector object that has the row key.
     * @param rowKey
     * @return 
     */
    @Override
    public Sector getRowData(String rowKey) {
        if(datasource == null)
            return null;
       for(Sector sector : datasource) {  
           if(sector.getId().toString().equals(rowKey))  
           return sector;  
       }  
       return null;  
    }
    
    
    /*
     * ===== Getters and Setters of LazySectorDataModel fields
     */
    
    
    /**
     *
     * @param pageSize
     */
    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Returns page size
     * @return int
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Returns current row index
     * @return int
     */
    @Override
    public int getRowIndex() {
        return this.rowIndex;
    }
    
    /**
     * Sets row index
     * @param rowIndex
     */
    @Override
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * Sets row count
     * @param rowCount
     */
    @Override
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    /**
     * Returns row count
     * @return int
     */
    @Override
    public int getRowCount() {
        return this.rowCount;
    }
     
    /**
     * Sets wrapped data
     * @param list
     */
    @Override
    public void setWrappedData(Object list) {
        this.datasource = (List<Sector>) list;
    }
    
    /**
     * Returns wrapped data
     * @return
     */
    @Override
    public Object getWrappedData() {
        return datasource;
    }
}
                    
