package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.Publicacao;
import br.gov.ce.tce.srh.domain.TipoDocumento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.service.PublicacaoService;
import br.gov.ce.tce.srh.service.TipoDocumentoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
* Use case : SRH_UC029_Manter Documento Publicação
* 
* @since   : Out 21, 2011, 10:10:50 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("publicacaoBean")
@Scope("session")
public class PublicacaoBean implements Serializable {

	static Logger logger = Logger.getLogger(PublicacaoBean.class);

	@Autowired
	private PublicacaoService publicacaoService;

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private TipoDocumento tipoDocumento;
	private List<Publicacao> lista;

	// entidades das telas
	private Publicacao entidade = new Publicacao();
	private Long esfera;

	// combos
	private List<TipoDocumento> comboTipoDocumento;
	private List<TipoDocumento> comboTipoDocumentoConsulta;

	// arquivo
	private UploadedFile arquivo;

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Publicacao> pagedList = new ArrayList<Publicacao>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {

			this.esfera = getEntidade().getTipoDocumento().getEsfera();
			this.comboTipoDocumento = this.getTipoDocumentoByEsfera();
			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu ao carregar os dados. Operação cancelada.");
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

			// validando campos da cosnulta
			if (this.tipoDocumento == null )
				throw new SRHRuntimeException("Selecione um tipo de documento.");

			count = publicacaoService.count( tipoDocumento.getId() );

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
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

			// verificando se tem arquivo para fazer upload
			if (this.arquivo != null)
				upload();

			publicacaoService.salvar(entidade);
			setEntidade( new Publicacao() );

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


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			publicacaoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new Publicacao() );
		return consultar();
	}


	/**
	 * Carregar o tipo de documento
	 * 
	 * @param event
	 */
	public void carregaTipoDocumento() {
		this.comboTipoDocumento = null;
		getTipoDocumentoByEsfera();
	}

	private List<TipoDocumento> getTipoDocumentoByEsfera() {

        try {

        	if ( this.esfera != null && this.comboTipoDocumento == null)
        		this.comboTipoDocumento = tipoDocumentoService.findByEsfera( esfera );

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo documento. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoDocumento;
	}


	/**
	 * Combo Tipo Documento
	 * 
	 * @return
	 */
	public List<TipoDocumento> getComboTipoDocumentoConsulta() {

        try {

        	if ( this.comboTipoDocumentoConsulta == null )
        		this.comboTipoDocumentoConsulta = tipoDocumentoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo documento. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoDocumentoConsulta;
	}


	/**
	 * Upload da foto
	 * 
	 * @throws IOException 
	 * 
	 */
	private void upload() {

		// criando nome do arquivo baseado na data
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHMMss");
		String data = formato.format(new Date());

        // pegando o caminho do arquivo no servidor
        Parametro parametro = parametroService.getByNome("CAMARQPUB");

		try {

	       	if (parametro == null)
				throw new SRHRuntimeException("Parametro do caminho do documento nao encontrado na tabela SAPJAVA.FWPARAMETER");

	       	if ( getEntidade().getTipoPublicacao() == null )
	       		throw new SRHRuntimeException("O tipo de publicação é obrigatório.");

	        // setando o do arquivo
			if ( getEntidade().getTipoPublicacao() == 1 )
				getEntidade().setArquivo( "DOE" + data + SRHUtils.getTipoArquivo( arquivo.getName() ) );
			if ( getEntidade().getTipoPublicacao() == 2 )
				getEntidade().setArquivo( "DOTCE" + data + SRHUtils.getTipoArquivo( arquivo.getName() ) );
			if ( getEntidade().getTipoPublicacao() == 3 )
				getEntidade().setArquivo( "DOU" + data + SRHUtils.getTipoArquivo( arquivo.getName() ) );

			// gravando em disco
			java.io.File file = new java.io.File(parametro.getValor() + getEntidade().getArquivo());
			FileOutputStream fop;

			fop = new FileOutputStream(file);
			fop.write(arquivo.getBytes());
			fop.flush();
			fop.close();

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na gravação do arquivo no servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na gravação da foto do servidor.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}


	/**
	 * Abrir o arquivo
	 * 
	 * @return
	 */
	public String abrirArquivo() {

		FacesContext facesContext = FacesContext.getCurrentInstance();  
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();  

		// pegando o caminho
		Parametro parametro = parametroService.getByNome("CAMARQPUB");

		try {

			if (parametro == null)
				throw new SRHRuntimeException("Parametro do caminho da imagem nao encontrado na tabela SAPJAVA.FWPARAMETER");

			// pegando o arquivo
			File file = new File(parametro.getValor() + getEntidade().getArquivo()); // LINHA ALTERADA 

			response.setHeader("Content-Disposition", "attachment; filename=\"" + getEntidade().getArquivo() + "\";");
			response.setContentLength((int) file.length());

			// gerando a saida
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();

			byte[] buf = new byte[(int)file.length()];    
			int count;    
			while ((count = in.read(buf)) >= 0) {    
				out.write(buf, 0, count);    
			}    
			in.close();    
			out.flush();    
			out.close();    
			facesContext.responseComplete();

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na visualização do arquivo.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na visualização do arquivo.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {
	
		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.tipoDocumento != null && this.tipoDocumento.getId() != null) {
				String filtro = " WHERE publ.IDTIPODOCUMENTO = " + this.tipoDocumento.getId();
				parametros.put("FILTRO", filtro);
			}

			relatorioUtil.relatorio("publicacao.jasper", parametros, "publicacao.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {	
		setEntidade(new Publicacao());
		return "listar";
	}
	
	public String limpaForm() {
		setEntidade(new Publicacao());
		return "publicacaoForm";
	}

	/**
	 * Gets and Sets
	 */
	public List<TipoDocumento> getComboTipoDocumento() {return comboTipoDocumento;}

	public TipoDocumento getTipoDocumento() {return tipoDocumento;}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {this.tipoDocumento = tipoDocumento;}

	public Publicacao getEntidade() {return entidade;}
	public void setEntidade(Publicacao entidade) {this.entidade = entidade;}

	public Long getEsfera() {return esfera;}
	public void setEsfera(Long esfera) {this.esfera = esfera;}

	public List<Publicacao> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new Publicacao() );
			comboTipoDocumento = null;
			comboTipoDocumentoConsulta = null;
			tipoDocumento = new TipoDocumento();
			lista = new ArrayList<Publicacao>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}

	public UploadedFile getArquivo() {return arquivo;}
	public void setArquivo(UploadedFile arquivo) {this.arquivo = arquivo;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Publicacao>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(publicacaoService.search(tipoDocumento.getId(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Publicacao> getPagedList() {return pagedList;}
	public void setPagedList(List<Publicacao> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
}