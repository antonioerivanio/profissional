package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AtribuicaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AtribuicaoSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("atribuicaoSetorFormBean")
@Scope("session")
public class AtribuicaoSetorFormBean  implements Serializable {
	
	static Logger logger = Logger.getLogger(AtribuicaoSetorFormBean.class);
	
	@Autowired
	private AtribuicaoSetorService atribuicaoSetorService;
	
	@Autowired
	private SetorService setorService;

	private AtribuicaoSetor entidade = new AtribuicaoSetor();
	private Setor setor = new Setor();
	private Long tipo = 1L;	
	
	private List<Setor> comboSetor;
	
	public String prepareIncluir() {
		setSetor(new Setor());
		setEntidade(new AtribuicaoSetor());
		comboSetor = null;
		return "incluirAlterar";
	}
	
	public String prepareAlterar() {
		this.comboSetor = null;
		this.setor = entidade.getSetor();
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

			if (this.comboSetor == null)
				this.comboSetor = setorService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboSetor;
	}
	
	public String limpaTela() {
		setTipo(1L);
		setEntidade(new AtribuicaoSetor());
		setSetor(new Setor());
		return null;
	}
	
	public AtribuicaoSetor getEntidade() {return entidade;}
	public void setEntidade(AtribuicaoSetor entidade) {this.entidade = entidade;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public Long getTipo() {return this.tipo;}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
		if(entidade != null){
			entidade.setTipo(tipo); 
		}
	}
	
}
