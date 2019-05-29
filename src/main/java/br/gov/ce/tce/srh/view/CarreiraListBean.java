package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Carreira;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CarreiraService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("carreiraListBean")
@Scope("session")
public class CarreiraListBean implements Serializable{

	static Logger logger = Logger.getLogger(CarreiraListBean.class);

	@Autowired
	private CarreiraService service;

	private HtmlForm form;
	private boolean passouConsultar = false;

	private String descricao;
	private Carreira entidade = new Carreira();

	private List<Carreira> carreiraList = new ArrayList<Carreira>();
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Carreira> pagedList = new ArrayList<Carreira>();
	private int registroInicial = 0;
	private Integer pagina = 1;

	public String consultar() {

		try {

			limparListas();

			carreiraList = service.search(this.descricao, null, null);

			count = carreiraList.size();

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

			service.encerrarVigencia(entidade);

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
		this.entidade = new Carreira();
		return "listar";
	}

	private void limparListas() {
		carreiraList = new ArrayList<Carreira>();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Carreira>();
		pagina = 1;
	}

	public void setForm(HtmlForm form) {
		this.form = form;
	}

	public HtmlForm getForm() {

		if (!passouConsultar) {
			this.descricao = new String();
			this.entidade = new Carreira();
			limparListas();
			registroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}

	public PagedListDataModel getDataModel() {
		if (registroInicial != getPrimeiroDaPagina()) {
			registroInicial = getPrimeiroDaPagina();

			setPagedList(service.search(this.descricao, registroInicial, dataModel.getPageSize()));

			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Carreira> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<Carreira> pagedList) {
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Carreira getEntidade() {
		return entidade;
	}

	public void setEntidade(Carreira entidade) {
		this.entidade = entidade;
	}	
	
}
