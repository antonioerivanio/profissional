package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

/**
* Use case : SRH_UC005_Manter Cargo
* 
* @since   : Sep 13, 2011, 17:28:36 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("cargoFormBean")
@Scope("session")
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

	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		setEntidade( new Ocupacao() );
		limpar();
		return "incluirAlterar";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {
			
			limpar();
			this.listaEspecialidade = especialidadeCargoService.findByOcupacao( entidade.getId() );
			this.listaSimbolo = simboloService.findByOcupacao( entidade.getId() );			
			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

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

		return null;
	}


	/**
	 * Adicionar especialidade
	 * 
	 * @return
	 */
	public String addEspecialidade() {

		try {

			// validando o campo obrigatorio Simbologia
			if (entidadeEspecialidade.getEspecialidade() == null || entidadeEspecialidade.getEspecialidade().getId() == 0l) {
				FacesUtil.addErroMessage("A especialidade é obrigatória.");
				return null;
			}

			// verificando se a especialidade ja foi cadatrada
			for (EspecialidadeCargo item : listaEspecialidade) {
				if (item.getEspecialidade().getId().equals(entidadeEspecialidade.getEspecialidade().getId())) {
					FacesUtil.addErroMessage("A especialidade já está na lista.");
					return null;
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

		return null;
	}


	/**
	 * Excluir especialidade
	 * 
	 * @return
	 */
	public String excluirEspecialidade() {

		if (this.entidadeEspecialidade.getId() != null) {

			try {

				especialidadeCargoService.excluir(entidadeEspecialidade);
				listaEspecialidade.remove(entidadeEspecialidade);
				this.entidadeEspecialidade = new EspecialidadeCargo();

			} catch (Exception e) {
				FacesUtil.addErroMessage("Existem registros filhos utilizando a especialidade excluida. Exclusão não poderá ser realizada.");
				return null;
			}

		}

		return null;
	}


	/**
	 * Adicionar simbolo
	 * 
	 * @return
	 */
	public String addSimbologia() {

		try {

			// validando o campo obrigatorio Simbologia
			if (simbologia == null || simbologia.equalsIgnoreCase("")) {
				FacesUtil.addErroMessage("A simbologia é obrigatória.");
				return null;
			}

			// verificando se a simbologia ja foi cadatrada
			for (Simbolo item : listaSimbolo) {
				if (item.getSimbolo().equalsIgnoreCase(simbologia)) {
					FacesUtil.addErroMessage("A simbologia já está na lista.");
					return null;
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

		return null;
	}


	/**
	 * Excluir simbolo
	 * 
	 * @return
	 */
	public String excluirSimbologia() {

		if (this.entidadeSimbolo.getId() != null) {

			try {

				simboloService.excluir(entidadeSimbolo);
				listaSimbolo.remove(entidadeSimbolo);
				this.simbologia = new String();

			} catch (Exception e) {
				FacesUtil.addErroMessage("Existem registros filhos utilizando o simbolo excluido. Exclusão não poderá ser realizada.");
				return null;
			}

		}

		return null;
	}


	/**
	 * Combo Especialidade
	 * 
	 * @return
	 */
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


	/**
	 * Combo Escolaridade
	 * 
	 * @return
	 */
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


	/**
	 * Combo Tipo Ocupacao
	 * 
	 * @return
	 */
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

	/**
	 * Limpar form
	 */
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
	


	/**
	 * Gets and Sets
	 */
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