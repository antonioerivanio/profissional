package br.gov.ce.tce.srh.util;

import java.util.List;
import javax.faces.model.DataModel;

@SuppressWarnings("rawtypes")
public class PagedListDataModel extends DataModel {

	private int rowIndex = -1;

	private int totalNumRows;

	private int pageSize = 10;

	private List list;

	public PagedListDataModel() {super();}
	
	public PagedListDataModel(List list, int totalNumRows) {
		super();
		setWrappedData(list);
		this.totalNumRows = totalNumRows;
	}

	public boolean isRowAvailable() {
		if (list == null)
			return false;
		
		if (getRowIndex() >= 0 && getRowIndex() < list.size())
			return true;
		
		return false;
	}

	public int getRowCount() {return totalNumRows;}

	public Object getRowData() {
		if (list == null)			
			return null;
		
		else if (!isRowAvailable())
			throw new IllegalArgumentException();
		
		else {
			int dataIndex = getRowIndex();
			
			if(dataIndex < list.size())			
				return list.get(dataIndex);
			
			return null;
		}
	}

	public int getRowIndex() {
		
		if(getPageSize() == 0)
			this.rowIndex = -1;
		
		return ( this.rowIndex == -1 ? -1 : this.rowIndex % this.pageSize );
	}
	public void setRowIndex(int rowIndex) {this.rowIndex = rowIndex;}

	public Object getWrappedData() {return list;}
	public void setWrappedData(Object list) {this.list = (List) list;}

	public int getPageSize() {return pageSize;}
	public void setPageSize(int pageSize) {this.pageSize = pageSize;}

}
