/*    */ package br.com.votacao.sindagri.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.faces.context.FacesContext;
/*    */ import org.springframework.beans.factory.ObjectFactory;
/*    */ import org.springframework.beans.factory.config.Scope;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViewScope
/*    */   implements Scope
/*    */ {
/*    */   public static final String SCOPE_NAME = "view";
/*    */   
/*    */   public Object get(String name, ObjectFactory<?> objectFactory) {
/* 16 */     Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
/*    */     
/* 18 */     if (viewMap.containsKey(name)) {
/* 19 */       return viewMap.get(name);
/*    */     }
/* 21 */     Object object = objectFactory.getObject();
/* 22 */     viewMap.put(name, object);
/*    */     
/* 24 */     return object;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object remove(String name) {
/* 30 */     return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConversationId() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerDestructionCallback(String name, Runnable callback) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveContextualObject(String key) {
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\ViewScope.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */