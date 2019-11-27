package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Deducao;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DeducaoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("deducaoFormBean")
@Scope("view")
public class DeducaoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(DeducaoFormBean.class);

	@Autowired
	private DeducaoService deducaoService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private ParametroService parametroService;

	//endidade das telas
	private Deducao entidade = new Deducao();
	private String nrProcesso = new String();

	private String matricula = new String();
	private String nome = new String();

	private Date inicio;
	private Date fim;

	private boolean bloquearDatas = false;
	private boolean alterar = false;
	private boolean nrProcessoValido = false;

	@PostConstruct
	public void init() {
		Deducao flashParameter = (Deducao)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Deducao());

		try {
			if(this.entidade.getId() != null) {				
				this.alterar = true;
				
				Funcional funcional = funcionalService.getByPessoaAtivo( getEntidade().getPessoal().getId() );
				this.matricula = funcional.getMatricula();
				this.nome = entidade.getPessoal().getNomeCompleto();
				
				this.inicio = entidade.getInicio();
				this.fim = entidade.getFim();
				
				if(getEntidade().getNrProcesso() != null){
					this.nrProcesso = SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(getEntidade().getNrProcesso(), 1);
				} else {
					this.nrProcesso = getNrProcesso();
				}
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
			Parametro p = parametroService.getByNome("PROCESSOFALTA");
			if (p.getValor().equals("0") && this.nrProcesso.equals("")) {
				FacesUtil.addErroMessage("O Número do processo é obrigatório.");
				logger.warn("Ocorreu o seguinte erro: O Número do Processo informado é inválido.");
			} else {
				if(!nrProcesso.isEmpty()){
					this.entidade.setNrProcesso(SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(nrProcesso, 0));
				}
				
				if(p.getValor().equals("1") || nrProcessoValido)  {
					deducaoService.salvar(entidade);
					limpar();
					FacesUtil.addInfoMessage("Operação realizada com sucesso.");
					logger.info("Operação realizada com sucesso.");
				} 
			}
		}  catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} 
		
		nrProcesso = new String();
	}

	private void limpar() {

		this.alterar = false;

		setEntidade(new Deducao());

		this.matricula = new String();
		this.nome = new String();
		
		this.inicio = null;
		this.fim = null;
		this.bloquearDatas = false;
		
		nrProcesso = new String();
	}
	
	public Deducao getEntidade() { return entidade; }
	public void setEntidade(Deducao entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula );
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

	public String getNrProcesso() {return nrProcesso;}
	public void setNrProcesso(String nrProcesso) {
		if ( !this.nrProcesso.equals(nrProcesso) ) {
			this.nrProcesso = nrProcesso;

			try {

				if (!SRHUtils.validarProcesso(SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(this.nrProcesso, 0)) ) {
					nrProcessoValido = false;
					FacesUtil.addErroMessage("O Número do Processo informado é inválido.");
					logger.warn("Ocorreu o seguinte erro: O Número do Processo informado é inválido.");
				    
				} else {
					nrProcessoValido = true;
				}

			} catch (SRHRuntimeException e) {
				FacesUtil.addErroMessage(e.getMessage());
				logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro no campo processo. Operação cancelada.");
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

			if ( this.inicio != null && this.fim != null)
				entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicio, this.fim ));

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

			if (this.inicio != null && this.fim != null )
				entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicio, this.fim ) );

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
}
