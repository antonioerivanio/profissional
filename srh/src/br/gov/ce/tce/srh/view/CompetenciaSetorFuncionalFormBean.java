package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaSetorFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.service.CompetenciaSetorFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
 * Use case : Compentencia Setor Funcional
 * 
 * @since   : Dez 12, 2012, 12:12:12 PM
 * @author  : raphael.ferreira@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("competenciaSetorFuncionalFormBean")
@Scope("session")
public class CompetenciaSetorFuncionalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CompetenciaSetorFuncionalFormBean.class);

	@Autowired
	private CompetenciaSetorFuncionalService competenciaSetorFuncionalService;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;

	//endidade das telas
	private CompetenciaSetorFuncional entidade = new CompetenciaSetorFuncional();

	// combos
	private List<Competencia> comboCompetencia;
	private List<CategoriaFuncionalSetor> comboCategoria;
	@SuppressWarnings("unused")
	private List<Setor> comboSetor;

	
	private String tipoCompetencia;
	private Setor setor;


	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {
		
	//	try {
			
			//this.entidade = competenciaSetorFuncionalService.getById( this.entidade.getId() );     
			this.setor = entidade.getCategoria().getSetor();
			this.tipoCompetencia = ""+entidade.getCompetencia().getTipo();

//		} catch (Exception e) {
//			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
//			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
//		}

		
		return "incluirAlterar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {
		
		try {
			
			competenciaSetorFuncionalService.salvar(entidade, setor);
			limpar();

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

	public void filtrarCompetencia(ValueChangeEvent event) {

		tipoCompetencia =  (String) event.getNewValue() ;

	}

	/**
	 * Combo Competencia
	 * 
	 * @return
	 */
	public List<Competencia> getComboCompetencia() {

		try {

			if ( this.tipoCompetencia == null || this.tipoCompetencia.equals("0")){
				this.comboCompetencia = competenciaService.findAll();
			}else{
				this.comboCompetencia = competenciaService.search(tipoCompetencia);
			}

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar a combo competencia. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return this.comboCompetencia;
	}

	/**
	 * Combo Categoria
	 * 
	 * @return
	 */
	public List<CategoriaFuncionalSetor> getComboCategoria() {

		try {

			if ( this.setor == null ){
				this.comboCategoria = categoriaFuncionalSetorService.findAll();
			}else{
				this.comboCategoria = categoriaFuncionalSetorService.findBySetor(setor);
			}

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar a combo competencia. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return this.comboCategoria;
	}

	
	/**
	 * Limpar form
	 */
	private void limpar() {

		setEntidade(new CompetenciaSetorFuncional());
		getEntidade().setCompetencia(new Competencia());
		getEntidade().setAtiva(true);
		tipoCompetencia = null;
		setor = null;

		comboCompetencia = null;
		comboCategoria = null;
	}

	/**
	 * Combo Competencia
	 * 
	 * @return
	 */
	public void carregaCompetencia() {
		this.comboCompetencia = null;
	}

	/**
	 * Combo Categoria
	 * 
	 * @return
	 */
	public void carregaCategoria() {
		this.comboCategoria = null;
	}

	/**
	 * Gets and Sets
	 */
	public CompetenciaSetorFuncional getEntidade() { return entidade; }
	public void setEntidade(CompetenciaSetorFuncional entidade) { this.entidade = entidade; }


	public String getTipoCompetencia() {
		return tipoCompetencia;
	}


	public void setTipoCompetencia(String tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}


	public void setComboCompetencia(List<Competencia> comboCompetencia) {
		this.comboCompetencia = comboCompetencia;
	}

	public Setor getSetor() {
		return setor;
	}


	public void setSetor(Setor setor) {
		this.setor = setor;
	}


	public void setComboCategoria(List<CategoriaFuncionalSetor> comboCategoria) {
		this.comboCategoria = comboCategoria;
	}


	public List<Setor> getComboSetor() {
		return comboSetor = setorService.findAll();
	}


	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}
	
}
