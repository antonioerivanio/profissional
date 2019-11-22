package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.TipoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.TipoDependenciaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@Component("relatorioDependenteBean")
@Scope("view")
public class RelatorioDependenteBean  implements Serializable  {

	private static final long serialVersionUID = 2375679883082367578L;
	static Logger logger = Logger.getLogger(RelatorioServidorBean.class);
	
	@Autowired		
	private TipoDependenciaService tipoDependenciaService;
	
	@Autowired
	private DependenteService dependenteService;
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;	
		
	private List<TipoDependencia> comboTipoDependencia;
		
	private String nomeResponsavel = new String();
	private String cpf = new String();
	private Dependente dependente = new Dependente();
	private boolean darBaixa;
	private boolean pessoaSelecionada;
		
	@PostConstruct
	public void init() {
		this.cpf = new String();
		this.nomeResponsavel = new String();
		this.dependente = new Dependente(); 
		this.darBaixa = false;
		this.pessoaSelecionada = false;
	}
	
	public void relatorio() {

		try {
			
			List<Dependente> dependentes = dependenteService.find(dependente);
			
			if (darBaixa)
				dependentes = dependenteService.listaParaDarBaixa(dependentes);
			
			if (dependentes.size() == 0){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			} else {
				Map<String, Object> parametros = new HashMap<String, Object>();					
				parametros.put("darBaixa", this.darBaixa);									
				relatorioUtil.relatorio("relatorioDependentes.jasper", parametros, "dependentes.pdf", dependentes);
			}		
			
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public List<TipoDependencia> getComboTipoDependencia() {
		try {
        	if ( this.comboTipoDependencia == null )
        		this.comboTipoDependencia = tipoDependenciaService.findAll();
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
        return this.comboTipoDependencia;
	}
	public void setComboTipoDependencia(List<TipoDependencia> comboTipoDependencia) {
		this.comboTipoDependencia = comboTipoDependencia;
	}	
	
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		this.cpf = SRHUtils.removerMascara(cpf);
		if ( !this.cpf.isEmpty() ) {
			try {
				
				List<Pessoal> list = pessoalService.findServidorByNomeOuCpf(null, this.cpf);
				
				if (list.isEmpty()) {
					this.cpf = new String();
					FacesUtil.addInfoMessage("CPF não encontrado.");
				} else {
					Pessoal responsavel = list.get(0);
					this.nomeResponsavel = responsavel.getNomeCompleto();
					this.dependente = new Dependente();
					this.dependente.setResponsavel(responsavel);
					this.pessoaSelecionada = true;
				}
				
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}
	public String getNomeResponsavel() {
		return nomeResponsavel;
	}
	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}
	public Dependente getDependente() {
		return dependente;
	}
	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}
	public boolean isDarBaixa() {
		return darBaixa;
	}
	public void setDarBaixa(boolean darBaixa) {
		this.darBaixa = darBaixa;
	}
	public boolean isPessoaSelecionada() {
		return pessoaSelecionada;
	}
	public void setPessoaSelecionada(boolean pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}
	
}
