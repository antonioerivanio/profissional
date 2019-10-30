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
import br.gov.ce.tce.srh.domain.Estabelecimento;
import br.gov.ce.tce.srh.enums.AprendizContrato;
import br.gov.ce.tce.srh.enums.PcdContrato;
import br.gov.ce.tce.srh.enums.RegistroPonto;
import br.gov.ce.tce.srh.enums.TipoInscricao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EstabelecimentoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("estabelecimentoFormBean")
@Scope("view")
public class EstabelecimentoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(EstabelecimentoFormBean.class);

	@Autowired
	private EstabelecimentoService service;	

	private Estabelecimento entidade;
	
	@PostConstruct
	private void init() {		
		Estabelecimento flashParameter = (Estabelecimento)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Estabelecimento());		
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
		this.entidade = new Estabelecimento();
	}

	public Estabelecimento getEntidade() {
		return entidade;
	}

	public void setEntidade(Estabelecimento entidade) {
		this.entidade = entidade;
	}
	
	public List<TipoInscricao> getComboTipoInscricao() {
		return Arrays.asList(TipoInscricao.values());
	}
	
	public List<RegistroPonto> getComboRegistroPonto() {
		return Arrays.asList(RegistroPonto.values());
	}	
	
	public List<AprendizContrato> getComboAprendizContrato() {
		return Arrays.asList(AprendizContrato.values());
	}	
	
	public List<PcdContrato> getComboPcdContrato() {
		return Arrays.asList(PcdContrato.values());
	}	

}
