package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AmbienteTrabalho;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AmbienteTrabalhoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("ambienteTrabalhoListBean")
@Scope("session")
public class AmbienteTrabalhoListBean implements Serializable{

	static Logger logger = Logger.getLogger(AmbienteTrabalhoListBean.class);

	@Autowired
	private AmbienteTrabalhoService service;

	private HtmlForm form;
	private boolean passouConsultar = false;

	private String nome;
	private AmbienteTrabalho entidade = new AmbienteTrabalho();

	private List<AmbienteTrabalho> ambienteTrabalhoList = new ArrayList<AmbienteTrabalho>();
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AmbienteTrabalho> pagedList = new ArrayList<AmbienteTrabalho>();
	private int registroInicial = 0;
	private Integer pagina = 1;

	public String consultar() {

		try {

			limparListas();

			ambienteTrabalhoList = service.search(this.nome, null, null);

			count = ambienteTrabalhoList.size();

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			registroInicial = -1;

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

	public String excluir() {

		try {

			service.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}

	public String limpaTela() {
		this.entidade = new AmbienteTrabalho();
		return "listar";
	}

	private void limparListas() {
		ambienteTrabalhoList = new ArrayList<AmbienteTrabalho>();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AmbienteTrabalho>();
		pagina = 1;
	}

	public void setForm(HtmlForm form) {
		this.form = form;
	}

	public HtmlForm getForm() {

		if (!passouConsultar) {
			this.nome = new String();
			this.entidade = new AmbienteTrabalho();
			limparListas();
			registroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}

	public PagedListDataModel getDataModel() {
		if (registroInicial != getPrimeiroDaPagina()) {
			registroInicial = getPrimeiroDaPagina();

			setPagedList(service.search(this.nome, registroInicial, dataModel.getPageSize()));

			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AmbienteTrabalho> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<AmbienteTrabalho> pagedList) {
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public AmbienteTrabalho getEntidade() {
		return entidade;
	}

	public void setEntidade(AmbienteTrabalho entidade) {
		this.entidade = entidade;
	}	
	
}
