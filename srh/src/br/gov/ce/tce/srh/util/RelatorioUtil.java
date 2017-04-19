package br.gov.ce.tce.srh.util;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;


public class RelatorioUtil {
	
	static Logger logger = Logger.getLogger(RelatorioUtil.class);

	public String relatorio(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo) throws Exception {  

		// pegando o servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		// pegando o ds do spring
		String springVar = "org.springframework.web.context.WebApplicationContext.ROOT";
		WebApplicationContext wctx = (WebApplicationContext) servletContext.getAttribute(springVar);
		DataSource dataSource = (DataSource) wctx.getBean("dataSource");

		String logo = servletContext.getRealPath("//img/" + "logo-srh.png");
		String logo_tce = servletContext.getRealPath("//img/" + "logo-tce-report.png");
		String back = servletContext.getRealPath("//img/" + "bg-topo.png");
		String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/" + arquivoRelatorio);  

		//parametros  
		//Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("LOGO", logo);
		parametros.put("LOGO_TCE", logo_tce);
		parametros.put("BACK", back);

		// Fill the report using an empty data source
		JasperPrint print;
		
		Connection connection = dataSource.getConnection();

		print = JasperFillManager.fillReport(pathRel, parametros, connection);
		byte[] bytes = JasperExportManager.exportReportToPdf(print);
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);

		connection.close();
		
		return null;
	}
	
	public String relatorio(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo, List<?> relatorioList) throws Exception {  

		// pegando o servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		JRRewindableDataSource dataSource = relatorioList == null ? new net.sf.jasperreports.engine.JREmptyDataSource() : new JRBeanCollectionDataSource(relatorioList);

		String logo = servletContext.getRealPath("//img/" + "logo-srh.png");
		String logo_tce = servletContext.getRealPath("//img/" + "logo-tce-report.png");
		String back = servletContext.getRealPath("//img/" + "bg-topo.png");
		String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/" + arquivoRelatorio);  

		//parametros 		
		parametros.put("LOGO", logo);
		parametros.put("LOGO_TCE", logo_tce);
		parametros.put("BACK", back);

		// Fill the report using an empty data source
		JasperPrint print;

		print = JasperFillManager.fillReport(pathRel, parametros, dataSource);
		byte[] bytes = JasperExportManager.exportReportToPdf(print);
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);

		return null;
	}	
	
	public String relatorioComEmptyDataSource(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo) throws Exception {  

		// pegando o servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		JRRewindableDataSource dataSource = new net.sf.jasperreports.engine.JREmptyDataSource();

		String logo = servletContext.getRealPath("//img/" + "logo-srh.png");
		String logo_tce = servletContext.getRealPath("//img/" + "logo-tce-report.png");
		String back = servletContext.getRealPath("//img/" + "bg-topo.png");
		String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/" + arquivoRelatorio);  

		//parametros 		
		parametros.put("LOGO", logo);
		parametros.put("LOGO_TCE", logo_tce);
		parametros.put("BACK", back);

		// Fill the report using an empty data source
		JasperPrint print;

		print = JasperFillManager.fillReport(pathRel, parametros, dataSource);
		byte[] bytes = JasperExportManager.exportReportToPdf(print);
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);

		return null;
	}	
	
	protected void writeBytesAsAttachedTextFile(byte[] bytes, String fileName) throws Exception {  

		if (bytes == null)  
			throw new Exception("Array de bytes nulo.");  

		if (fileName == null)  
			throw new Exception("Nome do arquivo é nulo.");  

		FacesContext facesContext = FacesContext.getCurrentInstance();  
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();  
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();

	}
	
	public void openPdf(byte[] bytes, String fileName) throws Exception {  

		if (bytes == null)  
			throw new Exception("Array de bytes nulo.");  

		if (fileName == null)  
			throw new Exception("Nome do arquivo é nulo.");  

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-Type", "application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\";");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		facesContext.responseComplete();

	}

}
