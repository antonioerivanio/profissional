package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Averbacao;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Municipio;
import br.gov.ce.tce.srh.domain.SubtipoTempoServico;
import br.gov.ce.tce.srh.domain.TipoTempoServico;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.exception.PeriodoConcomitanteException;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AverbacaoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.MunicipioService;
import br.gov.ce.tce.srh.service.SubtipoTempoServicoService;
import br.gov.ce.tce.srh.service.TipoTempoServicoService;
import br.gov.ce.tce.srh.service.UfService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("averbacaoFormBean")
@Scope("view")
public class AverbacaoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AverbacaoFormBean.class);

	@Autowired
	private AverbacaoService averbacaoService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private UfService ufService;
	
	@Autowired
	private TipoTempoServicoService tipoTempoServicoService;
	
	@Autowired
	private SubtipoTempoServicoService subtipoTempoServicoService;	
	
	
	private Averbacao entidade = new Averbacao();

	private String matricula = new String();
	private String nome = new String();

	private Date inicio;
	private Date fim;

	private boolean bloquearDatas = false;
	private boolean alterar = false;
	private boolean somenteDias = false;
	private boolean verificaAverbacaoExistente = true;

	
	private List<Uf> comboUf;
	private List<Municipio> comboMunicipio;
	private List<TipoTempoServico> comboEsfera;
	private List<SubtipoTempoServico> comboSubtipo;

	@PostConstruct
	public void init() {
		Averbacao flashParameter = (Averbacao)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Averbacao());
		
		try {
			if(this.entidade.getId() != null) {
				this.alterar = true;
	
				Funcional funcional = funcionalService.getByPessoaAtivo( getEntidade().getPessoal().getId() );
				this.matricula = funcional.getMatricula();
				this.nome = entidade.getPessoal().getNomeCompleto();
	
				this.inicio = entidade.getInicio();
				this.fim = entidade.getFim();
				
				if (this.inicio == null 
						&& this.fim == null
						&& entidade.getQtdeDias() != null
						&& entidade.getQtdeDias() > 0)
					this.somenteDias = true;
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

			averbacaoService.salvar(entidade, verificaAverbacaoExistente);
			limpar();
			verificaAverbacaoExistente = true;

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");
			
		} catch (SRHRuntimeException e) {			
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (PeriodoConcomitanteException e) {
			verificaAverbacaoExistente = false;
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}		
	}
	

	public List<Uf> getComboUf() {

		try {

			if ( this.comboUf == null )
				this.comboUf = ufService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo UF. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboUf;
	}
	
	
	public void carregaMunicipio() {
		this.comboMunicipio = null;
		getComboMunicipio();
	}
	

	public List<Municipio> getComboMunicipio() {

        try {

			if (getEntidade().getUf() != null && this.comboMunicipio == null ) {
				this.comboMunicipio = municipioService.findByUF( getEntidade().getUf().getId() );
			}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo licença especial. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboMunicipio;
	}


	public List<TipoTempoServico> getComboEsfera() {

		try {

			if ( this.comboEsfera == null)
				this.comboEsfera = tipoTempoServicoService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Esfera. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEsfera;
	}
	
	
	public void carregaSubtipo() {
		this.comboSubtipo = null;
		getComboSubtipo();			
	}


	public List<SubtipoTempoServico> getComboSubtipo() {

		try {

			if ( this.entidade.getEsfera() != null && this.comboSubtipo == null ) {

				this.comboSubtipo = subtipoTempoServicoService.findByTipoTempoServico(this.entidade.getEsfera());
				
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Subtipo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboSubtipo;
	}
	
	
	private void limpar() {		

		setEntidade(new Averbacao());

		this.matricula = new String();
		this.nome = new String();
		
		this.inicio = null;
		this.fim = null;
		this.bloquearDatas = false;
		this.alterar = false;
		this.somenteDias = false;
		
		this.comboUf = null;
		this.comboMunicipio = null;
		this.comboEsfera = null;
		this.comboSubtipo = null;

	}


	public Averbacao getEntidade() { return entidade; }
	public void setEntidade(Averbacao entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( matricula );
				if ( funcional != null ) {
					getEntidade().setPessoal( funcional.getPessoal() );
					this.nome = getEntidade().getPessoal().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {

		this.inicio = inicio;

		try {

			if (this.inicio == null) {
				entidade.setQtdeDias(null);
			} else if ( this.fim != null) {
				entidade.setQtdeDias( (long) SRHUtils.dataDiffAverbacao( this.inicio, this.fim ) );
			}
			

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo data inicial. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {

		this.fim = fim; 

		try {

			if (this.fim == null) {
				entidade.setQtdeDias(null);
			} else if (this.inicio != null) {
				entidade.setQtdeDias( (long) SRHUtils.dataDiffAverbacao( this.inicio, this.fim ) );
			}	

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo data final. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	
	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}	
	public boolean isSomenteDias() { return somenteDias; }
	public void setSomenteDias(boolean somenteDias) { this.somenteDias = somenteDias; }
	
	public void atualizaDatasEQtdeDias() {
		if (somenteDias) {
			this.inicio = null;
			this.fim = null;
		} else {
			this.entidade.setQtdeDias(null);
			this.setInicio(this.entidade.getInicio());
			this.setFim(this.entidade.getFim());
		}		
	}
	
}
