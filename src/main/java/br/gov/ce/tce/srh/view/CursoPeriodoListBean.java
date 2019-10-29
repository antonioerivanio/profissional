package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.enums.TipoCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CursoServidorService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("cursoPeriodoListBean")
@Scope("session")
public class CursoPeriodoListBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoPeriodoListBean.class);


	@Autowired
	private CursoServidorService cursoServidorService;

	@Autowired
	private RelatorioUtil relatorioUtil;
			
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;
	
	@Autowired
	private SetorService setorService;


	private HtmlForm form;
	private boolean passouConsultar = false;

	private Date inicio;
	private Date fim;
	private boolean areaAtuacao;
	private boolean somentePosGraduacao;
	private TipoCursoProfissional tipoCurso;	
	private String curso;
	private Long idCurso;
	
	private TipoOcupacao tipoOcupacao;
	private List<TipoOcupacao> comboTipoOcupacao;
	
	private Setor setor;
	private List<Setor> comboSetor;
	
	private CursoProfissional cursoProfissional = new CursoProfissional();

	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<PessoalCursoProfissional> pagedList = new ArrayList<PessoalCursoProfissional>();
	private int registroInicial = 0;
	private Integer pagina = 1;

	private Long totalCargaHoraria;
	private String labelTotalCargaHoraria;
	
	
	public String consultar() {

		try {
			
			limparListas();
			
			validaCamposObrigatorios();			
			
			count = cursoServidorService.count(inicio, fim, areaAtuacao, tipoCurso, somentePosGraduacao, tipoOcupacao, setor, idCurso);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			totalCargaHoraria = 0L;

			registroInicial = -1;
			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}
	

	public String relatorio() {

		try {

			validaCamposObrigatorios();
			   
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");			 

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			filtro.append(" WHERE TB_FUNCIONAL.DATASAIDA IS NULL AND TB_FUNCIONAL.IDSITUACAO = 1 " );
			
			if(inicio != null)
				filtro.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') >= To_Date('"+formatador.format(inicio)+"','dd/mm/yyyy') " );
			
			if(fim != null)
				filtro.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') <= To_Date('"+formatador.format(fim)+"','dd/mm/yyyy') ");
			
			if(areaAtuacao)
				filtro.append(" AND TB_PESSOALCURSOPROF.AREAATUACAO = 1 ");			
			
			if(tipoCurso != null)
				filtro.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO = " + tipoCurso.ordinal());
			
			if(somentePosGraduacao)
				filtro.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO > 0" );
			
			if (tipoOcupacao != null && tipoOcupacao.getId() != null){				
				filtro.append(" AND TB_TIPOOCUPACAO.ID = " + tipoOcupacao.getId() );
			}
			
			if (setor != null && setor.getId() != null){				
				filtro.append(" AND SAPJAVA.SETOR.IDSETOR = " + setor.getId() );
			}
			
			if (idCurso != null && idCurso.intValue() != 0) {				
				filtro.append(" AND TB_CURSOPROFISSIONAL.ID = " + idCurso );
			}
				
			parametros.put("FILTRO", filtro.toString());
			
			
			if(somentePosGraduacao) {
				parametros.put( "ORDEM", " ORDER BY TB_PESSOAL.NOMECOMPLETO, TB_CURSOPROFISSIONAL.POSGRADUACAO, TB_CURSOPROFISSIONAL.INICIO DESC, TB_CURSOPROFISSIONAL.ID DESC ");			
			} else {
				parametros.put( "ORDEM", " ORDER BY TB_CURSOPROFISSIONAL.INICIO DESC, TB_CURSOPROFISSIONAL.ID DESC, TB_PESSOAL.NOMECOMPLETO ");
			}
			
			
			relatorioUtil.relatorio("cursoPeriodo.jasper", parametros, "cursoPeriodo.pdf");			

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório de Curso por Período. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	
	private void validaCamposObrigatorios(){
		if ( (idCurso == null || idCurso == 0) && (inicio == null || fim == null) && !somentePosGraduacao )
			throw new SRHRuntimeException("Informe o período, pesquise um curso ou marque Somente Pós-Graduação");
	}
	

	public String limpaTela() {
		limparVariaveis();
		return "listar";
	}
	
	
	public void limparVariaveis() {
		setCursoProfissional( new CursoProfissional() );
		totalCargaHoraria = null;
		labelTotalCargaHoraria = "";
		areaAtuacao = false;
		somentePosGraduacao = false;
		tipoCurso = null;
		inicio = null;
		fim = null;
		tipoOcupacao = null;
		comboTipoOcupacao = null;
		curso = null;
		idCurso = null;
		tipoOcupacao = null;
		comboTipoOcupacao = null;
		setor = null;
		comboSetor = null;
	}
	
	
	public List<TipoOcupacao> getComboTipoOcupacao() {

        try {

        	if( this.comboTipoOcupacao == null )
        		this.comboTipoOcupacao = tipoOcupacaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo ocupação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoOcupacao;
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
	
	
	public List<TipoCursoProfissional> getComboTipoCurso() {
		return Arrays.asList(TipoCursoProfissional.values());
	}
	
		
	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public CursoProfissional getCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(CursoProfissional cursoProfissional) {this.cursoProfissional = cursoProfissional;}
    
	public Long getTotalCargaHoraria() {return totalCargaHoraria;}
	public void setTotalCargaHoraria(Long totalCargaHoraria) {this.totalCargaHoraria = totalCargaHoraria;}
	
	public boolean isAreaAtuacao() {return areaAtuacao;}
	public void setAreaAtuacao(boolean areaAtuacao) {this.areaAtuacao = areaAtuacao;}
	
	public TipoCursoProfissional getTipoCurso() {return tipoCurso;}
	public void setTipoCurso(TipoCursoProfissional tipoTitulacao) {this.tipoCurso = tipoTitulacao;}
	
	public String getLabelTotalCargaHoraria() {return labelTotalCargaHoraria;}
	public void setLabelTotalCargaHoraria(String labelTotalCargaHoraria) {this.labelTotalCargaHoraria = labelTotalCargaHoraria;}	
	
	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}	

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}
		
	public String getCurso() {return curso;}
	public void setCurso(String curso) {this.curso = curso;}

	public Long getIdCurso() {return idCurso;}
	public void setIdCurso(Long idCurso) {this.idCurso = idCurso;}

	public boolean isSomentePosGraduacao() {return somentePosGraduacao;}
	public void setSomentePosGraduacao(boolean somentePosGraduacao) {this.somentePosGraduacao = somentePosGraduacao;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			limparVariaveis();
			limparListas();
			registroInicial = 0;			
		}
		passouConsultar = false;
		return form;
	}
	
	
	//PAGINAÇÃO
	
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<PessoalCursoProfissional>();
		pagina = 1;
	}	

	public PagedListDataModel getDataModel() {
		if( registroInicial != getPrimeiroDaPagina() ) {
			registroInicial = getPrimeiroDaPagina();	
			setPagedList(cursoServidorService.search( inicio, fim, areaAtuacao, tipoCurso, somentePosGraduacao, tipoOcupacao, setor, idCurso, registroInicial, dataModel.getPageSize()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
			
		}
		return dataModel;
	}

	public List<PessoalCursoProfissional> getPagedList() {return pagedList;}
	public void setPagedList(List<PessoalCursoProfissional> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	
	//FIM PAGINAÇÃO

}