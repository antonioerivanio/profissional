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

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CompetenciaSetorFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CadastroCategoriaFuncionalService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.service.CompetenciaSetorFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
 * Use case : Competencia Setor Funcional
 * 
 * @since   : Dez 12, 2012, 12:12:12 PM
 * @author  : raphael.ferreira@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("competenciaSetorFuncionalListBean")
@Scope("session")
public class CompetenciaSetorFuncionalListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(CompetenciaSetorFuncionalListBean.class);

	@Autowired
	private CompetenciaSetorFuncionalService competenciaSetorFuncionalService;
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private CadastroCategoriaFuncionalService categoriaFuncionalService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;


	//controle de acesso do formulário
	private HtmlForm form;
	private boolean passouConsultar = false;

	//parametos de tela de consulta
	private String tipo = new String();
	private Setor setor;
	private CategoriaFuncional categoriaFuncional;

	private CompetenciaSetorFuncional entidade = new CompetenciaSetorFuncional();

	//entidades das telas
	private List<CompetenciaSetorFuncional> lista;

	// combos
	private List<Setor> comboSetor;
	private List<CategoriaFuncional> comboCategoriaFuncional;
	
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CompetenciaSetorFuncional> pagedList = new ArrayList<CompetenciaSetorFuncional>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {
			
			if( this.setor == null )
				throw new SRHRuntimeException("Selecione um setor.");
			
			count = competenciaSetorFuncionalService.count( setor, tipo, categoriaFuncional );

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
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


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			competenciaSetorFuncionalService.excluir(entidade);

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


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {

			//valida consulta pessoa
			if( count == 0 )
				throw new SRHRuntimeException("Realize uma consulta primeiro.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("FILTRO", setor.getId());
			parametros.put("TIPO", tipo);
			
			if (categoriaFuncional != null) {
				categoriaFuncional = categoriaFuncionalService.getById(categoriaFuncional.getId());
				parametros.put("CATEGORIAFUNCIONAL_ID", categoriaFuncional.getId());
				parametros.put("CATEGORIAFUNCIONAL_DESCRICAO", categoriaFuncional.getDescricao());
			}

			relatorioUtil.relatorio("competenciaSetorFuncional.jasper", parametros, "competenciaSetorFuncional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório das Competências Setor Funcional. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
		tipo = null;
		passouConsultar = false;
		this.setor = null;
		this.comboSetor = null;
		this.comboCategoriaFuncional = null;
		setEntidade(new CompetenciaSetorFuncional());
		lista = new ArrayList<CompetenciaSetorFuncional>();
		limparListas();
		flagRegistroInicial = 0;
		
		return "listar";
	}


	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			limpaTela();		
		}
		return form;
	}

	public List<CompetenciaSetorFuncional> getLista() {return lista;}
	public void setLista(List<CompetenciaSetorFuncional> lista) {this.lista = lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CompetenciaSetorFuncional>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(competenciaSetorFuncionalService.search(setor, tipo, categoriaFuncional, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	/**
	 * Combo Setor
	 * 
	 * @return
	 */
	public List<Setor> getComboSetor() {

        try {

        	if ( this.comboSetor == null )
        		this.comboSetor = setorService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}
	
	/**
	 * Combo Categoria Funcional
	 * 
	 * @return
	 */
	public List<CategoriaFuncional> getComboCategoriaFuncional() {

        try {

        	if ( getSetor() != null && this.comboCategoriaFuncional == null )
        		this.comboCategoriaFuncional = categoriaFuncionalService.findBySetor(getSetor());

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCategoriaFuncional;
	}
	
	/**
	 * Combo Categoria Funcional
	 * 
	 * @return
	 */
	public void carregaCategoriaFuncional() {
		if ( getSetor() != null) {
			setSetor( setorService.getById( getSetor().getId() ));
			this.comboCategoriaFuncional = null;
			getCategoriaFuncional();
		}
	}

	public List<CompetenciaSetorFuncional> getPagedList() {return pagedList;}
	public void setPagedList(List<CompetenciaSetorFuncional> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO


	public boolean isPassouConsultar() {
		return passouConsultar;
	}


	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public CompetenciaSetorFuncional getEntidade() {
		return entidade;
	}


	public void setEntidade(CompetenciaSetorFuncional entidade) {
		this.entidade = entidade;
	}


	public Setor getSetor() {
		return setor;
	}


	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	

	public CategoriaFuncional getCategoriaFuncional() {
		return categoriaFuncional;
	}

	
	public void setCategoriaFuncional(CategoriaFuncional categoriaFuncional) {
		this.categoriaFuncional = categoriaFuncional;
	}


	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}
	
	public void setComboCategoriaFuncional(List<CategoriaFuncional> comboCategoriaFuncional) {
		this.comboCategoriaFuncional = comboCategoriaFuncional;
	}

}