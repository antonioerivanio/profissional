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
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ReferenciaFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC031_Manter Progressão Funcional do Servidor
* 
* @since   : Jan 17, 2012, 18:22:00
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("progressaoFuncionalListBean")
@Scope("session")
public class ProgressaoFuncionalListBean implements Serializable {

	static Logger logger = Logger.getLogger(ProgressaoFuncionalListBean.class);

	@Autowired
	private ReferenciaFuncionalService referenciaFuncionalService;
	
	@Autowired
	private FuncionalService funcionalService;

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
	private List<ReferenciaFuncional> lista;
	private ReferenciaFuncional entidade = new ReferenciaFuncional();

	//paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<ReferenciaFuncional> pagedList = new ArrayList<ReferenciaFuncional>();
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
			if ( getEntidade().getFuncional() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			count = referenciaFuncionalService.count(getEntidade().getFuncional().getPessoal().getId());
	
			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			
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
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {
			
			// validando campos da entidade
			if ( getEntidade().getFuncional() == null || getEntidade().getFuncional().getPessoal() == null)
				throw new SRHRuntimeException("Selecione uma pessoa.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("FILTRO", entidade.getFuncional().getPessoal().getId().toString());
			
			relatorioUtil.relatorio("progressaoFuncional.jasper", parametros, "progressaoFuncional.pdf");

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
		setEntidade(new ReferenciaFuncional());
		return "listar";
	}
	
	/**
	 * Limpar form
	 */
	private void limpar() {
		setEntidade( new ReferenciaFuncional() );
		matricula = new String();
		cpf = new String();
		nome = new String();
		lista = new ArrayList<ReferenciaFuncional>();
	}

	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatricula( this.matricula )); 
				if ( getEntidade().getFuncional() != null) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
					this.cpf = getEntidade().getFuncional().getPessoal().getCpf();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada.");
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

				getEntidade().setFuncional( funcionalService.getMatriculaAndNomeByCpf( this.cpf ));
				if ( getEntidade().getFuncional() != null) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
					this.matricula = getEntidade().getFuncional().getMatricula();
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public ReferenciaFuncional getEntidade() {return entidade;}
	public void setEntidade(ReferenciaFuncional entidade) {this.entidade = entidade;}

	public List<ReferenciaFuncional> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {				
				setCpf(authenticationService.getUsuarioLogado().getCpf());								
				count = referenciaFuncionalService.count(getEntidade().getFuncional().getPessoal().getId());
				limparListas();
				flagRegistroInicial = -1;				
				
			} catch (Exception e) {
				limparListas();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}			
			
		}else if (!passouConsultar) {
			limpar();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<ReferenciaFuncional>();
		pagina = 1;	
	}


	public PagedListDataModel getDataModel() {
		
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			
			flagRegistroInicial = getPrimeiroDaPagina();
			
			setPagedList(referenciaFuncionalService.search(getEntidade().getFuncional().getPessoal().getId(), flagRegistroInicial, dataModel.getPageSize()));
			
			if(count != 0){			
				dataModel = new PagedListDataModel(getPagedList(), count);			
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<ReferenciaFuncional> getPagedList() {return pagedList;}
	public void setPagedList(List<ReferenciaFuncional> pagedList) {this.pagedList = pagedList;}
	
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