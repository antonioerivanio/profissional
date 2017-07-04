package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@Component("relatorioServidorBean")
@Scope("session")
public class RelatorioServidorBean  implements Serializable  {

	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(RelatorioServidorBean.class);
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	private HtmlForm form;
		
	//por setor
	private Setor setor;
	private Integer vinculo; 
	private Boolean ativoPortal;
	private List<Setor> comboSetor;
	
	//por sexo	
	private Integer sexo;
	private Boolean incluirMembros;
	private Boolean agruparServidoresQueTemFilhos;
	
	//aniversariantes
	private Integer mes;
	private Integer ordemAniversariantes;
	
	//falecidos
	private Date inicio;
	private Date fim;
			
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		setor = null;
		vinculo = null;
		ativoPortal = true;
		comboSetor = null;
		sexo = 0;					
		incluirMembros = false;
		agruparServidoresQueTemFilhos = false;
		mes = 0;
		ordemAniversariantes = 1;
		inicio = null;
		fim = null;
		return form;
	}	
	
	public String limpar() {
		return "listar";
	}
	
	public String relatorioPorSetor() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			if(this.ativoPortal){
				filtro.append("AND F.FLPORTALTRANSPARENCIA = 1 ");
			}
			
			if(this.setor != null){
				filtro.append("AND S.IDSETOR = " + this.setor.getId() + " ");
			}
			
			if(vinculo == 1){ // MEMBROS
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND TOC.ID = 1 ");
				filtro.append("ORDER BY O.ORDEMOCUPACAO, S.NRORDEMSETORFOLHA, P.NOMECOMPLETO");
			}else if(vinculo == 2){ // SERVIDORES ATIVOS
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND O.SITUACAO < 3 ");
				filtro.append("AND TOC.ID IN (2, 3, 6) ");
//				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, F.IDFOLHA, O.ORDEMOCUPACAO, P.NOMECOMPLETO");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, RC.SIMBOLO, P.NOMECOMPLETO");
			}else if(vinculo == 3){ // SERVIDORES EFETIVOS
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND O.SITUACAO < 3 ");
				filtro.append("AND (TOC.ID = 2) ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, F.IDFOLHA, O.ORDEMOCUPACAO, P.NOMECOMPLETO");
			}else if(vinculo == 4){ // SERVIDORES INATIVOS
				filtro.append("AND F.STATUS = 5 ");
				filtro.append("AND P.DATAOBITO IS NULL ");
				filtro.append("ORDER BY O.ORDEMOCUPACAO, P.NOMECOMPLETO");
			}else if(vinculo == 5){ // OCUPANTES DE CARGO COMISSIONADO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");					
				filtro.append("AND RF.ID IS NOT NULL ");
				filtro.append("AND O.SITUACAO < 3 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, P.NOMECOMPLETO");
			}else if(vinculo == 6){ // OCUPANTES SOMENTE CARGO COMISSIONADO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 1 ");
				filtro.append("AND RF.ID IS NOT NULL ");
				filtro.append("AND TOC.ID = 6 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, P.NOMECOMPLETO");
			}else if(vinculo == 7){ // ESTAGIÁRIOS NÍVEL UNIVERSITÁRIO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 2 ");
				filtro.append("AND TOC.ID = 5 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, P.NOMECOMPLETO");
			}else if(vinculo == 8){ // ESTAGIÁRIOS NÍVEL MÉDIO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS = 2 ");
				filtro.append("AND TOC.ID = 4 ");
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, P.NOMECOMPLETO");
			}else if(vinculo == 9){ // CESSÃO DE SERVIDOR SEM NENHUMA REMUNERAÇÃO
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND O.SITUACAO < 3 ");	
				filtro.append("AND TOC.ID = 8 ");
				filtro.append("ORDER BY P.NOMECOMPLETO");
			}else{
				filtro.append("AND F.DATASAIDA IS NULL ");
				filtro.append("AND F.STATUS < 3 ");
				filtro.append("AND O.SITUACAO < 3 ");				 
				filtro.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, P.NOMECOMPLETO");
			}
			
			parametros.put("FILTRO", filtro.toString());
			parametros.put("VINCULO", vinculo);
				
			if(vinculo == 1 || vinculo >= 7)
				relatorioUtil.relatorio("servidorSetorAlt1.jasper", parametros, "servidorSetor.pdf");
			else if(vinculo == 4)
				relatorioUtil.relatorio("servidorSetorAlt2.jasper", parametros, "servidorSetor.pdf");
			else if(vinculo == 6)
				relatorioUtil.relatorio("servidorSetorAlt3.jasper", parametros, "servidorSetor.pdf");
			else
				relatorioUtil.relatorio("servidorSetor.jasper", parametros, "servidorSetor.pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
		
	public String relatorioPorSexo() {

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
	
	public String relatorioAniversariantes() {

		try {
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			if ( mes > 0 )
				filtro.append(" AND MESANIVERSARIO = " + mes );
			
			parametros.put("FILTRO", filtro.toString());
			
			if ( ordemAniversariantes == 1 ) {
				parametros.put("ORDEM", "ORDER BY mesAniversario, diaAniversario, P.NOMECOMPLETO");
			} if ( ordemAniversariantes == 2 ) {
				parametros.put("ORDEM", "ORDER BY mesAniversario, P.NOMECOMPLETO");
			}			
			
			relatorioUtil.relatorio("servidoresAniversariantes.jasper", parametros, "aniversariantes"	+ ".pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	public String relatorioFalecidos() {

		try {
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
						
			if ( inicio == null && fim == null ) {
				filtro.append(" AND p.DATAOBITO IS NOT NULL " );
			} else {			
				if ( inicio != null )
					filtro.append(" AND p.DATAOBITO >= TO_DATE('" + SRHUtils.formataData(SRHUtils.FORMATO_DATA, inicio) + "', 'DD/MM/YYYY') " );
				
				if ( fim != null )
					filtro.append(" AND p.DATAOBITO <= TO_DATE('" + SRHUtils.formataData(SRHUtils.FORMATO_DATA, fim) + "', 'DD/MM/YYYY') " );			
			}
			
			parametros.put("FILTRO", filtro.toString());						
			
			relatorioUtil.relatorio("servidoresFalecidos.jasper", parametros, "falecidos"	+ ".pdf");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	
	public List<Setor> getComboSetor() {
		try {
        	if ( this.comboSetor == null )
        		this.comboSetor = setorService.findAll();
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
        return this.comboSetor;
	}
	public void setComboSetor(List<Setor> comboSetor) {this.comboSetor = comboSetor;}
	public Integer getSexo() {return sexo;}
	public void setSexo(Integer sexo) {this.sexo = sexo;}
	public Boolean getIncluirMembros() {return incluirMembros;}
	public void setIncluirMembros(Boolean incluirMembros) {this.incluirMembros = incluirMembros;}
	public Boolean getAgruparServidoresQueTemFilhos() {return agruparServidoresQueTemFilhos;}
	public void setAgruparServidoresQueTemFilhos(Boolean agruparServidoresQueTemFilhos) {this.agruparServidoresQueTemFilhos = agruparServidoresQueTemFilhos;}
	public Integer getMes() {return mes;}
	public void setMes(Integer mes) {this.mes = mes;}
	public Integer getOrdemAniversariantes() {return ordemAniversariantes;}
	public void setOrdemAniversariantes(Integer ordemAniversariantes) {this.ordemAniversariantes = ordemAniversariantes;}
	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}
	public Integer getVinculo() {return vinculo;}
	public void setVinculo(Integer vinculo) {this.vinculo = vinculo;}
	public Boolean getAtivoPortal() {return ativoPortal;}
	public void setAtivoPortal(Boolean ativoPortal) {this.ativoPortal = ativoPortal;}
	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}
	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}			

}
