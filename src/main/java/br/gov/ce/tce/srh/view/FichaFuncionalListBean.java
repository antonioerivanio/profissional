 package br.gov.ce.tce.srh.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("fichaFuncionalListBean")
@Scope("view")
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

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private Long idFuncional;
	private List<Funcional> lista;
	private Funcional entidade = new Funcional();

	@PostConstruct
	public void init() {
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());			
		}
	}

	public void consultar() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null || getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");			 

			if (lista.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	
	public void relatorio() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			Map<String, Object> parametros = new HashMap<String, Object>();

	        // pegando a foto
	        String imagemPessoa = null;

			// pegando o caminho do arquivo no servidor
	        Parametro parametro = parametroService.getByNome("pathImageSRH");

			try {
				
				if ( entidade.getPessoal().getFoto() != null && !entidade.getPessoal().getFoto().equals("") ) {
					new FileInputStream( parametro.getValor() + entidade.getPessoal().getFoto() );
					imagemPessoa = parametro.getValor() + entidade.getPessoal().getFoto();
				}

			} catch (FileNotFoundException e) {
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
				imagemPessoa = parametro.getValor() + "sem_imagem.jpg";
			}

			String filtro = " where f.matricula = '" + getEntidade().getMatricula() + "' ";
			parametros.put("FILTRO", filtro.toString());
			parametros.put("IMAGEM_PESSOA", imagemPessoa);

			relatorioUtil.relatorio("emitirFichaFuncional.jasper", parametros, "fichaFuncional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}
	
	
	public void relatorioFichaAntiga() {

		try {

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");
			
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

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}
	
	public String limpaTela() {
		setEntidade(new Funcional());
		return "listar";
	}

	public Long getIdFuncional() {return idFuncional;}
	public void setIdFuncional(Long idFuncional) {this.idFuncional = idFuncional;}

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
				List<Funcional> funcionalList = funcionalService.getMatriculaAndNomeByCpfList( this.cpf );				
				if ( funcionalList != null && funcionalList.size() > 0 ) {
					this.lista = funcionalList;
					setEntidade(funcionalList.get(0));
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

}