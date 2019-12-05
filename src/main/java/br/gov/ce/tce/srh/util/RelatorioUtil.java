package br.gov.ce.tce.srh.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;


public class RelatorioUtil {
	
	static Logger logger = Logger.getLogger(RelatorioUtil.class);

	public String relatorio(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo) throws Exception {  

		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		String springVar = "org.springframework.web.context.WebApplicationContext.ROOT";
		WebApplicationContext wctx = (WebApplicationContext) servletContext.getAttribute(springVar);
		DataSource dataSource = (DataSource) wctx.getBean("dataSource");

		String logo = servletContext.getRealPath(File.separator + "img" + File.separator + "logo-srh.svg");
		String logo_tce = servletContext.getRealPath(File.separator + "img" + File.separator + "logo-tce-report.png");
		String back = servletContext.getRealPath(File.separator + "img" + File.separator + "bg-topo.png");
		String pathRel = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "relatorios" + File.separator + arquivoRelatorio);  
		
		parametros.put("LOGO", logo);
		parametros.put("LOGO_TCE", logo_tce);
		parametros.put("BACK", back);

		JasperPrint print;
		
		Connection connection = dataSource.getConnection();

		print = JasperFillManager.fillReport(pathRel, parametros, connection);
		byte[] bytes = JasperExportManager.exportReportToPdf(print);
		
		if (bytes.length < 1024) {
			FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
			logger.info("Nenhum registro foi encontrado.");
			return null;		
		}
		
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);

		connection.close();
		
		return null;
	}
	
	public String relatorio(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo, List<?> relatorioList) throws Exception {  

		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		JRRewindableDataSource dataSource = relatorioList == null ? new net.sf.jasperreports.engine.JREmptyDataSource() : new JRBeanCollectionDataSource(relatorioList);

		String logo = servletContext.getRealPath(File.separator + "img" + File.separator + "logo-srh.svg");
		String logo_tce = servletContext.getRealPath(File.separator + "img" + File.separator + "logo-tce-report.png");
		String back = servletContext.getRealPath(File.separator + "img" + File.separator + "bg-topo.png");
		String pathRel = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "relatorios" + File.separator + arquivoRelatorio);  
 		
		parametros.put("LOGO", logo);
		parametros.put("LOGO_TCE", logo_tce);
		parametros.put("BACK", back);

		JasperPrint print;

		print = JasperFillManager.fillReport(pathRel, parametros, dataSource);
		byte[] bytes = JasperExportManager.exportReportToPdf(print);
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);

		return null;
	}	
	
	public String relatorioXls(String arquivoRelatorio, Map<String, Object> parametros, String nomeArquivo) throws Exception {  

		FacesContext facesContext = FacesContext.getCurrentInstance();  
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

		String springVar = "org.springframework.web.context.WebApplicationContext.ROOT";
		WebApplicationContext wctx = (WebApplicationContext) servletContext.getAttribute(springVar);
		DataSource dataSource = (DataSource) wctx.getBean("dataSource");
		Connection connection = dataSource.getConnection();		
		
		String pathRel = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "relatorios" + File.separator + arquivoRelatorio);  

		JasperPrint print = JasperFillManager.fillReport(pathRel, parametros, connection);
		
		ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
		
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setExporterInput(new SimpleExporterInput(print));
		exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outputByteArray));
		
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		configuration.setDetectCellType(true);
		configuration.setWhitePageBackground(false);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		
		exporterXLS.setConfiguration(configuration);
		
		exporterXLS.exportReport();		 
				
		byte[] bytes = outputByteArray.toByteArray();		
		
		writeBytesAsAttachedTextFile(bytes, nomeArquivo);		

		connection.close();		 
		
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
