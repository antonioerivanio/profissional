package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Empregador;
import br.gov.ce.tce.srh.enums.ClassificacaoTributaria;
import br.gov.ce.tce.srh.enums.NaturezaJuridica;
import br.gov.ce.tce.srh.enums.SimNao;
import br.gov.ce.tce.srh.enums.SituacaoPj;
import br.gov.ce.tce.srh.enums.Subteto;
import br.gov.ce.tce.srh.enums.TipoInscricao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EmpregadorService;
//import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("empregadorFormBean")
@Scope("view")
public class EmpregadorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(EmpregadorFormBean.class);

	@Autowired
	private EmpregadorService service;
	
//	@Autowired
//	private PessoalService pessoalService;

	private Empregador entidade;	
	private Long idContato;
	private String nomeContato;
	
	@PostConstruct
	private void init() {		
		Empregador flashParameter = (Empregador)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Empregador());
		
//		if (entidade.getContato() != null) {			
//			idContato = entidade.getContato().getId();
//			nomeContato = entidade.getContato().getNomeCompleto();
//		}
		
		if(entidade.getEsocialVigencia() == null) {
			entidade.setEsocialVigencia(new ESocialEventoVigencia());
		}	
    }

	public void salvar() {

		try {

			service.salvar(entidade);
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	private void limpar() {
		this.entidade = new Empregador();
		this.nomeContato = null;
		this.idContato = null;
	}

	public Empregador getEntidade() {
		return entidade;
	}

	public void setEntidade(Empregador entidade) {
		this.entidade = entidade;
	}
	
	public List<TipoInscricao> getComboTipoInscricao() {
		return Arrays.asList(TipoInscricao.values());
	}
	
	public List<SimNao> getComboSimNao() {
		return Arrays.asList(SimNao.values());
	}
	
	public List<ClassificacaoTributaria> getComboClassificacaoTributaria() {
		return Arrays.asList(ClassificacaoTributaria.values());
	}
	
	public List<NaturezaJuridica> getComboNaturezaJuridica() {
		return Arrays.asList(NaturezaJuridica.values());
	}
	
	public List<Subteto> getComboSubteto() {
		return Arrays.asList(Subteto.values());
	}
	
	public List<SituacaoPj> getComboSituacaoPj() {
		return Arrays.asList(SituacaoPj.values());
	}

	public Long getIdContato() {
		return idContato;
	}

//	public void setIdContato(Long idContato) {
//		if ( idContato != null && (this.idContato == null || !this.idContato.equals(idContato)) ) {
//			this.idContato = idContato;
//			try {
//				entidade.setContato(pessoalService.getById(this.idContato));
//			} catch (Exception e) {
//				FacesUtil.addInfoMessage("Erro ao carregar o contato. Operação cancelada.");
//				logger.fatal("Ocorreu o seguinte erro: "+ e.getMessage());
//			}			
//		}
//	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}
	

}
