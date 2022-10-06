/*    */ package br.com.votacao.sindagri.converter;
/*    */ 
/*    */ import br.com.votacao.sindagri.util.SRHUtils;
/*    */ import java.text.ParseException;
/*    */ import javax.faces.component.UIComponent;
/*    */ import javax.faces.context.FacesContext;
/*    */ import javax.faces.convert.Converter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CPFConverter
/*    */   implements Converter
/*    */ {
/*    */   public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
/* 19 */     return arg2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
/* 25 */     if (arg2 == null) {
/* 26 */       return "";
/*    */     }
/*    */     
/*    */     try {
/* 30 */       return SRHUtils.formatarCPF(SRHUtils.removerMascara(arg2.toString()));
/* 31 */     } catch (ParseException e) {
/* 32 */       e.printStackTrace();
/*    */ 
/*    */       
/* 35 */       return "";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\converter\CPFConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */