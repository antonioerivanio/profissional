package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Recadastramento;
import br.gov.ce.tce.srh.service.RecadastramentoService;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("recadastramentoBean")
@Scope("view")
public class RecadastramentoBean implements Serializable {

	static Logger logger = Logger.getLogger(RecadastramentoBean.class);	
	
	@Autowired
	private RecadastramentoService recadastramentoService;	
	
	
	public boolean verificaRecadastramento() {
				
		try {
			Recadastramento recadastramento = recadastramentoService.findMaisRecente();
			
			if (recadastramento != null) {			
				Date hoje = SRHUtils.getHoje();
				
				if(!hoje.before(recadastramento.getInicio()) && !hoje.after(recadastramento.getFim())) {
					return true;
				}				
			}			
		} catch (Exception e) {}
		
		return false;
	}

}