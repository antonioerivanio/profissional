package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

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
@Scope("session")
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

	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		setSetor(new Setor());
		setEntidade(new CompetenciaSetorial());
		comboSetor = null;
		return "incluirAlterar";
	}

	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {
		comboSetor = null;
		this.setor = entidade.getSetor();
		return "incluirAlterar";
	}

	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {
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

		return null;
	}

	/**
	 * Combo setor
	 * 
	 * @return
	 */
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

	/**
	 * Combo Competencia
	 * 
	 * @return
	 */
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
	
	/**
	 * Combo Competencia
	 * 
	 * @return
	 */
	public void carregaCompetencia() {
		this.comboCompetencia = null;
	}
	
	public String limpaTela() {
		setTipo(0L);
		setEntidade(new CompetenciaSetorial());
		setSetor(new Setor());
		return null;
	}

	/**
	 * Gets and Sets
	 */
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
