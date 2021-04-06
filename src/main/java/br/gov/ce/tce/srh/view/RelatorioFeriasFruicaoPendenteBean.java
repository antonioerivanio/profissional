package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("relatorioFeriasFruicaoPendenteBean")
@Scope("view")
public class RelatorioFeriasFruicaoPendenteBean implements Serializable {
	
	static Logger logger = Logger.getLogger(RelatorioFeriasFruicaoPendenteBean.class);

	@Autowired
	private RelatorioUtil relatorioUtil;

	private String origem = new String();
	private Long anoReferencia;
	private Date inicio;
	private Date fim;	
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public void relatorio() {

		try {			
			
			Map<String, Object> parametros = new HashMap<String, Object>();
	
			String filtro_origem = "";
			if (origem != null && !origem.isEmpty()) {
				parametros.put("ORIGEM", origem);
				filtro_origem += " and origem = '" + origem + "' ";
			}
			parametros.put("FILTRO_ORIGEM", filtro_origem);		
			
			String filtro_ferias = "";
			
			if (anoReferencia != null && anoReferencia > 0) {
				parametros.put("ANO_REF", anoReferencia);
				filtro_ferias += " AND fe.anoreferencia = " + anoReferencia +" ";					
			}
			
			if (inicio != null) {
				parametros.put("PERIODO_INICIO", dateFormat.format(inicio));
				filtro_ferias += " AND fe.inicio >= to_date('" + dateFormat.format(inicio) +"', 'dd/MM/yyyy') ";					
			}
			
			if (fim != null) {
				parametros.put("PERIODO_FIM", dateFormat.format(fim));
				filtro_ferias += " AND fe.inicio <= to_date('" + dateFormat.format(fim) +"', 'dd/MM/yyyy') ";					
			}
			
			parametros.put("FILTRO_FERIAS", filtro_ferias);			
			
			
			relatorioUtil.relatorioXls("ferias_fruicao_pendente.jasper", parametros, "ferias_fruicao_pendente.xls");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public Long getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(Long anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}	

}