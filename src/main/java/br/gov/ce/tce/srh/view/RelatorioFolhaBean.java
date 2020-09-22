package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@Component("relatorioFolhaBean")
@Scope("view")
public class RelatorioFolhaBean  implements Serializable  {

	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(RelatorioFolhaBean.class);
		
	String filtro;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
		
	@PostConstruct
	public void init() {
		
	}	
	
	public void relatorio() {

		try {
			
			if(filtro == null || filtro.isEmpty()) {
				throw new SRHRuntimeException("Selecione uma opção de filtro");
			}
										
			if(filtro.equals("RECEBENDO_ABONO")) {				
				relatorioUtil.relatorio("relatorioFolhaAbono.jasper", new HashMap<String, Object>(), "relatorioFolhaAbono.pdf");
			} else {
				throw new SRHRuntimeException("Relatório não encontrado");
			}
			
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
}