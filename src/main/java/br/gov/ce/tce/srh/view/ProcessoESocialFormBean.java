package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Municipio;
import br.gov.ce.tce.srh.domain.ProcessoESocial;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.enums.IndicativoAutoria;
import br.gov.ce.tce.srh.enums.IndicativoMateria;
import br.gov.ce.tce.srh.enums.TipoProcesso;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.MunicipioService;
import br.gov.ce.tce.srh.service.ProcessoESocialService;
import br.gov.ce.tce.srh.service.UfService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("processoESocialFormBean")
@Scope("session")
public class ProcessoESocialFormBean implements Serializable {

	static Logger logger = Logger.getLogger(ProcessoESocialFormBean.class);

	@Autowired
	private ProcessoESocialService service;
	
	@Autowired
	private UfService ufService;

	@Autowired
	private MunicipioService municipioService;

	private ProcessoESocial entidade = new ProcessoESocial();
	private Uf uf;
	private List<Uf> comboUf;
	private List<Municipio> comboMunicipio;
	
	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}

	public String prepareAlterar() {		
		
		try {
			
			if (entidade.getMunicipioVara() != null)
				this.uf = entidade.getMunicipioVara().getUf();

		} catch (Exception e) {			
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}

	public String salvar() {

		try {

			service.salvar(entidade);
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

	private void limpar() {
		this.entidade = new ProcessoESocial();
	}
	
	public List<Uf> getComboUf() {

		try {

			if (this.comboUf == null)
				this.comboUf = ufService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo UF. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboUf;
	}
	
	public List<Municipio> getComboMunicipio() {
		try {

			if (this.uf != null
					|| this.comboMunicipio == null
					|| this.entidade.getMunicipioVara() == null
					|| !this.comboMunicipio.get(0).getUf().equals(this.entidade.getMunicipioVara().getUf()))
			
			this.comboMunicipio = municipioService.findByUF(this.uf.getId());

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os municípios. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return comboMunicipio;
	}
	
	public List<TipoProcesso> getComboTipoProcesso() {
		return Arrays.asList(TipoProcesso.values());
	}
	
	public List<IndicativoAutoria> getComboIndicativoAutoria() {
		return Arrays.asList(IndicativoAutoria.values());
	}
	
	public List<IndicativoMateria> getComboIndicativoMateria() {
		return Arrays.asList(IndicativoMateria.values());
	}

	public ProcessoESocial getEntidade() {
		return entidade;
	}

	public void setEntidade(ProcessoESocial entidade) {
		this.entidade = entidade;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}	

}
