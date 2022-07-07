package br.gov.ce.tce.srh.sca.service;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.sca.domain.Usuario;

@Component
public class AuthenticationService implements Serializable{

	private static final long serialVersionUID = 9035974727707754521L;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	
	@Transactional
	public String login(String username, String password) {

			// autenticando spring security
			//UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			
		    //DESENVOLVIMENTO
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "e10adc3949ba59abbe56e057f20f883e"); //"c4ca4238a0b923820dcc509a6f75849b"


			Authentication authenticate;
			
			
			try {
				 authenticate = authenticationManager.authenticate(token);
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
				throw new RuntimeException("Login ou senha inválidos!");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Erro ao logar!");
			}			
			
			SecurityContextHolder.getContext().setAuthentication(authenticate);
			
			// pergando o usuario
			Usuario usuario = (Usuario) getUsuarioLogado();
			
			// verificando se o usuario esta ativo
			if (!usuario.isEnabled()) {
				return "Usuário não está ativo!";
			}
			
			// Verificando se a senha esta expirada
			if (!usuario.isAccountNonExpired()) {
				return "Conta expirada!";
			}

			// verificando se o usuario tem alguma permissao
			if (usuario.getAuthorities().size() == 0) {
				return "Usuário não tem permissão de acesso!";
			}
			
			// verificando se o usuario esta bloqueado
			if (!usuario.isAccountNonLocked()) {
				return "Usuário bloqueado!";
			}
			
			// verificando se a senha expirou
			if (!usuario.isCredentialsNonExpired()) {
				return "Senha expirada!";
			}

			// verificando autenticacao
			if (authenticate.isAuthenticated()) {
				return "ok";
			}
		
		return null;
	}


	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		invalidateSession();
	}


	public Usuario getUsuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private void invalidateSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		if (session != null)
			session.invalidate();
	}


	/**
	 * Encripta valores com hash md5
	 * 
	 * @param String valor
	 * 
	 * @return String md5
	 */
	private static String toMd5(String valor){
		String md5 = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");			
			BigInteger hashForm = new BigInteger(1, md.digest(valor.getBytes()));
			md5 = hashForm.toString(16);	
			  if (md5.length() %2 != 0) 
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
