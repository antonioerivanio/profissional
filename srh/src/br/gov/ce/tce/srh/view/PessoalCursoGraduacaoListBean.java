package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.PessoalCursoAcademicaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC024_Manter Graduação da Pessoa
* 
* @since   : Nov 15, 2011, 10:03:00 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("pessoalCursoGraduacaoListBean")
@Scope("session")
public class PessoalCursoGraduacaoListBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoListBean.class);

	@Autowired
	private PessoalCursoAcademicaService pessoalCursoAcademicaService;

	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private List<PessoalCursoAcademica> lista;
	private PessoalCursoAcademica entidade = new PessoalCursoAcademica();

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<PessoalCursoAcademica> pagedList = new ArrayList<PessoalCursoAcademica>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			if ( getEntidade().getPessoal() == null)
				throw new SRHRuntimeException("Selecione um funcionário.");			
			
			count = pessoalCursoAcademicaService.count( getEntidade().getPessoal().getId() );			

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
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			pessoalCursoAcademicaService.excluir(entidade);

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
			
			if ( getEntidade().getPessoal() == null)
				throw new SRHRuntimeException("Selecione uma pessoa.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("FILTRO", " WHERE pessoalcurso.IDPESSOAL = " + getEntidade().getPessoal().getId());
			
			relatorioUtil.relatorio("pessoalCursoGraduacao.jasper", parametros, "pessoalCursoGraduacao.pdf");

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
		setEntidade(new PessoalCursoAcademica());
		return "listar";
	}


	/**
	 * Gets and Sets
	 */
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				getEntidade().setPessoal( pessoalService.getByCpf( cpf ) );
				
				if (getEntidade().getPessoal() != null) {
					this.nome = getEntidade().getPessoal().getNomeCompleto();
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

	public PessoalCursoAcademica getEntidade() {return entidade;}
	public void setEntidade(PessoalCursoAcademica entidade) {this.entidade = entidade;}

	public List<PessoalCursoAcademica> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new PessoalCursoAcademica() );
			this.cpf = new String();
			this.nome = new String();
			this.lista = new ArrayList<PessoalCursoAcademica>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<PessoalCursoAcademica>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			
			setPagedList(pessoalCursoAcademicaService.search(getEntidade().getPessoal().getId(), getDataTable().getFirst(), getDataTable().getRows()));
			
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<PessoalCursoAcademica> getPagedList() {return pagedList;}
	public void setPagedList(List<PessoalCursoAcademica> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}