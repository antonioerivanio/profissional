package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.enums.ContagemEspecial;
import br.gov.ce.tce.srh.enums.SituacaoLei;
import br.gov.ce.tce.srh.enums.TipoAcumuloDeCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EscolaridadeService;
import br.gov.ce.tce.srh.service.EspecialidadeCargoService;
import br.gov.ce.tce.srh.service.EspecialidadeService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.service.SimboloService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("cargoFormBean")
@Scope("view")
public class CargoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CargoFormBean.class);

	@Autowired
	private OcupacaoService ocupacaoService;

	@Autowired
	private EspecialidadeCargoService especialidadeCargoService;

	@Autowired
	private EspecialidadeService especialidadeService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private SimboloService simboloService;

	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;


	// entidades das telas
	private Ocupacao entidade = new Ocupacao();	

	private EspecialidadeCargo entidadeEspecialidade = new EspecialidadeCargo();
	private List<EspecialidadeCargo> listaEspecialidade = new ArrayList<EspecialidadeCargo>();
	
	// simbolo
	private String simbologia = new String();
	private Simbolo entidadeSimbolo = new Simbolo();
	private List<Simbolo> listaSimbolo = new ArrayList<Simbolo>();

	// combos
	private List<Especialidade> comboEspecialidade;
	private List<Escolaridade> comboEscolaridade;
	private List<TipoOcupacao> comboTipoOcupacao;

	
	@PostConstruct
	private void init() {
		Ocupacao flashParameter = (Ocupacao)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Ocupacao());
		if(entidade.getEsocialVigencia() == null) {
			entidade.setEsocialVigencia(new ESocialEventoVigencia());
		}			
		
		this.listaEspecialidade = especialidadeCargoService.findByOcupacao( entidade.getId() );
		this.listaSimbolo = simboloService.findByOcupacao( entidade.getId() );			
    }

	
	public void salvar() {

		try {

			this.entidade = ocupacaoService.salvar(entidade, listaEspecialidade, listaSimbolo);			

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

	public void addEspecialidade() {

		try {

			// validando o campo obrigatorio Simbologia
			if (entidadeEspecialidade.getEspecialidade() == null || entidadeEspecialidade.getEspecialidade().getId() == 0l) {
				FacesUtil.addErroMessage("A especialidade é obrigatória.");
			}

			// verificando se a especialidade ja foi cadatrada
			for (EspecialidadeCargo item : listaEspecialidade) {
				if (item.getEspecialidade().getId().equals(entidadeEspecialidade.getEspecialidade().getId())) {
					FacesUtil.addErroMessage("A especialidade já está na lista.");
				}
			}

			entidadeEspecialidade.setOcupacao(entidade);
			entidadeEspecialidade.setEspecialidade(especialidadeService.getById(entidadeEspecialidade.getEspecialidade().getId()));

			listaEspecialidade.add(entidadeEspecialidade);

			this.entidadeEspecialidade = new EspecialidadeCargo();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao adicionar a especialidade. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void excluirEspecialidade() {

		if (this.entidadeEspecialidade.getId() != null) {

			try {

				especialidadeCargoService.excluir(entidadeEspecialidade);
				listaEspecialidade.remove(entidadeEspecialidade);
				this.entidadeEspecialidade = new EspecialidadeCargo();

			} catch (Exception e) {
				FacesUtil.addErroMessage("Existem registros filhos utilizando a especialidade excluida. Exclusão não poderá ser realizada.");
			}

		}
	}

	public void addSimbologia() {

		try {

			// validando o campo obrigatorio Simbologia
			if (simbologia == null || simbologia.equalsIgnoreCase("")) {
				FacesUtil.addErroMessage("A simbologia é obrigatória.");
			}

			// verificando se a simbologia ja foi cadatrada
			for (Simbolo item : listaSimbolo) {
				if (item.getSimbolo().equalsIgnoreCase(simbologia)) {
					FacesUtil.addErroMessage("A simbologia já está na lista.");
				}
			}

			Simbolo simbolo = new Simbolo();
			simbolo.setSimbolo(simbologia);

			listaSimbolo.add(simbolo);

			this.simbologia = new String();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao adicionar a simbologia. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void excluirSimbologia() {

		if (this.entidadeSimbolo.getId() != null) {

			try {

				simboloService.excluir(entidadeSimbolo);
				listaSimbolo.remove(entidadeSimbolo);
				this.simbologia = new String();

			} catch (Exception e) {
				FacesUtil.addErroMessage("Existem registros filhos utilizando o simbolo excluido. Exclusão não poderá ser realizada.");
			}
		}
	}
	
	public List<Especialidade> getComboEspecialidade() {

		try {

			if ( this.comboEspecialidade == null )
				this.comboEspecialidade = especialidadeService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo especialidade. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEspecialidade;
	}


	public List<Escolaridade> getComboEscolaridade() {

		try {

			if ( this.comboEscolaridade == null )
				this.comboEscolaridade = escolaridadeService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo escolaridade. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEscolaridade;
	}

	
	public List<TipoOcupacao> getComboTipoOcupacao() {

		try {

			if ( this.comboTipoOcupacao == null )
				this.comboTipoOcupacao = tipoOcupacaoService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo tipo ocupação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoOcupacao;
	}

	public List<SituacaoLei> getComboSituacaoLei() {
		return Arrays.asList(SituacaoLei.values());
	}	
	
	public List<TipoAcumuloDeCargo> getComboTipoAcumulo() {
		return Arrays.asList(TipoAcumuloDeCargo.values());
	}
	
	public List<ContagemEspecial> getComboContagemEspecial() {
		return Arrays.asList(ContagemEspecial.values());
	}

	
	private void limpar() {
		this.listaEspecialidade = new ArrayList<EspecialidadeCargo>();
		this.listaSimbolo = new ArrayList<Simbolo>();

		this.comboEscolaridade = null;
		this.comboEspecialidade = null;
		this.comboTipoOcupacao = null;
	}
	
	public String limpaTela() {
		limpar();
		return "listar";
	}
	

	public Ocupacao getEntidade() {return entidade;}
	public void setEntidade(Ocupacao entidade) {this.entidade = entidade;}

	public String getSimbologia() {return simbologia;}
	public void setSimbologia(String simbologia) {this.simbologia = simbologia;}

	public Simbolo getEntidadeSimbolo() {return entidadeSimbolo;}
	public void setEntidadeSimbolo(Simbolo entidadeSimbolo) {this.entidadeSimbolo = entidadeSimbolo;}

	public List<Simbolo> getListaSimbolo() {return listaSimbolo;}

	public EspecialidadeCargo getEntidadeEspecialidade() {return entidadeEspecialidade;}
	public void setEntidadeEspecialidade(EspecialidadeCargo entidadeEspecialidade) {this.entidadeEspecialidade = entidadeEspecialidade;}

	public List<EspecialidadeCargo> getListaEspecialidade() {return listaEspecialidade;}

}