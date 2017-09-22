package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.MotivoDependencia;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.TipoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteService;
import br.gov.ce.tce.srh.service.MotivoDependenciaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.TipoDependenciaService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("dependenteFormBean")
@Scope("session")
public class DependenteFormBean implements Serializable{
	
	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoFormBean.class);
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired		
	private TipoDependenciaService tipoDependenciaService;
	
	@Autowired
	private MotivoDependenciaService motivoDependenciaService;
	
	@Autowired
	private DependenteService dependenteService;
	
	
//	entidades das telas
	private Long idResponsavel = 0L;
	private String nomeResponsavel;
	
	private Long idDependente = 0L;
	
	
	private boolean alterar = false;

	private Dependente entidade = new Dependente();
	
//	combos
	private List<Pessoal> comboDependente;
	private List<TipoDependencia> comboTipoDependencia;
	private List<MotivoDependencia> comboMotivoInicio;
	private List<MotivoDependencia> comboMotivoFim;
	
	
	public String prepareIncluir() {
		limpar();
		this.alterar = false;
		return "incluirAlterar";
	}
	
	
	public String prepareAlterar() {

		this.alterar = true;

		try {
			
			idResponsavel = entidade.getResponsavel().getId();
			nomeResponsavel = entidade.getResponsavel().getNomeCompleto();				

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}
	
	
	public String salvar() {

		try {
			
			dependenteService.salvar(entidade, alterar);
			
			this.alterar = false;
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	
	public List<Pessoal> getComboDependente() {

        try {

        	if ( comboDependente == null ) {
        		comboDependente = pessoalService.findByCategoria(4L); // DEPENDENTES -> IDCATEGORIA = 4 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar os dependentes. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

    	return comboDependente;
	}
	
	
	public List<TipoDependencia> getComboTipoDependencia() {

        try {

        	if ( comboTipoDependencia == null ) {
        		comboTipoDependencia = tipoDependenciaService.findAll(); 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar os tipos de dependência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

    	return comboTipoDependencia;
	}
	
	
	public List<MotivoDependencia> getComboMotivoInicio() {

        try {

        	if ( comboMotivoInicio == null ) {
        		comboMotivoInicio = motivoDependenciaService.findByTipo(1L); 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar os motivos de dependência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

    	return comboMotivoInicio;
	}
	
	
	public List<MotivoDependencia> getComboMotivoFim() {

        try {

        	if ( comboMotivoFim == null ) {
        		comboMotivoFim = motivoDependenciaService.findByTipo(2L); 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar os motivos de dependência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

    	return comboMotivoFim;
	}
	
	
	public void tipoDependenciaSelecionado() {
		if (entidade.getTipoDependencia() != null){
			if (entidade.getTipoDependencia().getId() == 1)
				entidade.setTipoDuracao(1L);
			else
				entidade.setTipoDuracao(0L);		
		}else{
			entidade.setTipoDuracao(0L);
		}
	}
	
	
	public void motivoInicioSelecionado() {		
		if (entidade.getMotivoInicio() != null){
			Long idMotivo = entidade.getMotivoInicio().getId();
			
			if(idMotivo == 6 || idMotivo == 7 || idMotivo == 8)
				entidade.setTipoDuracao(1L);
			else
				entidade.setTipoDuracao(0L);
		}else{
			entidade.setTipoDuracao(0L);
		}		
	}
	
	
	private void limpar() {

		idResponsavel = 0L;
		nomeResponsavel = null;
		
		idDependente = 0L;
		
		entidade = new Dependente();
		
		// combos
		comboDependente = null;
		comboTipoDependencia = null;
		comboMotivoInicio = null;
		comboMotivoFim = null;
		
	}


	public Long getIdResponsavel() {return idResponsavel;}
	public void setIdResponsavel(Long idResposavel) {
		if ( !this.idResponsavel.equals( idResposavel ) ) {
			this.idResponsavel = idResposavel;

			try {

				entidade.setResponsavel( pessoalService.getById( this.idResponsavel ));

			} catch (Exception e) {
				FacesUtil.addInfoMessage("Erro ao carregar o responsável. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: "+ e.getMessage());
			}			
		}	
	}

	public String getNomeResponsavel() {return nomeResponsavel;}
	public void setNomeResponsavel(String nomeResponsavel) {this.nomeResponsavel = nomeResponsavel;}

	public Long getIdDependente() {return idDependente;}
	public void setIdDependente(Long idDependente) {
		if ( !this.idDependente.equals( idDependente ) ) {
			this.idDependente = idDependente;

			try {

				entidade.setDependente( pessoalService.getById( this.idDependente ));

			} catch (Exception e) {
				FacesUtil.addInfoMessage("Erro ao carregar o dependente. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: "+ e.getMessage());
			}			
		}		
	}	

	public boolean isAlterar() {return alterar;}	

	public Dependente getEntidade() {return entidade;}
	public void setEntidade(Dependente entidade) {this.entidade = entidade;}	
	
}
