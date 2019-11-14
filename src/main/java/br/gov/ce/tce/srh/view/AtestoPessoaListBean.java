package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AtestoPessoaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC026_Manter Competência do Servidor
* 
* @since   : Jan 12, 2012, 11:53:50 PM
* @author  : joel.barbosa@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("atestoPessoaListBean")
@Scope("session")
public class AtestoPessoaListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(AtestoPessoaListBean.class);
	
	@Autowired
	private AtestoPessoaService atestoPessoaService;
	
	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private String matricula = new String();
	private String nome = new String();
	private String cpf = new String();

	//entidades de tela
	private List<AtestoPessoa> lista = new ArrayList<AtestoPessoa>();
	private AtestoPessoa entidade = new AtestoPessoa();

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AtestoPessoa> pagedList = new ArrayList<AtestoPessoa>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */	
	public String consultar() {

		try {

			if (getEntidade().getPessoal() == null) 
				throw new SRHRuntimeException("Selecione um funcionário.");

			count = atestoPessoaService.count( getEntidade().getPessoal().getId() );

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch(SRHRuntimeException e) {
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
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {
		
		try {

			atestoPessoaService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");
			
		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return consultar();
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {

			if (getEntidade().getPessoal() == null) 
				throw new SRHRuntimeException("Selecione um funcionário.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			String filtro = " AND ap.IDPESSOA = " + this.entidade.getPessoal().getId();
			parametros.put("FILTRO", filtro);

			relatorioUtil.relatorio("atestoPessoa.jasper", parametros, "atestoPessoa.pdf");

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
		setEntidade(new AtestoPessoa());
		return "listar";
	}

	
	/**
	 * Gets and Sets
	 */	
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula );
				if ( funcional != null ) {
					getEntidade().setPessoal( funcional.getPessoal() );
					this.cpf = getEntidade().getPessoal().getCpf();
					this.nome = getEntidade().getPessoal().getNomeCompleto();
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

				Funcional funcional = funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf );
				if ( funcional != null ) {
					getEntidade().setPessoal( funcional.getPessoal() );
					this.matricula = funcional.getMatricula();
					this.nome = getEntidade().getPessoal().getNomeCompleto();
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

	public AtestoPessoa getEntidade() {return entidade;}
	public void setEntidade(AtestoPessoa entidade) {this.entidade = entidade;}
	
	public List<AtestoPessoa> getLista() {return lista;}
	public void setLista(List<AtestoPessoa> lista) {this.lista = lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new AtestoPessoa() );
			this.matricula = new String();
			this.nome = new String();
			this.cpf = new String();
			lista = new ArrayList<AtestoPessoa>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AtestoPessoa>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(atestoPessoaService.search(getEntidade().getPessoal().getId(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AtestoPessoa> getPagedList() {return pagedList;}
	public void setPagedList(List<AtestoPessoa> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}