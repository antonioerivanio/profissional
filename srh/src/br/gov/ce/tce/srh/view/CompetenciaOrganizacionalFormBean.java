package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaOrganizacional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaOrganizacionalService;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
 * Use case : Compentencia Organizacional
 * 
 * @since   : Dez 12, 2012, 12:12:12 PM
 * @author  : raphael.ferreira@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("competenciaOrganizacionalFormBean")
@Scope("session")
public class CompetenciaOrganizacionalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CompetenciaOrganizacionalFormBean.class);

	@Autowired
	private CompetenciaOrganizacionalService competenciaOrganizacionalService;

	@Autowired
	private CompetenciaService competenciaService;

	//endidade das telas
	private CompetenciaOrganizacional entidade = new CompetenciaOrganizacional();

	// combos
	private List<Competencia> comboCompetencia;
	private String tipoCompetencia;


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
		return "incluirAlterar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {
		
		try {

			competenciaOrganizacionalService.salvar(entidade);
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
	 * Combo Tipo de Ferias
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
	 * Limpar form
	 */
	private void limpar() {

		setEntidade(new CompetenciaOrganizacional());
		getEntidade().setCompetencia(new Competencia());
		getEntidade().setAtiva(true);
		tipoCompetencia = null;

		this.comboCompetencia = null;
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
	 * Gets and Sets
	 */
	public CompetenciaOrganizacional getEntidade() { return entidade; }
	public void setEntidade(CompetenciaOrganizacional entidade) { this.entidade = entidade; }


	public String getTipoCompetencia() {
		return tipoCompetencia;
	}


	public void setTipoCompetencia(String tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}


	public void setComboCompetencia(List<Competencia> comboCompetencia) {
		this.comboCompetencia = comboCompetencia;
	}

}
