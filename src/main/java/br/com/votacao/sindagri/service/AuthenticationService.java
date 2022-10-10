package br.com.votacao.sindagri.service;

import br.com.votacao.sindagri.domain.Usuario;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticationService implements Serializable {
  private static final long serialVersionUID = 9035974727707754521L;
  
  public static final String TOKEN1 = "c4ca4238a0b923820dcc509a6f75849b";
  
  public static final String TOKEN123456 = "e10adc3949ba59abbe56e057f20f883e";
  
  @Autowired
  @Qualifier("authenticationManager")
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private AmbienteService ambienteService;
  
  @Autowired
  private UsuarioService usuarioService;
  
  @Transactional
  public String login(String username, String password) {
    Authentication authenticate;
    UsernamePasswordAuthenticationToken token = null;
    if (this.ambienteService.isAmbienteDesenvolvimento()) {
    	
    	 token = new UsernamePasswordAuthenticationToken(username, password);
     // token = getTokenDevPorNomeUsuario(username, password);
    } else {
      token = new UsernamePasswordAuthenticationToken(username, password);
    } 
    try {
      authenticate = this.authenticationManager.authenticate((Authentication)token);
    } catch (DisabledException e) {
      e.printStackTrace();
      throw new RuntimeException("Usuário desabilitado!");
    } catch (LockedException e) {
      e.printStackTrace();
      throw new RuntimeException("Usuário bloqueado!");
    } catch (CredentialsExpiredException e) {
      e.printStackTrace();
      throw new RuntimeException("Conta expirada!");
    } catch (UsernameNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("Nenhum usuário com esse login foi encontrado!");
    } catch (BadCredentialsException e) {
      e.printStackTrace();
      throw new RuntimeException("Login ou senha inválido");
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Erro ao se autenticar! Se continuar por gentileza entrar encontado com administrador do sistema ");
    } 
    SecurityContextHolder.getContext().setAuthentication(authenticate);
    Usuario usuario = getUsuarioLogado();
    if (!usuario.isEnabled())
      return "Usuario bloqueado!"; 
    if (authenticate.isAuthenticated())
      return "ok"; 
    return null;
  }
  
  private UsernamePasswordAuthenticationToken getTokenDevPorNomeUsuario(String username, String password) {
    if (password == null || password.isEmpty())
      throw new BadCredentialsException("Por gentileza digitar a senha!"); 
    UsernamePasswordAuthenticationToken token = null;
    Usuario usuario = this.usuarioService.findByUsername(username);
    boolean temPermissaoSRH = false;
    if (temPermissaoSRH) {
      if (usuario.getPassword() != null && usuario.getPassword().equals("c4ca4238a0b923820dcc509a6f75849b")) {
        token = new UsernamePasswordAuthenticationToken(username, "c4ca4238a0b923820dcc509a6f75849b");
      } else {
        token = new UsernamePasswordAuthenticationToken(username, "e10adc3949ba59abbe56e057f20f883e");
      } 
    } else {
      throw new BadCredentialsException("Usuntem permissde acesso!");
    } 
    return token;
  }
  
  public void logout() {
    SecurityContextHolder.getContext().setAuthentication(null);
    invalidateSession();
  }
  
  public Usuario getUsuarioLogado() {
    return (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
  
  private void invalidateSession() {
    FacesContext fc = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession)fc.getExternalContext().getSession(false);
    if (session != null)
      session.invalidate(); 
  }
  
  private static String toMd5(String valor) {
    String md5 = "";
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
      BigInteger hashForm = new BigInteger(1, md.digest(valor.getBytes()));
      md5 = hashForm.toString(16);
      if (md5.length() % 2 != 0)
        md5 = "0" + md5; 
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } 
    return md5;
  }
  
  public static void main(String[] args) {
    System.out.println(toMd5("1234"));
  }
}
