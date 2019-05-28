package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Aposentadoria;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AposentadoriaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("aposentadoriaListBean")
@Scope("session")
public class AposentadoriaListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(AposentadoriaListBean.class);

	@Autowired
	private AposentadoriaService aposentadoriaService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	
	private HtmlForm form;
	private boolean passouConsultar = false;

	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();
	
	private Aposentadoria entidade = new Aposentadoria();
	
	private List<Aposentadoria> aposentadoriaList = new ArrayList<Aposentadoria>();
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Aposentadoria> pagedList = new ArrayList<Aposentadoria>();
	private int registroInicial = 0;
	private Integer pagina = 1;


	public String consultar() {

		try {
			
			limparListas();
						
			if( getEntidade().getFuncional() == null ) 
				aposentadoriaList = aposentadoriaService.search(null, -1, 0);
			else
				aposentadoriaList = aposentadoriaService.search(getEntidade().getFuncional().getPessoal().getId(), -1, 0);

			count = aposentadoriaList.size();
			
			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			registroInicial = -1;
			
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

	
	public String excluir() {

		try {

			aposentadoriaService.excluir(entidade);			

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

		return "listar";
	}


	public String relatorio() {

		try {
			
			if (aposentadoriaList == null || aposentadoriaList.size() == 0)
				throw new SRHRuntimeException("Faça uma consulta antes de gerar o relatório.");
			
			relatorioUtil.relatorio("aposentadoria.jasper", new HashMap<String, Object>(), "aposentadoria.pdf", aposentadoriaList);				

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
		setEntidade(new Aposentadoria());
		return "listar";
	}
	
	public String getMatricula() {return matricula;	}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatricula( this.matricula ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getPessoal().getNomeCompleto();
					this.cpf = getEntidade().getFuncional().getPessoal().getCpf();	
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {return cpf;	}
	public void setCpf(String cpf) {
		if (!cpf.equals("") && !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				getEntidade().setFuncional( funcionalService.getMatriculaAndNomeByCpf( this.cpf ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getPessoal().getNomeCompleto();
					this.matricula = getEntidade().getFuncional().getMatricula();
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if (!passouConsultar) {
			limparAtributos();
			limparListas();
			registroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}


	private void limparAtributos() {
		setEntidade( new Aposentadoria() );
		matricula = new String();
		nome = new String();
		cpf = new String();		
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public Aposentadoria getEntidade() {return entidade;}
	public void setEntidade(Aposentadoria entidade) {this.entidade = entidade;}
	
	//PAGINAÇÃO
	private void limparListas() {
		aposentadoriaList = new ArrayList<Aposentadoria>();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Aposentadoria>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if( registroInicial != getPrimeiroDaPagina() ) {
			registroInicial = getPrimeiroDaPagina();
						
			Long idPessoal = null;
			
			if(entidade.getFuncional() != null) {
				idPessoal = getEntidade().getFuncional().getPessoal().getId();
			}			
			
			setPagedList(aposentadoriaService.search(idPessoal, registroInicial, dataModel.getPageSize()));			
			
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Aposentadoria> getPagedList() {return pagedList;}
	public void setPagedList(List<Aposentadoria> pagedList) {this.pagedList = pagedList;}
	
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