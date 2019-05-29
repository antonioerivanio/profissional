package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Carreira;
import br.gov.ce.tce.srh.enums.SituacaoCarreira;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CarreiraService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("carreiraFormBean")
@Scope("session")
public class CarreiraFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CarreiraFormBean.class);

	@Autowired
	private CarreiraService service;

	private Carreira entidade = new Carreira();

	private boolean alterar = false;
	
	private boolean encerrado = false;

	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}

	public String prepareAlterar() {

		this.alterar = true;
		
		if(entidade.getInicioExclusao() != null)
			this.encerrado = true;
		else
			this.encerrado = false;

		try {

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}

	public String salvar() {

		try {

			service.salvar(entidade);
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

	private void limpar() {

		this.alterar = false;
		this.encerrado = false;
		this.entidade = new Carreira();

	}
	
	public List<SituacaoCarreira> getComboSituacao() {
		return Arrays.asList(SituacaoCarreira.values());
	}

	public Carreira getEntidade() {
		return entidade;
	}

	public void setEntidade(Carreira entidade) {
		this.entidade = entidade;
	}

	public boolean isAlterar() {
		return alterar;
	}

	public void setAlterar(boolean alterar) {
		this.alterar = alterar;
	}

	public boolean isEncerrado() {
		return encerrado;
	}

	public void setEncerrado(boolean encerrado) {
		this.encerrado = encerrado;
	}

}
