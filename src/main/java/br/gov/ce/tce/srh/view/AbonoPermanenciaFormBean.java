package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AbonoPermanencia;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AbonoPermanenciaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("abonoPermanenciaFormBean")
@Scope("session")
public class AbonoPermanenciaFormBean implements Serializable{
	
	static Logger logger = Logger.getLogger(AbonoPermanenciaFormBean.class);
	
	@Autowired
	private AbonoPermanenciaService abonoPermanenciaService;
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	private AbonoPermanencia entidade = new AbonoPermanencia();
	
	private List<Funcional> comboFuncional;
	
	private String cpf = new String();
	private String nome = new String();
	private Pessoal pessoal;
	private String nrProcesso = new String();
	private Funcional funcional;
	
	
	private boolean alterar = false;
	
	public String prepareIncluir() {
		limpar();
		this.alterar = false;
		return "incluirAlterar";
	}
	
	
	public String prepareAlterar() {

		this.alterar = true;

		try {
			
			nome = entidade.getFuncional().getPessoal().getNomeCompleto();
			setCpf(entidade.getFuncional().getPessoal().getCpf());
			nrProcesso = entidade.getProcessoFormatoTela();
			funcional = entidade.getFuncional();			
			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}
	
	
	public String salvar() {

		try {
			
			entidade.setProcesso(this.nrProcesso);
			if (this.funcional != null){
				entidade.setFuncional(funcionalService.getById(funcional.getId()));
			}		
			
			abonoPermanenciaService.salvar(entidade, alterar);
			
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
	
	
	public List<Funcional> getComboFuncional() {

        try {

        	if ( this.pessoal != null ) {
        		comboFuncional = funcionalService.findByPessoal(pessoal.getId(), "DESC"); 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar os dependentes. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

    	return comboFuncional;
	}	
		
	private void limpar() {
		nome = new String();
		cpf = new String();
		nrProcesso = new String();
		pessoal = null;
		entidade = new AbonoPermanencia();			
		comboFuncional = null;
		funcional = null;
	}		

	public boolean isAlterar() {return alterar;}	

	public AbonoPermanencia getEntidade() {return entidade;}
	public void setEntidade(AbonoPermanencia entidade) {this.entidade = entidade;}
	
	public String getNrProcesso() {return nrProcesso;}
	public void setNrProcesso(String nrProcesso) {
		if ( !nrProcesso.equals(this.nrProcesso) ) {
			
			this.nrProcesso = nrProcesso;

			try {

				if (!SRHUtils.validarProcesso( SRHUtils.formatatarDesformatarNrProcessoPadraoSAP(nrProcesso, 0) ) ) {
					throw new SRHRuntimeException("O Número do Processo informado é inválido.");
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

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		this.cpf = SRHUtils.removerMascara(cpf);
		if ( !this.cpf.isEmpty() ) {			
			try {
				
				List<Pessoal> list = pessoalService.findServidorEfetivoByNomeOuCpf(null, this.cpf, true);
				
				this.pessoal = pessoalService.getByCpf(this.cpf);
				
				if (list.isEmpty()) {
					this.cpf = new String();
					FacesUtil.addInfoMessage("CPF não encontrado.");
				} else {
					this.pessoal = list.get(0);
					this.nome = this.pessoal.getNomeCompleto();
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
	}

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}	
	
}
