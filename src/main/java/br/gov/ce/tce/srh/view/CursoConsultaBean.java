package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.service.CursoServidorService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("cursoConsultaBean")
@Scope("view")
public class CursoConsultaBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoConsultaBean.class);

	@Autowired
	private CursoServidorService cursoServidorService;	

	// entidades das telas
	private List<CursoProfissional> lista;

	@PostConstruct
	private void consultar() {

		try {

			// pegando o parametro
			FacesContext facesContext = FacesContext.getCurrentInstance();  
			String curso = facesContext.getExternalContext().getRequestParameterMap().get("curso");
			
			lista = cursoServidorService.search(curso);
			
			if (lista.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public List<CursoProfissional> getLista(){return lista;}	

}
