package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.enums.EnumTipoCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaProfissionalService;
import br.gov.ce.tce.srh.service.CursoProfissionalService;
import br.gov.ce.tce.srh.service.InstituicaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC044_Manter Curso de Formação Profissional
* 
* @since   : Out 11, 2011, 17:11:20 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("cursoProfissionalBean")
@Scope("session")
public class CursoProfissionalBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoProfissionalBean.class);

	@Autowired
	private CursoProfissionalService cursoProfissionalService;

	@Autowired
	private AreaProfissionalService areaProfissionalService;
	
	@Autowired InstituicaoService instituicaoService;

	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private AreaProfissional area;
	private String descricao = new String();

	// entidades das telas
	private List<CursoProfissional> lista = new ArrayList<CursoProfissional>();
	private CursoProfissional entidade = new CursoProfissional();

	// combos
	private List<AreaProfissional> comboArea;
	private List<Instituicao> comboInstituicao;
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CursoProfissional> pagedList = new ArrayList<CursoProfissional>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			if (this.area == null) {
				count = cursoProfissionalService.count(this.descricao);
			} else {
				count = cursoProfissionalService.count(this.area.getId(), this.descricao);
			}

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
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			cursoProfissionalService.salvar(entidade);	
			setEntidade( new CursoProfissional() );

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


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			cursoProfissionalService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new CursoProfissional() );
		return consultar();
	}


	/**
	 * Combo Area
	 * 
	 * @return
	 */
	public List<AreaProfissional> getComboArea() {

        try {

        	if ( this.comboArea == null )
        		this.comboArea = areaProfissionalService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo área. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboArea;
	}
	
	/**
	 * Combo Instituicao
	 * 
	 * @return
	 */
	public List<Instituicao> getComboInstituicao() {

		try {

			if ( this.comboInstituicao == null )
				this.comboInstituicao = instituicaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo instituição. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboInstituicao;
	}
	
	
	public List<EnumTipoCursoProfissional> getComboTipoCurso() {
		return Arrays.asList(EnumTipoCursoProfissional.values());
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {
			
			List<CursoProfissional> cursos;
			
			if (this.area == null) {
				cursos = cursoProfissionalService.search(this.descricao, -1, -1);
			} else {
				cursos = cursoProfissionalService.search(this.area.getId(), this.descricao, -1, -1);
			}
			
			if (cursos == null || cursos.size() == 0){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
				return null;
			}
			
			relatorioUtil.relatorio("cursoprofissional.jasper", new HashMap<String, Object>(), "cursoprofissional.pdf", cursos);

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return null;
	}
	
	public String limpaTela() {
		setEntidade(new CursoProfissional());
		setArea(new AreaProfissional());
		return "listar";
	}

	/**
	 * Gets and Sets
	 */
	public AreaProfissional getArea() {return area;}
	public void setArea(AreaProfissional area) {this.area = area;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public CursoProfissional getEntidade() {return entidade;}
	public void setEntidade(CursoProfissional entidade) {this.entidade = entidade;}

	public List<CursoProfissional> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new CursoProfissional() );
			this.area = null;
			this.comboArea = null;
			this.descricao = new String();
			this.lista = new ArrayList<CursoProfissional>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CursoProfissional>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			if (this.area == null) {
				setPagedList(cursoProfissionalService.search(this.descricao, getDataTable().getFirst(), getDataTable().getRows()));
			} else {
				setPagedList(cursoProfissionalService.search(this.area.getId(), this.descricao, getDataTable().getFirst(), getDataTable().getRows()));
			}
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<CursoProfissional> getPagedList() {return pagedList;}
	public void setPagedList(List<CursoProfissional> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}