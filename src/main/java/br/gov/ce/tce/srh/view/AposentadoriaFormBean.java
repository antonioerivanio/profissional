package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Aposentadoria;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.domain.TipoBeneficio;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AposentadoriaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.FuncionalSetorService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.service.TipoBeneficioService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("aposentadoriaFormBean")
@Scope("session")
public class AposentadoriaFormBean implements Serializable {

	static Logger logger = Logger.getLogger(FeriasFormBean.class);

	@Autowired
	private AposentadoriaService aposentadoriaService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private TipoBeneficioService tipoBeneficioService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;
	
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	
	@Autowired
	private FuncionalSetorService funcionalSetorService;
	
	
	private Aposentadoria entidade = new Aposentadoria();

	private String matricula = new String();
	private String nome = new String();

	private boolean alterar = false;
	private String mensagemRepresentacao;
	private String mensagemLotacao;

	private List<TipoBeneficio> comboTipoBeneficio;
	private List<TipoPublicacao> comboTipoPublicacao;

	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}

	public String prepareAlterar() {

		this.alterar = true;

		try {
			
			this.matricula = entidade.getFuncional().getMatricula();
			this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();
			
			atualizaMensagemRepresentacao();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}	
	
	public String salvar() {
		
		try {		

			aposentadoriaService.salvar(entidade);
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
	
	
	public List<TipoBeneficio> getComboTipoBeneficio() {

		try {

			if ( this.comboTipoBeneficio == null )
				this.comboTipoBeneficio = tipoBeneficioService.findAll();

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo Tipo Benefício. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoBeneficio;
	}
	
	public List<TipoPublicacao> getComboTipoPublicacao() {

		try {

			if ( this.comboTipoPublicacao == null )
				this.comboTipoPublicacao = tipoPublicacaoService.findAll();

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoPublicacao;
	}

	private void limpar() {

		this.alterar = false;

		setEntidade(new Aposentadoria());

		this.matricula = new String();
		this.nome = new String();
		
		this.comboTipoPublicacao = null;
		
		this.mensagemRepresentacao = null;
		this.mensagemLotacao = null;
	}
	
	public Aposentadoria getEntidade() { return entidade; }
	public void setEntidade(Aposentadoria entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getByMatriculaAtivo( this.matricula ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
					atualizaMensagemRepresentacao();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}
	
	private void atualizaMensagemRepresentacao() {
		
		this.mensagemRepresentacao = null;
		
		List<RepresentacaoFuncional> representacaList = representacaoFuncionalService.findByPessoal(entidade.getFuncional().getPessoal().getId());
		String cargos = "";
		
		for (RepresentacaoFuncional representacaoFuncional : representacaList) {
			if(representacaoFuncional.getFim() == null) {
				cargos += representacaoFuncional.getRepresentacaoCargo().getNomenclatura() + ", ";
			}
		}
		
		if(!cargos.isEmpty()) {
		
			cargos = cargos.substring(0, cargos.length() - 2);
			
			this.mensagemRepresentacao = "Você está aposentando " + entidade.getFuncional().getNomeCompleto() 
					+ " e verificamos que o(a) mesmo(a) ainda está cadastrado como ocupante do cargo comissionado " 
					+ cargos + ". Caso o(a) servidor(a) continue no cargo comissionado, favor desconsiderar essa mensagem, "
					+ "do contrário, favor gerar a exoneração do cargo comissionado do(a) mesmo(a).";
		
		}
	}
	
	public void atualizaMensagemLotacao() {
		
		this.mensagemLotacao = null;
		
		if (entidade.getFuncional().isProvenienteDoTCM()
				&& entidade.getDataInicioBeneficio() != null 
				&& (entidade.getDataInicioBeneficio().before(SRHUtils.extincaoTCM()) 
						|| entidade.getDataInicioBeneficio().equals(SRHUtils.extincaoTCM())) ) {
		
			List<FuncionalSetor> lotacaoList = funcionalSetorService.findByPessoal(entidade.getFuncional().getPessoal().getId());
			
			if(!lotacaoList.isEmpty()) {
			
				this.mensagemLotacao = "Você está aposentando " + entidade.getFuncional().getNomeCompleto() 
						+ " com Data Início Benefício menor ou igual a 21/08/2017."
						+ " Esta ação irá excluir permanentemente o histórico de lotação do Servidor.";
			
			}
		}
	}

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public boolean isAlterar() {return alterar;}
	public void setAlterar(boolean alterar) {this.alterar = alterar;}
	
	public String getMensagemRepresentacao() {return mensagemRepresentacao;}
	public void setMensagemRepresentacao(String mensagemRepresentacao) {this.mensagemRepresentacao = mensagemRepresentacao;}
	
	public String getMensagemLotacao() {return mensagemLotacao;}
	public void setMensagemLotacao(String mensagemLotacao) {this.mensagemLotacao = mensagemLotacao;}
	
}
