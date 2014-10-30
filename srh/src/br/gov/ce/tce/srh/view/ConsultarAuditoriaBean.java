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
import br.gov.ce.tce.srh.domain.Revisao.Variavel;
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

//	private List<Restricao> restricoes;		
//	private String valorRestricao;	
//	private Variavel coluna;		
	
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
	
	
	public void atributoSelecionado(){		
		
		if(this.atributo != null && (!this.atributo.equals(""))){
			for (Variavel atributo : getAtributosEntidade()) {
				if(atributo.getNome().equalsIgnoreCase(this.atributo)){
					this.revisaoConsulta.setColuna(atributo);					
					return;
				}
			}
		}else{
			this.revisaoConsulta.setColuna(null);
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
	
	
	public List<Variavel> getAtributosEntidade() {
		
		Set<Field> fieldsEntidade = null;
		
		List<Variavel> atributosEntidade = null;
		
		if(this.revisaoConsulta.getEntidade() != null){			
			fieldsEntidade = this.auditoriaService.getAtributosSimplesEntidade((Class<?>)this.revisaoConsulta.getEntidade());			
			atributosEntidade = new ArrayList<Variavel>(fieldsEntidade.size());
			
			for (Field field : fieldsEntidade)
				atributosEntidade.add(new Variavel(field.getName(), field.getType(), null));			
			
			Collections.sort(atributosEntidade);			
		}
		return atributosEntidade;
	}
	
//	POPULA AS COMBOS - FIM
	
	
	
//	public void adicionarRestricao() {
//	
//	try {
//
//		auditoriaService.validarAdicionarRestricao(restricao, entidade, valorRestricao);			
//		
//		//Obtem o converter apropriado para converter a String do valor em objeto:
//		Converter atributoConverter = getFacesContext().getApplication().createConverter(restricao.getTipo());
//		Object valorRestricaoObj;
//		
//		if (atributoConverter == null) {				
//			valorRestricaoObj = Long.parseLong(valorRestricao);				
//		} else {
//			//Cria um componente temporário para converter o valor em objeto:
//			UIInput uixInput = new UIInput();
//			uixInput.setConverter(atributoConverter);
//			//Converte a String para o objeto apropriado:
//			valorRestricaoObj = atributoConverter.getAsObject(getFacesContext(), uixInput, (String) valorRestricao);
//		}
//		
//		Restricao novaRestricao = new Restricao(restricao.getAtributo(), restricao.getTipo(), valorRestricaoObj);
//		Iterator<Restricao> itRestricoes = revisaoConsulta.getRestricoes().iterator();
//		while (itRestricoes.hasNext()) {
//			Restricao restricao = (Revisao.Restricao) itRestricoes.next();
//			if (restricao.getAtributo().equals(novaRestricao.getAtributo())) {
//				itRestricoes.remove();
//				break;
//			}
//		}
//		
//		revisaoConsulta.getRestricoes().add(novaRestricao);
//		
//	} catch (SRHRuntimeException e) {
//		FacesUtil.addErroMessage(e.getMessage());
//		logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
//	} catch (ConverterException e) {
//		FacesUtil.addErroMessage("Erro ao tentar converter o valor da coluna informado.");
//		logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
//	} catch (Exception e) {
//		FacesUtil.addErroMessage("Ocorreu algum erro ao adicionar restrição. Operação cancelada.");
//		logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
//	}
//}


//	public void excluirRestricao() {
//	
//		List<Restricao> restricoesAux = new ArrayList<Revisao.Restricao>();
//		restricoesAux.addAll(revisaoConsulta.getRestricoes());
//	
//		if(atributo != null && (!atributo.equals(""))){			
//			for (Restricao rest : restricoes) {
//				if(rest.getAtributo().equals(atributo))					
//					restricoesAux.remove(rest);				
//			}
//		}
//	
//		if(restricoesAux.size() == 0){
//			restricoes = null;
//			restricao = null;				
//			atributo = null;
//			valorRestricao = null;		
//			revisaoConsulta.getRestricoes().clear();			
//		}else
//			revisaoConsulta.setRestricoes(restricoesAux);
//	}
	
	
//	public List<Restricao> getRestricoes() {
//		restricoes = revisaoConsulta.getRestricoes();
//		return restricoes;
//	}	

	
//	public List<Restricao> getRestricoesEntidade() {
//		Set<Field> atributosSimplesEntidade = null;
//		List<Restricao> restricoesEntidade = null;
//		if(revisaoConsulta.getEntidade() != null){
//			atributosSimplesEntidade = auditoriaService.getAtributosSimplesEntidade((Class<?>) revisaoConsulta.getEntidade());
//			restricoesEntidade = new ArrayList<Restricao>(atributosSimplesEntidade.size());
//			for (Field field : atributosSimplesEntidade) {
//				restricoesEntidade.add(new Restricao(field.getName(), field.getType(), null));
//			}
//			Collections.sort(restricoesEntidade);
//		}
//		return restricoesEntidade;
//	}
	
	
//	public String getTipoDadoAtributoSelecionado() {
//		if (restricao != null) {
//			return restricao.getAtributo();
//		}
//		return null;
//	}	
	
	
//	public void setColuna(Variavel coluna) {this.coluna = coluna;}
//	public Variavel getColuna() {return coluna;}
	
//	public void setValorRestricao(String valorRestricao) {this.valorRestricao = valorRestricao;}
//	public String getValorRestricao() {	return valorRestricao; }
	
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
			
			setPagedList(auditoriaService.search(revisaoConsulta, getDataTable().getFirst(), 10));
			
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
