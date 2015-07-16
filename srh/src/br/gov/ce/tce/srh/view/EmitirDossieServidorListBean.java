 package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;
/**
* Use case :
* 
* @since   : Dez 19, 2011, 17:09:00 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("emitirDossieServidorListBean")
@Scope("session")
public class EmitirDossieServidorListBean implements Serializable {

	static Logger logger = Logger.getLogger(EmitirDossieServidorListBean.class);
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	private boolean dadosPessoais = false;
	private boolean historicoLotacao = false;
	private boolean historicoLicenca = false;
	private boolean historicoFerias = false;
	private boolean progracaoFuncional = false;
	private boolean representacaoFuncional = false;
	private boolean anotacaoServidor = false;
	private boolean marcaTodos = false;


	// entidades das telas
	private List<Funcional> lista;
	private Funcional entidade;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			lista = new ArrayList<Funcional>();
			lista.add( getEntidade() ); 

			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}



	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("CPF", this.cpf);
			parametros.put("IDFUNCIONAL", entidade.getId().toString() );
			parametros.put("IDPESSOAL", entidade.getPessoal().getId().toString() );

			if ( progracaoFuncional == true )
				parametros.put("PROGRACAOFUNCIONAL", servletContext.getRealPath("//WEB-INF/relatorios/emitirDossiseServidor_progressaoFuncionalSub.jasper") );
			
			if ( representacaoFuncional == true )
				parametros.put("REPRESENTACAOFUNCIONAL", servletContext.getRealPath("//WEB-INF/relatorios/emitirDossiseServidor_representacaoFuncionalSub.jasper") );

			if ( historicoLotacao == true )
				parametros.put("HISTORICOLOTACAO",  servletContext.getRealPath("//WEB-INF/relatorios/emitirDossiseServidor_historicolotacaoSub.jasper") );

			if ( historicoLicenca == true )
				parametros.put("HISTORICOLICENCA",  servletContext.getRealPath("//WEB-INF/relatorios/emitirDossiseServidor_historicoLicencaSub.jasper") );

			if ( historicoFerias == true )
				parametros.put("HISTORICOFERIAS",  servletContext.getRealPath("//WEB-INF/relatorios/emitirDossiseServidor_historicoFeriasSub.jasper") );
			
			if ( anotacaoServidor == true )
				parametros.put("ANOTACAOSERVIDOR",  servletContext.getRealPath("//WEB-INF/relatorios/emitirDossiseServidor_anotacaoServidor.jasper") );

			relatorioUtil.relatorio("emitirDossiseServidor.jasper", parametros, "HistoricoFuncionalServidor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
		setEntidade(new Funcional());
		return "emitirDossieServidorList";
	}

	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {
				setEntidade( funcionalService.getCpfAndNomeByMatricula( this.matricula ));
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}		
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {				
				setEntidade( funcionalService.getMatriculaAndNomeByCpf( this.cpf ));				
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.matricula = getEntidade().getMatricula();		
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public List<Funcional> getLista() {return lista;}
	public void setLista(List<Funcional> lista) {this.lista = lista;}

	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public boolean isDadosPessoais() {return dadosPessoais;}
	public void setDadosPessoais(boolean dadosPessoais) {this.dadosPessoais = dadosPessoais;}

	public boolean isHistoricoLotacao() {return historicoLotacao;}
	public void setHistoricoLotacao(boolean historicoLotacao) {this.historicoLotacao = historicoLotacao;}

	public boolean isHistoricoLicenca() {return historicoLicenca;}
	public boolean getPassouConsultar() {
		return passouConsultar;
	}

	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}

	public void setHistoricoLicenca(boolean historicoLicenca) {this.historicoLicenca = historicoLicenca;}

	public boolean isHistoricoFerias() {return historicoFerias;}
	public void setHistoricoFerias(boolean historicoFerias) {this.historicoFerias = historicoFerias;}

	public boolean isProgracaoFuncional() {return progracaoFuncional;}
	public void setProgracaoFuncional(boolean progracaoFuncional) {this.progracaoFuncional = progracaoFuncional;}
	
	public boolean isRepresentacaoFuncional() {return representacaoFuncional;}
	public void setRepresentacaoFuncional(boolean representacaoFuncional) {this.representacaoFuncional = representacaoFuncional;}

	public boolean isAnotacaoServidor() {return anotacaoServidor;}
	public void setAnotacaoServidor(boolean anotacaoServidor) {this.anotacaoServidor = anotacaoServidor;}
	
	public boolean isMarcaTodos() {return marcaTodos;}
	public void setMarcaTodos(boolean marcaTodos) {
		this.marcaTodos = marcaTodos;		
		if(marcaTodos){
			this.dadosPessoais = true;
			this.historicoLotacao = true;
			this.historicoLicenca = true;
			this.historicoFerias = true;
			this.progracaoFuncional = true;
			this.representacaoFuncional = true;
			this.anotacaoServidor = true;
		}else{
			this.dadosPessoais = false;
			this.historicoLotacao = false;
			this.historicoLicenca = false;
			this.historicoFerias = false;
			this.progracaoFuncional = false;
			this.representacaoFuncional = false;
			this.anotacaoServidor = false;			
		}
	}


	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());
			lista = new ArrayList<Funcional>();
			lista.add(entidade); 
		} else if (!passouConsultar) {
			setEntidade( null );
			matricula = new String();
			cpf = new String();
			nome = new String();
			lista = new ArrayList<Funcional>();
			setMarcaTodos(false);
		}
		passouConsultar = false;
		return form;
	}

}