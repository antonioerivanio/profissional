/*    */ package br.com.votacao.sindagri.converter;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import javax.faces.component.UIComponent;
/*    */ import javax.faces.context.FacesContext;
/*    */ import javax.faces.convert.Converter;
/*    */ import javax.faces.convert.ConverterException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateConverter
/*    */   implements Converter
/*    */ {
/* 17 */   public static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getAsObject(FacesContext fc, UIComponent ui, String value) {
/*    */     try {
/* 27 */       return (value == null || "".equals(value) || "  /  /    ".equals(value)) ? null : df.parse(value);
/* 28 */     } catch (Exception e) {
/* 29 */       throw new ConverterException("Erro na conversão da data: " + value);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAsString(FacesContext fc, UIComponent ui, Object value) {
/* 40 */     if (value == null || "".equals(value)) {
/* 41 */       return null;
/*    */     }
/* 43 */     if (!(value instanceof java.util.Date)) {
/* 44 */       throw new ConverterException(value + "deve ser do tipo java.util.Date");
/*    */     }
/*    */     
/* 47 */     return df.format(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\converter\DateConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */