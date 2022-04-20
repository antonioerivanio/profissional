package br.gov.ce.tce.srh.view;

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

import br.gov.ce.tce.srh.domain.EstagiarioESocial;
import br.gov.ce.tce.srh.service.EstagiarioESocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("estagiarioListBean")
@Scope("view")
public class EstagiarioListBean {
	
	static Logger logger = Logger.getLogger(EstagiarioListBean.class);

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private EstagiarioESocialService estagiarioESocialService;
	
	// parametro da tela de consulta
		private String nome = new String();
		private String cpf = new String();

		
		// entidades das telas
		private List<EstagiarioESocial> lista;
		private EstagiarioESocial entidade = new EstagiarioESocial();
		
		//paginação
		private int count;
		private UIDataTable dataTable = new UIDataTable();
		private PagedListDataModel dataModel = new PagedListDataModel();
		private List<EstagiarioESocial> pagedList = new ArrayList<>();
		private int flagRegistroInicial = 0;
		
		@PostConstruct
		private void init() {
			EstagiarioESocial flashParameter = (EstagiarioESocial)FacesUtil.getFlashParameter("entidade");
			setEntidade(flashParameter != null ? flashParameter : new EstagiarioESocial());
	    }
		
		public void consultar() {

			try {

				count = estagiarioESocialService.count( this.nome, this.cpf );

				if (count == 0) {
					FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
					logger.info("Nenhum registro foi encontrado.");
				}

				flagRegistroInicial = -1;

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

				estagiarioESocialService.excluir(entidade);

				FacesUtil.addInfoMessage("Registro excluído com sucesso.");
				logger.info("Registro excluído com sucesso.");

			} catch (DataAccessException e) {
				FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
				logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

			setEntidade( new EstagiarioESocial() );
			consultar();
		}

		public void relatorio() {

			try {

				Map<String, Object> parametros = new HashMap<String, Object>();

				if (this.nome != null && !this.nome.equalsIgnoreCase("")) {
					String filtro = "where upper( descricao ) like '%" + this.nome.toUpperCase() + "%' ";
					parametros.put("FILTRO", filtro);
				}

				relatorioUtil.relatorio("Estagiario.jasper", parametros, "Estagiario.pdf");

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

		public EstagiarioESocial getEntidade() {return entidade;}
		public void setEntidade(EstagiarioESocial entidade) {this.entidade = entidade;}

		public List<EstagiarioESocial> getLista(){return lista;}
		
		//PAGINAÇÃO
		private void limparListas() {
			dataTable = new UIDataTable();
			dataModel = new PagedListDataModel();
			pagedList = new ArrayList<EstagiarioESocial>(); 
		}

		public UIDataTable getDataTable() {return dataTable;}
		public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

		public PagedListDataModel getDataModel() {
			if( flagRegistroInicial != getDataTable().getFirst() ) {
				flagRegistroInicial = getDataTable().getFirst();
				setPagedList(estagiarioESocialService.search(this.nome, this.cpf, getDataTable().getFirst(), getDataTable().getRows()));
				if(count != 0){
					dataModel = new PagedListDataModel(getPagedList(), count);
				} else {
					limparListas();
				}
			}
			return dataModel;
		}

		public List<EstagiarioESocial> getPagedList() {return pagedList;}
		public void setPagedList(List<EstagiarioESocial> pagedList) {this.pagedList = pagedList;}
}
