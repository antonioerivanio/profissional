package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CursoServidorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.service.sca.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("cursoPeriodoListBean")
@Scope("session")
public class CursoPeriodoListBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoPeriodoListBean.class);


	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private CursoServidorService cursoServidorService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();
	private Date inicio;
	private Date fim;
	private boolean areaAtuacao;
	private boolean posGraduacao;
	
	private TipoOcupacao tipoOcupacao;
	private List<TipoOcupacao> comboTipoOcupacao;
	
	// entidades das telas
//	private List<FuncionalSetor> lista;
	private Funcional entidade = new Funcional();
//	private List<String> lista;
	private CursoProfissional cursoProfissional = new CursoProfissional();

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<PessoalCursoProfissional> pagedList = new ArrayList<PessoalCursoProfissional>();
	private int flagRegistroInicial = 0;

//componente
	private Long totalCargaHoraria;
	private String labelTotalCargaHoraria;

	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {
			
			// validando campos da entidade
			if ( inicio == null || fim == null )
				throw new SRHRuntimeException("Informe o período ");

//			if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
//				count = funcionalSetorService.count(pessoalService.getByCpf(authenticationService.getUsuarioLogado().getCpf()).getId());
//			} else {
//				count = funcionalSetorService.count( getEntidade().getPessoal().getId() );
//			}
			
			count = cursoServidorService.count(inicio,fim, areaAtuacao,posGraduacao,tipoOcupacao);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
//			List<PessoalCursoProfissional> cursos = cursoServidorService.getCursos( inicio,fim, areaAtuacao,posGraduacao);
			totalCargaHoraria = new Long(0);
//			for (PessoalCursoProfissional curso : cursos) {
//				if(curso!=null && curso.getCursoProfissional()!=null && curso.getCursoProfissional().getCargaHoraria()!=null)
//					totalCargaHoraria = totalCargaHoraria + curso.getCursoProfissional().getCargaHoraria();
//			}
//			
//			labelTotalCargaHoraria = "Total Carga Horária:";
			flagRegistroInicial = -1;
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


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */	
	public String relatorio() {

		try {

			// validando campos da entidade
			if ( inicio == null || fim == null )
				throw new SRHRuntimeException("Selecione um período.");
			   
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");   
			String inicioFormato = formatador.format(inicio); 
			String fimFormato = formatador.format(fim); 

			Map<String, Object> parametros = new HashMap<String, Object>();
			StringBuilder filtro = new StringBuilder();
			
			if (tipoOcupacao != null && tipoOcupacao.getId() != null){
				filtro.append(" INNER JOIN TB_OCUPACAO ON TB_FUNCIONAL.IDOCUPACAO = TB_OCUPACAO.ID " );
				filtro.append(" INNER JOIN TB_TIPOOCUPACAO ON TB_TIPOOCUPACAO.ID = TB_OCUPACAO.TIPOOCUPACAO " );
			}
			
			filtro.append(" WHERE To_Date(To_Char(TB_CURSOPROFISSIONAL.INICIO,'dd/mm/yyyy'),'dd/mm/yyyy') >= To_Date('"+inicioFormato+"','dd/mm/yyyy') " );
			filtro.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.INICIO,'dd/mm/yyyy'),'dd/mm/yyyy') <= To_Date('"+fimFormato+"','dd/mm/yyyy') ");
			filtro.append(" AND TB_FUNCIONAL.DATASAIDA IS NULL AND TB_FUNCIONAL.IDSITUACAO = 1 " );
			
			if(areaAtuacao)
				filtro.append(" AND TB_PESSOALCURSOPROF.AREAATUACAO = 1 ");
			if(!posGraduacao)
				filtro.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO = 0 ");
			
			if (tipoOcupacao != null && tipoOcupacao.getId() != null){				
				filtro.append(" AND TB_TIPOOCUPACAO.ID = " + tipoOcupacao.getId() );
			}		
			 
			
			parametros.put("FILTRO", filtro.toString());
			
			System.out.println(filtro.toString());
			
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

	public String limpaTela() {
		setEntidade(new Funcional());
		totalCargaHoraria = null;
		labelTotalCargaHoraria = "";
		areaAtuacao = false;
		posGraduacao = false;
		inicio = null;
		fim = null;
		tipoOcupacao = null;
		comboTipoOcupacao = null;
		return "listar";
	}

	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {
				if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
					Funcional funcional = funcionalService.getMatriculaAndNomeByCpfAtiva(SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf()));
					setEntidade(funcional);
					if(funcional != null){
						this.matricula = funcional.getMatricula();
					} else {
						this.matricula = new String();
						this.cpf = new String();
						this.nome = new String();
					}
				} else {
					setEntidade( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				}
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();	
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
				
				if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
					this.cpf = authenticationService.getUsuarioLogado().getCpf();
					setEntidade( funcionalService.getMatriculaAndNomeByCpfAtiva( SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf()) ));
				} else {
					setEntidade( funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf ));
				}
				
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.matricula = getEntidade().getMatricula();	
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}

				
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}
	
	
	/**
	 * Combo Tipo Ocupação
	 * @return
	 */
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
	

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

//	public List<FuncionalSetor> getLista() {return lista;}
//	public void setLista(List<FuncionalSetor> lista) {this.lista = lista;}
	
	
	
	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}
	
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
	
	public boolean isPosGraduacao() {return posGraduacao;}
	public void setPosGraduacao(boolean posGraduacao) {this.posGraduacao = posGraduacao;}


	public String getLabelTotalCargaHoraria() {return labelTotalCargaHoraria;}
	public void setLabelTotalCargaHoraria(String labelTotalCargaHoraria) {this.labelTotalCargaHoraria = labelTotalCargaHoraria;}
	
	
	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}


	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setCursoProfissional( new CursoProfissional() );
			this.matricula = new String();
			this.cpf = new String();
			this.nome = new String();
//			this.lista  = new ArrayList<String>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<PessoalCursoProfissional>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();	
			setPagedList(cursoServidorService.search( inicio, fim, areaAtuacao, posGraduacao, tipoOcupacao, getDataTable().getFirst(), getDataTable().getRows()));
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
	//FIM PAGINAÇÃO

}