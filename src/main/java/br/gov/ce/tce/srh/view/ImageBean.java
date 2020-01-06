package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("imageBean")
@Scope("session")
public class ImageBean implements Serializable {
	
	static Logger logger = Logger.getLogger(ImageBean.class);
	
	@Autowired
	private ParametroService parametroService;
	
	private String foto;
	
	public void geraFoto(OutputStream out, Object data) {

		try {

			// pegando o servlet context
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

			// pegando o caminho do arquivo no servidor
			Parametro parametro = parametroService.getByNome("pathImageSRH");

			if (parametro == null)
				throw new SRHRuntimeException("Parametro do caminho da imagem nao encontrado na tabela SAPJAVA.FWPARAMETER");

			// pegando a foto
			InputStream in = null;

			try {
				// pegando a foto
				if (getFoto() != null && !getFoto().equalsIgnoreCase("")) {
					in = new FileInputStream(parametro.getValor() + getFoto());
				}	

			} catch (FileNotFoundException e) {
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

			// pegando a foto DEFAULT
			if (in == null) {
				in = new FileInputStream(servletContext.getRealPath(File.separator + "img" + File.separator + "semfoto.png"));
			}	
				
			// Retornando os bytes da foto
			byte[] buffer = new byte[1024];
			int n = 0;
			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
				out.flush();
			}

			out.close();

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage("Erro na geração da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na geração da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na geração da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}	
	
}
