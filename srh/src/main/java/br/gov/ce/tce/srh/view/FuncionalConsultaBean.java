package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : 
* 
* @since   : Nov 16, 2011, 13:39:19 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("funcionalConsultaBean")
@Scope("session")
public class FuncionalConsultaBean implements Serializable {

	static Logger logger = Logger.getLogger(FuncionalConsultaBean.class);

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	//controle de acesso do formulario
	private HtmlForm form;

	// entidades das telas
	private List<Funcional> lista;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */	
	private void consultar() {

		try {

			// pegando o parametro
			String nome = null;
			String findAll = null;
			FacesContext facesContext = FacesContext.getCurrentInstance();  
			nome = facesContext.getExternalContext().getRequestParameterMap().get("nome");
		
			
			if(nome==null || nome.equals(""))
			{

				String parametro = facesContext.getExternalContext().getRequestParameterMap().get("parametro");
				String[] listaParametro = parametro.split(";");
	
				
				if (listaParametro.length > 0)
					nome = listaParametro[0];
	
				if (listaParametro.length > 1)
					findAll = listaParametro[1];
			}
			
			if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				lista = funcionalService.findByUsuariologado(authenticationService.getUsuarioLogado());
			} else if (findAll!=null && findAll.equals("OK")){
				lista = funcionalService.findAllByNome(nome);
			} else{
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

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		lista = new ArrayList<Funcional>();
		consultar();
		return form;
	}

}
