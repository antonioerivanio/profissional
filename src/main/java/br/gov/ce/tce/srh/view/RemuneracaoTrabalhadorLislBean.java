package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.service.RemuneracaoTrabalhadorEsocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("remuneracaoTrabalhadorListBean")
@Scope("view")
public class RemuneracaoTrabalhadorLislBean implements Serializable {

	static Logger logger = Logger.getLogger(RemuneracaoTrabalhadorLislBean.class);

	@Autowired
	private RemuneracaoTrabalhadorEsocialService remuneracaoTrabalhadorESocialTCEService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametro da tela de consulta
	private String nome = new String();
	private String cpf = new String();
	private String anoReferencia;
	private String mesReferencia;
	private boolean isRGPA;
	private boolean isEstagiario;

	// entidades das telas
	private List<RemuneracaoTrabalhador> lista;
	private RemuneracaoTrabalhador entidade = new RemuneracaoTrabalhador();
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RemuneracaoTrabalhador> pagedList = new ArrayList<RemuneracaoTrabalhador>();
	private int flagRegistroInicial = 0;
	
	@PostConstruct
	private void init() {
		RemuneracaoTrabalhador flashParameter = (RemuneracaoTrabalhador)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RemuneracaoTrabalhador());
		isRGPA = false;
		isEstagiario = false;
    }
	
	public void consultar() {
		if(!mesReferencia.equalsIgnoreCase("0")  && !anoReferencia.equalsIgnoreCase("")) {

			try {
	
				count = remuneracaoTrabalhadorESocialTCEService.count( this.nome, this.cpf, anoReferencia, mesReferencia, isRGPA, isEstagiario );
	
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
		else {
			FacesUtil.addErroMessage("É necessário informar o Ano, o Mês Referência.");
		}
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	public void excluir() {

		try {

			remuneracaoTrabalhadorESocialTCEService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new RemuneracaoTrabalhador() );
		consultar();
	}

	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.nome != null && !this.nome.equalsIgnoreCase("")) {
				String filtro = "where upper( descricao ) like '%" + this.nome.toUpperCase() + "%' ";
				parametros.put("FILTRO", filtro);
			}

			relatorioUtil.relatorio("RemuneracaoTrabalhador.jasper", parametros, "RemuneracaoTrabalhador.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public String getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public RemuneracaoTrabalhador getEntidade() {return entidade;}
	public void setEntidade(RemuneracaoTrabalhador entidade) {this.entidade = entidade;}

	public List<RemuneracaoTrabalhador> getLista(){return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RemuneracaoTrabalhador>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(remuneracaoTrabalhadorESocialTCEService.search(this.nome, this.cpf, anoReferencia, mesReferencia, isRGPA, isEstagiario, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<RemuneracaoTrabalhador> getPagedList() {return pagedList;}
	public void setPagedList(List<RemuneracaoTrabalhador> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}