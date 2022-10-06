/*    */ package br.com.votacao.sindagri.util;
/*    */ 
/*    */ import java.util.Set;
/*    */ import javax.faces.bean.ApplicationScoped;
/*    */ import javax.faces.bean.RequestScoped;
/*    */ import javax.faces.bean.SessionScoped;
/*    */ import javax.faces.bean.ViewScoped;
/*    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
/*    */ import org.springframework.context.annotation.ScopeMetadata;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScopeMetadataResolver
/*    */   extends AnnotationScopeMetadataResolver
/*    */ {
/*    */   public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
/* 74 */     ScopeMetadata metadata = new ScopeMetadata();
/* 75 */     if (definition instanceof AnnotatedBeanDefinition) {
/* 76 */       AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition)definition;
/* 77 */       Set<String> annotationTypes = annDef.getMetadata().getAnnotationTypes();
/*    */       
/* 79 */       if (annotationTypes.contains(RequestScoped.class.getName())) {
/* 80 */         metadata.setScopeName("request");
/* 81 */       } else if (annotationTypes.contains(SessionScoped.class.getName())) {
/* 82 */         metadata.setScopeName("session");
/* 83 */       } else if (annotationTypes.contains(ApplicationScoped.class.getName())) {
/* 84 */         metadata.setScopeName("application");
/* 85 */       } else if (annotationTypes.contains(ViewScoped.class.getName())) {
/* 86 */         metadata.setScopeName("view");
/*    */       } else {
/*    */         
/* 89 */         return super.resolveScopeMetadata(definition);
/*    */       } 
/*    */     } 
/* 92 */     return metadata;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\ScopeMetadataResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */