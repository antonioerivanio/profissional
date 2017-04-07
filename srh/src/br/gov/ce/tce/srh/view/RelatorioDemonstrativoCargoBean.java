package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@Component("relatorioDemonstrativoCargoBean")
@Scope("session")
public class RelatorioDemonstrativoCargoBean  implements Serializable  {

	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(RelatorioDemonstrativoCargoBean.class);
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;
	
	
	private HtmlForm form;
	
	private Boolean ativoPortal;
	private Integer tipoDeQuantitativo;
	private TipoOcupacao tipoOcupacao;
	private List<TipoOcupacao> comboTipoOcupacao;
		
		
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		ativoPortal = true;		
		tipoDeQuantitativo = 1;
		tipoOcupacao = null;
		comboTipoOcupacao = null;
		return form;
	}	
	
	public String limpar() {
		return "listar";
	}
		
	public String relatorioDemonstrativoDeCargos() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			if(tipoOcupacao != null && tipoOcupacao.getId().intValue() > 0){
				filtro.append(" AND TOC.ID = " + tipoOcupacao.getId().intValue());
			}
			if(this.ativoPortal){
				filtro.append(" AND F.FLPORTALTRANSPARENCIA = 1 ");
			}
						
			parametros.put("FILTRO", filtro.toString());
						
			relatorioUtil.relatorio("demonstrativoDeCargos.jasper", parametros, "demonstrativoDeCargos"	+ ".pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}		
	
	public List<TipoOcupacao> getComboTipoOcupacao() {
		try {
        	if ( this.comboTipoOcupacao == null )
        		this.comboTipoOcupacao = tipoOcupacaoService.findAll();
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
        return this.comboTipoOcupacao;
	}
	public Boolean getAtivoPortal() {return ativoPortal;}
	public void setAtivoPortal(Boolean ativoPortal) {this.ativoPortal = ativoPortal;}
	public Integer getTipoDeQuantitativo() {return tipoDeQuantitativo;}
	public void setTipoDeQuantitativo(Integer tipoDeQuantitativo) {this.tipoDeQuantitativo = tipoDeQuantitativo;}
	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}
	
			

}
