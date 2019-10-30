package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.LotacaoTributaria;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.LotacaoTributariaService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("lotacaoTributariaListBean")
@Scope("view")
public class LotacaoTributariaListBean implements Serializable {

	static Logger logger = Logger.getLogger(LotacaoTributariaListBean.class);
	
	@Autowired
	private LotacaoTributariaService service;

	private LotacaoTributaria entidade = new LotacaoTributaria();

	private List<LotacaoTributaria> lotacaoTributariaList = new ArrayList<LotacaoTributaria>();
	
	@PostConstruct
	public void consultar() {

		try {
			
			lotacaoTributariaList = service.findAll();

			if (lotacaoTributariaList.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
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

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		this.consultar();
		
	}

	private void limparListas() {
		lotacaoTributariaList = new ArrayList<LotacaoTributaria>();
	}	

	public LotacaoTributaria getEntidade() {
		return entidade;
	}

	public void setEntidade(LotacaoTributaria entidade) {
		this.entidade = entidade;
	}

	public List<LotacaoTributaria> getLotacaoTributariaList() {
		return lotacaoTributariaList;
	}
	
}
