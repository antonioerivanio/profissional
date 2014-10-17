package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.Revisao.Restricao;
import br.gov.ce.tce.srh.domain.TipoRevisao;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AuditoriaService;
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
	
	// controle de acesso do formulario
	private HtmlForm form;

	private List<Restricao> restricoes;
	private Restricao restricao;	
	private String atributo;	
	private String valorRestricao;
	
	private Revisao revisaoConsulta = new Revisao();	
	private Long tipoRevisao;
	private Long usuario;
	private String entidade;	
	private Set<Class<?>> entidades;
	private List<Usuario> usuarios;
	private List<TipoRevisao> tiposRevisao;	
	
	//paginação
	private int count = 0;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Revisao> pagedList = new ArrayList<Revisao>();
	private int flagRegistroInicial = 0;
	
	
	public String consultar() {
		try {
			
			count = auditoriaService.count(revisaoConsulta);

			if(count == 0){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			flagRegistroInicial = -1;
						
		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return null;
	}
		
	public void adicionarRestricao() {
		
		try {

			auditoriaService.validarAdicionarRestricao(restricao, entidade, valorRestricao);			
			
			//Obtem o converter apropriado para converter a String do valor em objeto:
			Converter atributoConverter = getFacesContext().getApplication().createConverter(restricao.getTipo());
			Object valorRestricaoObj;
			
			//Verifica se é necessário, no caso de o atributo não ser do tipo String: 
			if (atributoConverter == null) {
				valorRestricaoObj = valorRestricao;
			} else {
				//Cria um componente temporário para converter o valor em objeto:
				UIInput uixInput = new UIInput();
				uixInput.setConverter(atributoConverter);
				//Converte a String para o objeto apropriado:
				valorRestricaoObj = atributoConverter.getAsObject(getFacesContext(), uixInput, (String) valorRestricao);
			}
			
			Restricao novaRestricao = new Restricao(restricao.getAtributo(), restricao.getTipo(), valorRestricaoObj);
			Iterator<Restricao> itRestricoes = revisaoConsulta.getRestricoes().iterator();
			while (itRestricoes.hasNext()) {
				Restricao restricao = (Revisao.Restricao) itRestricoes.next();
				if (restricao.getAtributo().equals(novaRestricao.getAtributo())) {
					itRestricoes.remove();
					break;
				}
			}
			
			revisaoConsulta.getRestricoes().add(novaRestricao);
			
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
		} catch (ConverterException e) {
			FacesUtil.addErroMessage("Erro ao tentar converter o valor da coluna informado.");
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao adicionar restrição. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void excluirRestricao() {
		
		List<Restricao> restricoesAux = null;
		
		if(atributo != null && (!atributo.equals(""))){
			
			for (Restricao rest : restricoes) {
				if(rest.getAtributo().equals(atributo)){
					
					restricoesAux = new ArrayList<Revisao.Restricao>();
					restricoesAux.addAll(revisaoConsulta.getRestricoes());
					restricoesAux.remove(rest);
					
					if(restricoesAux.size() == 0)
						limparRestricoes();					
				}
			}
		}
		revisaoConsulta.setRestricoes(restricoesAux);
	}
	
	public void tipoEventoSelecionado(){
		if(tipoRevisao > -1){
			revisaoConsulta.setTipoRevisao(TipoRevisao.getById(tipoRevisao));
		} else {
			revisaoConsulta.setTipoRevisao(null);
		}
	}
	
	public void usuarioSelecionado(){
		if(this.usuario != 0){
			for (Usuario usuario : usuarios) {
				if(usuario.getId().equals(this.usuario)){
					revisaoConsulta.setUsuario(usuario);
				}
			}
		} else {
			revisaoConsulta.setUsuario(null);
		}
	}
	
	public void entidadeSelecionada() {
		
		limparRestricoes();
		limparListas();
										
		if(this.entidade != null && (!this.entidade.equals(""))){
			for (Class<?> entidade : entidades) {
				if(entidade.getSimpleName().equalsIgnoreCase(this.entidade)){
					revisaoConsulta.setEntidade(entidade);
					return;
				}
			}
		} 
	}
	
	public void atributoSelecionado(){
		setRestricao(null);
		if(atributo != null && (!atributo.equals(""))){
			for (Restricao restricao : getRestricoesEntidade()) {
				if(restricao.getAtributo().equalsIgnoreCase(atributo)){
					setRestricao(restricao);
					return;
				}
			}
		} 
	}
	
	public List<Restricao> getRestricoes() {
		restricoes = revisaoConsulta.getRestricoes();
		return restricoes;
	}	
	
	public Set<Class<?>> getEntidades() throws ClassNotFoundException {
		if(entidades == null){
			entidades = auditoriaService.getEntidadesAuditadas();
		}
		return entidades;
	}
	public void setEntidades(Set<Class<?>> entidades) {this.entidades = entidades;}

	public List<Usuario> getUsuarios(){
		if (usuarios == null) {
			usuarios = usuarioService.findAll();
		}
		return usuarios;
	}
	
	public List<TipoRevisao> getTiposRevisao(){
		if (tiposRevisao == null) {
			tiposRevisao = new ArrayList<TipoRevisao>(Arrays.asList(TipoRevisao.values()));
		}
		return tiposRevisao;
	}
	
	public List<Restricao> getRestricoesEntidade() {
		Set<Field> atributosSimplesEntidade = null;
		List<Restricao> restricoesEntidade = null;
		if(revisaoConsulta.getEntidade() != null){
			atributosSimplesEntidade = auditoriaService.getAtributosSimplesEntidade((Class<?>) revisaoConsulta.getEntidade());
			restricoesEntidade = new ArrayList<Restricao>(atributosSimplesEntidade.size());
			for (Field field : atributosSimplesEntidade) {
				restricoesEntidade.add(new Restricao(field.getName(), field.getType(), null));
			}
			Collections.sort(restricoesEntidade);
		}
		return restricoesEntidade;
	}	
	
	public String getTipoDadoAtributoSelecionado() {
		if (restricao != null) {
			return restricao.getAtributo();
		}
		return null;
	}	
	
	public void setRestricao(Restricao restricao) {this.restricao = restricao;}
	public Restricao getRestricao() {return restricao;}
	
	public void setValorRestricao(String valorRestricao) {this.valorRestricao = valorRestricao;}
	public String getValorRestricao() {	return valorRestricao; }
	
	public Revisao getRevisaoConsulta() {return revisaoConsulta;}
	public void setRevisaoConsulta(Revisao revisaoConsulta) {this.revisaoConsulta = revisaoConsulta;}	

	public void setTipoRevisao(Long tipoRevisao) {this.tipoRevisao = tipoRevisao;}
	public Long getTipoRevisao() {return tipoRevisao;}
	
	public Long getUsuario() {return usuario;}
	public void setUsuario(Long usuario) {this.usuario = usuario;}
	
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
		
		limparRestricoes();
		
		revisaoConsulta = new Revisao();
		tipoRevisao = null;
		usuario = null;
		entidade = null;
		entidades = null;
		usuarios = null;
		tiposRevisao = null;
		
		limparListas();
		
		return null;
	}
	
	public void limparRestricoes(){
		restricoes = null;
		restricao = null;				
		atributo = null;
		valorRestricao = null;		
		revisaoConsulta.getRestricoes().clear();
		revisaoConsulta.setEntidade(null);	
	}
	
	//PAGINAÇÃO	
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
	//FIM PAGINAÇÃO

}
