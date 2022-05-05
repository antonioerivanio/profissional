package br.gov.ce.tce.srh.view;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.domain.VinculoRGPS;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalCedidoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("funcionalCedidoListBean")
@Scope("view")
public class FuncionalCedidoListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(FuncionalCedidoListBean.class);

	@Autowired
	private FuncionalCedidoService funcionalCedidoService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private AuthenticationService authenticationService;

	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();
	

	private List<FuncionalCedido> lista;
	private FuncionalCedido entidade = new FuncionalCedido();
	
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<FuncionalCedido> pagedList = new ArrayList<FuncionalCedido>();
	private int registroInicial = 0;
	private Integer pagina = 1;
	
	
	
	@PostConstruct
	public void init() {		
		FuncionalCedido flashParameter = (FuncionalCedido)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new FuncionalCedido());			
	}

	public void consultar() {

		try {
			
			limparListas();
			Long idPessoal = null;
			if( getEntidade().getFuncional() != null ) {
				idPessoal = getEntidade().getFuncional().getPessoal().getId();
			}
				
			count = funcionalCedidoService.count( idPessoal );

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			registroInicial = -1;
			
			getDataModel();

		} catch(SRHRuntimeException e) {
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
		FacesUtil.setFlashParameter("matricula", matricula);
		FacesUtil.setFlashParameter("pagina", pagina);
        return "incluirAlterar";
	}
	
	public String incluir() {
		FacesUtil.setFlashParameter("matricula", matricula);
		FacesUtil.setFlashParameter("pagina", pagina);
        return "incluirAlterar";
	}
	
	public void excluir() {

		try {

			funcionalCedidoService.excluir(entidade);

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

		consultar();
	}


	public String getMatricula() {return matricula;	}
	public void setMatricula(String matricula) {
		if ( matricula != null && (this.matricula == null || !this.matricula.equals(matricula)) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
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

				getEntidade().setFuncional( funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf ));
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
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public List<FuncionalCedido> getLista() {return lista;}
	public void setLista(List<FuncionalCedido> lista) {this.lista = lista;}

	public FuncionalCedido getEntidade() {return entidade;}
	public void setEntidade(FuncionalCedido entidade) {this.entidade = entidade;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<FuncionalCedido>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if( registroInicial != getPrimeiroDaPagina() ) {
			registroInicial = getPrimeiroDaPagina();
			Long idPessoal = null;
			if( getEntidade().getFuncional() != null ) {
				idPessoal = getEntidade().getFuncional().getPessoal().getId();
			}
			//setPagedList(funcionalCedidoService.search(idPessoal, registroInicial, dataModel.getPageSize()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
 		return dataModel;
	}

	public List<FuncionalCedido> getPagedList() {return pagedList;}
	public void setPagedList(List<FuncionalCedido> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}	
	//FIM PAGINAÇÃO
	
	
	
}