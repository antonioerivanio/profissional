package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaProfissionalService;
import br.gov.ce.tce.srh.service.PessoalCursoProfissionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("pessoalCursoProfissionalListBean")
@Scope("view")
public class PessoalCursoProfissionalListBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoProfissionalListBean.class);

	@Autowired
	private AreaProfissionalService areaProfissionalService;

	@Autowired
	private PessoalCursoProfissionalService pessoalCursoProfissionalService;

	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametros da tela de consulta
	private AreaProfissional area;
	private String curso;

	// entidades das telas
	private List<String> lista;
	private CursoProfissional cursoProfissional = new CursoProfissional();

	// combos
	private List<AreaProfissional> comboArea;

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CursoProfissional> pagedList = new ArrayList<CursoProfissional>();
	private int flagRegistroInicial = 0;


	public void consultar() {

		try {

			if ( this.area == null )
				throw new SRHRuntimeException("Selecione uma área.");

			count = pessoalCursoProfissionalService.count(area.getId(), curso);

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
		FacesUtil.setFlashParameter("entidade", getCursoProfissional());        
        return "incluirAlterar";
	}

	public void excluir() {

		try {

			pessoalCursoProfissionalService.excluir( cursoProfissional.getId() );

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
	}

	public List<AreaProfissional> getComboArea() {

		try {

			if ( this.comboArea == null )
				this.comboArea = areaProfissionalService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo area. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboArea;
	}

	public void relatorio() {

		try {

			if ( this.area == null )
				throw new SRHRuntimeException("Selecione o campo área.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" where cursoprofi.IDAREAPROFISSIONAL = " + this.area.getId());

			if (this.curso != null && !this.curso.equalsIgnoreCase(""))
				filtro.append(" AND upper( cursoprofi.DESCRICAO ) like upper('%" + this.curso + "%') ");

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("pessoalCursoProfissional.jasper", parametros, "pessoalCursoProfissional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void relatorioCurso() {

		try {
			
			if ( this.cursoProfissional == null )
				throw new SRHRuntimeException("Selecione um curso.");

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("ID", cursoProfissional.getId());
			parametros.put("COMPETENCIAS", servletContext.getRealPath("//WEB-INF/relatorios/cursoCompetencia.jasper") );
			parametros.put("FUNCIONARIOS", servletContext.getRealPath("//WEB-INF/relatorios/cursoFuncionarios.jasper") );

			relatorioUtil.relatorio("pessoalCursoProfissionalCompetencia.jasper", parametros, "pessoalCursoProfissionalCompetencia.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório de curso. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public AreaProfissional getArea() {return area;}
	public void setArea(AreaProfissional area) {this.area = area;}

	public String getCurso() {return curso;}
	public void setCurso(String curso) {this.curso = curso;}

	public CursoProfissional getCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(CursoProfissional cursoProfissional) {this.cursoProfissional = cursoProfissional;}

	public List<String> getLista() {return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CursoProfissional>();
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(pessoalCursoProfissionalService.search(area.getId(), curso, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<CursoProfissional> getPagedList() {return pagedList;}
	public void setPagedList(List<CursoProfissional> list) {this.pagedList = list;}
	//FIM PAGINAÇÃO

}