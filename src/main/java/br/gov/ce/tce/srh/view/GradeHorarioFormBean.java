package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.GradeHorario;
import br.gov.ce.tce.srh.enums.SimNao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.GradeHorarioService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("gradeHorarioFormBean")
@Scope("view")
public class GradeHorarioFormBean implements Serializable {
	
	static Logger logger = Logger.getLogger(GradeHorarioFormBean.class);

	@Autowired
	private GradeHorarioService service;
	
	private GradeHorario entidade;

	
	@PostConstruct
	private void init() {		
		GradeHorario flashParameter = (GradeHorario)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new GradeHorario());
		
		if(entidade.getEsocialVigencia() == null) {
			entidade.setEsocialVigencia(new ESocialEventoVigencia());
		}	
    }

	public void salvar() {

		try {

			service.salvar(entidade);
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	private void limpar() {
		this.entidade = new GradeHorario();
	}
	
	
	public GradeHorario getEntidade() {
		return entidade;
	}

	public void setEntidade(GradeHorario entidade) {
		this.entidade = entidade;
	}
	
	public List<SimNao> getComboSimNao() {
		return Arrays.asList(SimNao.values());
	}
	
}
