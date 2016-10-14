package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.RelatorioFeriasService;
import br.gov.ce.tce.srh.service.TipoFeriasService;
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
@Component("relatorioFeriasListBean")
@Scope("session")
public class RelatorioFeriasListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(RelatorioFeriasListBean.class);

	@Autowired
	private RelatorioFeriasService relatorioFeriasService;
	
	@Autowired
	private SetorService setorService;

	@Autowired
	private TipoFeriasService tipoFeriasService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;


	//controle de acesso do formulário
	private HtmlForm form;

	//parametos de tela de consulta
	private List<String> tiposFerias;
	private Setor setor;
	private Date inicio;
	private Date fim;

	//entidades das telas
	private List<RelatorioFerias> lista;

	// combos
	private List<Setor> comboSetor;
	private List<TipoFerias> comboTipoFerias;
	
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RelatorioFerias> pagedList = new ArrayList<RelatorioFerias>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {
			
			count = relatorioFeriasService.getCountFindByParameter(setor, tiposFerias, inicio, fim);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			
		} catch(SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
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
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String inicioAux = null;
			String fimAux = null;
			String tiposFeriasAux = "";
			
			if (inicio != null){
				inicioAux = dateFormat.format(inicio);
			}
			
			if (fim != null){
				fimAux = dateFormat.format(fim);
			}
			
			StringBuilder paramWhere = new StringBuilder("WHERE 1=1 "); 

			if (setor!=null && setor.getId() != null && setor.getId() != 0L){
				paramWhere.append("and tb_funcional.idsetor = '" + setor.getId() + "' " );
			}
			
			if (tiposFerias != null && !tiposFerias.isEmpty()){
				boolean primeiro = true;
				String temp = "";
				temp += "and TB_FERIAS.TIPOFERIAS in (";
				for (String string : tiposFerias) {
					if(primeiro){
						primeiro = false;
					}else{
						tiposFeriasAux += ", ";
					}
					tiposFeriasAux += string;
				}
				temp += tiposFeriasAux + ") ";
				paramWhere.append(temp);
			}
			
			if (inicio != null) {
				paramWhere.append("and TB_FERIAS.INICIO >= to_date('" + inicioAux +"', 'dd/MM/yyyy') ");					
			}
			
			if (fim != null) {
				paramWhere.append("and TB_FERIAS.INICIO <= to_date('" + fimAux +"', 'dd/MM/yyyy') ");					
			}			
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("IDSETORFILTRO", setor == null ? null : setor.getId());
			parametros.put("SETORFILTRO", setor == null ? null : setor.getNome());
			parametros.put("TIPOSFERIASFILTRO", tiposFerias.isEmpty() ? null : tiposFeriasAux);
			parametros.put("INICIOFILTRO",  inicioAux);
			parametros.put("FIMFILTRO", fimAux);
			parametros.put("PARAMWHERE", paramWhere.toString());
					

			relatorioUtil.relatorio("feriasSetor.jasper", parametros, "feriasSetor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório das Ferias por Setor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public void limpaTela() {
		tiposFerias = null;
		setor = null;
		inicio = null;
		fim = null;
		lista = new ArrayList<RelatorioFerias>();
		limparListas();
		flagRegistroInicial = 0;

	}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {return form;}

	public List<RelatorioFerias> getLista() {return lista;}
	public void setLista(List<RelatorioFerias> lista) {this.lista = lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RelatorioFerias>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(relatorioFeriasService.findByParameter(setor, tiposFerias, inicio, fim, getDataTable().getFirst(), getDataTable().getRows()));
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
        		this.comboSetor = setorService.findTodosAtivos();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}

	public List<RelatorioFerias> getPagedList() {return pagedList;}
	public void setPagedList(List<RelatorioFerias> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO


	public Setor getSetor() {
		return setor;
	}


	public void setSetor(Setor setor) {
		if (setor != null && setor.getId() != null)
			this.setor = setorService.getById(setor.getId());
		else
			this.setor = setor;
	}


	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}

	public List<String> getTiposFerias() {
		return tiposFerias;
	}

	public void setTiposFerias(List<String> tiposFerias) {
		this.tiposFerias = tiposFerias;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public List<TipoFerias> getComboTipoFerias() {
		if(this.comboTipoFerias == null){
			try{
				this.comboTipoFerias = tipoFeriasService.findAll();
			} catch (Exception e) {
	        	FacesUtil.addErroMessage("Erro ao carregar o campo Tipo Ferias. Operação cancelada.");
	        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
		return comboTipoFerias;
	}

	public void setComboTipoFerias(List<TipoFerias> comboTipoFerias) {
		this.comboTipoFerias = comboTipoFerias;
	}

}