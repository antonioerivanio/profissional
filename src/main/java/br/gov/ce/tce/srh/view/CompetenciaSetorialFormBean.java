package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaSetorial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.service.CompetenciaSetorialService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("competenciaSetorialFormBean")
@Scope("view")
public class CompetenciaSetorialFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CompetenciaSetorialFormBean.class);

	@Autowired
	private CompetenciaSetorialService competenciaSetorialService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private CompetenciaService competenciaService;

	// entidades das telas
	private CompetenciaSetorial entidade = new CompetenciaSetorial();
	private Setor setor = new Setor();
	private Long tipo;
	private boolean ativa = true;

	// combos
	private List<Setor> comboSetor;
	private List<Competencia> comboCompetencia;

	@PostConstruct
	public void init() {
		CompetenciaSetorial flashParameter = (CompetenciaSetorial)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new CompetenciaSetorial());
		
		if(this.entidade.getId() != null) {
			this.setor = entidade.getSetor();
		}
	}
	
	public void salvar() {
		if(ativa){
			entidade.setAtiva(1L);
		} else {
			entidade.setAtiva(0L);
		}	
		
		try {
			
			competenciaSetorialService.salvar(entidade);

			setSetor(new Setor());
			setEntidade(new CompetenciaSetorial());

			FacesUtil.addInfoMessage("Operação realizada com sucesso");
			logger.info("Operação realizada com sucesso");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public List<Setor> getComboSetor() {

		try {

			if (this.comboSetor == null)
				this.comboSetor = setorService.findAll();

		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboSetor;
	}

	public List<Competencia> getComboCompetencia() {

		try {

			if (this.comboCompetencia == null){
				if(this.tipo== null || this.tipo == 0){
					this.comboCompetencia = competenciaService.findAll();
				}else{
					this.comboCompetencia = competenciaService.findByTipo(tipo);
				}				
			}
				

		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro ao carregar o campo competência. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCompetencia;
	}
	
	public void carregaCompetencia() {
		this.comboCompetencia = null;
	}
	
	public CompetenciaSetorial getEntidade() {
		return entidade;
	}

	public void setEntidade(CompetenciaSetorial entidade) {
		this.entidade = entidade;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Long getTipo() {
		if(entidade!=null && entidade.getCompetencia()!=null){
			tipo = entidade.getCompetencia().getTipo(); 
		}
		return tipo;
	}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}

	public boolean isAtiva() {
		if(entidade != null && !entidade.equals(new CompetenciaSetorial())){
			ativa = entidade.getAtiva() == 0 ? false : true;
		}
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

}
