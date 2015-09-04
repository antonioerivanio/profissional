package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetorResponsabilidade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorResponsabilidadeService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("categoriaFuncionalSetorResponsabilidadeFormBean")
@Scope("session")
public class CategoriaFuncionalSetorResponsabilidadeFormBean implements Serializable {

static Logger logger = Logger.getLogger(AtribuicaoSetorFormBean.class);
	
	@Autowired
	private CategoriaFuncionalSetorResponsabilidadeService atribuicaoSetorService;	
	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;
	@Autowired
	private SetorService setorService;

	private CategoriaFuncionalSetorResponsabilidade entidade = new CategoriaFuncionalSetorResponsabilidade();
	
	private List<Setor> comboSetor;
	private boolean setoresAtivos = true;
	private Setor setor;
	
	private List<CategoriaFuncionalSetor> categorias;
	
	public String prepareIncluir() {
		limpaTela();
		return "incluirAlterar";
	}
	
	public String prepareAlterar() {
		entidade.setCategoriaFuncionalSetor(categoriaFuncionalSetorService.findById(entidade.getCategoriaFuncionalSetor().getId()));		
		setor = entidade.getCategoriaFuncionalSetor().getSetor();
		return "incluirAlterar";
	}
	
	public String salvar() {
				
		try {
						
			atribuicaoSetorService.salvar(entidade);

			limpaTela();

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
	
	public List<Setor> getComboSetor() {

        try {

        	if(comboSetor == null){
	        	if ( this.setoresAtivos )
	        		this.comboSetor = setorService.findTodosAtivos();
	        	else
	        		this.comboSetor = setorService.findAll();
        	}
        	
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}
	
	public String limpaTela() {
		setEntidade(new CategoriaFuncionalSetorResponsabilidade());		
		this.comboSetor = null;
		this.setor = null;
		this.categorias = new ArrayList<CategoriaFuncionalSetor>();
		this.setoresAtivos = true;
		return null;
	}	
	
	public CategoriaFuncionalSetorResponsabilidade getEntidade() {return entidade;}
	public void setEntidade(CategoriaFuncionalSetorResponsabilidade entidade) {this.entidade = entidade;}
	
	public boolean isSetoresAtivos() {return this.setoresAtivos;}
	public void setSetoresAtivos(boolean setoresAtivos) {this.setoresAtivos = setoresAtivos;}
	
	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}
	
	public List<CategoriaFuncionalSetor> getCategorias() {
		if(setor != null){
			categorias = categoriaFuncionalSetorService.findBySetor(setor);
		}		
		return categorias;
	}
	public void setCategorias(List<CategoriaFuncionalSetor> categorias) {this.categorias = categorias;}
	
}
