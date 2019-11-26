package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

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
@Scope("view")
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
	private Dependente registroBD = null;
	
//	combos
	private List<Pessoal> comboDependente;
	private List<TipoDependencia> comboTipoDependencia;
	private List<MotivoDependencia> comboMotivoInicio;
	private List<MotivoDependencia> comboMotivoFim;
	
	@PostConstruct		
	public void init() {
		Dependente flashParameter = (Dependente)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Dependente());

		try {
			if(this.entidade.getId() != null) {				
				this.alterar = true;			
				idResponsavel = entidade.getResponsavel().getId();
				nomeResponsavel = entidade.getResponsavel().getNomeCompleto();
				registroBD = dependenteService.getById(entidade.getId());
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
		
	public void salvar() {

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
				
		if (!alterar) {
			if (entidade.getTipoDependencia() != null){
				Long tipoDependencia = entidade.getTipoDependencia().getId(); 
				
				// Conjuge || Companheiro(a) || Filho(a) inválido(a)
				if (tipoDependencia == 1 || tipoDependencia == 2 || tipoDependencia == 4)
					entidade.setTipoDuracao(1L);
				else
					entidade.setTipoDuracao(0L);		
						
			}else{
				entidade.setTipoDuracao(0L);
			}
		}
		
		
		if (entidade.getTipoDependencia() != null) {
		// Filho(a) ou enteado(a) cursando estab. de ensino superior ou escola técnica de 2º grau, até 24 anos
			if (entidade.getTipoDependencia().getId() == 12) {
				entidade.setFlUniversitario(true);
			} else {
				if (registroBD != null)
					entidade.setFlUniversitario(registroBD.isFlUniversitario());
				else
					entidade.setFlUniversitario(false);
			}		
		}
		
		
	}
	
	
	public void motivoInicioSelecionado() {		
		if (entidade.getMotivoInicio() != null){
			Long idMotivo = entidade.getMotivoInicio().getId();
			
			// Invalidez || Casamento || União Estável
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
		registroBD = null;
		
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
