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

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AreaSetorCompetenciaService;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("areaSetorCompetenciaListBean")
@Scope("view")
public class AreaSetorCompetenciaListBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaSetorCompetenciaListBean.class);

	@Autowired
	private AreaSetorCompetenciaService areaSetorCompetenciaService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private AreaSetorService areaSetorService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// entidades das telas
	private List<AreaSetorCompetencia> lista;
	private AreaSetorCompetencia entidade;

	private Setor setor;
	private AreaSetor areaSetor;
	private Competencia competencia;

	// combos
	private List<Setor> comboSetor;
	private List<AreaSetor> comboArea;
	private List<Competencia> comboCompetencia;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AreaSetorCompetencia> pagedList = new ArrayList<AreaSetorCompetencia>();
	private int flagRegistroInicial = 0;

	public void consultar() {

		try {

			// validando campos da consulta
			if( this.setor == null )
				throw new SRHRuntimeException("Selecione um setor.");
			if( this.areaSetor == null )
				throw new SRHRuntimeException("Selecione uma área.");

			// realizando consulta
			if( this.competencia == null) {
				count  = areaSetorCompetenciaService.count( this.areaSetor.getId() );
			} else {
				count  = areaSetorCompetenciaService.count( this.areaSetor.getId(), this.competencia.getId() );
			}
	
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

			areaSetorCompetenciaService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new AreaSetorCompetencia();
		consultar();
	}

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

	public void carregaArea() {
		lista = new ArrayList<AreaSetorCompetencia>();
		this.areaSetor = new AreaSetor();
		this.comboArea = null;
	}

	public List<AreaSetor> getComboArea() {

		try {

			if( this.setor != null && this.comboArea == null ) {
				this.comboArea = areaSetorService.findBySetor( this.setor.getId() );
			}

			return this.comboArea;

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo área. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public void carregaCompetencia() {
		lista = new ArrayList<AreaSetorCompetencia>();
		this.competencia = new Competencia();
		this.comboCompetencia = null;
	}

	public List<Competencia> getComboCompetencia() {

		try {

			if (getAreaSetor() != null && this.comboCompetencia == null) {
				this.comboCompetencia = new ArrayList<Competencia>();
				List<AreaSetorCompetencia> listaAreaSetorComp = areaSetorCompetenciaService.findByArea(getAreaSetor().getId());
				for (AreaSetorCompetencia areaSetorComp : listaAreaSetorComp) {
					this.comboCompetencia.add(areaSetorComp.getCompetencia());
				}
			}

			return this.comboCompetencia;

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo competência. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return null;
	}
 
	public void relatorio() {

		try {

			// validando campos da consulta
			if (setor == null)
				throw new SRHRuntimeException("Selecione o campo setor.");
			if (areaSetor == null)
				throw new SRHRuntimeException("Selecione o campo área.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE IDAREASETOR = " + this.areaSetor.getId() );

			if (this.competencia != null)
				filtro.append( " AND IDCOMPETENCIA = " + this.competencia.getId() );

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("areasetorcompetencia.jasper", parametros, "areasetorcompetencia.pdf");
			
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public AreaSetorCompetencia getEntidade() {return entidade;}
	public void setEntidade(AreaSetorCompetencia entidade) {this.entidade = entidade;}

	public List<AreaSetorCompetencia> getLista() {return lista;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public AreaSetor getAreaSetor() {return areaSetor;}
	public void setAreaSetor(AreaSetor areaSetor) {this.areaSetor = areaSetor;}

	public Competencia getCompetencia() {return competencia;}
	public void setCompetencia(Competencia competencia) {this.competencia = competencia;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AreaSetorCompetencia>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();

			// realizando consulta
			if (competencia == null) {
				setPagedList(areaSetorCompetenciaService.search( this.areaSetor.getId(), getDataTable().getFirst(), getDataTable().getRows()) );
			} else {
				setPagedList(areaSetorCompetenciaService.search( this.areaSetor.getId(), this.competencia.getId(), getDataTable().getFirst(), getDataTable().getRows()) );
			}

			if(count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
			
		}
		return dataModel;
	}

	public List<AreaSetorCompetencia> getPagedList() {return pagedList;}
	public void setPagedList(List<AreaSetorCompetencia> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}