/*    */ package br.com.votacao.sindagri.util;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.web.context.ServletContextAware;
/*    */ 
/*    */ @Component
/*    */ public class ServletContextUtil
/*    */   implements ServletContextAware {
/*    */   private String serverRootUrl;
/*    */   
/*    */   public String getServerRootUrl() {
/* 13 */     return this.serverRootUrl;
/*    */   }
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 17 */     this.serverRootUrl = servletContext.getRealPath("/");
/*    */   }
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\ServletContextUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */