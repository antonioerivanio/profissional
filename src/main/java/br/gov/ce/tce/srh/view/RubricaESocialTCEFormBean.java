package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.dao.RubricaESocialDAO;
import br.gov.ce.tce.srh.dao.RubricaESocialTabelaDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Rubrica;
import br.gov.ce.tce.srh.domain.RubricaESocial;
import br.gov.ce.tce.srh.domain.RubricaESocialTCE;
import br.gov.ce.tce.srh.domain.RubricaESocialTabela;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaCPTipo;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaFGTS;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaIRRFTipo;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaSIND;
import br.gov.ce.tce.srh.enums.TipoRubrica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RubricaESocialTCEService;
import br.gov.ce.tce.srh.service.RubricaService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("rubricaESocialTCEFormBean")
@Scope("view")
public class RubricaESocialTCEFormBean implements Serializable {

	static Logger logger = Logger.getLogger(RubricaESocialTCEFormBean.class);

	@Autowired
	private RubricaESocialTCEService service;
	
	@Autowired
	private RubricaESocialTabelaDAO rubricaESocialTabelaDAO;
	
	@Autowired
	private RubricaService rubricaService;
	
	@Autowired
	private RubricaESocialDAO rubricaESocialDAO;

	private RubricaESocialTCE entidade = new RubricaESocialTCE();
	
	private List<RubricaESocialTabela> tabelasRubrica;
	
	private List<Rubrica> rubricasTCE;
	
	private List<RubricaESocial> rubricasESocial;
	
	private RubricaIncidenciaCPTipo codigoPrevidTipo;
	private RubricaIncidenciaIRRFTipo codigoIrrfTipo;
	
	@PostConstruct
	private void init() {
		RubricaESocialTCE flashParameter = (RubricaESocialTCE)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RubricaESocialTCE());
		if(entidade.getEsocialVigencia() == null) {
			entidade.setEsocialVigencia(new ESocialEventoVigencia());
		}
		this.tabelasRubrica = rubricaESocialTabelaDAO.findAll();
		this.rubricasTCE = rubricaService.findAll();
		this.rubricasESocial = rubricaESocialDAO.findAll();
		this.codigoPrevidTipo = entidade.getCodigoPrevid() != null ? entidade.getCodigoPrevid().getTipo() : null;
		this.codigoIrrfTipo = entidade.getCodigoIrrf() != null ? entidade.getCodigoIrrf().getTipo() : null;
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
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	private void limpar() {
		this.entidade = new RubricaESocialTCE();
	}
	
	public List<TipoRubrica> getComboTipoRubrica() {
		return Arrays.asList(TipoRubrica.values());
	}
	
	public List<RubricaIncidenciaCPTipo> getComboCodigoPrevidTipo() {
		return Arrays.asList(RubricaIncidenciaCPTipo.values());
	}
	
	public List<RubricaIncidenciaIRRFTipo> getComboCodigoIrrfTipo() {
		return Arrays.asList(RubricaIncidenciaIRRFTipo.values());
	}
	
	public List<RubricaIncidenciaFGTS> getComboCodigoFgts() {
		return Arrays.asList(RubricaIncidenciaFGTS.values());
	}
	
	public List<RubricaIncidenciaSIND> getComboCodigoSindicato() {
		return Arrays.asList(RubricaIncidenciaSIND.values());
	}
	
	public RubricaESocialTCE getEntidade() {
		return entidade;
	}

	public void setEntidade(RubricaESocialTCE entidade) {
		this.entidade = entidade;
	}

	public List<RubricaESocialTabela> getTabelasRubrica() {
		return this.tabelasRubrica;
	}
	
	public List<Rubrica> getRubricasTCE() {
		return this.rubricasTCE;
	}
	
	public List<RubricaESocial> getRubricasESocial() {
		return this.rubricasESocial;
	}

	public RubricaIncidenciaCPTipo getCodigoPrevidTipo() {
		return codigoPrevidTipo;
	}

	public void setCodigoPrevidTipo(RubricaIncidenciaCPTipo codigoPrevidTipo) {
		this.codigoPrevidTipo = codigoPrevidTipo;
	}

	public RubricaIncidenciaIRRFTipo getCodigoIrrfTipo() {
		return codigoIrrfTipo;
	}

	public void setCodigoIrrfTipo(RubricaIncidenciaIRRFTipo codigoIrrfTipo) {
		this.codigoIrrfTipo = codigoIrrfTipo;
	}

}
