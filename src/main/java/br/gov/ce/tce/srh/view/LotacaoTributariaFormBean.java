package br.gov.ce.tce.srh.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.LotacaoTributaria;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.LotacaoTributariaService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("lotacaoTributariaFormBean")
@Scope("view")
public class LotacaoTributariaFormBean implements Serializable {
	
	static Logger logger = Logger.getLogger(LotacaoTributariaFormBean.class);

	@Autowired
	private LotacaoTributariaService service;
	
	private LotacaoTributaria entidade;

	
	@PostConstruct
	private void init() {		
		LotacaoTributaria flashParameter = (LotacaoTributaria)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new LotacaoTributaria());
		
		/*
		 * if (entidade.getContato() != null) { idContato =
		 * entidade.getContato().getId(); nomeContato =
		 * entidade.getContato().getNomeCompleto(); }
		 */
		
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
		this.entidade = new LotacaoTributaria();
	}
	
	
	public LotacaoTributaria getEntidade() {
		return entidade;
	}

	public void setEntidade(LotacaoTributaria entidade) {
		this.entidade = entidade;
	}
	
	
	

}
