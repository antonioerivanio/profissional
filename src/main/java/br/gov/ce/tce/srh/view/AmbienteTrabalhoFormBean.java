package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.dao.EstabelecimentoDAO;
import br.gov.ce.tce.srh.domain.AmbienteTrabalho;
import br.gov.ce.tce.srh.domain.Estabelecimento;
import br.gov.ce.tce.srh.enums.LocalAmbiente;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AmbienteTrabalhoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("ambienteTrabalhoFormBean")
@Scope("view")
public class AmbienteTrabalhoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AmbienteTrabalhoFormBean.class);

	@Autowired
	private AmbienteTrabalhoService service;
	
	@Autowired
	private EstabelecimentoDAO estabelecimentoDAO;

	private AmbienteTrabalho entidade = new AmbienteTrabalho();
	
	@PostConstruct
	private void init() {
		AmbienteTrabalho flashParameter = (AmbienteTrabalho)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new AmbienteTrabalho());
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
		this.entidade = new AmbienteTrabalho();
	}
	
	public List<LocalAmbiente> getComboLocal() {
		return Arrays.asList(LocalAmbiente.values());
	}
	
	public List<Estabelecimento> getComboEstabelecimento() {
		return estabelecimentoDAO.findAll();
	}

	public AmbienteTrabalho getEntidade() {
		return entidade;
	}

	public void setEntidade(AmbienteTrabalho entidade) {
		this.entidade = entidade;
	}	

}
