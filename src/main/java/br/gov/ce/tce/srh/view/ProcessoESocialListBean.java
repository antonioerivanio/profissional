package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ProcessoESocial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ProcessoESocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("processoESocialListBean")
@Scope("session")
public class ProcessoESocialListBean implements Serializable{

	static Logger logger = Logger.getLogger(ProcessoESocialListBean.class);

	@Autowired
	private ProcessoESocialService service;

	private HtmlForm form;
	private boolean passouConsultar = false;

	private String numero;
	private ProcessoESocial entidade = new ProcessoESocial();

	private List<ProcessoESocial> processoESocialList = new ArrayList<ProcessoESocial>();
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<ProcessoESocial> pagedList = new ArrayList<ProcessoESocial>();
	private int registroInicial = 0;
	private Integer pagina = 1;

	public String consultar() {

		try {

			limparListas();

			processoESocialList = service.search(this.numero, null, null);

			count = processoESocialList.size();

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
		this.entidade = new ProcessoESocial();
		return "listar";
	}

	private void limparListas() {
		processoESocialList = new ArrayList<ProcessoESocial>();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<ProcessoESocial>();
		pagina = 1;
	}

	public void setForm(HtmlForm form) {
		this.form = form;
	}

	public HtmlForm getForm() {

		if (!passouConsultar) {
			this.numero = new String();
			this.entidade = new ProcessoESocial();
			limparListas();
			registroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}

	public PagedListDataModel getDataModel() {
		if (registroInicial != getPrimeiroDaPagina()) {
			registroInicial = getPrimeiroDaPagina();

			setPagedList(service.search(this.numero, registroInicial, dataModel.getPageSize()));

			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<ProcessoESocial> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<ProcessoESocial> pagedList) {
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public ProcessoESocial getEntidade() {
		return entidade;
	}

	public void setEntidade(ProcessoESocial entidade) {
		this.entidade = entidade;
	}	
	
}
