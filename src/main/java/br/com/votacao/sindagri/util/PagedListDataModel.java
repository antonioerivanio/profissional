/*    */ package br.com.votacao.sindagri.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ import javax.faces.model.DataModel;
/*    */ 
/*    */ 
/*    */ public class PagedListDataModel
/*    */   extends DataModel
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6661053520289463045L;
/* 13 */   private int rowIndex = -1;
/*    */   
/*    */   private int totalNumRows;
/*    */   
/* 17 */   private int pageSize = 10;
/*    */   
/*    */   private List list;
/*    */ 
/*    */   
/*    */   public PagedListDataModel() {}
/*    */   
/*    */   public PagedListDataModel(List list, int totalNumRows) {
/* 25 */     setWrappedData(list);
/* 26 */     this.totalNumRows = totalNumRows;
/*    */   }
/*    */   
/*    */   public boolean isRowAvailable() {
/* 30 */     if (this.list == null) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (getRowIndex() >= 0 && getRowIndex() < this.list.size()) {
/* 34 */       return true;
/*    */     }
/* 36 */     return false;
/*    */   }
/*    */   public int getRowCount() {
/* 39 */     return this.totalNumRows;
/*    */   }
/*    */   public Object getRowData() {
/* 42 */     if (this.list == null) {
/* 43 */       return null;
/*    */     }
/* 45 */     if (!isRowAvailable()) {
/* 46 */       throw new IllegalArgumentException();
/*    */     }
/*    */     
/* 49 */     int dataIndex = getRowIndex();
/*    */     
/* 51 */     if (dataIndex < this.list.size()) {
/* 52 */       return this.list.get(dataIndex);
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRowIndex() {
/* 60 */     if (getPageSize() == 0) {
/* 61 */       this.rowIndex = -1;
/*    */     }
/* 63 */     return (this.rowIndex == -1) ? -1 : (this.rowIndex % this.pageSize);
/*    */   } public void setRowIndex(int rowIndex) {
/* 65 */     this.rowIndex = rowIndex;
/*    */   }
/* 67 */   public Object getWrappedData() { return this.list; } public void setWrappedData(Object list) {
/* 68 */     this.list = (List)list;
/*    */   }
/* 70 */   public int getPageSize() { return this.pageSize; } public void setPageSize(int pageSize) {
/* 71 */     this.pageSize = pageSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\PagedListDataModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */