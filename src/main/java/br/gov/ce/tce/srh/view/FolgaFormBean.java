package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.dao.TipoFolgaDAO;
import br.gov.ce.tce.srh.domain.Folga;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.TipoFolga;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FolgaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("folgaFormBean")
@Scope("view")
public class FolgaFormBean implements Serializable {
	
static Logger logger = Logger.getLogger(FolgaListBean.class);
	
	@Autowired
	private FolgaService folgaService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private TipoFolgaDAO tipoFolgaDAO;
	
	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	private Folga entidade = new Folga();

	private String matricula = new String();
	private String matriculaConsulta = new String();
	private String nome = new String();

	private Date inicial;
	private Date fim;
	
	private boolean alterar;
	private boolean alterarSaldo;
	
	private List<TipoFolga> comboTipoFolga;
	
	private Integer pagina = 1;
	
	private UploadedFile comprovante;
	
	@PostConstruct
	public void init() {
		Folga flashParameter = (Folga) FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Folga());
		this.pagina = (Integer) FacesUtil.getFlashParameter("pagina");
		this.matriculaConsulta = (String) FacesUtil.getFlashParameter("matricula");
		
		try {
			if(this.entidade.getId() == null) {
				this.alterar = false;				
			} else {				
				this.alterar = true;
			
				this.matricula = this.matriculaConsulta;
				this.nome = entidade.getPessoal().getNomeCompleto();
	
				this.inicial = entidade.getInicio();
				this.fim = entidade.getFim();
			}
			this.alterarSaldo = this.entidade.getDebitos() == null || this.entidade.getDebitos().size() == 0;
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void salvar() {		
		try {
			this.entidade.setInicio(this.inicial);
			this.entidade.setFim(this.fim);

			folgaService.salvar(entidade);

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
	
	public String voltar() {
		if (entidade.getId() != null) {
			if (!alterar) {
				pagina = 1;				
			}			
			matriculaConsulta = matricula;
		}
		
		FacesUtil.setFlashParameter("matricula", matriculaConsulta);
		FacesUtil.setFlashParameter("pagina", pagina);
        return "listar";
	}
	
	public List<TipoFolga> getComboTipoFolga() {
		try {

			if ( this.comboTipoFolga == null ) {
				this.comboTipoFolga = tipoFolgaDAO.findAll();				
			}

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de folga. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoFolga;
	}
	
	public Folga getEntidade() { return entidade; }
	public void setEntidade(Folga entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( matricula != null && (this.matricula == null || !this.matricula.equals(matricula)) ) {
			this.matricula = matricula;

			try {

				getEntidade().setPessoal(funcionalService.getCpfAndNomeByMatriculaAtiva(this.matricula).getPessoal());
				if ( getEntidade().getPessoal() != null ) {
					this.nome = getEntidade().getPessoal().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Date getInicial() {return inicial;}
	public void setInicial(Date inicial) {
		this.inicial = inicial;
	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {
		this.fim = fim;
	}	
	
	public boolean isAlterar() {return alterar;}	
	
	public void uploadComprovante(FileUploadEvent event) {

		try {

			// pegando o caminho do arquivo no servidor
			Parametro parametro = parametroService.getByNome("pathComprovanteFolgaSRH");

			if (parametro == null) {
				throw new SRHRuntimeException(
						"Parâmetro do caminho do comprovante não encontrado na tabela SRH.TB_PARAMETRO");				
			}

			setComprovante(event.getUploadedFile());
			
			// gravando em disco
			File file = new File(parametro.getValor() + comprovante.getName());
			FileOutputStream fop;
			
			// setando o nome do comprovante
			getEntidade().setCaminhoComprovante(file.getName());

			fop = new FileOutputStream(file);
			fop.write(comprovante.getData());
			fop.flush();
			fop.close();

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage("Erro na gravação do comprovante de folga.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (FileNotFoundException e) {
			FacesUtil.addErroMessage("Erro na gravação do comprovante de folga.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErroMessage("Erro na gravação do comprovante de folga.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void comprovanteFolga() {
		try {
			
			Parametro parametro = parametroService.getByNome("pathComprovanteFolgaSRH");
			String comprovante = parametro.getValor() + entidade.getCaminhoComprovante();
			InputStream in = new FileInputStream(comprovante);
			byte[] comprovanteBytes = IOUtils.toByteArray(in);
			relatorioUtil.openPdf(comprovanteBytes, comprovante);
			IOUtils.closeQuietly(in);

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

	public UploadedFile getComprovante() {
		return comprovante;
	}

	public void setComprovante(UploadedFile comprovante) {
		this.comprovante = comprovante;
	}

	public boolean isAlterarSaldo() {
		return alterarSaldo;
	}	

}
