package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Acrescimo;
import br.gov.ce.tce.srh.domain.Averbacao;
import br.gov.ce.tce.srh.domain.Deducao;
import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.service.AcrescimoService;
import br.gov.ce.tce.srh.service.AverbacaoService;
import br.gov.ce.tce.srh.service.DeducaoService;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : 
* 
* @since   : Fev 09, 2012, 11:16:00
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("emitirContagemTempoServicoFormBean2")
@Scope("session")
public class EmitirContagemTempoServicoFormBean2 implements Serializable {

	static Logger logger = Logger.getLogger(EmitirContagemTempoServicoFormBean2.class);
	
	@Autowired
	private FeriasService feriasService;	
	
	@Autowired
	private LicencaService licencaService;
	
	@Autowired
	private AverbacaoService averbacaoService;
	
	@Autowired
	private AcrescimoService acrescimoService;
	
	@Autowired
	private DeducaoService deducaoService;
	
	// entidades das telas
	private Funcional entidade = new Funcional();
	
	
	private Boolean feriasDobro = false;
	private Boolean licenca = false;
	private Boolean averbacao = false;
	private Boolean acrescimo = false;
	private Boolean deducao = false;
	
	//Listas
 	private List<Ferias> listaFerias = new ArrayList<Ferias>();
 	private List<Licenca> listaLicenca = new ArrayList<Licenca>();
 	private List<Averbacao> listaAverbacao = new ArrayList<Averbacao>();
 	private List<Acrescimo> listaAcrescimo = new ArrayList<Acrescimo>();
    private List<Deducao> listaDeducao = new ArrayList<Deducao>();

	/**
	 * Realizar antes de carregar tela de visualizacao
	 * 
	 * @return
	 */
	public String visualizar() {

		try {
			listaFerias = feriasService.findByPessoalTipo( entidade.getPessoal().getId(), 5L );
			if (listaFerias.size() != 0) {
				feriasDobro = true;
				for (Ferias entidadeFerias : listaFerias) {
					if (entidadeFerias.getQtdeDias() != null) {

						entidadeFerias.setQtdeDias(entidadeFerias.getQtdeDias() * 2L);
					}
				}
			} else {
				feriasDobro = false;
			}
			listaLicenca = licencaService.findByPessoaLicencaEspecial(entidade.getPessoal().getId());
			if (listaLicenca.size() != 0) {
				licenca = true;
				for (Licenca entidadeLicenca : listaLicenca) {
					if (entidadeLicenca.getLicencaEspecial().isContaremdobro() == true) {
						entidadeLicenca.getLicencaEspecial().setQtdedias(entidadeLicenca.getLicencaEspecial().getQtdedias() * 2);
					}
				}
			} else {
				licenca = false;
			}

			listaAverbacao = averbacaoService.findByPessoal(entidade.getPessoal().getId());
			if (listaAverbacao.size() != 0) {
				averbacao = true;
			} else {
				averbacao = false;
			}

			listaAcrescimo = acrescimoService.findByPessoal(entidade.getPessoal().getId());
			if (listaAcrescimo.size() != 0) {
				acrescimo = true;
			} else {
				acrescimo = false;
			}

			listaDeducao = deducaoService.findByPessoal(entidade.getPessoal().getId());
			if (listaDeducao.size() != 0) {
				deducao = true;
			} else {
				deducao = false;
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} 
			
		return "incluirAlterar";
	}


	/**
	 * Gets and Sets
	 */
	public Funcional getEntidade() {return entidade;}
    public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public Boolean getFeriasDobro() {return feriasDobro;}
	public void setFeriasDobro(Boolean feriasDobro) {this.feriasDobro = feriasDobro;}

	public Boolean getLicenca() {return licenca;}
	public void setLicenca(Boolean licenca) {this.licenca = licenca;}

	public void setListaFerias(List<Ferias> listaFerias) {this.listaFerias = listaFerias;}
	public List<Ferias> getListaFerias() {return listaFerias;}
	
	public List<Licenca> getListaLicenca() {return listaLicenca;}
	public void setListaLicenca(List<Licenca> listaLicenca) {this.listaLicenca = listaLicenca;}

	public Boolean getAverbacao() {return averbacao;}
	public void setAverbacao(Boolean averbacao) {this.averbacao = averbacao;}

	public List<Averbacao> getListaAverbacao() {return listaAverbacao;}
	public void setListaAverbacao(List<Averbacao> listaAverbacao) {this.listaAverbacao = listaAverbacao;}

	public List<Acrescimo> getListaAcrescimo() {return listaAcrescimo;}
	public void setListaAcrescimo(List<Acrescimo> listaAcrescimo) {this.listaAcrescimo = listaAcrescimo;}

	public Boolean getAcrescimo() {return acrescimo;}
	public void setAcrescimo(Boolean acrescimo) {this.acrescimo = acrescimo;}

	public Boolean getDeducao() {return deducao;}
	public void setDeducao(Boolean deducao) {this.deducao = deducao;}

	public List<Deducao> getListaDeducao() {return listaDeducao;}
	public void setListaDeducao(List<Deducao> listaDeducao) {this.listaDeducao = listaDeducao;}
	
}