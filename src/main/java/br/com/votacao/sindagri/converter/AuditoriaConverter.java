/*    */ package br.com.votacao.sindagri.converter;
/*    */ 
/*    */ import br.com.votacao.sindagri.domain.BasicEntity;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import javax.faces.component.UIComponent;
/*    */ import javax.faces.context.FacesContext;
/*    */ import javax.faces.convert.Converter;
/*    */ 
/*    */ public class AuditoriaConverter
/*    */   implements Converter
/*    */ {
/*    */   public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
/* 13 */     if (value == null || value.trim().length() == 0) {
/* 14 */       return null;
/*    */     }
/* 16 */     if (value.matches("id:.+")) {
/*    */       
/* 18 */       Class<?> clazz = component.getValueExpression("value").getType(facesContext.getELContext());
/* 19 */       BasicEntity entidade = null;
/*    */       try {
/* 21 */         entidade = (BasicEntity)clazz.newInstance();
/* 22 */         String id = value.substring(3);
/* 23 */         Class<?> tipoId = (Class)((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
/* 24 */         if (tipoId.equals(String.class)) {
/* 25 */           entidade.setId(id);
/* 26 */         } else if (tipoId.equals(Long.class)) {
/* 27 */           entidade.setId(new Long(id));
/*    */         } 
/* 29 */         return entidade;
/* 30 */       } catch (Exception e) {
/* 31 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
/* 40 */     if (object == null)
/* 41 */       return null; 
/* 42 */     if (object instanceof BasicEntity) {
/* 43 */       BasicEntity<?> entity = (BasicEntity)object;
/*    */       
/* 45 */       return "id:" + entity.getId();
/*    */     } 
/* 47 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\converter\AuditoriaConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */