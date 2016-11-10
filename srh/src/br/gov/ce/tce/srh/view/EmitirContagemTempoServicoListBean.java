 package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

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
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.AcrescimoService;
import br.gov.ce.tce.srh.service.AverbacaoService;
import br.gov.ce.tce.srh.service.DeducaoService;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.LicencaEspecialService;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
* @since   : Dez 19, 2011, 17:09:00 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("emitirContagemTempoServicoListBean")
@Scope("session")
public class EmitirContagemTempoServicoListBean implements Serializable {

	static Logger logger = Logger.getLogger(EmitirContagemTempoServicoListBean.class);
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private FeriasService feriasService;	
	
	@Autowired
	private LicencaService licencaService;
	
	@Autowired
	private LicencaEspecialService licencaEspecialService;
	
	@Autowired
	private AverbacaoService averbacaoService;
	
	@Autowired
	private AcrescimoService acrescimoService;
	
	@Autowired
	private DeducaoService deducaoService;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String nome = new String();
	private Date dataFim;

	// entidades das telas
	private Funcional entidade;
	
	//Listas
	private List<Funcional> listaFuncional = new ArrayList<Funcional>();
	private List<Ferias> listaFerias = new ArrayList<Ferias>();
	private List<Licenca> listaLicenca = new ArrayList<Licenca>();
	private List<LicencaEspecial> listaLicencaEspecial = new ArrayList<LicencaEspecial>();
	private List<Licenca> listaLicencaExcluir = new ArrayList<Licenca>();
	private List<Averbacao> listaAverbacao = new ArrayList<Averbacao>();
	private List<Acrescimo> listaAcrescimo = new ArrayList<Acrescimo>();
	private List<Deducao> listaDeducao = new ArrayList<Deducao>();

	// componentes
	private Long totalDia = new Long(0);
	private Long ano = new Long(0);
	private Long mes = new Long(0);
	private Long dia = new Long(0);

	
	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			if (getEntidade() == null)
				throw new SRHRuntimeException("Selecione um funcionário.");

			this.entidade = funcionalService.getById( this.entidade.getId() );


			// limpando
			this.totalDia = new Long(0);
			this.ano = new Long(0);
			this.mes = new Long(0);
			this.dia = new Long(0);


			// carregando tempo servico
			this.listaFuncional = funcionalService.findByPessoal(entidade.getPessoal().getId(), "desc");

			// percorrendo a lista funcional
			for (Funcional funcional : listaFuncional) {

				// caso data fim preenchida
				if ( dataFim != null ) {
					// validando
					if ( funcional.getExercicio().after(dataFim) )
						throw new SRHRuntimeException("A data informada é anterior a data de Exercício do Cargo atual.");

					// caso data fim esteja NULA
					if ( funcional.getSaida() == null )
						funcional.setSaida(dataFim);

				}

				// caso data fim esteja NULA
				if ( funcional.getSaida() == null ){
					funcional.setSaida( new Date() );
					dataFim = funcional.getSaida();
				}
				
				// calculando dias
				funcional.setQtdeDias(((long) SRHUtils.dataDiffAverbacao( funcional.getExercicio(), funcional.getSaida() )));
				totalDia += funcional.getQtdeDias();

			}

			// carregando ferias em dobro
			this.listaFerias = feriasService.findByPessoalTipo( entidade.getPessoal().getId(), 5L );

			// percorrendo as ferias em dobro
			for (Ferias ferias : listaFerias) {
				totalDia += ferias.getQtdeDias()*2;
			}
			

			// carregando licencas especiais			
			this.listaLicencaEspecial = licencaEspecialService.findByPessoalComSaldo(entidade.getPessoal().getId());
			
			for (LicencaEspecial licencaEspecial : listaLicencaEspecial) {
				if ( licencaEspecial.isContaremdobro() )
					totalDia += licencaEspecial.getQtdedias()*2;
			}			

			// caregando averbacao
			this.listaAverbacao = averbacaoService.findByPessoal( entidade.getPessoal().getId() );

			// percorrendo as averbacoes
			for (Averbacao averbacao : listaAverbacao) {
				totalDia += averbacao.getQtdeDias();	
			}	

			// carregando acrescimo
			this.listaAcrescimo = acrescimoService.findByPessoal( entidade.getPessoal().getId() );

			// percorrendo os acrescimos
			for (Acrescimo acrescimo : listaAcrescimo) {
				totalDia += acrescimo.getQtdeDias();
			}			
			
			
			this.listaDeducao = new ArrayList<Deducao>();
			
			// carregando deducoes
			List<Deducao> deducoes = deducaoService.findByPessoal( entidade.getPessoal().getId() );
			
			for (Deducao deducao : deducoes) {
				if(deducao.isFalta()){
					if(deducao.getInicio()!= null && deducao.getInicio().before(SRHUtils.fimDeducaoDeFaltas())) {
						deducao.setDescricaoCompleta("Falta");
						listaDeducao.add(deducao);
					}	
				}else{
					deducao.setDescricaoCompleta("Dedução");
					listaDeducao.add(deducao);
				}
			}
			
