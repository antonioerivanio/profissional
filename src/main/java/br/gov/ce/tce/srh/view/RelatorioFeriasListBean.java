package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RelatorioFeriasService;
import br.gov.ce.tce.srh.service.TipoFeriasService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
 * Use case : Competencia Setor Funcional
 * 
 * @since   : Dez 12, 2012, 12:12:12 PM
 * @author  : raphael.ferreira@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("relatorioFeriasListBean")
@Scope("view")
public class RelatorioFeriasListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(RelatorioFeriasListBean.class);

	@Autowired
	private RelatorioFeriasService relatorioFeriasService;
	
	@Autowired
	private SetorService setorService;

	@Autowired
	private TipoFeriasService tipoFeriasService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;
	
	@Autowired
	private FuncionalService funcionalService;


	//parametos de tela de consulta
	private List<String> tiposFerias;
	private TipoOcupacao tipoOcupacao;
	private Setor setor;
	private Date inicio;
	private Date fim;
	private Long anoReferencia;
	private Integer formato = 1;
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();
	private Funcional funcional;
	private String origem = new String();

	//entidades das telas
	private List<RelatorioFerias> lista;
	private Integer mes;
	private Integer ano;

	// combos
	private List<Setor> comboSetor;
	private List<TipoFerias> comboTipoFerias;
	private List<TipoOcupacao> comboTipoOcupacao;
	
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RelatorioFerias> pagedList = new ArrayList<RelatorioFerias>();
	private int flagRegistroInicial = 0;


	public void consultar() {

		try {
			
			count = relatorioFeriasService.getCountFindByParameter(funcional, setor, tiposFerias, inicio, fim, anoReferencia, tipoOcupacao, origem);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			
		} catch(SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void relatorio() {

		try {		
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String inicioAux = null;
			String fimAux = null;
			String tiposFeriasAux = "";
			
			if (inicio != null){
				inicioAux = dateFormat.format(inicio);
			}
			
			if (fim != null){
				fimAux = dateFormat.format(fim);
			}
			
			StringBuilder paramWhere = new StringBuilder("WHERE 1=1 "); 

			if (funcional!=null && funcional.getId() != null && funcional.getId() != 0L){
				paramWhere.append("and tb_funcional.idpessoal = '" + funcional.getPessoal().getId() + "' " );
			}
			
			if (setor!=null && setor.getId() != null && setor.getId() != 0L){
				paramWhere.append("and tb_funcional.idsetor = '" + setor.getId() + "' " );
			}
			
			if (tiposFerias != null && !tiposFerias.isEmpty()){
				boolean primeiro = true;
				String temp = "";
				temp += "and TB_FERIAS.TIPOFERIAS in (";
				for (String string : tiposFerias) {
					if(primeiro){
						primeiro = false;
					}else{
						tiposFeriasAux += ", ";
					}
					tiposFeriasAux += string;
				}
				temp += tiposFeriasAux + ") ";
				paramWhere.append(temp);
			}
			
			if (inicio != null) {
				paramWhere.append("and TB_FERIAS.INICIO >= to_date('" + inicioAux +"', 'dd/MM/yyyy') ");					
			}
			
			if (fim != null) {
				paramWhere.append("and TB_FERIAS.INICIO <= to_date('" + fimAux +"', 'dd/MM/yyyy') ");					
			}
			
			if (anoReferencia != null && anoReferencia > 0) {
				paramWhere.append("and TB_FERIAS.ANOREFERENCIA = " + anoReferencia +" ");					
			}
			
			if (tipoOcupacao != null) {
				paramWhere.append("and TB_OCUPACAO.TIPOOCUPACAO = " + tipoOcupacao.getId());
			}
			
			if(origem != null && !origem.isEmpty()) {
				if(origem.equals("TCM")) {
					paramWhere.append("and TB_FUNCIONAL.idpessoal IN " );
				} else {
					paramWhere.append("and TB_FUNCIONAL.idpessoal NOT IN ");
				}
				paramWhere.append("( SELECT idpessoal FROM srh.tb_funcional WHERE TO_DATE(TO_CHAR(dataexercicio, 'dd/MM/yyyy'), 'dd/MM/yyyy') = TO_DATE('21/08/2017', 'dd/MM/yyyy') ");
				paramWhere.append(" AND idtipomovimentoentrada IN ( 3, 43 ) ) ");
			}
			
			// Se for escolhido o tipo ocupação Membros
			if (tipoOcupacao != null && tipoOcupacao.getId().intValue() == 1)			
				paramWhere.append(" ORDER BY TB_OCUPACAO.ORDEMOCUPACAO, TB_PESSOAL.NOMECOMPLETO, TB_FERIAS.ANOREFERENCIA DESC, TB_FERIAS.INICIO DESC ");
			else
				paramWhere.append(" ORDER BY TB_PESSOAL.NOMECOMPLETO, TB_FERIAS.ANOREFERENCIA DESC, TB_FERIAS.INICIO DESC ");
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("PARAMWHERE", paramWhere.toString());			
			parametros.put("IDSETORFILTRO", setor == null ? null : setor.getId());
			parametros.put("SETORFILTRO", setor == null ? "TODOS" : setor.getNome());
			parametros.put("NOMEFILTRO", funcional != null ? funcional.getPessoal().getNomeCompleto() : null);
			parametros.put("TIPOSFERIASFILTRO", tiposFerias.isEmpty() ? null : tiposFeriasAux);
			parametros.put("INICIOFILTRO",  inicioAux);
			parametros.put("FIMFILTRO", fimAux);			
			
			
			if (formato == 1) {
				if (funcional != null)
					relatorioUtil.relatorio("feriasServidor.jasper", parametros, "feriasServidor.pdf");
				else	
					relatorioUtil.relatorio("feriasSetor.jasper", parametros, "feriasSetor.pdf");
			} else {
				relatorioUtil.relatorioXls("feriasSetorXLS.jasper", parametros, "feriasSetor.xls");
			}			

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório das Ferias por Setor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}		

	public List<RelatorioFerias> getLista() {return lista;}
	public void setLista(List<RelatorioFerias> lista) {this.lista = lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RelatorioFerias>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(relatorioFeriasService.findByParameter(funcional, setor, tiposFerias, inicio, fim, anoReferencia, tipoOcupacao, origem, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}
	
	public List<Setor> getComboSetor() {

        try {

        	if ( this.comboSetor == null )
        		this.comboSetor = setorService.findTodosAtivos();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}

	public List<RelatorioFerias> getPagedList() {return pagedList;}
	public void setPagedList(List<RelatorioFerias> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO


	public Setor getSetor() {
		return setor;
	}


	public void setSetor(Setor setor) {
		if (setor != null && setor.getId() != null)
			this.setor = setorService.getById(setor.getId());
		else
			this.setor = setor;
	}


	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}

	public List<String> getTiposFerias() {
		return tiposFerias;
	}

	public void setTiposFerias(List<String> tiposFerias) {
		this.tiposFerias = tiposFerias;
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

	public List<TipoFerias> getComboTipoFerias() {
		if(this.comboTipoFerias == null){
			try{
				this.comboTipoFerias = tipoFeriasService.findAll();
			} catch (Exception e) {
	        	FacesUtil.addErroMessage("Erro ao carregar o campo Tipo Ferias. Operação cancelada.");
	        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
		return comboTipoFerias;
	}

	public void setComboTipoFerias(List<TipoFerias> comboTipoFerias) {
		this.comboTipoFerias = comboTipoFerias;
	}

	public Integer getFormato() {
		return formato;
	}

	public void setFormato(Integer formato) {
		this.formato = formato;
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
	
	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}

	public Long getAnoReferencia() {return anoReferencia;}

	public void setAnoReferencia(Long anoReferencia) {this.anoReferencia = anoReferencia;}
	
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {
				funcional =  funcionalService.getCpfAndNomeByMatricula( this.matricula );
				if (funcional != null ) {
					this.nome = funcional.getNomeCompleto();
					this.cpf = funcional.getPessoal().getCpf();	
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {			
				List<Funcional> funcionalList = funcionalService.getMatriculaAndNomeByCpfList( this.cpf );				
				if ( funcionalList != null && funcionalList.size() > 0 ) {
					funcional = funcionalList.get(0);
					this.nome = funcional.getNomeCompleto();
					this.matricula = funcional.getMatricula();	
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}
				
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	
	public void relatorioFeriasAlteradas() {

		try {
			
			if(ano == null || ano <= 0 || mes == null || mes == 0) {
				throw new SRHRuntimeException("Informe valores válidos para os campos Ano e Mês.");
			}
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ANO", ano);
			parametros.put("MES", mes);
			
			relatorioUtil.relatorioXls("ferias_alteradas_no_mes.jasper", parametros, "ferias_alteradas_no_mes.xls");
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
	public Integer getMes() {return mes;}
	public void setMes(Integer mes) {this.mes = mes;}	
	public Integer getAno() {return ano;}
	public void setAno(Integer ano) {this.ano = ano;}
	public String getOrigem() {return origem;}
	public void setOrigem(String origem) {this.origem = origem;}
	

}