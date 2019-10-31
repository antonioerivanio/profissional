package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.RubricaESocialTCE;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RubricaESocialTCEService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("rubricaESocialTCEListBean")
@Scope("view")
public class RubricaESocialTCEListBean implements Serializable{

	static Logger logger = Logger.getLogger(RubricaESocialTCEListBean.class);

	@Autowired
	private RubricaESocialTCEService service;

	private String descricao;
	private RubricaESocialTCE entidade = new RubricaESocialTCE();

	private List<RubricaESocialTCE> rubricaESocialTCEList = new ArrayList<RubricaESocialTCE>();
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RubricaESocialTCE> pagedList = new ArrayList<RubricaESocialTCE>();
	private int registroInicial = 0;
	private Integer pagina = 1;
	
	@PostConstruct
	private void init() {
		FacesUtil.setFlashParameter("entidade", null);
    }

	public void consultar() {

		try {

			limparListas();

			rubricaESocialTCEList = service.search(this.descricao, null, null);

			count = rubricaESocialTCEList.size();

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			registroInicial = -1;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	public void excluir() {

		try {

			service.excluir(entidade);
			this.consultar();

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}

	private void limparListas() {
		rubricaESocialTCEList = new ArrayList<RubricaESocialTCE>();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RubricaESocialTCE>();
		pagina = 1;
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

	public List<RubricaESocialTCE> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<RubricaESocialTCE> pagedList) {
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

	public RubricaESocialTCE getEntidade() {
		return entidade;
	}

	public void setEntidade(RubricaESocialTCE entidade) {
		this.entidade = entidade;
	}	
	
}
