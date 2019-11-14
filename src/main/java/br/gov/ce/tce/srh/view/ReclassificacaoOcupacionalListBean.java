 package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

/**
* Use case : SRH_UC042_Manter Reclassificação Ocupacional do Servidor
* 
* @since   : Fev 09, 2012, 10:00:00
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("reclassificacaoOcupacionalListBean")
@Scope("session")
public class ReclassificacaoOcupacionalListBean implements Serializable {

	/**
	 * Robson, estou fazendo alterações na exibição de relatório, a questão é que estava com muito codigo "desnecessário"
	 * , pois havia muitas linhas de comando repetida, segue a nova estrutura que irei comentar la em baixo ok!
	 * 
	 * 
	 * Wesllhey Holanda
	 * */
	
	static Logger logger = Logger.getLogger(ReclassificacaoOcupacionalListBean.class);



	@Autowired
	private FuncionalService funcionalService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();
	
	// entidades das telas
	private List<Funcional> lista;
	private Funcional entidade;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Funcional> pagedList = new ArrayList<Funcional>();
	private int flagRegistroInicial = 0;


	public String consultar() {

		try {
			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			count = funcionalService.count(entidade.getPessoal().getId(), "DESC");

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
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}

	public String limpaTela() {
		setEntidade(new Funcional());
		return "listar";
	}

	public String excluir() {

		try {

			funcionalService.excluir(entidade);

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

		return consultar();
	}
	

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				setEntidade( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula )); 
				if ( getEntidade() != null) {
					this.nome = getEntidade().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				setEntidade( funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf ));
				if ( getEntidade() != null) {
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

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( null );
			matricula = new String();
			cpf = new String();
			nome = new String();
			lista = new ArrayList<Funcional>();
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
		pagedList = new ArrayList<Funcional>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(funcionalService.searchForReclassificacao(entidade.getPessoal().getId(), "DESC", getDataTable().getFirst(), getDataTable().getRows()));
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
	//FIM PAGINAÇÃO

}