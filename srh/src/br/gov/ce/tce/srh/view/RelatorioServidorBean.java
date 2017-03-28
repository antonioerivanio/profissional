package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@Component("relatorioServidorBean")
@Scope("session")
public class RelatorioServidorBean  implements Serializable  {

	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(RelatorioServidorBean.class);
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	private HtmlForm form;
		
	private Integer sexo; 
	private Boolean incluirMembros;
	private Boolean agruparServidoresQueTemFilhos;	
		
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		sexo = 0;					
		incluirMembros = false;
		agruparServidoresQueTemFilhos = false;
		return form;
	}	
	
	public String limpar() {
		return "listar";
	}
		
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			if(!incluirMembros)
				filtro.append(" AND TOC.ID <> 1 ");
			
			if(sexo == 1)
				filtro.append(" AND P.SEXO = 'M' " );
			else if(sexo == 2) 
				filtro.append(" AND P.SEXO = 'F' " );
			
			parametros.put("FILTRO", filtro.toString());
			parametros.put("SEXO", sexo);
			parametros.put("AGRUPAR_QUEM_TEM_FILHO", agruparServidoresQueTemFilhos);
			
			
			relatorioUtil.relatorio("servidorPorSexo.jasper", parametros, "servidorPorSexo"	+ ".pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	
	public Integer getSexo() {return sexo;}
	public void setSexo(Integer sexo) {this.sexo = sexo;}
	public Boolean getIncluirMembros() {return incluirMembros;}
	public void setIncluirMembros(Boolean incluirMembros) {this.incluirMembros = incluirMembros;}
	public Boolean getAgruparServidoresQueTemFilhos() {return agruparServidoresQueTemFilhos;}
	public void setAgruparServidoresQueTemFilhos(Boolean agruparServidoresQueTemFilhos) {this.agruparServidoresQueTemFilhos = agruparServidoresQueTemFilhos;}

			

}