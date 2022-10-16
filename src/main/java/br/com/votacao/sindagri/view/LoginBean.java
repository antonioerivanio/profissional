package br.com.votacao.sindagri.view;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.email.EmailService;
import br.com.votacao.sindagri.email.Mail;
import br.com.votacao.sindagri.email.MailBuilder;
import br.com.votacao.sindagri.service.AuthenticationService;
import br.com.votacao.sindagri.service.UsuarioService;
import br.com.votacao.sindagri.util.FacesUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	@Autowired
	private EmailService emailService;

	@Value("${mail.to}")
	private final String mailTo;

	// private EMailController eMailController;

	private String login;

	private String senha;

	private String confirmarSenha;

	private Usuario usuario = new Usuario();

	private Boolean isMembroComissao = Boolean.valueOf(false);
	PrimeFaces current = PrimeFaces.current();

	private String mensagem = new String();

	/*
	 * { LocalDateTime dataHoraAgora = LocalDateTime.now(); LocalDateTime
	 * dataHoraEspecifica = LocalDateTime.of(2022, 10, 15, 20, 30, 50);
	 * LocalDateTime dataHoraEspecificaDoTexto =
	 * LocalDateTime.parse("2017-12-25T20:30:50");
	 * 
	 * boolean isEqual = dataHoraAgora.isEqual(dataHoraEspecifica); // false20 if
	 * (isEqual) { current.executeScript("PF('dlgVotacaoEncerrada').show();"); }
	 * 
	 * }
	 */

	public String registrar() {
		//sendTestReport();
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
			usuario.setSenhaCriada(this.senha);			
			this.usuarioService.salvar(this.usuario);
			sendTestReport(usuario);
			
			FacesUtil.addInfoMessage("Parabéns! Usuário registrado com sucesso!\n Sua SENHA foi enviada para o email informado.");
			current.executeScript("PF('dlgSenhaGerada').show();");
		} catch (Exception e) {
			FacesUtil.addErroMessage(e.getMessage());
			e.printStackTrace();
		}
		this.usuario = new Usuario();

		return null;
	}

	
	public void sendTestReport(Usuario usuario) {
		final Mail mail = new MailBuilder().From("appvotacaosindagri@antonio11566.c41.integrator.host") // For gmail, this field is ignored.
				.To(this.mailTo).Template("mail-template.html").AddContext("subject", "Esta é sua senha de acesso ao aplicativo de votação do Sindagri")
				.AddContext("content",usuario.getSenhaCriada()).Subject("Aplicativo Votação Sindagri").createMail();
		// String responseMessage = request.getRequestURI();
		try {
			this.emailService.sendHTMLEmail(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public String autenticar() {
		this.mensagem = new String();
		String mensagemLogin = new String();
		try {

			mensagemLogin = this.authenticationService.login(this.login, getHashBySenha(this.senha));
		} catch (Exception e) {
			this.mensagem = e.getMessage();
			FacesUtil.addErroMessage(String.valueOf(e.getMessage()));
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
			return "/paginas/votacao/votacao.xhtml";
		} else {
			return "/paginas/votacao/resultado.xhtml";
		}
	}

	public String logout() {
		this.authenticationService.logout();

		/*
		 * HttpServletRequest request = (HttpServletRequest)
		 * FacesContext.getCurrentInstance().getExternalContext() .getRequest();
		 * 
		 * ExternalContext externalContext =
		 * FacesContext.getCurrentInstance().getExternalContext(); try {
		 * externalContext.redirect(request.getContextPath() + "/logout.jsp"); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		return "/login.xhtml?faces-redirect=true";
	}

	public String sair() {
		this.authenticationService.logout();

		/*
		 * HttpServletRequest request = (HttpServletRequest)
		 * FacesContext.getCurrentInstance().getExternalContext() .getRequest();
		 * 
		 * ExternalContext externalContext =
		 * FacesContext.getCurrentInstance().getExternalContext(); try {
		 * externalContext.redirect(request.getContextPath() + "/logout.jsp"); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		return "/votofinalizado.xhtml?faces-redirect=true";
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

	public LoginBean(EmailService emailService, @Value("${mail.to}") String mailTo) {
		this.emailService = emailService;
		this.mailTo = mailTo;
	}

	public LoginBean() {
		this.emailService = null;
		this.mailTo = "";

	}

}
