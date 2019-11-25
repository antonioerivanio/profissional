package br.gov.ce.tce.srh.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CadastroCategoriaFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("cadastroCategoriaFuncionalFormBean")
@Scope("view")
public class CadastroCategoriaFuncionalFormBean implements Serializable {
	
	static Logger logger = Logger.getLogger(CadastroCategoriaFuncionalFormBean.class);
	
	@Autowired
	private CadastroCategoriaFuncionalService cadastroCategoriaService;
	
	private boolean alterar;
	
	private CategoriaFuncional entidade = new CategoriaFuncional();
	private boolean ativa = true;
	
	@PostConstruct
	public void init(){		
		try {
			
			CategoriaFuncional flashParameter = (CategoriaFuncional)FacesUtil.getFlashParameter("entidade");
			setEntidade(flashParameter != null ? flashParameter : new CategoriaFuncional());
			
			if(this.entidade.getId() != null) {				
				this.alterar = true;
				
				if(entidade.getAtiva() == 1L){
					ativa = true;
				} else {
					ativa = false;
				}
			}
			
		} catch (Exception e) {
			
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	private void limpar(){
		entidade = new CategoriaFuncional();
		ativa = true;
	}
	
	public void salvar(){
		if(ativa){
			entidade.setAtiva(1L);
		} else {
			entidade.setAtiva(0L);
		}
		try {
			cadastroCategoriaService.salvar(entidade);
			limpar();
			
			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");
			
		} catch (SRHRuntimeException e) {
			
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
			
		} catch(Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public CategoriaFuncional getEntidade() {
		return entidade;
	}

	public void setEntidade(CategoriaFuncional entidade) {
		this.entidade = entidade;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public boolean isAlterar() {
		return alterar;
	}

	public void setAlterar(boolean alterar) {
		this.alterar = alterar;
	}
	
	
}
