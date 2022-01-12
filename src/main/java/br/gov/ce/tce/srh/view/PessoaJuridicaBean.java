package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since : Dez 08, 2021, 10:41:00 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */

@Component("pessoaJuridicaBean")
@Scope("view")
public class PessoaJuridicaBean implements Serializable {

	private static final long serialVersionUID = 4322801258398227080L;

	static Logger logger = Logger.getLogger(PessoaJuridicaBean.class);

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	// entidades das telas
	private List<PessoaJuridica> lista = new ArrayList<PessoaJuridica>();
	private PessoaJuridica entidade = new PessoaJuridica();

	
	private String cnpj = new String();
	private String razaoSocial = new String();
	private String nomeFantasia = new String();
	
	
	
	// paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<PessoaJuridica> pagedList = new ArrayList<PessoaJuridica>();
	private int flagRegistroInicial = 0;

	@PostConstruct
	private void init() {
		PessoaJuridica flashParameter = (PessoaJuridica) FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new PessoaJuridica());
	}

	public void consultar() {

		try {
			count = pessoaJuridicaService.count(cnpj, razaoSocial, nomeFantasia);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;

		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	private boolean verificaCNPJ(String cnpj) {
		if (SRHUtils.isCNPJ(entidade.getCnpj())) {
			return true;
		} else {
			FacesUtil.addErroMessage("CNPJ inválido.");
			return false;
		}
	}

	public void salvar() {

			try {
				if (verificaCNPJ(entidade.getCnpj())) {
					
					entidade.setCnpj(SRHUtils.removerMascara(entidade.getCnpj()));
					entidade.setRazaoSocial(entidade.getRazaoSocial().toUpperCase());
					entidade.setNomeFantasia(entidade.getNomeFantasia().toUpperCase());
					pessoaJuridicaService.salvar(entidade);
					setEntidade(new PessoaJuridica());

					FacesUtil.addInfoMessage("Operação realizada com sucesso.");
					logger.info("Operação realizada com sucesso.");
				}
				
			} catch (SRHRuntimeException e) {
				FacesUtil.addErroMessage(e.getMessage());
				logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}

	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());
		return "incluirAlterar";
	}

	public void excluir() {

		try {

			pessoaJuridicaService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage(
					"Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade(new PessoaJuridica());
		consultar();
	}


	public PessoaJuridica getEntidade() {
		return entidade;
	}

	public void setEntidade(PessoaJuridica pessoaJuridica) {
		this.entidade = pessoaJuridica;
	}

	public List<PessoaJuridica> getLista() {
		return lista;
	}

	// PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<PessoaJuridica>();
	}

	public UIDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(UIDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public PagedListDataModel getDataModel() {
		if (flagRegistroInicial != getDataTable().getFirst()) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(pessoaJuridicaService.search(cnpj, razaoSocial, nomeFantasia, getDataTable().getFirst(),
					getDataTable().getRows()));
			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<PessoaJuridica> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<PessoaJuridica> pagedList) {
		this.pagedList = pagedList;
	}
	// FIM PAGINAÇÃO

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
}