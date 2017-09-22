package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC024_Manter Nomeação do Servidor
* 
* @since   : Dez 05, 2011, 10:03:00 AM
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("nomeacaoServidorListBean")
@Scope("session")
public class NomeacaoServidorListBean implements Serializable {

	static Logger logger = Logger.getLogger(NomeacaoServidorListBean.class);

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;
	private Boolean exibirTodosOsCampos = true;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private List<Funcional> lista;
	private Funcional entidade;

	//paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Funcional> pagedList = new ArrayList<Funcional>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;


	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {
			
			limparListas();

			// validando campos da entidade
			if ( getEntidade() == null || getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			count = funcionalService.count(getEntidade().getPessoal().getId(), "DESC");
	
			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			exibirTodosOsCampos = false;
			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}


	/**
	 * Limpar dados do formulario
	 * 
	 * @return
	 */
	public String limpar() {

		setEntidade( null );
		matricula = new String();
		cpf = new String();
		nome = new String();
		lista = new ArrayList<Funcional>();
		exibirTodosOsCampos = false;

		return "listar";
	}


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			funcionalService.excluir(entidade);
			limpar();
			limparListas();

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
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

			Map<String, Object> parametros = new HashMap<String, Object>();

			String filtro = " WHERE F.IDPESSOAL = " + this.entidade.getPessoal().getId();
			parametros.put("FILTRO", filtro.toString());			

			relatorioUtil.relatorio("nomeacaoServidor.jasper", parametros, "nomeacaoServidor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório de Nomeação de Servidor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}


		return null;
	}
	

	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				setEntidade( funcionalService.getCpfAndNomeByMatricula(matricula) ); 
				if (getEntidade() != null) {
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

				setEntidade( funcionalService.getMatriculaAndNomeByCpf( this.cpf ) );
				if (getEntidade() != null) {
					this.nome = getEntidade().getNomeCompleto();
					if (getEntidade().getMatricula() != null)
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

	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public List<Funcional> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {
				setCpf(authenticationService.getUsuarioLogado().getCpf());								
				count = funcionalService.count(getEntidade().getPessoal().getId(), "DESC");
				limparListas();
				flagRegistroInicial = -1;				
				
			} catch (Exception e) {
				limparListas();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}			
			
		}else if (!passouConsultar) {
			setEntidade( null );
			matricula = new String();
			cpf = new String();
			nome = new String();
			lista = new ArrayList<Funcional>();
			exibirTodosOsCampos = true;
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}


	public Boolean getExibirTodosOsCampos() {return exibirTodosOsCampos;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Funcional>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			flagRegistroInicial = getPrimeiroDaPagina();
			setPagedList(funcionalService.search(getEntidade().getPessoal().getId(), "DESC", flagRegistroInicial, dataModel.getPageSize()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Funcional> getPagedList() {return pagedList;}
	public void setPagedList(List<Funcional> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	
	//FIM PAGINAÇÃO


	public boolean isPassouConsultar() {
		return passouConsultar;
	}


	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}

}