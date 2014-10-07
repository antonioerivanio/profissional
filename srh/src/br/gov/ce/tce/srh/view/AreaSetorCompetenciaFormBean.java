package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaSetorCompetenciaService;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.service.sapjava.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC003_Manter Competências do Setor
* 
* @since   : Aug 31, 2011, 10:44:18 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("areaSetorCompetenciaFormBean")
@Scope("session")
public class AreaSetorCompetenciaFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaSetorCompetenciaFormBean.class);

	@Autowired
	private AreaSetorCompetenciaService areaSetorCompetenciaService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private AreaSetorService areaSetorService;

	@Autowired
	private CompetenciaService competenciaService;


	// entidades das telas
	private AreaSetorCompetencia entidade = new AreaSetorCompetencia();
	private Setor setor = new Setor();

	// combos
	private List<Setor> comboSetor;
	private List<Competencia> comboCompetencia;



	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		setSetor( new Setor() );
		setEntidade( new AreaSetorCompetencia() );
		comboSetor = null;
		comboCompetencia = null;
		return "incluirAlterar";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {
		comboSetor = null;
		comboCompetencia = null;
		this.setor = entidade.getAreaSetor().getSetor();
		return "incluirAlterar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			areaSetorCompetenciaService.salvar(entidade);

			setSetor( new Setor() );
			setEntidade( new AreaSetorCompetencia() );

			FacesUtil.addInfoMessage("Operação realizada com sucesso");
			logger.info("Operação realizada com sucesso");

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
	 * Combo setor
	 * 
	 * @return
	 */
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


	/**
	 * Combo Area
	 * 
	 * @return
	 */
	public void carregaArea() {
		getEntidade().setAreaSetor(new AreaSetor());
	}
	
	public List<AreaSetor> getComboArea() {

		try {

			if( this.setor != null && this.setor.getId() != null) {
				return areaSetorService.findBySetor( this.setor.getId() );
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo área. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Combo Competencia
	 * 
	 * @return
	 */
	public List<Competencia> getComboCompetencia() {

        try {

        	if ( this.comboCompetencia == null )
        		this.comboCompetencia = competenciaService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCompetencia;
	}
	
	public String limpaTela() {
		setEntidade(new AreaSetorCompetencia());
		setSetor(new Setor());
		return null;
	}


	/**
	 * Gets and Sets
	 */
	public AreaSetorCompetencia getEntidade() {return entidade;}
	public void setEntidade(AreaSetorCompetencia entidade) {this.entidade = entidade;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

}