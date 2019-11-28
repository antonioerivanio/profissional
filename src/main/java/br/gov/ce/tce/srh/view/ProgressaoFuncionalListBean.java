package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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

@SuppressWarnings("serial")
@Component("progressaoFuncionalListBean")
@Scope("view")
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

	@PostConstruct
	public void init() {		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());
			consultar();
		}
	}
	
	public void consultar() {

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

	public void relatorio() {

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

	}
	
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

}