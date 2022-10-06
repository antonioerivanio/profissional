package br.com.votacao.sindagri.view;

import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.service.AuthenticationService;
import br.com.votacao.sindagri.service.UsuarioService;
import br.com.votacao.sindagri.util.FacesUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component("loginBean")
@Scope("session")
public class LoginBean implements Serializable {
  private static final long serialVersionUID = 1L;
  

  static Logger logger = Logger.getLogger(LoginBean.class);
  
  @Autowired
  private AuthenticationService authenticationService;
  
  @Autowired
  private UsuarioService usuarioService;
  
  private String login;
  
  private String senha;
  
  private String confirmarSenha;
  
  private Usuario usuario = new Usuario();
  
  private Boolean isMembroComissao = Boolean.valueOf(false);
  
  private String mensagem = new String();
  
  public String registrar() {
    try {
      if (this.isMembroComissao.booleanValue()) {
        Pessoal pessoa = this.usuarioService.findPessoalByCpf(this.usuario.getCpf());
        if (!pessoa.isMembroComissao())
          throw new Exception("Ops!, Usuario não foi identificado como membro da comissão"); 
        this.usuario.setMembroComissao(this.isMembroComissao.booleanValue());
      } else {
        Pessoal pessoa = this.usuarioService.findPessoalByMatricula(this.usuario.getMatricula());
        if (pessoa == null)
          throw new Exception("Ops!, Usuário não identificado!. Verifique sua matricula"); 
      } 
      gerarSenha();
      this.usuarioService.salvar(this.usuario);
    } catch (Exception e) {
      FacesUtil.addAvisoMessage(e.getMessage());
      e.printStackTrace();
    } 
    this.usuario = new Usuario();
    return null;
  }
  
  private void gerarSenha() {
    UUID uuid = UUID.randomUUID();
    String uuidAsString = uuid.toString();
    Random gerador = new Random();
    this.senha = uuidAsString.substring(0, 6);
    BCryptPasswordEncoder criptografasenha = new BCryptPasswordEncoder();
    String bcryptHashString = criptografasenha.encode(this.senha);
    this.usuario.setPassword(bcryptHashString);
  }
  
  public String autenticar() {
    this.mensagem = new String();
    String mensagemLogin = new String();
    try {
      mensagemLogin = this.authenticationService.login(this.login, this.senha);
    } catch (Exception e) {
      this.mensagem = e.getMessage();
      FacesUtil.addErroMessage(String.valueOf(e.getMessage()) + " Operacancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
      return "loginInvalido";
    } 
    if (!mensagemLogin.equalsIgnoreCase("ok")) {
      this.login = new String();
      this.senha = new String();
      this.mensagem = mensagemLogin;
      return "loginInvalido";
    } 
    HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    request.getSession().setAttribute("logado", Boolean.valueOf(true));
    request.getSession().setAttribute("usuario", this.login);
    return "home";
  }
  
  public String logout() {
    this.authenticationService.logout();
    return "login";
  }
  
  public String getLoginUsuarioLogado() {
    try {
      return this.authenticationService.getUsuarioLogado().getUsername();
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro na identificado usuario logado. Favor efetuar novo login.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
      return null;
    } 
  }
  
  public Usuario getUsuarioLogado() {
    try {
      return this.authenticationService.getUsuarioLogado();
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro na identificado usuario logado. Favor efetuar novo login.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
      return null;
    } 
  }
  
  public String getCPFUsuarioLogado() {
    try {
      return this.authenticationService.getUsuarioLogado().getCpf();
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro na identificado usuario logado. Favor efetuar novo login.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
      return null;
    } 
  }
  
  public boolean anyGranted(String value) {
    String[] listaGrants = value.split(",");
    for (GrantedAuthority permissao : this.authenticationService.getUsuarioLogado().getAuthorities()) {
      byte b;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = listaGrants).length, b = 0; b < i; ) {
        String grant = arrayOfString[b];
        if (permissao.getAuthority().trim().equals(grant.trim()))
          return true; 
        b++;
      } 
    } 
    return false;
  }
  

  
  public void validarConfirmarSenha() {
    if (!this.usuario.getPassword().equals(this.confirmarSenha)) {
      this.confirmarSenha = null;
      FacesUtil.addErroMessage("As senhas deve ser iguais");
      return;
    } 
  }

  
}
