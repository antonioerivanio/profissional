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
/*    */ public class HoraConverter
/*    */   implements Converter
/*    */ {
/*    */   public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
/* 15 */     return arg2.replace(":", "");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
/* 21 */     if (arg2 == null) {
/* 22 */       return "";
/*    */     }
/*    */     
/*    */     try {
/* 26 */       return SRHUtils.formatarHora(SRHUtils.removerMascara(arg2.toString()));
/* 27 */     } catch (ParseException e) {
/* 28 */       e.printStackTrace();
/*    */ 
/*    */       
/* 31 */       return "";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\converter\HoraConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */