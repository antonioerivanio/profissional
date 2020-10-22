package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.dao.DeclaracaoBensDAO;
import br.gov.ce.tce.srh.domain.DeclaracaoBens;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.EstadoCivil;
import br.gov.ce.tce.srh.domain.Municipio;
import br.gov.ce.tce.srh.domain.Pais;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.PessoalCategoria;
import br.gov.ce.tce.srh.domain.PessoalRecadastramento;
import br.gov.ce.tce.srh.domain.Raca;
import br.gov.ce.tce.srh.domain.Recadastramento;
import br.gov.ce.tce.srh.domain.TipoLogradouro;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.enums.CategoriaCNH;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.CEPService;
import br.gov.ce.tce.srh.service.DependenteService;
import br.gov.ce.tce.srh.service.EscolaridadeService;
import br.gov.ce.tce.srh.service.EstadoCivilService;
import br.gov.ce.tce.srh.service.MunicipioService;
import br.gov.ce.tce.srh.service.PaisService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.service.PessoalCategoriaService;
import br.gov.ce.tce.srh.service.PessoalRecadastramentoService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.RacaService;
import br.gov.ce.tce.srh.service.RecadastramentoService;
import br.gov.ce.tce.srh.service.TipoLogradouroService;
import br.gov.ce.tce.srh.service.UfService;
import br.gov.ce.tce.srh.to.CEP;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("pessoaBean")
@Scope("view")
public class PessoaBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoaBean.class);

	@Autowired
	private PessoalService pessoalService;

	@Autowired
	private EstadoCivilService estadoCivilService;

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	private UfService ufService;

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private RacaService racaService;

	@Autowired
	private PaisService paisService;

	@Autowired
	private TipoLogradouroService tipoLogradouroService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private PessoalCategoriaService pessoalCategoriaService;	

	@Autowired
	private CEPService cepService;
	
	@Autowired
	private RecadastramentoService recadastramentoService;
	
	@Autowired
	private PessoalRecadastramentoService pessoalRecadastramentoService;
	
	@Autowired
	private DependenteService dependenteService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private ImageBean imageBean; 
	
	@Autowired
	private DeclaracaoBensDAO declaracaoBensDAO;

	private Boolean podeAlterar = null;
	private Boolean ehServidor = null;
	private boolean recadastramentoAtivo;
	private boolean cepValido;

	private String nome = new String();
	private String cpf = new String();

	private List<Pessoal> lista;
	private Pessoal entidade = new Pessoal();
	private List<Dependente> dependentes = new ArrayList<Dependente>();
	private PessoalRecadastramento pessoalRecadastramento;
	private Recadastramento recadastramento;
	private List<DeclaracaoBens> declaracaoBensList = new ArrayList<DeclaracaoBens>();
	private List<DeclaracaoBens> declaracaoBensListExcluir = new ArrayList<DeclaracaoBens>();

	private UploadedFile foto;
	private UploadedFile ficha;

	private List<EstadoCivil> comboEstadoCivil;
	private List<Escolaridade> comboEscolaridade;
	private List<Uf> comboUf;
	private List<PessoalCategoria> comboCategoria;
	private List<Raca> comboRaca;
	private List<Pais> comboPais;
	private List<TipoLogradouro> comboTipoLogradouro;
	private List<Municipio> comboMunicipioNaturalidade;
	private List<Municipio> comboMunicipioEndereco;
	
	private Parametro pathDeclaracaoBensSRH;

	// paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Pessoal> pagedList = new ArrayList<Pessoal>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;

	
	@PostConstruct
	private void init() {
		Pessoal flashParameter = (Pessoal)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Pessoal());		
		imageBean.setFoto(null);		
			
		try {
			if (ehServidor()) {
				this.entidade = pessoalService.getByCpf(authenticationService.getUsuarioLogado().getCpf());
				try {
					count = pessoalService.count(authenticationService.getUsuarioLogado());
					limparListas();
					flagRegistroInicial = -1;

				} catch (Exception e) {
					limparListas();
					FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
					logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
				}					
				
			} else if(this.entidade.getId() != null) {				
				this.entidade = pessoalService.getById(this.entidade.getId());				
			}			
			
			if (ehServidor() || ehProprioCadastro()) {
				verificaRecadastramento();
			}	
			
			atualizaNaturalidade();						
			
			buscarCep();
			
			this.dependentes = dependenteService.findByResponsavel(entidade.getId());
			this.declaracaoBensList = declaracaoBensDAO.getByPessoalId(entidade.getId());			
			
			imageBean.setFoto(this.entidade.getFoto());
			
			this.pathDeclaracaoBensSRH = parametroService.getByNome("pathDeclaracaoBensSRH");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	public void consultar() {

		try {

			limparListas();

			count = pessoalService.count(nome, cpf);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;

		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}	

	public void salvar(boolean finalizar) {

		try {

			entidade = pessoalService.salvar(entidade);		
			
			if(finalizar) {
				getPessoalRecadastramento().setStatus(1);
				recadastramentoAtivo = false;				
			}
			
			salvarDeclaracoes();
			
			atualizaRecadastramento();			

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	public void excluir() {

		try {

			pessoalService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage(
					"Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade(new Pessoal());
		consultar();
	}
	
	private void verificaRecadastramento() {
		
		recadastramentoAtivo = false;
		
		recadastramento = recadastramentoService.findMaisRecente();
		
		if (recadastramento != null) {
			
			Date hoje = SRHUtils.getHoje();
			
			if(!hoje.before(recadastramento.getInicio()) && !hoje.after(recadastramento.getFim())) {
				
				recadastramentoAtivo = true;				
				
				setPessoalRecadastramento(pessoalRecadastramentoService.findByPessoalAndRecadastramento(entidade.getId(), recadastramento.getId()));
			
				if(getPessoalRecadastramento().getStatus().equals(1)) {
					recadastramentoAtivo = false;
				}					
								
			}				
		}
	}
	
	private void atualizaRecadastramento() {
			
		if ( ehAlteracao() && recadastramento != null ) {
			
			getPessoalRecadastramento().setDataAtualizacao(new Date());			
			pessoalRecadastramentoService.salvar(pessoalRecadastramento);
		
		}
	}

	public boolean temPermisssaoParaAlterar() {
		if (podeAlterar == null)
			podeAlterar = authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_INSERIR")
				|| authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_ALTERAR");

		return podeAlterar;
	}

	public boolean ehServidor() {
		if (ehServidor == null)
			ehServidor = authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR");
		return ehServidor;
	}
	
	public boolean ehAlteracao() {
		return entidade.getId() != null;
	}

	public boolean podeAlterar() {
		return temPermisssaoParaAlterar() || cadastroAindaNaoFinalizado();
	}
	
	public boolean ehProprioCadastro() {
		try {
			return authenticationService.getUsuarioLogado().getCpf().equals(entidade.getCpf());
		} catch (Exception e) {
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}		
		return false;
	}	
	
	public boolean cadastroAindaNaoFinalizado() {
		return (ehServidor() || ehProprioCadastro()) && recadastramentoAtivo;
	}
	
	public boolean getCepValido() {
		return this.cepValido;
	}

	public List<EstadoCivil> getComboEstadoCivil() {

		try {

			if (this.comboEstadoCivil == null)
				this.comboEstadoCivil = estadoCivilService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo estado civil. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEstadoCivil;
	}

	public List<Escolaridade> getComboEscolaridade() {

		try {

			if (this.comboEscolaridade == null)
				this.comboEscolaridade = escolaridadeService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo escolaridade. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEscolaridade;
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

	public List<PessoalCategoria> getComboCategoria() {

		try {

			if (this.comboCategoria == null)
				this.comboCategoria = pessoalCategoriaService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo categoria. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCategoria;
	}

	public List<Raca> getComboRaca() {

		try {

			if (this.comboRaca == null)
				this.comboRaca = racaService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo biotipo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboRaca;
	}

	public List<CategoriaCNH> getComboCategoriaCNH() {
		return Arrays.asList(CategoriaCNH.values());
	}

	public List<Pais> getComboPais() {

		try {

			if (this.comboPais == null)
				this.comboPais = paisService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo biotipo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboPais;
	}

	public List<TipoLogradouro> getComboTipoLogradouro() {

		try {

			if (this.comboTipoLogradouro == null)
				this.comboTipoLogradouro = tipoLogradouroService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Raça / Cor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoLogradouro;
	}

	public List<Municipio> getComboMunicipioEndereco() {
		try {
			if (entidade.getUfEndereco() != null) {
				if (this.comboMunicipioEndereco == null || !this.comboMunicipioEndereco.get(0).getUf().equals(this.entidade.getUfEndereco())) {
					this.comboMunicipioEndereco = municipioService.findByUF(entidade.getUfEndereco().getId());
				}	
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os municípios. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return comboMunicipioEndereco;
	}

	public List<Municipio> getComboMunicipioNaturalidade() {
		try {
			
			if (entidade.getUf() != null) {
				if (this.comboMunicipioNaturalidade == null	|| !this.comboMunicipioNaturalidade.get(0).getUf().equals(this.entidade.getUf())) {
					this.comboMunicipioNaturalidade = municipioService.findByUF(entidade.getUf().getId());
				}	
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os municípios. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return comboMunicipioNaturalidade;
	}

	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE 1 = 1 ");

			if (ehServidor()) {
				filtro.append(" AND upper( cpf ) like '%" + authenticationService.getUsuarioLogado().getCpf() + "%' ");
			} else {
				if (this.cpf != null && !this.cpf.equalsIgnoreCase(""))
					cpf = SRHUtils.removerMascara(cpf);
				filtro.append(" AND upper( cpf ) like '%" + this.cpf.toUpperCase() + "%' ");
				if (this.nome != null && !this.nome.equalsIgnoreCase(""))
					filtro.append(" AND upper( nomeCompleto ) like upper('%" + this.nome + "%') ");
			}

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("pessoal.jasper", parametros, "pessoal.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public void uploadFoto(FileUploadEvent event) {

		if (event.getUploadedFile() != null) {
		
			// criando nome da foto
			SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHMMss");
			String nomeDoArquivo = formato.format(new Date());
	
			try {
	
				// pegando o caminho do arquivo no servidor
				Parametro parametro = parametroService.getByNome("pathImageSRH");
	
				if (parametro == null)
					throw new SRHRuntimeException("Parâmetro do caminho da imagem não encontrado na tabela SRH.TB_PARAMETRO");
	
				// setando o nome da foto
				setFoto(event.getUploadedFile());
				nomeDoArquivo = SRHUtils.getNomeArquivo(foto.getName()) + "-" + nomeDoArquivo + SRHUtils.getTipoArquivo(foto.getName());
				getEntidade().setFoto(nomeDoArquivo);
				imageBean.setFoto(nomeDoArquivo);
	
				// gravando em disco
				File file = new File(parametro.getValor() + getEntidade().getFoto());
				FileOutputStream fop;
	
				fop = new FileOutputStream(file);
				fop.write(foto.getData());
				fop.flush();
				fop.close();
	
			} catch (SRHRuntimeException e) {
				FacesUtil.addErroMessage("Erro na gravação da foto do servidor.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			} catch (FileNotFoundException e) {
				FacesUtil.addErroMessage("Erro na gravação da foto do servidor.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			} catch (IOException e) {
				FacesUtil.addErroMessage("Erro na gravação da foto do servidor.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}

	}	

	public void uploadFicha(FileUploadEvent event) {

		try {

			// pegando o caminho do arquivo no servidor
			Parametro parametro = parametroService.getByNome("pathFichaFuncionalSRH");

			if (parametro == null)
				throw new SRHRuntimeException(
						"Parâmetro do caminho da ficha não encontrado na tabela SRH.TB_PARAMETRO");

			// setando o nome da ficha
			setFicha(event.getUploadedFile());
			getEntidade().setFicha(new File(ficha.getName()).getName());

			// gravando em disco
			File file = new File(parametro.getValor() + getEntidade().getFicha());
			FileOutputStream fop;

			fop = new FileOutputStream(file);
			fop.write(ficha.getData());
			fop.flush();
			fop.close();

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage("Erro na gravação da ficha do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na gravação da ficha do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na gravação da ficha do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void fichaAntiga() {
		try {
			
			Parametro parametro = parametroService.getByNome("pathFichaFuncionalSRH");
			String fichaAntiga = parametro.getValor() + entidade.getFicha();
			InputStream in = new FileInputStream(fichaAntiga);
			byte[] fichaAntigaBytes = IOUtils.toByteArray(in);
			relatorioUtil.openPdf(fichaAntigaBytes, fichaAntiga);

		} catch (FileNotFoundException e) {
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void uploadDeclaracaoBens(FileUploadEvent event) {

		try {
			
			if (this.pathDeclaracaoBensSRH == null) {
				throw new SRHRuntimeException("Parâmetro do caminho da declaração de bens não encontrado na tabela SRH.TB_PARAMETRO");
			}
				
			UploadedFile arquivo = event.getUploadedFile();
			String nomeArquivo = arquivo.getName();
			
			DeclaracaoBens declaracao = new DeclaracaoBens();			
			declaracao.setNomeArquivo(nomeArquivo);
			declaracao.setCaminho(this.entidade.getId() + File.separator + UUID.randomUUID() + nomeArquivo.substring(nomeArquivo.lastIndexOf('.'), nomeArquivo.length()));
			declaracao.setPessoal(this.entidade);		
			
			File diretorio = new File(this.pathDeclaracaoBensSRH.getValor() + this.entidade.getId());
			diretorio.mkdirs();
			
			File file = new File(this.pathDeclaracaoBensSRH.getValor() + declaracao.getCaminho());			
			FileOutputStream fop = new FileOutputStream(file);
			fop.write(arquivo.getData());
			fop.flush();
			fop.close();
			
			this.declaracaoBensList.add(0, declaracao);

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage("Erro na gravação da declaração do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na gravação da declaração do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na gravação da declaração do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}
	
	public void abrirDeclaracao(String caminho) {
		try {
			
			Parametro parametro = parametroService.getByNome("pathDeclaracaoBensSRH");
			String caminhoDeclaracao = parametro.getValor() + caminho;
			InputStream in = new FileInputStream(caminhoDeclaracao);
			byte[] declaracaoBytes = IOUtils.toByteArray(in);
			relatorioUtil.openPdf(declaracaoBytes, caminhoDeclaracao);

		} catch (FileNotFoundException e) {
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void excluirDeclaracao(DeclaracaoBens declaracao) {		
		this.declaracaoBensListExcluir.add(declaracao);
		this.declaracaoBensList.remove(declaracao);
	}	
	
	private void salvarDeclaracoes() {
	
		for(DeclaracaoBens declaracao: this.declaracaoBensList) {
			
			if(declaracao.getExercicio() == null || !SRHUtils.anoExercicioValido(declaracao.getExercicio())) {
				throw new SRHRuntimeException("Declaração " + declaracao.getNomeArquivo() + " com exercício inválido");
			}			
			
			if(declaracao.getAnoCalendario() == null || !SRHUtils.anoExercicioValido(declaracao.getAnoCalendario())) {
				throw new SRHRuntimeException("Declaração " + declaracao.getNomeArquivo() + " com ano calendário inválido");
			}
			
			this.declaracaoBensDAO.salvar(declaracao);
		}
		
		for(DeclaracaoBens declaracao: this.declaracaoBensListExcluir) {
			this.declaracaoBensDAO.excluir(declaracao);
		}		
		
	}	

	public void formularioDependentes() {
		try {

			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("pessoa", entidade);
			parametros.put("temDependente", dependentes.size() > 0);
			relatorioUtil.relatorio("dependentesFormulario.jasper", parametros, "dependentesFormulario.pdf", dependentes);

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do formulário. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	private void atualizaNaturalidade() {
		
		try {
			if(entidade.getMunicipioNaturalidade() == null
					&& entidade.getNaturalidade() != null
					&& !entidade.getNaturalidade().isEmpty()
					&& entidade.getUf() != null) {
				entidade.setMunicipioNaturalidade(municipioService.findByNome(entidade.getUf().getId(), entidade.getNaturalidade()));
			}
		} catch (Exception e) {
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	public void buscarCep() {

		try {
			
			this.cepValido = false;

			if (entidade.getCep().length() >= 8) {

				CEP cep = cepService.buscarCep(SRHUtils.removerMascara(entidade.getCep()));				
				
				if (!cep.getLogradouro().isEmpty()) {
					
					entidade.setTipoLogradouro(tipoLogradouroService.getTipoLogradouroInString(getComboTipoLogradouro(), cep.getLogradouro()));
					if (entidade.getTipoLogradouro() != null)
						entidade.setEndereco(cep.getLogradouro().substring(entidade.getTipoLogradouro().getDescricao().length(), cep.getLogradouro().length()).trim());
					else
						entidade.setEndereco(cep.getLogradouro());					
				}
				
				if (entidade.getBairro() == null || entidade.getBairro().isEmpty())
					entidade.setBairro(cep.getBairro());				
				
				entidade.setMunicipioEndereco(municipioService.findByCodigoIBGE(cep.getIbge()));
				
				if(entidade.getMunicipioEndereco() != null)
					entidade.setUfEndereco(entidade.getMunicipioEndereco().getUf());
				
				if ( !cep.getLogradouro().isEmpty() && !cep.getIbge().isEmpty() )
					this.cepValido = true;				

			}

		} catch (SRHRuntimeException e) {
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Pessoal getEntidade() {
		return entidade;
	}

	public void setEntidade(Pessoal entidade) {
		this.entidade = entidade;
	}	
	
	public PessoalRecadastramento getPessoalRecadastramento() {
		if ( pessoalRecadastramento == null ) {
			pessoalRecadastramento = new PessoalRecadastramento();
			pessoalRecadastramento.setRecadastramento(recadastramento);
			pessoalRecadastramento.setPessoal(entidade);
			pessoalRecadastramento.setStatus(0);				
		}
		return pessoalRecadastramento;
	}
	
	public void setPessoalRecadastramento(PessoalRecadastramento pessoalRecadastramento) {
		this.pessoalRecadastramento = pessoalRecadastramento;
	}

	public List<Pessoal> getLista() {
		return lista;
	}
	
	public List<Dependente> getDependentes() {
		return dependentes;
	}	

	public UploadedFile getFoto() {
		return foto;
	}

	public void setFoto(UploadedFile arquivo) {
		this.foto = arquivo;
	}

	public UploadedFile getFicha() {
		return ficha;
	}

	public void setFicha(UploadedFile ficha) {
		this.ficha = ficha;
	}

	public RelatorioUtil getRelatorioUtil() {
		return relatorioUtil;
	}

	public void setRelatorioUtil(RelatorioUtil relatorioUtil) {
		this.relatorioUtil = relatorioUtil;
	}

	public List<DeclaracaoBens> getDeclaracaoBensList() {
		return declaracaoBensList;
	}	

	public List<DeclaracaoBens> getDeclaracaoBensListExcluir() {
		return declaracaoBensListExcluir;
	}

	// PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Pessoal>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if (flagRegistroInicial != getPrimeiroDaPagina()) {
			flagRegistroInicial = getPrimeiroDaPagina();

			if (ehServidor()) {
				setPagedList(pessoalService.search(authenticationService.getUsuarioLogado(), flagRegistroInicial,
						dataModel.getPageSize()));
			} else {
				setPagedList(pessoalService.search(nome, cpf, flagRegistroInicial, dataModel.getPageSize()));
			}

			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();

			}
		}
		return dataModel;
	}

	public List<Pessoal> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<Pessoal> pagedList) {
		this.pagedList = pagedList;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	private int getPrimeiroDaPagina() {
		return dataModel.getPageSize() * (pagina - 1);
	}

	// FIM PAGINAÇÃO

}