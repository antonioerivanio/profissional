package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.EstadoCivil;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.PessoalCategoria;
import br.gov.ce.tce.srh.domain.Raca;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.EscolaridadeService;
import br.gov.ce.tce.srh.service.EstadoCivilService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.service.PessoalCategoriaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.RacaService;
import br.gov.ce.tce.srh.service.UfService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
* Use case : SRH_UC022_Manter Pessoa
* 
* @since   : Out 1, 2011, 5:43:50 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("pessoaBean")
@Scope("session")
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
	private RacaService racaService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private PessoalCategoriaService pessoalCategoriaService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private String nome = new String();
	private String cpf = new String();

	// entidades das telas
	private List<Pessoal> lista;
	private Pessoal entidade = new Pessoal();

	// imagem
	private UploadedFile arquivo;
	private UploadedFile ficha;

	// combos
	private List<EstadoCivil> comboEstadoCivil;
	private List<Escolaridade> comboEscolaridade;
	private List<Uf> comboUf;
	private List<PessoalCategoria> comboCategoria;
	private List<Raca> comboRaca;
	
	//paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Pessoal> pagedList = new ArrayList<Pessoal>();
	private int flagRegistroInicial = 0;	
	private Integer pagina = 1;
	
	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {
			if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				this.entidade = pessoalService.getByCpf(authenticationService.getUsuarioLogado().getCpf());
			}else{
				this.entidade = pessoalService.getById( this.entidade.getId() );
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";

	}


	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {			
			
			limparListas();

			count = pessoalService.count(nome, cpf);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			
			passouConsultar = true;
			
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return "listar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {
			entidade.setNomeCompleto(entidade.getNomeCompleto().replaceAll(" +", " "));
			pessoalService.salvar(entidade);
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil. addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());		
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			pessoalService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new Pessoal() );
		return consultar();
	}


	/**
	 * Combo Estado Civil
	 * 
	 * @return
	 */
	public List<EstadoCivil> getComboEstadoCivil() {

		try {

			if ( this.comboEstadoCivil == null )
				this.comboEstadoCivil = estadoCivilService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo estado civil. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEstadoCivil;
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
	 * Combo UF
	 * 
	 * @return
	 */
	public List<Uf> getComboUf() {

		try {

			if ( this.comboUf == null )
				this.comboUf = ufService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo UF. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboUf;
	}


	/**
	 * Combo Categoria
	 * 
	 * @return
	 */
	public List<PessoalCategoria> getComboCategoria() {

		try {

			if ( this.comboCategoria == null )
				this.comboCategoria = pessoalCategoriaService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo categoria. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCategoria;
	}


	/**
	 * Combo Raca
	 * 
	 * @return
	 */
	public List<Raca> getComboRaca() {

		try {

			if ( this.comboRaca == null )
				this.comboRaca = racaService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo biotipo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboRaca;
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {
		
		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE 1 = 1 ");
			
			if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				filtro.append(" AND upper( cpf ) like '%" + SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf()) + "%' ");
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


	/**
	 * Upload da foto
	 * 
	 * @throws IOException 
	 * 
	 */
	public void uploadFoto(ValueChangeEvent event) {

		// criando nome da foto
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHMMss");
		String data = formato.format(new Date());

		try {

	        // pegando o caminho do arquivo no servidor
	        Parametro parametro = parametroService.getByNome("pathImageSRH");

	       	if (parametro == null)
				throw new SRHRuntimeException("Parametro do caminho da imagem nao encontrado na tabela SAPJAVA.FWPARAMETER");

	        // setando o nome da foto
			setArquivo((UploadedFile) event.getNewValue());
			getEntidade().setFoto( data + FacesUtil.getTipoArquivo( arquivo.getName() ) );

			// gravando em disco
			java.io.File file = new java.io.File(parametro.getValor() + getEntidade().getFoto());
			FileOutputStream fop;

			fop = new FileOutputStream(file);
			fop.write(arquivo.getBytes());
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
	
	
	
	/**
	 * Upload da ficha
	 * 
	 * @throws IOException 
	 * 
	 */
	public void uploadFicha(ValueChangeEvent event) {

		try {

	        // pegando o caminho do arquivo no servidor
	        Parametro parametro = parametroService.getByNome("pathFichaFuncionalSRH");

	       	if (parametro == null)
				throw new SRHRuntimeException("Parametro do caminho da imagem nao encontrado na tabela SRH.TB_PARAMETRO");

	        // setando o nome da ficha
			setFicha((UploadedFile) event.getNewValue());
			getEntidade().setFicha( new File(ficha.getName()).getName() );

			// gravando em disco
			java.io.File file = new java.io.File(parametro.getValor() + getEntidade().getFicha());
			FileOutputStream fop;

			fop = new FileOutputStream(file);
			fop.write(ficha.getBytes());
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


	/** 
	* Evento para gerar a Foto 
	* 
	* @return String 
	*/  
	public void geraFoto(OutputStream out, Object data) {  

		try {

			// pegando o servlet context
			FacesContext facesContext = FacesContext.getCurrentInstance();  
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

			// pegando o caminho do arquivo no servidor
			Parametro parametro = parametroService.getByNome("pathImageSRH");
        
        	if (parametro == null)
				throw new SRHRuntimeException("Parametro do caminho da imagem nao encontrado na tabela SAPJAVA.FWPARAMETER");

	        // pegando a foto
	        InputStream in = null;

			try {

				// pegando a foto
				if (getEntidade().getFoto() != null && !getEntidade().getFoto().equalsIgnoreCase(""))
					in = new FileInputStream(parametro.getValor() + getEntidade().getFoto());

			} catch (FileNotFoundException e) {
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
			
			// pegando a foto DEFAULT
			if (in == null)
				in = new FileInputStream(servletContext.getRealPath("//img/" + "sem_imagem.jpg"));
	
			// Retornando os bytes da foto  
	        byte[] buffer = new byte[1024];
	        int n = 0;
	        while ( ( n = in.read(buffer)) != -1) {
	        	out.write(buffer, 0, n);
	        	out.flush();
	        }
	
	        out.close();

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage("Erro na geração da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na geração da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na geração da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	
	/**
	 * Limpar form
	 */
	private void limpar() {
		setEntidade( new Pessoal() );

		this.nome = new String();
		this.cpf = new String();
		this.lista = new ArrayList<Pessoal>();

		this.comboEstadoCivil = null;
		this.comboEscolaridade = null;
		this.comboUf = null;
		this.comboCategoria = null;
		this.comboRaca = null;
	}
	
	public String limpaTela() {
		limpar();
		return "listar";
	}


	/**
	 * Gets and Sets
	 */
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {this.cpf = cpf;}

	public Pessoal getEntidade() {return entidade;}
	public void setEntidade(Pessoal entidade) {this.entidade = entidade;}

	public List<Pessoal> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {			
				count = pessoalService.count(authenticationService.getUsuarioLogado());
				limparListas();
				flagRegistroInicial = -1;				
			
			} catch (Exception e) {
				limparListas();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}			
		}else if (!passouConsultar){
			limpar();
			limparListas();
			flagRegistroInicial = 0;
		}
		
		passouConsultar = false;
		return form;
	}

	public UploadedFile getArquivo() {return arquivo;}
	public void setArquivo(UploadedFile arquivo) {this.arquivo = arquivo;}
	
	public UploadedFile getFicha() {return ficha;}
	public void setFicha(UploadedFile ficha) {this.ficha = ficha;}

	public RelatorioUtil getRelatorioUtil() {return relatorioUtil;}
	public void setRelatorioUtil(RelatorioUtil relatorioUtil) {this.relatorioUtil = relatorioUtil;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Pessoal>();		
		pagina = 1;
	}
	
	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			flagRegistroInicial = getPrimeiroDaPagina();
			
			if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
				setPagedList(pessoalService.search(authenticationService.getUsuarioLogado(), flagRegistroInicial, dataModel.getPageSize()));
			} else {
				setPagedList(pessoalService.search(nome, cpf, flagRegistroInicial, dataModel.getPageSize()));
			}
			
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			
			}
		}
		return dataModel;
	}

	public List<Pessoal> getPagedList() {return pagedList;}
	public void setPagedList(List<Pessoal> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	
	//FIM PAGINAÇÃO

}