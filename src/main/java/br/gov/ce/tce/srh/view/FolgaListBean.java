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

import br.gov.ce.tce.srh.domain.Folga;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FolgaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("folgaListBean")
@Scope("view")
public class FolgaListBean implements Serializable {

	static Logger logger = Logger.getLogger(FolgaListBean.class);
	
	@Autowired
	private FolgaService folgaService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private AuthenticationService authenticationService;

	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	private List<Folga> lista;
	private Folga entidade = new Folga();
	
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Folga> pagedList = new ArrayList<Folga>();
	private int registroInicial = 0;
	private Integer pagina = 1;
	
	private Double saldoTotal;
	
	@PostConstruct
	public void init() {		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());
			consultarAutomaticamente();
		} else {
			setMatricula((String) FacesUtil.getFlashParameter("matricula"));
			pagina = (Integer) FacesUtil.getFlashParameter("pagina");
			pagina = pagina == null ? 1 : pagina;
			
			if (getMatricula() != null) {
				consultarAutomaticamente();
			}
		}		
	}
	
	public void consultar() {

		try {
			
			limparListas();

			if( getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			count = folgaService.count(getEntidade().getPessoal().getId());

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			} else {
				saldoTotal = folgaService.saldoTotal(this.entidade.getPessoal().getId());
			}

			registroInicial = -1;

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
	
	private void consultarAutomaticamente() {
		try {		
			count = folgaService.count(this.entidade.getPessoal().getId());
			saldoTotal = folgaService.saldoTotal(this.entidade.getPessoal().getId());
			registroInicial = -1;
		} catch (Exception e) {}
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

			folgaService.excluir(entidade);

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

				getEntidade().setPessoal(funcionalService.getCpfAndNomeByMatriculaAtiva(this.matricula).getPessoal());
				if ( getEntidade().getPessoal() != null ) {
					this.nome = getEntidade().getPessoal().getNomeCompleto();
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

	public String getCpf() {return cpf;	}
	public void setCpf(String cpf) {
		if (!cpf.equals("") && !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				Funcional funcional = funcionalService.getMatriculaAndNomeByCpfAtiva(this.cpf);
				getEntidade().setPessoal(funcional.getPessoal());
				if ( getEntidade().getPessoal() != null ) {
					this.nome = getEntidade().getPessoal().getNomeCompleto();
					this.matricula = funcional.getMatricula();
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

	public List<Folga> getLista() {return lista;}
	public void setLista(List<Folga> lista) {this.lista = lista;}

	public Folga getEntidade() {return entidade;}
	public void setEntidade(Folga entidade) {this.entidade = entidade;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Folga>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if( registroInicial != getPrimeiroDaPagina() ) {
			registroInicial = getPrimeiroDaPagina();
			setPagedList(folgaService.search(getEntidade().getPessoal().getId(), registroInicial, dataModel.getPageSize()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Folga> getPagedList() {return pagedList;}
	public void setPagedList(List<Folga> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}	
	//FIM PAGINAÇÃO

	public Double getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(Double saldoTotal) {
		this.saldoTotal = saldoTotal;
	}	
	
}
