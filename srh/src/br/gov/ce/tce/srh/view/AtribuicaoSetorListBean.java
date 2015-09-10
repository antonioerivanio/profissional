package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AtribuicaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AtribuicaoSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("atribuicaoSetorListBean")
@Scope("session")
public class AtribuicaoSetorListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(AtribuicaoSetorListBean.class);
	
	@Autowired
	private AtribuicaoSetorService atribuicaoSetorService;
	
	@Autowired
	private SetorService setorService;

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// entidades das telas
	private AtribuicaoSetor entidade;
	
	// combos
	private List<Setor> comboSetor;
	private boolean setoresAtivos = true;
	private int opcaoAtiva = 0;
	
	// paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AtribuicaoSetor> pagedList = new ArrayList<AtribuicaoSetor>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;
		
	
	public String consultar() {

		try {
			
			limparListas();

			if( entidade == null || entidade.getSetor() == null )
				throw new SRHRuntimeException("Selecione um setor.");
			
			count = atribuicaoSetorService.count(entidade.getSetor(), opcaoAtiva);
	
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
	
	public String excluir() {

		try {

			atribuicaoSetorService.excluir(entidade);

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
	
	public List<Setor> getComboSetor() {

        try {

        	if ( this.setoresAtivos )
        		this.comboSetor = setorService.findTodosAtivos();
        	else
        		this.comboSetor = setorService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}
	
	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}	
	
	public AtribuicaoSetor getEntidade() {return entidade;}
	public void setEntidade(AtribuicaoSetor entidade) {this.entidade = entidade;}
		
	public boolean isPassouConsultar() {return passouConsultar;}
	public void setPassouConsultar(boolean passouConsultar) {this.passouConsultar = passouConsultar;}
	
	public boolean isSetoresAtivos() {return this.setoresAtivos;}
	public void setSetoresAtivos(boolean setoresAtivos) {this.setoresAtivos = setoresAtivos;}	

	public int getOpcaoAtiva() {return opcaoAtiva;}
	public void setOpcaoAtiva(int opcaoAtiva) {this.opcaoAtiva = opcaoAtiva;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {			
			limpar();
			limparListas();
			flagRegistroInicial = 0;
		}				
		passouConsultar = false;
		return form;
	}
	
	private void limpar() {
		setEntidade( new AtribuicaoSetor() );
		comboSetor = null;
		setoresAtivos = true;
		opcaoAtiva = 0;
	}
	
	public String limpaTela() {
		setEntidade(new AtribuicaoSetor());		
		return "listar";
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AtribuicaoSetor>();
		pagina = 1;	
	}


	public PagedListDataModel getDataModel() {
		
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			
			flagRegistroInicial = getPrimeiroDaPagina();
			
			setPagedList(atribuicaoSetorService.search(entidade.getSetor(), opcaoAtiva, flagRegistroInicial, dataModel.getPageSize()));
			
			if(count != 0){			
				dataModel = new PagedListDataModel(getPagedList(), count);			
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AtribuicaoSetor> getPagedList() {return pagedList;}
	public void setPagedList(List<AtribuicaoSetor> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	
	//FIM PAGINAÇÃO
	
}
