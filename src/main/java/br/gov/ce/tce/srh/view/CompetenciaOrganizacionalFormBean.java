package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
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

@SuppressWarnings("serial")
@Component("competenciaOrganizacionalFormBean")
@Scope("view")
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

	@PostConstruct
	public void init() {
		CompetenciaOrganizacional flashParameter = (CompetenciaOrganizacional)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new CompetenciaOrganizacional());
	}	

	public void salvar() {
		
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
	}

	public void filtrarCompetencia(ValueChangeEvent event) {
		tipoCompetencia =  (String) event.getNewValue() ;
	}

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
	
	private void limpar() {

		setEntidade(new CompetenciaOrganizacional());
		getEntidade().setCompetencia(new Competencia());
		getEntidade().setAtiva(true);
		tipoCompetencia = null;

		this.comboCompetencia = null;
	}

	public void carregaCompetencia() {
		this.comboCompetencia = null;
	}

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
