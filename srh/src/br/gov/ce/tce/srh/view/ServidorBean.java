package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.richfaces.component.html.HtmlDatascroller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Servidor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ServidorService;
import br.gov.ce.tce.srh.service.sapjava.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_Consulta de Servidor
* 
* @since   : Jan 24, 2013, 11:36:55 AM
* @author  : rpf1404@gmail.com
*/
@Component("servidorBean")
@Scope("session")
public class ServidorBean  implements Serializable  {


	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(ServidorBean.class);
	/**
	 * Atributos para paginação
	 */
	@Autowired
	private ServidorService servidorService;
	
	@Autowired
	private SetorService setorService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	// controle de acesso do formulario
	private HtmlForm form;
	
	// entidades das telas
	private List<Servidor> lista;	
	private Setor setor;
	private Integer vinculo; 
	
	// combos
	private List<Setor> comboSetor;
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private HtmlDatascroller dataScroller = new HtmlDatascroller();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Servidor> pagedList = new ArrayList<Servidor>();
	private int flagRegistroInicial = 0;
	private boolean passouConsultar = false;
	
	
	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			setCount(servidorService.getCountServidoresPorSetor( this.setor, this.vinculo ));

			if (getCount() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			dataTable.setFirst(0);
			dataScroller.setPage(0);
			
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
	
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			count = 0;
			setor = null;
			comboSetor = null;			
			lista = new ArrayList<Servidor>();
			limparListas();
			vinculo = null;
			flagRegistroInicial = 0;			
		}
		passouConsultar = false;
		return form;
	}
	
	
	/**
	 * Limpar dados do formulario
	 * 
	 * @return
	 */
	public String limpar() {
		passouConsultar = false;
		return "listar";
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataScroller = new HtmlDatascroller();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Servidor>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public HtmlDatascroller getDataScroller() {return dataScroller;}
	public void setDataScroller(HtmlDatascroller dataScroller) {this.dataScroller = dataScroller;}
	
	public PagedListDataModel getDataModel() throws Exception {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(servidorService.consultarServidoresPorSetor(this.setor, this.vinculo, getDataTable().getFirst(), getDataTable().getRows()));
			if(getCount() != 0){
				dataModel = new PagedListDataModel(getPagedList(), getCount());
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
						
			if(this.setor != null){
				filtro.append("AND S.IDSETOR = " + this.setor.getId() + " ");
			}
			
			if(vinculo == 1){ // MEMBROS
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND TOC.ID = 1 ");
				filtro.append("ORDER BY O.ORDEMOCUPACAO, S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");
			}else if(vinculo == 2){ // SERVIDORES ATIVOS
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND (TOC.ID = 2 OR TOC.ID = 3) ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, F.IDFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
			}else if(vinculo == 3){ // SERVIDORES INATIVOS
				filtro.append("AND F.STATUS = 5 ");
				filtro.append("ORDER BY O.ORDEMOCUPACAO, F.NOMECOMPLETO");
			}else if(vinculo == 4){ // OCUPANTES DE CARGO COMICIONADO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND RF.ID IS NOT NULL ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
			}else if(vinculo == 5){ // OCUPANTES SOMENTE CARGO COMICIONADO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND RF.ID IS NOT NULL ");
				filtro.append("AND TOC.ID = 6 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
			}else if(vinculo == 6){ // ESTAGIÁRIOS NÍVEL UNIVERSITÁRIO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 2 ");
				filtro.append("AND TOC.ID = 5 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");
			}else if(vinculo == 7){ // ESTAGIÁRIOS NÍVEL MÉDIO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 2 ");
				filtro.append("AND TOC.ID = 4 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");
			}else if(vinculo == 8){ // CESSÃO DE SERVIDOR SEM NENHUMA REMUNERAÇÃO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND TOC.ID = 8 ");
				filtro.append("ORDER BY F.NOMECOMPLETO");
			}else{
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS < 3 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
			}
			
			parametros.put("FILTRO", filtro.toString());
			parametros.put("VINCULO", vinculo);
				
			if(vinculo == 1 || vinculo >= 6)
				relatorioUtil.relatorio("servidorSetorAlt1.jasper", parametros, "servidorSetor.pdf");
			else if(vinculo == 3)
				relatorioUtil.relatorio("servidorSetorAlt2.jasper", parametros, "servidorSetor.pdf");
			else if(vinculo == 5)
				relatorioUtil.relatorio("servidorSetorAlt3.jasper", parametros, "servidorSetor.pdf");
			else
				relatorioUtil.relatorio("servidorSetor.jasper", parametros, "servidorSetor.pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public List<Servidor> getPagedList() {return pagedList;}
	public void setPagedList(List<Servidor> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

	public List<Servidor> getLista() {return lista;}
	public void setLista(List<Servidor> lista) {this.lista = lista;}

	public boolean isPassouConsultar() {return passouConsultar;}

	public void setPassouConsultar(boolean passouConsultar) {this.passouConsultar = passouConsultar;}

	public void setDataModel(PagedListDataModel dataModel) {this.dataModel = dataModel;}

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
	public void setComboSetor(List<Setor> comboSetor) {this.comboSetor = comboSetor;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public Integer getVinculo() {return vinculo;}
	public void setVinculo(Integer vinculo) {this.vinculo = vinculo;}

	public int getCount() {return count;}
	public void setCount(int count) {this.count = count;}

}
