package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CarreiraPessoal;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.enums.EnumCarreira;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.CarreiraPessoalService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("carreiraPessoalBean")
@Scope("session")
public class CarreiraPessoalBean implements Serializable {

	static Logger logger = Logger.getLogger(CarreiraPessoalBean.class);
	
	@Autowired
	private CarreiraPessoalService carreiraPessoalService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;

	private HtmlForm form;
	private boolean passouConsultar = false;

	private String cpf = new String();
	private String nome = new String();

	private boolean alterar = false;

	private List<CarreiraPessoal> lista;
	private CarreiraPessoal entidade = new CarreiraPessoal();
	
	private int count;	
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CarreiraPessoal> pagedList = new ArrayList<CarreiraPessoal>();
	private int flagRegistroInicial;
	private Integer pagina = 1;

	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}

	public String prepareAlterar() {
		this.alterar = true;
		return "incluirAlterar";
	}
	
	public String consultar() {

		try {

			limparListas();			
			
			if ( getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione uma pessoa.");

			count =  carreiraPessoalService.count( getEntidade().getPessoal().getId() );

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

	
	public String salvar() {

		try {

			carreiraPessoalService.salvar(entidade);
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());		
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	
	public String excluir() {

		try {

			carreiraPessoalService.excluir(entidade);

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


	public String relatorio() {

		try {

			if ( getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione uma Pessoa.");			

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
		limpar();
		return "listar";
	}
	
	
	public String limpaForm() {
		limpar();
		return "pessoaAnotacaoForm";
	}

	
	private void limpar() {
		setEntidade( new CarreiraPessoal() );
		this.alterar = false;
		this.cpf = new String();
		this.nome = new String();
		this.lista = new ArrayList<CarreiraPessoal>();		
	}


	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				getEntidade().setPessoal( pessoalService.getByCpf( this.cpf ));
				if ( getEntidade().getPessoal() != null ) {
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

	public CarreiraPessoal getEntidade() {return entidade;}
	public void setEntidade(CarreiraPessoal entidade) {this.entidade = entidade;}

	public List<CarreiraPessoal> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {
				
				limparListas();
				
				setCpf(authenticationService.getUsuarioLogado().getCpf());				
				
				count = carreiraPessoalService.count( getEntidade().getPessoal().getId() );				
				
				if (count == 0) {
					FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
					logger.info("Nenhum registro foi encontrado.");
				}	
				
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

	public boolean isAlterar() {return alterar;}	
	
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CarreiraPessoal>();
		pagina = 1;		
	}

	public PagedListDataModel getDataModel() {		
		
		if( flagRegistroInicial != getPrimeiroDaPagina()) {
			
			flagRegistroInicial = getPrimeiroDaPagina();						
				
			setPagedList(carreiraPessoalService.search(getEntidade().getPessoal().getId(), flagRegistroInicial, dataModel.getPageSize()));
			
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}
	public void setDataModel(PagedListDataModel dataModel) {this.dataModel = dataModel;}

	public int getCount() {return count;}
	public void setCount(int count) {this.count = count;}

	public List<CarreiraPessoal> getPagedList() {return pagedList;}
	public void setPagedList(List<CarreiraPessoal> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	
	public List<EnumCarreira> getComboCarreira(){
		return Arrays.asList(EnumCarreira.values());
	}

	public List<Ocupacao> getComboOcupacao() {
		
		List<Ocupacao> cargos = new ArrayList<>();
		
		if (entidade != null && entidade.getPessoal() != null) {
		
			List<Funcional> funcionais = funcionalService.findByPessoal(getEntidade().getPessoal().getId(), "DESC");
			
			for( Funcional funcional: funcionais ) {
				cargos.add(funcional.getOcupacao());
			}
		}
		
		return cargos;
				
	}	

}