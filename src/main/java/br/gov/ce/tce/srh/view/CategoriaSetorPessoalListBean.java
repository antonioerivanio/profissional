package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Acrescimo;
import br.gov.ce.tce.srh.domain.CategoriaSetorPessoal;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CategoriaSetorPessoalService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("categoriaSetorPessoalListBean")
@Scope("view")
public class CategoriaSetorPessoalListBean implements Serializable {

	static Logger logger = Logger
			.getLogger(CategoriaSetorPessoalListBean.class);

	@Autowired
	private CategoriaSetorPessoalService categoriaSetorPessoalService;
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametos de tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private List<Acrescimo> lista;
	private CategoriaSetorPessoal entidade = new CategoriaSetorPessoal();

	// paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CategoriaSetorPessoal> pagedList = new ArrayList<CategoriaSetorPessoal>();
	private int flagRegistroInicial = 0;
	
	public void consultar() {

		try {

			// valida consulta pessoa
			if (getEntidade().getPessoal() == null)
				throw new SRHRuntimeException("Selecione um funcionário.");

			count = categoriaSetorPessoalService.count(getEntidade()
					.getPessoal().getId());

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
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

			categoriaSetorPessoalService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (DataAccessException e) {
			FacesUtil
					.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		consultar();
	}

	public void relatorio() {

		try {

			// valida consulta pessoa
			if (getEntidade().getPessoal() == null)
				throw new SRHRuntimeException("Selecione um funcionário.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("FILTRO", this.entidade.getPessoal().getId());
			parametros.put("MATRICULA", this.matricula);
			parametros.put("NOME_COMPLETO", this.nome);

			relatorioUtil.relatorio("categoriaSetorServidor.jasper", parametros,
					"categoriaSetorServidor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro na geração do Relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		if (!this.matricula.equals(matricula)) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService
						.getCpfAndNomeByMatriculaAtiva(this.matricula);
				if (funcional != null) {
					getEntidade().setPessoal(funcional.getPessoal());
					this.nome = getEntidade().getPessoal().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();
				} else {
					FacesUtil
							.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil
						.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if (!this.cpf.equals(cpf)) {
			this.cpf = cpf;

			try {

				Funcional funcional = funcionalService
						.getMatriculaAndNomeByCpfAtiva(this.cpf);
				if (funcional != null) {
					getEntidade().setPessoal(funcional.getPessoal());
					this.nome = getEntidade().getPessoal().getNomeCompleto();
					this.matricula = funcional.getMatricula();
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}

			} catch (Exception e) {
				FacesUtil
						.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Acrescimo> getLista() {
		return lista;
	}

	public void setLista(List<Acrescimo> lista) {
		this.lista = lista;
	}

	// PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CategoriaSetorPessoal>();
	}

	public CategoriaSetorPessoal getEntidade() {
		return entidade;
	}

	public void setEntidade(CategoriaSetorPessoal entidade) {
		this.entidade = entidade;
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
			setPagedList(categoriaSetorPessoalService.search(getEntidade()
					.getPessoal().getId(), getDataTable().getFirst(),
					getDataTable().getRows()));
			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<CategoriaSetorPessoal> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<CategoriaSetorPessoal> pagedList) {
		this.pagedList = pagedList;
	}
	// FIM PAGINAÇÃO

}