package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.VinculoRGPS;
import br.gov.ce.tce.srh.domain.TipoVinculoRGPS;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.VinculoRGPSService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.TipoVinculoRGPSService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("feriasFormBean")
@Scope("view")
public class VinculoRGPSFormBean implements Serializable {

	static Logger logger = Logger.getLogger(VinculoRGPSFormBean.class);

	@Autowired
	private VinculoRGPSService VinculoRGPSService;

	@Autowired
	private FuncionalService funcionalService;

	private VinculoRGPS entidade = new VinculoRGPS();

	private String matricula = new String();
	private String matriculaConsulta = new String();
	private String nome = new String();

	private Date inicial;
	private Date fim;

	private boolean bloquearDatas = false;
	private boolean alterar = false;

	private List<TipoVinculoRGPS> comboTipoVinculoRGPS;
	
	private Integer pagina = 1;

	@PostConstruct
	public void init() {
		VinculoRGPS flashParameter = (VinculoRGPS) FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new VinculoRGPS());
		this.pagina = (Integer) FacesUtil.getFlashParameter("pagina");
		this.matriculaConsulta = (String) FacesUtil.getFlashParameter("matricula");
		
		try {
			if(this.entidade.getId() == null) {
				this.entidade.setPeriodo(1L);
			} else {
				
				this.alterar = true;
			
				this.matricula = entidade.getFuncional().getMatricula();
				this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();
	
				this.inicial = entidade.getInicio();
				this.fim = entidade.getFim();
				
				if(entidade.getTipoVinculoRGPS().consideraSomenteQtdeDias())
					bloquearDatas = true;
				else
					bloquearDatas = false;
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}	
	
	public void salvar() {
		
		try {

			this.entidade.setInicio(this.inicial);
			this.entidade.setFim(this.fim);

			VinculoRGPSService.salvar(entidade);

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
	
	public List<TipoVinculoRGPS> getComboTipoVinculoRGPS() {

		try {

			if ( this.comboTipoVinculoRGPS == null )
				this.comboTipoVinculoRGPS = tipoVinculoRGPSService.findAll();

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de ferias. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoVinculoRGPS;
	}
	
	public List<TipoPublicacao> getComboTipoPublicacao() {

		try {

			if ( this.comboTipoPublicacao == null )
				this.comboTipoPublicacao = tipoPublicacaoService.findAll();

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoPublicacao;
	}	
	
	public VinculoRGPS getEntidade() { return entidade; }
	public void setEntidade(VinculoRGPS entidade) { this.entidade = entidade; }

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

	public Date getInicial() {return inicial;}
	public void setInicial(Date inicial) {
		this.inicial = inicial;
		atualizaQtdeDias();
	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {
		this.fim = fim;
		atualizaQtdeDias();
	}	
	
	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}
			
	public void atualizaBloqueioDeDatas(){
		
		this.inicial = null;
		this.fim = null;
		this.entidade.setQtdeDias(null);
		this.bloquearDatas = false;
		
		if ( entidade.getTipoVinculoRGPS() != null && entidade.getTipoVinculoRGPS().consideraSomenteQtdeDias() ) {
			this.bloquearDatas = true;
		}	
	}
	
	private void atualizaQtdeDias(){
		try {
			
			if( this.inicial != null && this.fim != null )
				entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicial, this.fim ));
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro na atualização da quantidade de dias. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
}
