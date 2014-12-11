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

import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.sca.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("pessoalConsultaBean")
@Scope("session")
public class PessoalConsultaBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalConsultaBean.class);

	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private AuthenticationService authenticationService;

	// controle de acesso do formulario
	private HtmlForm form;

	// entidades das telas
	private List<Pessoal> lista;


	private void consultar() {

		try {

			// pegando o parametro
			FacesContext facesContext = FacesContext.getCurrentInstance();  
			String nome = facesContext.getExternalContext().getRequestParameterMap().get("nome");
			String flServidor = facesContext.getExternalContext().getRequestParameterMap().get("flServidor");
			
			if(flServidor != null){
				lista = pessoalService.findServidorByNome(nome);
			
			} else if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				lista = new ArrayList<Pessoal>();
				lista.add(pessoalService.getByCpf(SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf())));
			} else {
				lista = pessoalService.findByNome(nome);
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

	public List<Pessoal> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		lista = new ArrayList<Pessoal>();
		consultar();
		return form;
	}

}
