package br.gov.ce.tce.srh.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("LoginBean")
@Scope("session")
public class LoginBean implements Serializable {

	static Logger logger = Logger.getLogger(LoginBean.class);

	@Autowired
	private AuthenticationService authenticationService;

	private String login;
	private String senha;

	private String mensagem = new String();


	public String autenticar() {

		this.mensagem = new String();
		String mensagemLogin = new String();
		
		try {
			
			mensagemLogin = authenticationService.login(login, senha);

		} catch (Exception e) {
	    	this.mensagem = e.getMessage();
			FacesUtil.addErroMessage(e.getMessage() + " Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			return "loginInvalido";
		}

	    if (!mensagemLogin.equalsIgnoreCase("ok")) {
	    	this.login = new String();
	    	this.senha = new String();
	    	this.mensagem = mensagemLogin;
	    	return "loginInvalido";
	    }

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		//Setando atributo logado como true para posterior identificação
		request.getSession().setAttribute("logado", true);
		
		//Setando o nome do usuário na sessão
		request.getSession().setAttribute("usuario", login);
		
		return "home";
	}


	public String logout() {
		authenticationService.logout();
		return "login";
	}

	public String getLoginUsuarioLogado() {

		try {

			return authenticationService.getUsuarioLogado().getUsername();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na identificação do usuario logado. Favor efetuar novo login.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	public Usuario getUsuarioLogado() {

		try {

			return authenticationService.getUsuarioLogado();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na identificação do usuario logado. Favor efetuar novo login.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	
	
	public String getCPFUsuarioLogado() {

		try {

			return authenticationService.getUsuarioLogado().getCpf();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na identificação do usuario logado. Favor efetuar novo login.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public boolean anyGranted(String value) {

		// pegando a lista de grants passado
		String[] listaGrants = value.split(",");

		for (GrantedAuthority permissao : authenticationService.getUsuarioLogado().getAuthorities()) {
			
			for (String grant : listaGrants) {
				if (permissao.getAuthority().trim().equals(grant.trim())) {				 
					return true;				
				}
			}

		}

		return false;
	}


	public String getLogin() {return login;}
	public void setLogin(String login) {this.login = login;}

	public String getSenha() {return senha;}
	public void setSenha(String senha) {this.senha = senha;}

	public String getMensagem() {return mensagem;}
	public void setMensagem(String mensagem) {this.mensagem = mensagem;}

}
