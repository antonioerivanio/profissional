package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.CompetenciaSetorial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CadastroCategoriaFuncionalService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("categoriaFuncionalSetorFormBean")
@Scope("session")
public class CategoriaFuncionalSetorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CategoriaFuncionalSetorFormBean.class);

	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private CadastroCategoriaFuncionalService cadastroCategoriaService;

	// entidades das telas
	private CategoriaFuncionalSetor entidade = new CategoriaFuncionalSetor();
	private Setor setor = new Setor();
	private CategoriaFuncional categoriaFuncional;
	private boolean ativa = true;

	// combos
	private List<Setor> comboSetor;
	private List<CategoriaFuncional> comboCategoriaFuncional;

	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		setSetor(new Setor());
		setCategoriaFuncional(new CategoriaFuncional());
		setEntidade(new CategoriaFuncionalSetor());
		comboSetor = null;
		ativa = true;
		return "incluirAlterar";
	}

	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {
		comboSetor = null;
		comboCategoriaFuncional = null;
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
			
			if(entidade.getInicio() != null && entidade.getFim() != null){
				if(entidade.getInicio().after(entidade.getFim())){
					throw new SRHRuntimeException("A Data Fim deve ser maior que a Data Início.");
				}
			}

			categoriaFuncionalSetorService.salvar(entidade);

			setSetor(new Setor());
			setCategoriaFuncional(new CategoriaFuncional());
			setEntidade(new CategoriaFuncionalSetor());

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
	public List<CategoriaFuncional> getComboCategoriaFuncional() {

		try {

			if (this.comboCategoriaFuncional == null){
				this.comboCategoriaFuncional = cadastroCategoriaService.findAll();							
			}
				

		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro ao carregar o campo competência. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCategoriaFuncional;
	}
	
	/**
	 * Combo Categoria Funcional
	 * 
	 * @return
	 */
	public void carregaCategoriaFuncional() {
		this.comboCategoriaFuncional = null;
	}
	
	public String limpaTela() {
		setEntidade(new CategoriaFuncionalSetor());
		setSetor(new Setor());
		setCategoriaFuncional(new CategoriaFuncional());
		return null;
	}

	/**
	 * Gets and Sets
	 */
	public CategoriaFuncionalSetor getEntidade() {
		return entidade;
	}

	public void setEntidade(CategoriaFuncionalSetor entidade) {
		this.entidade = entidade;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public boolean isAtiva() {
		if(entidade != null && !entidade.equals(new CompetenciaSetorial())){
			if(entidade.getAtiva() == null){
				ativa = true;
			}else{
				ativa = entidade.getAtiva() == 0 ? false : true;
			}			
		}
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public CategoriaFuncional getCategoriaFuncional() {
		return categoriaFuncional;
	}

	public void setCategoriaFuncional(CategoriaFuncional categoriaFuncional) {
		this.categoriaFuncional = categoriaFuncional;
	}

}
