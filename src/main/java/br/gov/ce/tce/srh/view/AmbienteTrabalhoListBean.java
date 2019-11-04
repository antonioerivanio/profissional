package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AmbienteTrabalho;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AmbienteTrabalhoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("ambienteTrabalhoListBean")
@Scope("view")
public class AmbienteTrabalhoListBean implements Serializable{

	static Logger logger = Logger.getLogger(AmbienteTrabalhoListBean.class);

	@Autowired
	private AmbienteTrabalhoService service;

	private AmbienteTrabalho entidade = new AmbienteTrabalho();

	private List<AmbienteTrabalho> ambienteTrabalhoList = new ArrayList<AmbienteTrabalho>();
	private int count;		

	@PostConstruct
	public void consultar() {
		
		FacesUtil.setFlashParameter("entidade", null);

		try {

			limparListas();

			ambienteTrabalhoList = service.search(null, null, null);

			count = ambienteTrabalhoList.size();

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	public void excluir() {

		try {

			service.excluir(entidade);
			this.consultar();

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	private void limparListas() {
		ambienteTrabalhoList = new ArrayList<AmbienteTrabalho>();
	}	

	public AmbienteTrabalho getEntidade() {
		return entidade;
	}

	public void setEntidade(AmbienteTrabalho entidade) {
		this.entidade = entidade;
	}

	public List<AmbienteTrabalho> getAmbienteTrabalhoList() {
		return ambienteTrabalhoList;
	}
	
}
