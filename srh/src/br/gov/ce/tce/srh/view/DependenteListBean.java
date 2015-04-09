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

import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.sca.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("dependenteListBean")
@Scope("session")
public class DependenteListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(DependenteListBean.class);
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private DependenteService dependenteService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	
	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;
	
	// parametros da tela de consulta
	private String cpf = new String();
	private String nome = new String();
	
	// entidades das telas
	private Dependente entidade = new Dependente();
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Dependente> pagedList = new ArrayList<Dependente>();
	private int flagRegistroInicial = 0;
	
	
	public String consultar() {

		try {

			if (getEntidade() == null || getEntidade().getResponsavel() == null)
				throw new SRHRuntimeException("Selecione um servidor. Digite o nome e efetue a pesquisa.");
			
			count = dependenteService.count(entidade.getResponsavel().getId());			
			
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
	
	
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			String filtro;
			
			if (getEntidade() == null || getEntidade().getResponsavel() == null)
				filtro = "";
			else
				filtro = " WHERE dependente.IDPESSOALRESP = " + entidade.getResponsavel().getId();			
			
			parametros.put("FILTRO", filtro.toString());
						
			relatorioUtil.relatorio("dependente.jasper", parametros, "dependentes.pdf");

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
		setEntidade(new Dependente());
		return "listar";
	}
	
	
	public String excluir() {

		try {

			dependenteService.excluir(entidade);			
			
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
	
		
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {
				getEntidade().setResponsavel(pessoalService.getByCpf(this.cpf));
				
				if (getEntidade().getResponsavel() != null) {
					this.nome = getEntidade().getResponsavel().getNomeCompleto();					
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

	public Dependente getEntidade() {return entidade;}
	public void setEntidade(Dependente entidade) {this.entidade = entidade;}
	
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {
				setCpf(authenticationService.getUsuarioLogado().getCpf());
				count = dependenteService.count(entidade.getResponsavel().getId());	
				flagRegistroInicial = -1;				
				
			} catch (Exception e) {
				limparListas();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}			
			
		}else if (!passouConsultar) {
			
			setEntidade(new Dependente());
			
			cpf = new String();
			nome = new String();
			
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	
//	PAGINAÇÃO
	
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Dependente>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			
			setPagedList(dependenteService.search(entidade.getResponsavel().getId(), getDataTable().getFirst(), getDataTable().getRows()));
						
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Dependente> getPagedList() {return pagedList;}
	public void setPagedList(List<Dependente> pagedList) {this.pagedList = pagedList;}

//	FIM PAGINAÇÃO
	
	
	

}