package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
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

@SuppressWarnings("serial")
@Component("pessoalCursoGraduacaoListBean")
@Scope("view")
public class PessoalCursoGraduacaoListBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoListBean.class);

	@Autowired
	private PessoalCursoAcademicaService pessoalCursoAcademicaService;

	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	// parametros da tela de consulta
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private List<PessoalCursoAcademica> lista;
	private PessoalCursoAcademica entidade = new PessoalCursoAcademica();

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<PessoalCursoAcademica> pagedList = new ArrayList<PessoalCursoAcademica>();
	private int flagRegistroInicial = 0;

	public void consultar() {

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

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}
	
	public void excluir() {

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

		consultar();
	}
	
	public void relatorio() {

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
	}
	
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

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<PessoalCursoAcademica>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

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