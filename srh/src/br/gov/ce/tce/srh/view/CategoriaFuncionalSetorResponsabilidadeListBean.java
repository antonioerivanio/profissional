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

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetorResponsabilidade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorResponsabilidadeService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("categoriaFuncionalSetorResponsabilidadeListBean")
@Scope("session")
public class CategoriaFuncionalSetorResponsabilidadeListBean implements Serializable {

static Logger logger = Logger.getLogger(AtribuicaoSetorListBean.class);
	
	@Autowired
	private CategoriaFuncionalSetorResponsabilidadeService categoriaFuncionalSetorResponsabilidadeService;
	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;	
	@Autowired
	private SetorService setorService;

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// entidades das telas
	private CategoriaFuncionalSetorResponsabilidade entidade;
	
	// combos
	private List<Setor> comboSetor;
	private boolean setoresAtivos = true;
	private Setor setor;
	
	
	// paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CategoriaFuncionalSetorResponsabilidade> pagedList = new ArrayList<CategoriaFuncionalSetorResponsabilidade>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;
		
	
	public String consultar() {

		try {		
			
			limparListas();

			if( setor == null )
				throw new SRHRuntimeException("Selecione um setor.");
			
			count = categoriaFuncionalSetorResponsabilidadeService.count(setor);
	
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

			categoriaFuncionalSetorResponsabilidadeService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new CategoriaFuncionalSetorResponsabilidade();
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
	
	public CategoriaFuncionalSetorResponsabilidade getEntidade() {return entidade;}
	public void setEntidade(CategoriaFuncionalSetorResponsabilidade entidade) {this.entidade = entidade;}
		
	public boolean isPassouConsultar() {return passouConsultar;}
	public void setPassouConsultar(boolean passouConsultar) {this.passouConsultar = passouConsultar;}
	
	public boolean isSetoresAtivos() {return this.setoresAtivos;}
	public void setSetoresAtivos(boolean setoresAtivos) {this.setoresAtivos = setoresAtivos;}
	
	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

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
		setEntidade( new CategoriaFuncionalSetorResponsabilidade() );
		comboSetor = null;
		setoresAtivos = true;
		setor = null;
	}
	
	public String limpaTela() {
		setEntidade(new CategoriaFuncionalSetorResponsabilidade());		
		return "listar";
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CategoriaFuncionalSetorResponsabilidade>();
		pagina = 1;	
	}


	public PagedListDataModel getDataModel() {
		
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			
			flagRegistroInicial = getPrimeiroDaPagina();
			
			setPagedList(categoriaFuncionalSetorResponsabilidadeService.search(setor, flagRegistroInicial, dataModel.getPageSize()));
			
			if(count != 0){			
				dataModel = new PagedListDataModel(getPagedList(), count);			
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<CategoriaFuncionalSetorResponsabilidade> getPagedList() {return pagedList;}
	public void setPagedList(List<CategoriaFuncionalSetorResponsabilidade> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	
	//FIM PAGINAÇÃO
	
}
