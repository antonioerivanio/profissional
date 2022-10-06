/*    */ package br.com.votacao.sindagri.converter;
/*    */ 
/*    */ import br.com.votacao.sindagri.domain.BasicEntity;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import javax.faces.component.UIComponent;
/*    */ import javax.faces.context.FacesContext;
/*    */ import javax.faces.convert.Converter;
/*    */ 
/*    */ public class EntityConverter
/*    */   implements Converter
/*    */ {
/*    */   public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
/* 13 */     if (value == null || value.trim().length() == 0) {
/* 14 */       return null;
/*    */     }
/* 16 */     if (value.matches("id:.+")) {
/*    */ 
/*    */       
/* 19 */       Class<?> clazz = component.getValueExpression("value").getType(facesContext.getELContext());
/* 20 */       BasicEntity entidade = null;
/*    */       try {
/* 22 */         entidade = (BasicEntity)clazz.newInstance();
/* 23 */         String id = value.substring(3);
/* 24 */         Class<?> tipoId = (Class)((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
/* 25 */         if (tipoId.equals(String.class)) {
/* 26 */           entidade.setId(id);
/* 27 */         } else if (tipoId.equals(Long.class)) {
/* 28 */           entidade.setId(new Long(id));
/*    */         } 
/* 30 */         return entidade;
/* 31 */       } catch (Exception e) {
/* 32 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
/* 41 */     if (object == null)
/* 42 */       return null; 
/* 43 */     if (object instanceof BasicEntity) {
/* 44 */       BasicEntity<?> entity = (BasicEntity)object;
/*    */       
/* 46 */       return "id:" + entity.getId();
/*    */     } 
/* 48 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagri\converter\EntityConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */