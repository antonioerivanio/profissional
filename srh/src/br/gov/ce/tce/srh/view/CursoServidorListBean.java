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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.CursoServidorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("cursoServidorListBean")
@Scope("session")
public class CursoServidorListBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoServidorListBean.class);

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private CursoServidorService cursoServidorService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;
	


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	private boolean areaAtuacao;
	private boolean posGraduacao = true;
	private boolean profissional = true;
	private boolean somenteCargaHoraria;

	// entidades das telas
	private Funcional entidade = new Funcional();
	private CursoProfissional cursoProfissional = new CursoProfissional();

	//paginação
	private int count;	
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<PessoalCursoProfissional> pagedList = new ArrayList<PessoalCursoProfissional>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;

	//componente
	private Long totalCargaHoraria;
	private String labelTotalCargaHoraria;
	private Date inicio;
	private Date fim;

	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {
			
			limparListas();
			
			// validando campos da entidade
			if ( getEntidade() == null || getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Para realizar uma consulta, selecione um funcionário.");		
			
			
			count = cursoServidorService.count(getEntidade().getPessoal().getId(), areaAtuacao, posGraduacao, profissional, inicio, fim);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			List<PessoalCursoProfissional> cursos = cursoServidorService.getCursos( getEntidade().getPessoal().getId(), areaAtuacao, posGraduacao, profissional, inicio,fim);
			totalCargaHoraria = new Long(0);
			for (PessoalCursoProfissional curso : cursos) {
				if(curso!=null && curso.getCursoProfissional()!=null && curso.getCursoProfissional().getCargaHoraria()!=null)
					totalCargaHoraria = totalCargaHoraria + curso.getCursoProfissional().getCargaHoraria();
			}
			
			labelTotalCargaHoraria = "Total Carga Horária:";
			
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
			if (  !somenteCargaHoraria && (getEntidade() == null || getEntidade().getPessoal() == null) )
				throw new SRHRuntimeException("Para gerar o Relatório de Curso por Servidor, selecione um funcionário. Para gerar o Relatório de Carga Horária Cursada por Servidor, marque a opção Somente Carga Horária.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");   
			String inicioFormato="";
			String fimFormato="";
			
			if ( inicio != null )
				inicioFormato = formatador.format(inicio); 
			if ( fim != null )
				fimFormato = formatador.format(fim); 
			
			StringBuilder filtro = new StringBuilder();
			filtro.append(" WHERE  1 = 1 ");
			
			if(!somenteCargaHoraria)
				filtro.append(" AND TB_PESSOAL.id = "+getEntidade().getPessoal().getId());
			
			filtro.append(" AND TB_FUNCIONAL.DATASAIDA IS NULL AND TB_FUNCIONAL.IDSITUACAO = 1 " );
			
			if ( inicio != null )
				filtro.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') >= To_Date('"+inicioFormato+"','dd/mm/yyyy') " );			
			if ( fim != null )
				filtro.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') <= To_Date('"+fimFormato+"','dd/mm/yyyy') ");	
			
			if(areaAtuacao)
				filtro.append(" AND TB_PESSOALCURSOPROF.AREAATUACAO = 1 ");			
			if(posGraduacao && !profissional)
				filtro.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO = 1 ");
			else if(!posGraduacao && profissional)
				filtro.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO = 0 ");
			
			System.out.println(filtro.toString());
			
			parametros.put("FILTRO", filtro.toString());
			
			if(somenteCargaHoraria){
				parametros.put("DATA_INICIO", inicioFormato);
				parametros.put("DATA_FIM", fimFormato);
				relatorioUtil.relatorio("cursoServidorSomenteCargaHoraria.jasper", parametros, "cursoPeriodoSomenteCargaHoraria.pdf");
			}else{				
				relatorioUtil.relatorio("cursoServidor.jasper", parametros, "cursoServidor.pdf");
			}

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório de Curso Servidor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
		setEntidade(new Funcional());
		limpar();
		return "listar";
	}
	
	public String limpaTelaServidor() {
		limparListas();
		limpar();
		return "listar";
	}

	public void limpar(){
		totalCargaHoraria = null;
		labelTotalCargaHoraria = "";
		areaAtuacao = false;
		posGraduacao = true;
		profissional = true;
		somenteCargaHoraria = false;
		inicio = null;
		fim = null;
	}
	
	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !matricula.equals("") && !matricula.equals(this.matricula) ) {
			this.matricula = matricula;

			try {
				
				setEntidade( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();
					this.somenteCargaHoraria = false;
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
		if ( !cpf.equals("") && !cpf.equals(this.cpf) ) {
			this.cpf = cpf;

			try {
				
				setEntidade( funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf ));
								
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
					this.matricula = getEntidade().getMatricula();
					this.somenteCargaHoraria = false;
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


	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}	
	

	public CursoProfissional getCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(CursoProfissional cursoProfissional) {this.cursoProfissional = cursoProfissional;}
    
	public Long getTotalCargaHoraria() {return totalCargaHoraria;}
	public void setTotalCargaHoraria(Long totalCargaHoraria) {this.totalCargaHoraria = totalCargaHoraria;}
	
	public boolean isAreaAtuacao() {return areaAtuacao;}
	public void setAreaAtuacao(boolean areaAtuacao) {this.areaAtuacao = areaAtuacao;}
	
	public String getLabelTotalCargaHoraria() {return labelTotalCargaHoraria;}
	public void setLabelTotalCargaHoraria(String labelTotalCargaHoraria) {this.labelTotalCargaHoraria = labelTotalCargaHoraria;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public boolean isPosGraduacao() {return posGraduacao;}
	public void setPosGraduacao(boolean posGraduacao) {	this.posGraduacao = posGraduacao;}
	
	public boolean isProfissional() {return profissional;}
	public void setProfissional(boolean profissional) {	this.profissional = profissional;}
	
	public boolean isSomenteCargaHoraria() {return this.somenteCargaHoraria;}
	public void setSomenteCargaHoraria(boolean somenteCargaHoraria) {this.somenteCargaHoraria = somenteCargaHoraria;}
	
	public void somenteCargaHorariaAction() {	
		this.matricula = null;
		this.cpf = null;
		this.nome = null;	
		this.totalCargaHoraria = null;
		this.labelTotalCargaHoraria = null;
		limparListas();
		flagRegistroInicial = 0;
	}

	
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());						
		} 
		if (!passouConsultar) {
			setCursoProfissional( new CursoProfissional() );
			this.matricula = new String();
			this.cpf = new String();
			this.nome = new String();
			limparListas();
			limpar();
			flagRegistroInicial = 0;
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
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			flagRegistroInicial = getPrimeiroDaPagina();	
			setPagedList(cursoServidorService.search( getEntidade().getPessoal().getId(), areaAtuacao, posGraduacao, profissional, inicio, fim, flagRegistroInicial, dataModel.getPageSize()));
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