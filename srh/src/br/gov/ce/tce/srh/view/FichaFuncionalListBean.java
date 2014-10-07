 package br.gov.ce.tce.srh.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.service.sca.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
* Use case : SRH_UC039_Emitir Ficha Funcional
* 
* @since   : Dez 19, 2012, 10:09:00 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("fichaFuncionalListBean")
@Scope("session")
public class FichaFuncionalListBean implements Serializable {

	static Logger logger = Logger.getLogger(FichaFuncionalListBean.class);

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private Long idFuncional;
	private List<Funcional> lista;
	private Funcional entidade = new Funcional();



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			//if ( getEntidade() == null )
			if ( getEntidade() == null || getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione um funcion�rio.");

			lista = new ArrayList<Funcional>();
			lista.add(entidade); 

			if (lista.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	@SuppressWarnings("unused")
	public String relatorio() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcion�rio.");

			Map<String, Object> parametros = new HashMap<String, Object>();

	        // pegando a foto
	        String imagemPessoa = null;

			// pegando o caminho do arquivo no servidor
	        Parametro parametro = parametroService.getByNome("pathImageSRH");

			try {

				// testando o carregamento da foto
				InputStream in = null;
				if ( entidade.getPessoal().getFoto() != null && !entidade.getPessoal().getFoto().equals("") ) {
					in = new FileInputStream( parametro.getValor() + entidade.getPessoal().getFoto() );
					imagemPessoa = parametro.getValor() + entidade.getPessoal().getFoto();
				}

			} catch (FileNotFoundException e) {
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
				imagemPessoa = parametro.getValor() + "sem_imagem.jpg";
			}

			String filtro = " where f.matricula = '" + this.matricula + "' ";
			parametros.put("FILTRO", filtro.toString());
			parametros.put("IMAGEM_PESSOA", imagemPessoa);

			relatorioUtil.relatorio("emitirFichaFuncional.jasper", parametros, "fichaFuncional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na gera��o do relat�rio. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	@SuppressWarnings("unused")
	public String relatorioFichaAntiga() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcion�rio.");

			Map<String, Object> parametros = new HashMap<String, Object>();

	        // pegando a foto
	        String imagemFichaAntiga = entidade.getPessoal().getFicha();

			// pegando o caminho do arquivo no servidor
	        Parametro parametro = parametroService.getByNome("pathFichaFuncionalSRH");

			try {

				// testando o carregamento da foto
				imagemFichaAntiga = parametro.getValor() + imagemFichaAntiga;
				InputStream in = new FileInputStream( imagemFichaAntiga );
				byte[] imagemFichaAntigaBytes = IOUtils.toByteArray(in);
				relatorioUtil.openPdf(imagemFichaAntigaBytes, imagemFichaAntiga);

			} catch (FileNotFoundException e) {
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
				imagemFichaAntiga = parametro.getValor() + "sem_imagem.jpg";
			}

			//String filtro = " where f.matricula = '" + this.matricula + "' ";
			//parametros.put("FILTRO", filtro.toString());
			//parametros.put("IMAGEM_PESSOA", imagemFichaAntiga);

			//relatorioUtil.relatorio("emitirFichaFuncional.jasper", parametros, "fichaFuncional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na gera��o do relat�rio. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	public String limpaTela() {
		setEntidade(new Funcional());
		return "listar";
	}


	/**
	 * Gets and Sets
	 */
	public Long getIdFuncional() {return idFuncional;}
	public void setIdFuncional(Long idFuncional) {this.idFuncional = idFuncional;}

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {
				if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
					Funcional funcional = funcionalService.getCpfAndNomeByMatricula(SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf()));
					setEntidade(funcional);
					if(funcional != null){
						this.matricula = funcional.getMatricula();
					} else {
						this.matricula = new String();
						this.cpf = new String();
						this.nome = new String();
					}
				} else {
					setEntidade( funcionalService.getCpfAndNomeByMatricula( this.matricula ));
				}
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();	
				} else {
					FacesUtil.addInfoMessage("Matr�cula n�o encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Opera��o cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {
				
				if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
					this.cpf = authenticationService.getUsuarioLogado().getCpf();
					setEntidade( funcionalService.getMatriculaAndNomeByCpf( SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf()) ));
				} else {
					setEntidade( funcionalService.getMatriculaAndNomeByCpf( this.cpf ));
				}
				
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.matricula = getEntidade().getMatricula();	
				} else {
					FacesUtil.addInfoMessage("CPF n�o encontrado ou inativo.");
				}

				
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Opera��o cancelada.");
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

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new Funcional() );
			matricula = new String();
			cpf = new String();
			nome = new String();
			lista = new ArrayList<Funcional>();
		}
		passouConsultar = false;
		return form;
	}

}