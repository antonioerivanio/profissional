package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.TipoRevisao;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AuditoriaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.UsuarioService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("consultarAuditoriaBean")
@Scope("session")
public class ConsultarAuditoriaBean implements Serializable{
	
	static Logger logger = Logger.getLogger(ConsultarAuditoriaBean.class);
	
	@Autowired
	private AuditoriaService auditoriaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	private HtmlForm form;

	private Revisao revisaoConsulta = new Revisao();
	
	private Long tipoRevisao;
	private Long usuario;
	private Long pessoal;
	private String entidade;
	private String atributo;
	
	private List<TipoRevisao> tiposRevisao;
	private List<Usuario> usuarios;
	private List<Pessoal> pessoais;
	private Set<Class<?>> entidades;
		
	
//	PAGINAÇÃO - INICIO
	
	private int count = 0;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Revisao> pagedList = new ArrayList<Revisao>();
	private int flagRegistroInicial = 0;
	
//	PAGINAÇÃO - FIM	
	
	public String consultar() {
		try {
			
			this.limparListas();
			
			if (this.entidade == null || this.entidade.equals("")){
				FacesUtil.addErroMessage("A tabela é obrigatória.");
				return null;
			}
			
			this.count = this.auditoriaService.count(this.revisaoConsulta);

			if(this.count == 0){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			this.flagRegistroInicial = -1;
						
		} catch (SRHRuntimeException e) {
			this.limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return null;
	}
	
	
//	AÇÕES AJAX DAS COMBOS - INICIO
	
	public void tipoEventoSelecionado(){
		if(this.tipoRevisao > -1){
			this.revisaoConsulta.setTipoRevisao(TipoRevisao.getById(this.tipoRevisao));
		} else {
			this.revisaoConsulta.setTipoRevisao(null);
		}
	}
	
	
	public void usuarioSelecionado(){
		if(this.usuario != 0){			
			for (Usuario usuario : this.usuarios){
				if(usuario.getId().equals(this.usuario)){
					this.revisaoConsulta.setUsuario(usuario);
					return;
				}
			}			
		} else {
			this.revisaoConsulta.setUsuario(null);
		}
	}
	
	
	public void pessoalSelecionado(){
		if(this.pessoal != 0){
			for (Pessoal pessoal : this.pessoais){
				if(pessoal.getId().equals(this.pessoal)){
					this.revisaoConsulta.setPessoal(pessoal);
					this.revisaoConsulta.setFuncionais(this.funcionalService.findByPessoal(this.getPessoal(), ""));
					return;
				}
			}
		} else {
			this.revisaoConsulta.setPessoal(null);
			this.revisaoConsulta.setFuncionais(null);
		}
	}
	
	
	public void entidadeSelecionada() {
												
		if(this.entidade != null && (!this.entidade.equals(""))){
			for (Class<?> entidade : this.entidades) {
				if(entidade.getSimpleName().equalsIgnoreCase(this.entidade)){
					this.revisaoConsulta.setEntidade(entidade);
					return;
				}
			}
		} else {
			this.revisaoConsulta.setEntidade(null);
		} 
	}
	
//	AÇÕES AJAX DAS COMBOS - FIM	
	
	
//	POPULA AS COMBOS - INICIO
	
	public List<TipoRevisao> getTiposRevisao(){
		if (this.tiposRevisao == null)
			this.tiposRevisao = new ArrayList<TipoRevisao>(Arrays.asList(TipoRevisao.values()));
		
		return this.tiposRevisao;
	}
	
	
	public List<Usuario> getUsuarios(){
		if (this.usuarios == null)
			this.usuarios = this.usuarioService.findAll();
		
		return this.usuarios;
	}
	
	
	public List<Pessoal> getPessoais(){
		if (this.pessoais == null)
			this.pessoais = this.pessoalService.findAllComFuncional();
		
		return this.pessoais;
	}	
		
	
	public Set<Class<?>> getEntidades() throws ClassNotFoundException {
		if(this.entidades == null)
			this.entidades = this.auditoriaService.getEntidadesAuditadas();
		
		return this.entidades;
	}	
	
	
	public List<String> getAtributosEntidade() {
		
		Set<Field> fieldsEntidade = null;
		
		List<String> atributosEntidade = null;
		
		if(this.revisaoConsulta.getEntidade() != null){			
			fieldsEntidade = this.auditoriaService.getAtributosEntidade((Class<?>)this.revisaoConsulta.getEntidade());			
			atributosEntidade = new ArrayList<String>(fieldsEntidade.size());
			
			for (Field field : fieldsEntidade)
				atributosEntidade.add(field.getName());			
			
			Collections.sort(atributosEntidade);			
		}
		return atributosEntidade;
	}
	
//	POPULA AS COMBOS - FIM

	
	public Revisao getRevisaoConsulta() {return revisaoConsulta;}
	public void setRevisaoConsulta(Revisao revisaoConsulta) {this.revisaoConsulta = revisaoConsulta;}	

	public void setTipoRevisao(Long tipoRevisao) {this.tipoRevisao = tipoRevisao;}
	public Long getTipoRevisao() {return tipoRevisao;}
	
	public Long getUsuario() {return usuario;}
	public void setUsuario(Long usuario) {this.usuario = usuario;}
	
	public Long getPessoal() {return pessoal;}
	public void setPessoal(Long pessoal) {this.pessoal = pessoal;}
	
	public String getEntidade() {return entidade;}
	public void setEntidade(String entidade) {this.entidade = entidade;}
	
	public String getAtributo() {return atributo;}
	public void setAtributo(String atributo) {this.atributo = atributo;}

	public FacesContext getFacesContext() {return FacesContext.getCurrentInstance();}
	
	public HttpSession getSession() {return (HttpSession) getFacesContext().getExternalContext().getSession(false);}
	
	public HttpServletRequest getRequestSession() {return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();}
	
	
	/**
	 * Obtém o usuário logado na sessão
	 * @return
	 */
	public Usuario getUsuarioLogado() {
		return SRHUtils.getUsuarioLogado();
	}
	
	
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {

		limpar();

		return form;
	}
	
	
	public String limpar(){

		revisaoConsulta = new Revisao();
		
		tipoRevisao = null;
		usuario = null;
		pessoal = null;
		entidade = null;
		atributo = null;		
		
		tiposRevisao = null;
		usuarios = null;
		pessoais = null;
		entidades = null;
		
		limparListas();
		
		return null;
	}	

	
//	PAGINAÇÃO	
	
	public void limparListas() {
		count = 0;
		dataTable = new HtmlDataTable();		
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Revisao>();
		flagRegistroInicial = 0;		
	}

	
	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}
	
	
	public PagedListDataModel getDataModel() {
		
		if( flagRegistroInicial != getDataTable().getFirst()) {
			
			flagRegistroInicial = getDataTable().getFirst();
			
			setPagedList(auditoriaService.search(revisaoConsulta, getDataTable().getFirst(), 10, this.atributo));
			
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	
	public List<Revisao> getPagedList() {return pagedList;}
	public void setPagedList(List<Revisao> pagedList) {this.pagedList = pagedList;}
	
//	FIM PAGINAÇÃO

}
