package br.com.votacao.sindagri.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionTimeoutCookieFilter implements Filter, Serializable {
  private static final long serialVersionUID = 1L;
  
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
    HttpServletResponse httpResp = (HttpServletResponse)resp;
    HttpServletRequest httpReq = (HttpServletRequest)req;
    long currTime = System.currentTimeMillis();
    HttpSession session = httpReq.getSession(false);
    if (session != null) {
      long expiryTime = currTime + (session.getMaxInactiveInterval() * 1000);
      Cookie cookie = new Cookie("serverTime", ""+currTime);
      cookie.setPath("/");
      httpResp.addCookie(cookie);
      if (httpReq.getRemoteUser() != null) {
        cookie = new Cookie("sessionExpiry", ""+expiryTime);
      } else {
        cookie = new Cookie("sessionExpiry",""+ currTime);
      } 
      cookie.setPath("/");
      httpResp.addCookie(cookie);
      session.setAttribute("dateGetTime", Long.valueOf((new Date()).getTime()));
    } 
    filterChain.doFilter(req, resp);
  }
  
  public void init(FilterConfig filterConfig) throws ServletException {}
  
  public void destroy() {}
}