			// carregando licenca
			this.listaLicencaExcluir = licencaService.findByPessoaLicencasExcluirTempoServico(entidade.getPessoal().getId() );			
			
			for (Licenca licenca : listaLicencaExcluir) {
				Deducao deducao = new Deducao();
				deducao.setInicio(licenca.getInicio());
				deducao.setFim(licenca.getFim());
				deducao.setQtdeDias(licenca.getDias().longValue());
				deducao.setDescricao(licenca.getObs());
				deducao.setDescricaoCompleta(licenca.getTipoLicenca().getDescricao());
				listaDeducao.add(deducao);				
			}
			
			// percorrendo as deducoes
			for (Deducao deducao : listaDeducao) {
				totalDia -= deducao.getQtdeDias();							 
			}			
	
			long [] anosMesesDias = SRHUtils.anosMesesDiasEstatuto(totalDia); 			
			
			this.ano = anosMesesDias[0];
			this.mes = anosMesesDias[1];
			this.dia = anosMesesDias[2];			
			
			passouConsultar = true;

		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {			
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}

	
	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */	
	public String relatorio() {

		try {

			if( getListaFuncional().size() == 0 )
				throw new SRHRuntimeException("Dados insuficientes para gerar o relatório.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

			parametros.put("CPF", entidade.getPessoal().getCpf() );
			parametros.put("IDFUNCIONAL", entidade.getId().toString() );
			parametros.put("IDPESSOAL", entidade.getPessoal().getId().toString() );
			
			parametros.put("TOTALDIA", totalDia.toString() );
			parametros.put("TOTALANO", ano.toString() );
			parametros.put("TOTALMES", mes.toString() );
			parametros.put("DIA", dia.toString() );

			if (dataFim != null) {
				parametros.put("SAIDA", dataFim);
			} else {
				parametros.put("SAIDA", new Date());
			}
			
			parametros.put("A", servletContext.getRealPath("//WEB-INF/relatorios/EmitirContagemTempoServico_TempoServico.jasper") );

			parametros.put("B",  servletContext.getRealPath("//WEB-INF/relatorios/EmitirContagemTempoServico_FeriasDobro.jasper") );

			parametros.put("C",  servletContext.getRealPath("//WEB-INF/relatorios/EmitirContagemTempoServico_LicencaEpecialDobro.jasper") );

			parametros.put("D",  servletContext.getRealPath("//WEB-INF/relatorios/EmitirContagemTempoServico_averbacaoTempoServico.jasper") );
		
			parametros.put("E",  servletContext.getRealPath("//WEB-INF/relatorios/EmitirContagemTempoServico_AcreascimosTempoServico.jasper") );
			
			parametros.put("F",  servletContext.getRealPath("//WEB-INF/relatorios/EmitirContagemTempoServico_DeducoesTempoServico.jasper") );

			relatorioUtil.relatorio("EmitirContagemTempoServico.jasper", parametros, "EmitirContagemTempoServico.pdf");

		} catch (SRHRuntimeException e) {			
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
			return "listar";
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");			
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			return "listar";
		}

		return null;
	}
	
	public String limpaTela() {
		setEntidade(new Funcional());
		return "listar";
	}


	/**
	 * Gets and Sets
	 */
	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				setEntidade( funcionalService.getCpfAndNomeByMatricula( this.matricula ));
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {				
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}		
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Date getDataFim() {return dataFim;}
	public void setDataFim(Date dataFim) {this.dataFim = dataFim;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setMatricula(funcionalService.getMatriculaAndNomeByCpfAtiva(authenticationService.getUsuarioLogado().getCpf()).getMatricula());
			consultar();
		} else if (!passouConsultar) {
			setEntidade( null );
			matricula = new String();
			nome = new String();
			listaFuncional = new ArrayList<Funcional>();
			dataFim = null;
		}
		passouConsultar = false;
		return form;
	}	

	public List<Funcional> getListaFuncional() {return listaFuncional;}
	public List<Ferias> getListaFerias() {return listaFerias;}
	public List<Licenca> getListaLicenca() {return listaLicenca;}
	public List<LicencaEspecial> getListaLicencaEspecial() {return listaLicencaEspecial;}
	public List<Averbacao> getListaAverbacao() {return listaAverbacao;}
	public List<Acrescimo> getListaAcrescimo() {return listaAcrescimo;}
	public List<Deducao> getListaDeducao() {return listaDeducao;}

	public Long getTotalDia() {return totalDia;}
	public Long getAno() {return ano;}
	public Long getMes() {return mes;}
	public Long getDia() {return dia;}
	
	public boolean isPassouConsultar() {return passouConsultar;}
	public void setPassouConsultar(boolean passouConsultar) {this.passouConsultar = passouConsultar;}

	public List<Licenca> getListaLicencaExcluir() {return listaLicencaExcluir;}
	public void setListaLicencaExcluir(List<Licenca> listaLicencaExcluir) {this.listaLicencaExcluir = listaLicencaExcluir;}


}
