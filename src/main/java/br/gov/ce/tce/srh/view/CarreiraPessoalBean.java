package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CarreiraPessoal;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.Carreira;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CarreiraPessoalService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("carreiraPessoalBean")
@Scope("view")
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
	
	private String cpf = new String();
	private String nome = new String();

	private boolean alterar = false;

	private CarreiraPessoal entidade = new CarreiraPessoal();
	private List<Funcional> funcionais = new ArrayList<>();
	private List<Ocupacao> cargos = new ArrayList<>();
	
	private int count;	
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CarreiraPessoal> pagedList = new ArrayList<CarreiraPessoal>();
	private int flagRegistroInicial;
	private Integer pagina = 1;
	
	@PostConstruct
	private void init() {
		CarreiraPessoal flashParameter = (CarreiraPessoal)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new CarreiraPessoal());
		if(this.entidade.getId() != null) {
			this.nome = entidade.getPessoal().getNomeCompleto();
			this.cpf = entidade.getPessoal().getCpf();
			atualizaFuncionais();
			this.alterar = true;
		}
    }	
	
	
	public void consultar() {

		try {		
			
			if (entidade.getPessoal() == null)			
				count = carreiraPessoalService.count( null );
			else
				count = carreiraPessoalService.count( getEntidade().getPessoal().getId() );

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
	
	public void salvar() {

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
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}
	
	public void excluir() {

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
		setEntidade(new CarreiraPessoal());
		consultar();
	}

	public void relatorio() {

		try {
			
			if (pagedList == null || pagedList.size() == 0)
				throw new SRHRuntimeException("Faça uma consulta antes de gerar o relatório.");
			
			relatorioUtil.relatorio("carreiraPessoal.jasper", new HashMap<String, Object>(), "carreiraPessoal.pdf", pagedList);				

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}	
	
	public String limpaForm() {
		limpar();
		return "carreiraPessoalForm";
	}
	
	private void limpar() {
		
		this.entidade = new CarreiraPessoal();
		this.funcionais = new ArrayList<>();
		this.cargos = new ArrayList<>();
		this.alterar = false;
		this.cpf = new String();
		this.nome = new String();
		
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		this.cpf = SRHUtils.removerMascara(cpf);	
		if ( !this.cpf.equals(cpf) ) {
			
			try {
				
				List<Pessoal> list = pessoalService.findServidorByNomeOuCpf(null, this.cpf);
				
				if (list.isEmpty()) {
					
					this.cpf = new String();
					FacesUtil.addInfoMessage("CPF não encontrado.");
				
				} else {
					
					this.entidade.setPessoal( list.get(0) );
					
					this.nome = getEntidade().getPessoal().getNomeCompleto();
					
					atualizaFuncionais();
					
					this.entidade.setInicioCarreira(funcionais.get(funcionais.size() - 1).getExercicio());
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

	public boolean isAlterar() {return alterar;}	
	
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CarreiraPessoal>();
		pagina = 1;		
	}

	public PagedListDataModel getDataModel() {		
		
		if( flagRegistroInicial != getPrimeiroDaPagina()) {
			
			flagRegistroInicial = getPrimeiroDaPagina();
						
			if (entidade.getPessoal() == null)
				setPagedList(carreiraPessoalService.search(null, null, null));
			else
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
	
	public List<Carreira> getComboCarreira(){ return Arrays.asList(Carreira.values()); }
	
	public List<Funcional> getFuncionais(){	return this.funcionais;	}
		
	private void atualizaFuncionais() {
				
		if (entidade != null && entidade.getPessoal() != null) {		
			this.funcionais = funcionalService.findByPessoal(getEntidade().getPessoal().getId(), "DESC");			
		}		
		atualizaCargos();		
		
	}

	private void atualizaCargos() {
		Set<Ocupacao> cargos = new LinkedHashSet<>();
		
		for (Funcional funcional : funcionais) {
			cargos.add(funcional.getOcupacao());
		}		
		
		this.cargos = new ArrayList<>(); 
		this.cargos.addAll(cargos);
	}	
	
	public List<Ocupacao> getCargos() {
		return cargos;
	}

	public void atualizaInicioCargo() {
				
		if(!alterar) {		
			int index = 0;			
			for (int i = 0; i < funcionais.size(); i++) {				
				Funcional funcional = funcionais.get(i);				
				if(funcional.getOcupacao().getId().intValue() == entidade.getOcupacao().getId().intValue())
					index = i;				
			}			
			entidade.setInicioCargoAtual(funcionais.get(index).getExercicio());
		}
	}
	
	public void atualizaDatasCarreira() {
		
		this.entidade.setInicioCarreira(null);
		this.entidade.setFimCarreira(null);
		
		if(this.entidade.getCarreira() != Carreira.NAO_SE_APLICA && !this.funcionais.isEmpty())
			this.entidade.setInicioCarreira(funcionais.get(funcionais.size() - 1).getExercicio());
		
	}
	
	public boolean isDesabilitaDatasCarreira() {
		if(this.entidade.getCarreira() == Carreira.NAO_SE_APLICA)
			return true;
		return false;
	}

}