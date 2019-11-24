package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ClasseReferenciaService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("classeReferenciaListBean")
@Scope("view")
public class ClasseReferenciaListBean implements Serializable {

	static Logger logger = Logger.getLogger(ClasseReferenciaListBean.class);

	@Autowired
	private ClasseReferenciaService classeReferenciaService;

	@Autowired
	private OcupacaoService ocupacaoService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametros da tela de consulta
	private Ocupacao cargo;
	private List<ClasseReferencia> lista;
	private ClasseReferencia entidade = new ClasseReferencia();

	// combos
	private List<Ocupacao> comboCargo;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<ClasseReferencia> pagedList = new ArrayList<ClasseReferencia>();
	private int flagRegistroInicial = 0;

	/*
	 * @PostConstruct public void init() { cargo = null; this.comboCargo = null;
	 * this.lista = new ArrayList<ClasseReferencia>(); limparListas();
	 * flagRegistroInicial = 0; }
	 * 
	 */
	public void consultar() {

		try {
			
			if ( this.cargo == null )
				throw new SRHRuntimeException("Selecione um cargo.");

			count = classeReferenciaService.count(this.cargo.getId());
	
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

	public void excluir() {

		try {

			classeReferenciaService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new ClasseReferencia();
		consultar();
	}

	
	public List<Ocupacao> getComboCargo() {

		try {

			if ( this.comboCargo == null ) {
				this.comboCargo = new ArrayList<Ocupacao>();
				for (Ocupacao entidade : ocupacaoService.findAll()) {
					// somente os que nao forem cargo isolados
			        if (!entidade.isCargoIsolado()) {
			        	this.comboCargo.add(entidade);
			        }
				}	
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo cargo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCargo;
	}

	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.cargo != null && this.cargo.getId() != 0) {
				String filtro = " WHERE ocupacao.id = " + this.cargo.getId();
				parametros.put("FILTRO", filtro.toString());
			}

			relatorioUtil.relatorio("classereferencia.jasper", parametros, "classereferencia.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public String limpaTela() {
		limparListas();
		return "listar";
	}

	public Ocupacao getCargo() {return cargo;}
	public void setCargo(Ocupacao cargo) {this.cargo = cargo;}
	
	public ClasseReferencia getEntidade() {return entidade;}
	public void setEntidade(ClasseReferencia entidade) {this.entidade = entidade;}

	public List<ClasseReferencia> getLista() {return lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<ClasseReferencia>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(classeReferenciaService.search(this.cargo.getId(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<ClasseReferencia> getPagedList() {return pagedList;}
	public void setPagedList(List<ClasseReferencia> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
}