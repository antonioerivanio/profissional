package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("representanteFuncionalConsultaBean")
@Scope("view")
public class RepresentanteFuncionalConsultaBean implements Serializable {

	static Logger logger = Logger.getLogger(RepresentanteFuncionalConsultaBean.class);

	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	
	// entidades das telas
	private List<RepresentacaoFuncional> lista = new ArrayList<RepresentacaoFuncional>();;

	@PostConstruct
	private void consultar() {

		try {

			// pegando o parametro
			FacesContext facesContext = FacesContext.getCurrentInstance();

			String parametro = facesContext.getExternalContext().getRequestParameterMap().get("parametro");
			String[] listaParametro = parametro.split(";");

			String nome = null;
			if (listaParametro.length > 0)
				nome = listaParametro[0];
			/*String setor = null;
			if (listaParametro.length > 1)
				setor = listaParametro[1];*/
			
			/*if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				if (setor != null && !setor.equals("")) {
					lista = representacaoFuncionalService.findByUsuarioLogadoSetor(authenticationService.getUsuarioLogado(), new Long(setor) );
				} else {
					lista = representacaoFuncionalService.findByUsuarioLogado(authenticationService.getUsuarioLogado());	
				}
			} else {
				if (setor != null && !setor.equals("")) {
					lista = representacaoFuncionalService.findByNomeSetor(nome, new Long(setor) );
				} else {
					lista = representacaoFuncionalService.findByNome(nome);	
				}
			}*/

			lista = representacaoFuncionalService.findByNome(nome);	

			if (lista.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public List<RepresentacaoFuncional> getLista(){return lista;}

}
