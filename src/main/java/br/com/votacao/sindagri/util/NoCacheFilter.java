/*    */ package br.com.votacao.sindagri.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.annotation.WebFilter;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ @WebFilter(servletNames = {"Faces Servlet"})
/*    */ public class NoCacheFilter
/*    */   implements Filter
/*    */ {
/*    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
/* 21 */     HttpServletRequest req = (HttpServletRequest)request;
/* 22 */     HttpServletResponse res = (HttpServletResponse)response;
/*    */     
/* 24 */     if (!req.getRequestURI().startsWith(String.valueOf(req.getContextPath()) + "/javax.faces.resource")) {
/* 25 */       res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
/* 26 */       res.setHeader("Pragma", "no-cache");
/* 27 */       res.setDateHeader("Expires", 0L);
/*    */     } 
/*    */     
/* 30 */     chain.doFilter(request, response);
/*    */   }
/*    */   
/*    */   public void init(FilterConfig filterConfig) throws ServletException {}
/*    */   
/*    */   public void destroy() {}
/*    */ }


/* Location:              C:\Users\erivanio\Desktop\sindagri\WEB-INF\classes\!\br\com\votacao\sindagr\\util\NoCacheFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */