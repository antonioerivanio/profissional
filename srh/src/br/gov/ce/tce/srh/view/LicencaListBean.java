package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.service.TipoLicencaService;
import br.gov.ce.tce.srh.service.sca.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC036_Lançar Licença
* 
* @since   : Nov 15, 2011, 10:03:00 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("licencaListBean")
@Scope("session")
public class LicencaListBean implements Serializable {

	static Logger logger = Logger.getLogger(LicencaListBean.class);

	@Autowired
	private LicencaService licencaService;

	@Autowired
	private TipoLicencaService tipoLicencaService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private List<Licenca> lista;
	private Licenca entidade = new Licenca();
	private TipoLicenca tipoLicenca;

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Licenca> pagedList = new ArrayList<Licenca>(); 
	private int flagRegistroInicial = -1;

	// combo
	private List<TipoLicenca> comboTipoLicenca;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			if ( getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			if ( this.tipoLicenca != null ) {
				count = licencaService.count( getEntidade().getPessoal().getId(), this.tipoLicenca.getId() );
			} else {
				count = licencaService.count(getEntidade().getPessoal().getId());
			}

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
			FacesUtil.addErroMessage("Erro de conexão com a base de dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {
			
			licencaService.excluir(entidade);
			
			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao excluir a Licença. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return consultar();
	}
	

	/**
	 * Combo Tipo Licenca
	 * 
	 * @return
	 */
	public List<TipoLicenca> getComboTipoLicenca() {

		try {

			if ( this.comboTipoLicenca == null)
				this.comboTipoLicenca = tipoLicencaService.findAll();

		} catch (Exception e) {
	      	FacesUtil.addErroMessage("Erro ao carregar o campo tipo de licença. Operação cancelada.");
	       	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoLicenca;
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {

			// validando campos da entidade
			if ( getEntidade().getPessoal() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer(" L.IDPESSOAL = " + this.entidade.getPessoal().getId());
			if ( this.tipoLicenca != null )
				filtro.append(" AND L.IDTIPOLICENCA = " + this.tipoLicenca.getId());

			parametros.put("FILTRO", filtro.toString());
			parametros.put("MATRICULA", this.matricula);

			relatorioUtil.relatorio("licenca.jasper", parametros, "licenca.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório de Licença. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	public String limpaTela() {
		setEntidade(new Licenca());
		return "listar";
	}
	
	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;
			this.flagRegistroInicial = -1;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ); 
				if (funcional != null) {
					getEntidade().setPessoal( funcional.getPessoal() );
					this.nome = getEntidade().getPessoal().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
					limparListas();
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;
			this.flagRegistroInicial = -1;

			try {

				Funcional funcional = funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf );
				if (funcional != null) {
					getEntidade().setPessoal( funcional.getPessoal() );
					this.nome = funcional.getNomeCompleto();
					this.matricula = funcional.getMatricula();
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
					limparListas();
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Licenca getEntidade() {return entidade;}
	public void setEntidade(Licenca entidade) {this.entidade = entidade;}
	
	public TipoLicenca getTipoLicenca() {return tipoLicenca;}
	public void setTipoLicenca(TipoLicenca tipoLicenca) {this.tipoLicenca = tipoLicenca;}

	public List<Licenca> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public boolean isPassouConsultar() {
		return passouConsultar;
	}


	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}


	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {								
				setCpf(authenticationService.getUsuarioLogado().getCpf());								
				count = licencaService.count(getEntidade().getPessoal().getId());
				flagRegistroInicial = -1;				
				
			} catch (Exception e) {
				limparListas();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}			
			
		}else if (!passouConsultar) {
			setEntidade( new Licenca() );
			setTipoLicenca( null );
			this.matricula = new String();
			this.cpf = new String();
			this.nome = new String();
			this.lista = new ArrayList<Licenca>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}


	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Licenca>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			
			if ( this.tipoLicenca != null) {
				setPagedList( licencaService.search( getEntidade().getPessoal().getId(), this.tipoLicenca.getId(), getDataTable().getFirst(), getDataTable().getRows()) );
			} else { 
				setPagedList(licencaService.search(getEntidade().getPessoal().getId(), getDataTable().getFirst(), getDataTable().getRows()));
			}

			if ( count != 0 ) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Licenca> getPagedList() {return pagedList;}
	public void setPagedList(List<Licenca> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
}