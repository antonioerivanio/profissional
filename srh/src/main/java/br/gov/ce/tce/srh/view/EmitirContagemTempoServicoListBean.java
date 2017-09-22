 package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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


	private HtmlForm form;
	private boolean passouConsultar = false;

	private String matricula = new String();
	private String nome = new String();
	private Date dataFim;

	private Funcional entidade;
	
	private List<Funcional> listaFuncional = new ArrayList<Funcional>();
	private List<Ferias> listaFerias = new ArrayList<Ferias>();
	private List<LicencaEspecial> listaLicencaEspecial = new ArrayList<LicencaEspecial>();
	private List<Licenca> listaLicencaExcluir = new ArrayList<Licenca>();
	private List<Averbacao> listaAverbacao = new ArrayList<Averbacao>();
	private List<Acrescimo> listaAcrescimo = new ArrayList<Acrescimo>();
	private List<Deducao> listaDeducao = new ArrayList<Deducao>();
	
	private Long totalDia = new Long(0);
	private Long ano = new Long(0);
	private Long mes = new Long(0);
	private Long dia = new Long(0);

		
	public String consultar() {

		try {

			if (getEntidade() == null)
				throw new SRHRuntimeException("Selecione um funcionário.");

			this.entidade = funcionalService.getById( this.entidade.getId() );

			this.totalDia = new Long(0);
			this.ano = new Long(0);
			this.mes = new Long(0);
			this.dia = new Long(0);

			
			this.listaFuncional = funcionalService.findByPessoal(entidade.getPessoal().getId(), "desc");

			for (Funcional funcional : listaFuncional) {

				if ( dataFim != null ) {
					
					if ( funcional.getExercicio().after(dataFim) )
						throw new SRHRuntimeException("A data informada é anterior a data de Exercício do Cargo atual.");

					if ( funcional.getSaida() == null )
						funcional.setSaida(dataFim);

				} else if ( funcional.getSaida() == null ){
					
					funcional.setSaida( new Date() );
					dataFim = funcional.getSaida();
				}				
				
				int [] anosMesesDias = SRHUtils.contagemDeTempoDeServico( funcional.getExercicio(), funcional.getSaida() );
				
				funcional.setAnos(anosMesesDias[0]);
				funcional.setMeses(anosMesesDias[1]);
				funcional.setDias(anosMesesDias[2]);
								
				funcional.setQtdeDias(((long) SRHUtils.calculaQtdeDias(anosMesesDias)));
				
				totalDia += funcional.getQtdeDias();

			}
			

			// carregando ferias em dobro
			this.listaFerias = feriasService.findByPessoalTipo( entidade.getPessoal().getId(), 5L );

			// percorrendo as ferias em dobro
			for (Ferias ferias : listaFerias) {
				totalDia += ferias.getQtdeDias()*2;
			}
			

			// carregando licencas especiais			
			this.listaLicencaEspecial = licencaEspecialService.findByPessoalContaEmDobro(entidade.getPessoal().getId());
			
			for (LicencaEspecial licencaEspecial : listaLicencaEspecial) {
				if ( licencaEspecial.isContaremdobro() )
					totalDia += licencaEspecial.getSaldodias()*2;
			}
			
			
			List<Licenca> licencas = licencaService.findByPessoa(entidade.getPessoal().getId());
			
			for (Licenca licenca : licencas) {
				
				if(licenca.getContarDiasEmDobro() > 0 && licenca.getLicencaEspecial() != null) {
					LicencaEspecial licencaEspecial = licenca.getLicencaEspecial();
					licencaEspecial.setDescricao(licenca.getObs());
					licencaEspecial.setSaldodias(licenca.getContarDiasEmDobro());
					
					this.listaLicencaEspecial.add(licencaEspecial);
					
					totalDia += licenca.getContarDiasEmDobro()*2;
				}
				
			}			

			
			// caregando averbacao
			this.listaAverbacao = averbacaoService.findByPessoal( entidade.getPessoal().getId() );

			// percorrendo as averbacoes
			for (Averbacao averbacao : listaAverbacao) {
				
				if (averbacao.getInicio() != null && averbacao.getFim() != null) {
					
					int [] anosMesesDias = SRHUtils.contagemDeTempoDeServico( averbacao.getInicio(), averbacao.getFim() );
					
					averbacao.setAnos(String.valueOf(anosMesesDias[0]));
					averbacao.setMeses(String.valueOf(anosMesesDias[1]));
					averbacao.setDias(String.valueOf(anosMesesDias[2]));
									
					averbacao.setQtdeDias(((long) SRHUtils.calculaQtdeDias(anosMesesDias)));
					
				} else {
					averbacao.setAnos("-");
					averbacao.setMeses("-");
					averbacao.setDias("-");
				}
				
				
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
				
				if (deducao.getInicio() != null && deducao.getFim() != null) {
					
					int [] anosMesesDias = SRHUtils.contagemDeTempoDeServico( deducao.getInicio(), deducao.getFim() );
					
					deducao.setAnos(String.valueOf(anosMesesDias[0]));
					deducao.setMeses(String.valueOf(anosMesesDias[1]));
					deducao.setDias(String.valueOf(anosMesesDias[2]));
									
					deducao.setQtdeDias(((long) SRHUtils.calculaQtdeDias(anosMesesDias)));
					
				} else {
					deducao.setAnos("-");
					deducao.setMeses("-");
					deducao.setDias("-");					
				}
				
				totalDia -= deducao.getQtdeDias();							 
			}			
	
			long [] anosMesesDias = SRHUtils.anosMesesDias(totalDia); 			
			
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
	
	
	public String relatorio() {

		try {

			if( getListaFuncional().size() == 0 )
				throw new SRHRuntimeException("Dados insuficientes para gerar o relatório.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("funcional", entidade );
			
			parametros.put("totalDia", totalDia.toString() );
			parametros.put("totalAno", ano.toString() );
			parametros.put("totalMes", mes.toString() );
			parametros.put("dia", dia.toString() );

			if (dataFim != null) {
				parametros.put("saida", dataFim);
			} else {
				parametros.put("saida", new Date());
			}
			
			parametros.put("listaFuncional", new JRBeanCollectionDataSource(listaFuncional) );
			parametros.put("listaFerias", new JRBeanCollectionDataSource(listaFerias) );
			parametros.put("listaLicencaEspecial", new JRBeanCollectionDataSource(listaLicencaEspecial) );
			parametros.put("listaAverbacao", new JRBeanCollectionDataSource(listaAverbacao) );
			parametros.put("listaAcrescimo", new JRBeanCollectionDataSource(listaAcrescimo) );
			parametros.put("listaDeducao", new JRBeanCollectionDataSource(listaDeducao) );
			

			relatorioUtil.relatorio("emitirContagemTempoServico.jasper", parametros, "contagemTempoServico.pdf", null);

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


}
