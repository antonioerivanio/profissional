package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

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
@Scope("view")
public class RelatorioDemonstrativoCargoBean  implements Serializable  {

	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(RelatorioDemonstrativoCargoBean.class);
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;	
		
	private Boolean ativoPortal;
	private Boolean ativoPortalAbaQuantitativo;
	private Boolean quantitativoDetalhado;
	private Integer tipoDeQuantitativo;
	private TipoOcupacao tipoOcupacao;
	private List<TipoOcupacao> comboTipoOcupacao;
	private Integer ordem;		
		
	@PostConstruct
	public void init() {
		ativoPortal = true;
		ativoPortalAbaQuantitativo = true;
		quantitativoDetalhado = false;
		tipoDeQuantitativo = 1;		
		ordem = 1;
	}	
		
	public void relatorioDemonstrativoDeCargos() {

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
						
			if(ordem == 1){
				parametros.put("ORDEM", " ORDER BY O.ORDEMOCUPACAO, CARGO, P.NOMECOMPLETO ");
			}else if (ordem == 2){
				parametros.put("ORDEM", " ORDER BY P.NOMECOMPLETO ");
			}			
			
			if (ordem == 1)
				relatorioUtil.relatorio("demonstrativoDeCargos.jasper", parametros, "demonstrativoDeCargos.pdf");
			else {
				relatorioUtil.relatorio("demonstrativoDeCargosAlt1.jasper", parametros, "demonstrativoDeCargos.pdf");
			}
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void relatorioQuantitativoDeCargos() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			if(this.ativoPortalAbaQuantitativo){
				filtro.append(" AND TB_FUNCIONAL.FLPORTALTRANSPARENCIA = 1 ");
			}
						
			parametros.put("FILTRO", filtro.toString());
			parametros.put("DETALHAR", quantitativoDetalhado);
			
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			parametros.put("CAMINHO_COMPLEMENTO", servletContext.getRealPath("//WEB-INF/relatorios/quantitativoCargoComplemento.jasper") );
			parametros.put("CAMINHO_DETALHE",  servletContext.getRealPath("//WEB-INF/relatorios/quantitativoCargoDetalhe.jasper") );
						
			relatorioUtil.relatorio("quantitativoCargo.jasper", parametros, "quantitativoCargo"	+ ".pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public List<TipoOcupacao> getComboTipoOcupacao() {
		try {
        	if ( this.comboTipoOcupacao == null ) {
        		this.comboTipoOcupacao = tipoOcupacaoService.findAll();
        		// Exclui "Pessoal de Obras" da lista
	        	for (TipoOcupacao tipoOcupacao : comboTipoOcupacao) {
					if(tipoOcupacao.getId().intValue() == 7)
						comboTipoOcupacao.remove(tipoOcupacao);
				}
        	}
        	
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
        return this.comboTipoOcupacao;
	}
	
	public Boolean getAtivoPortal() {return ativoPortal;}
	public void setAtivoPortal(Boolean ativoPortal) {this.ativoPortal = ativoPortal;}	
	public Boolean getAtivoPortalAbaQuantitativo() {return ativoPortalAbaQuantitativo;}
	public void setAtivoPortalAbaQuantitativo(Boolean ativoPortalAbaQuantitativo) {this.ativoPortalAbaQuantitativo = ativoPortalAbaQuantitativo;}
	public Boolean getQuantitativoDetalhado() {return quantitativoDetalhado;}
	public void setQuantitativoDetalhado(Boolean quantitativoDetalhado) {this.quantitativoDetalhado = quantitativoDetalhado;}
	public Integer getTipoDeQuantitativo() {return tipoDeQuantitativo;}
	public void setTipoDeQuantitativo(Integer tipoDeQuantitativo) {this.tipoDeQuantitativo = tipoDeQuantitativo;}
	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}
	public Integer getOrdem() {return ordem;}
	public void setOrdem(Integer ordem) {this.ordem = ordem;}			

}
