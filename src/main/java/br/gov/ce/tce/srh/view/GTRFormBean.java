package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.GTR;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.GTRService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("gtrFormBean")
@Scope("view")
public class GTRFormBean implements Serializable {

	static Logger logger = Logger.getLogger(GTRFormBean.class);

	@Autowired
	private GTRService gtrService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private AuthenticationService authenticationService;
	
	private GTR entidade = new GTR();

	private String matricula = new String();
	private String matriculaConsulta = new String();
	private String nome = new String();

	private Date inicio;
	private Date fim;
	private String portaria;
	private Date dtPublicacao;
	private String grupo;
	
	private boolean bloquearDatas = false;
	private boolean alterar = false;

	
	private Integer pagina = 1;

	@PostConstruct
	public void init() {
		GTR flashParameter = (GTR) FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new GTR());
		this.pagina = (Integer) FacesUtil.getFlashParameter("pagina");
		this.matriculaConsulta = (String) FacesUtil.getFlashParameter("matricula");
		
		try {
			if(this.entidade.getId() == null) {
				this.entidade.setId(0L);
			} else {
				
				this.alterar = true;
			
				this.matricula = entidade.getFuncional().getMatricula();
				this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();
	
				this.inicio = entidade.getInicio();
				this.fim = entidade.getFim();
				
				this.dtPublicacao = entidade.getDtPublicacao();
				this.portaria = entidade.getPortaria();
				this.grupo = entidade.getGrupo();
				
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}	
	
	public void salvar() {
		
		try {

			this.entidade.setInicio(this.inicio);
			this.entidade.setFim(this.fim);
			this.entidade.setUsuarioAlteracao(authenticationService.getUsuarioLogado());
			this.entidade.setDtAlteracao(new Date());
			this.entidade.setPortaria(this.portaria);
			this.entidade.setDtPublicacao(this.dtPublicacao);
			this.entidade.setGrupo(this.grupo);
			
			gtrService.salvar(entidade);

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");
			
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public String voltar() {
		if (entidade.getId() != null) {
			if (!alterar) {
				pagina = 1;				
			}
			
			matriculaConsulta = matricula;
		}
		
		FacesUtil.setFlashParameter("matricula", matriculaConsulta);
		FacesUtil.setFlashParameter("pagina", pagina);
        return "listar";
	}
	
	
	
	
	
	public GTR getEntidade() { return entidade; }
	public void setEntidade(GTR entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( matricula != null && (this.matricula == null || !this.matricula.equals(matricula)) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {
		this.fim = fim;
	}	
	
	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}
	
	
	
	public String getPortaria() {
		return portaria;
	}

	public void setPortaria(String portaria) {
		this.portaria = portaria;
	}

	public Date getDtPublicacao() {
		return dtPublicacao;
	}

	public void setDtPublicacao(Date dtPublicacao) {
		this.dtPublicacao = dtPublicacao;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public void atualizaBloqueioDeDatas(){
		
		this.inicio = null;
		this.fim = null;
		this.bloquearDatas = false;
		
	}
	
	
}
