/*    */ package br.com.votacao.sindagri.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ import org.apache.velocity.app.VelocityEngine;
/*    */ import org.apache.velocity.exception.VelocityException;
/*    */ 
/*    */ 
/*    */ public class TemplateEngineFactory
/*    */ {
/*    */   public static VelocityEngine velocityEngine() throws VelocityException, IOException {
/* 12 */     Properties props = new Properties();
/* 13 */     props.put("resource.loader", "class");
/* 14 */     props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
/* 15 */     VelocityEngine velocityEngine = new VelocityEngine(props);
/* 16 */     return velocityEngine;
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\TemplateEngineFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */