package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("funcionalConsultaBean")
@Scope("view")
public class FuncionalConsultaBean implements Serializable {

	static Logger logger = Logger.getLogger(FuncionalConsultaBean.class);

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private AuthenticationService authenticationService;
		
	private List<Funcional> lista;

	@PostConstruct
	private void consultar() {

		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();  
			String nome = facesContext.getExternalContext().getRequestParameterMap().get("nome");
			String find = facesContext.getExternalContext().getRequestParameterMap().get("find");
		
			if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				
				lista = funcionalService.findByUsuariologado(authenticationService.getUsuarioLogado());
			
			} else if (find!=null && find.equals("all")){
				
				lista = funcionalService.findAllByNome(nome);
			
			} else {
				
				lista = funcionalService.findByNome(nome);
			}		
			
			
			if (lista.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public List<Funcional> getLista(){return lista;}
}
