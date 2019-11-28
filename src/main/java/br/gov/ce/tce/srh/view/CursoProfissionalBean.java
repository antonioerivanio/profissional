package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.enums.TipoCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaProfissionalService;
import br.gov.ce.tce.srh.service.CursoProfissionalService;
import br.gov.ce.tce.srh.service.InstituicaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("cursoProfissionalBean")
@Scope("view")
public class CursoProfissionalBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoProfissionalBean.class);

	@Autowired
	private CursoProfissionalService cursoProfissionalService;

	@Autowired
	private AreaProfissionalService areaProfissionalService;
	
	@Autowired InstituicaoService instituicaoService;

	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametros da tela de consulta
	private AreaProfissional area;
	private String descricao = new String();
	private Date inicio;
	private Date fim;

	// entidades das telas
	private List<CursoProfissional> lista = new ArrayList<CursoProfissional>();
	private CursoProfissional entidade = new CursoProfissional();

	// combos
	private List<AreaProfissional> comboArea;
	private List<Instituicao> comboInstituicao;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CursoProfissional> pagedList = new ArrayList<CursoProfissional>();
	private int flagRegistroInicial = 0;
	
	@PostConstruct
	private void init() {
		CursoProfissional flashParameter = (CursoProfissional)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new CursoProfissional());
    }

	public void consultar() {

		try {
			
			count = cursoProfissionalService.count(this.descricao, this.getIdArea(), this.inicio, this.fim);
			
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
	}

	public void salvar() {

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
	}

	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}
	
	public void excluir() {

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
		consultar();
	}
	
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
		
	public List<TipoCursoProfissional> getComboTipoCurso() {
		return Arrays.asList(TipoCursoProfissional.values());
	}

	public void relatorio() {

		try {
			
			List<CursoProfissional> cursos = cursoProfissionalService.search(this.descricao, this.getIdArea(), this.inicio, this.fim, -1, -1);
			
			if (cursos == null || cursos.size() == 0){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			} else {				
				relatorioUtil.relatorio("cursoprofissional.jasper", new HashMap<String, Object>(), "cursoprofissional.pdf", cursos);
			}			

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public AreaProfissional getArea() {return area;}
	public void setArea(AreaProfissional area) {this.area = area;}
	private Long getIdArea(){
		Long id = null;
		
		if(this.area != null)
			id = area.getId();
		
		return id;
	}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	
	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public CursoProfissional getEntidade() {return entidade;}
	public void setEntidade(CursoProfissional entidade) {this.entidade = entidade;}

	public List<CursoProfissional> getLista(){return lista;}

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
			
			setPagedList(cursoProfissionalService.search(this.descricao, this.getIdArea(), this.inicio, this.fim, getDataTable().getFirst(), getDataTable().getRows()));
			
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