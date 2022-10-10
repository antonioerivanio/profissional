package br.com.votacao.sindagri.view;

import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.service.AuthenticationService;
import br.com.votacao.sindagri.service.UsuarioService;
import br.com.votacao.sindagri.util.FacesUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

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

	/*
	 * @Autowired public PasswordEncoder passwordEncoder() { return
	 * PasswordEncoderFactories.createDelegatingPasswordEncoder(); }
	 */

	public String registrar() {
		Pessoal pessoa = null;

		try {

			if (this.isMembroComissao.booleanValue()) {
				pessoa = this.usuarioService.findPessoalByCpf(this.usuario.getCpf());
				if (!pessoa.isMembroComissao())
					throw new Exception("Ops!, Usuario não foi identificado como membro da comissão");
				this.usuario.setMembroComissao(this.isMembroComissao.booleanValue());
			} else {
				pessoa = this.usuarioService.findPessoalByMatricula(this.usuario.getMatricula());
				if (pessoa == null)
					throw new Exception("Ops!, Usuário não identificado!. Verifique sua matricula");
			}

			usuario.setCpf(pessoa.getCpf());
			usuario.setNome(pessoa.getNome());
			usuario.setEnabled(true);

			gerarSenha();

			this.usuarioService.salvar(this.usuario);

			FacesUtil.addInfoMessage("Parabéns! Usuário registrado com sucesso!");
		} catch (Exception e) {
			FacesUtil.addErroMessage(e.getMessage());
			e.printStackTrace();
		}
		this.usuario = new Usuario();
		return null;
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		// Random gerador = new Random();
		this.senha = uuidAsString.substring(0, 6);

		String bcryptHashString = getHashBySenha(this.senha);
		this.usuario.setPassword(bcryptHashString);
	}

	private String getHashBySenha(String senha) {
		// String pw_hash = BCrypt.hashpw(senha, BCrypt.gensalt());
		return DigestUtils.md5DigestAsHex(senha.toString().getBytes());
		// String bcryptHashString = encoder().encode(senha.trim());
		// return pw_hash;
	}

	private boolean compararHashSenha(String senha, String s) {
		String encode = DigestUtils.md5DigestAsHex(senha.toString().getBytes());
		boolean res = s.equals(encode);

		return res;
	}

	public String autenticar() {
		this.mensagem = new String();
		String mensagemLogin = new String();
		try {
			// boolean t = compararHashSenha(this.senha,
			// "8fdd53583c7337ed520e6bab88bd4e97");

			mensagemLogin = this.authenticationService.login(this.login, getHashBySenha(this.senha));
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
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		request.getSession().setAttribute("logado", Boolean.valueOf(true));
		request.getSession().setAttribute("usuario", this.login);
		
		if (anyGranted("ROLE_USUARIO")) {
			return "/paginas/votacao/votacao.xhtml?faces-redirect=true";
		} else {
			return "/paginas/votacao/resultado.xhtml?faces-redirect=true";
		}
	}

	public String logout() {
		this.authenticationService.logout();
		return "loout.jsp?faces-redirect=true";
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
		boolean resultado = false;
		String[] listaGrants = value.split(",");
		for (GrantedAuthority permissao : this.authenticationService.getUsuarioLogado().getAuthorities()) {
			byte b;
			int i;
			String[] arrayOfString;
			for (i = (arrayOfString = listaGrants).length, b = 0; b < i;) {
				String grant = arrayOfString[b];
				if (permissao.getAuthority().trim().equals(grant.trim())) {
					resultado = true;
					break;
				}
				b++;
			}
		}
		return resultado;
	}

	public void validarConfirmarSenha() {
		if (!this.usuario.getPassword().equals(this.confirmarSenha)) {
			this.confirmarSenha = null;
			FacesUtil.addErroMessage("As senhas deve ser iguais");
			return;
		}
	}

}
