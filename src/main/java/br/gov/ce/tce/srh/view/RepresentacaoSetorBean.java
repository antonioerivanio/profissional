package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.dao.RepresentacaoFuncionalDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.RepresentacaoCargoService;
import br.gov.ce.tce.srh.service.RepresentacaoSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("representacaoSetorBean")
@Scope("view")
public class RepresentacaoSetorBean implements Serializable {

	static Logger logger = Logger.getLogger(RepresentacaoSetorBean.class);

	@Autowired
	private RepresentacaoSetorService representacaoSetorService;

	@Autowired
	private RepresentacaoCargoService representacaoCargoService;
	
	@Autowired
	private RepresentacaoFuncionalDAO representacaoFuncionalDAO;

	@Autowired
	private SetorService setorService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametro da tela de consulta
	private RepresentacaoCargo cargo = new RepresentacaoCargo();

	// entidades das telas
	private List<RepresentacaoSetor> lista = new ArrayList<RepresentacaoSetor>();
	private RepresentacaoSetor entidade = new RepresentacaoSetor();

	// combos
	private List<RepresentacaoCargo> comboCargo;
	private List<Setor> comboSetor;
	
	//paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RepresentacaoSetor> pagedList = new ArrayList<RepresentacaoSetor>();
	private int registroInicial = 0;
	private Integer pagina = 1;
	
	@PostConstruct
	public void init() {
		RepresentacaoSetor flashParameter = (RepresentacaoSetor)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RepresentacaoSetor());
		
		if(this.entidade.getId() == null)
			getEntidade().setAtivo(true);
		
		comboCargo = null;
		comboSetor = null;
		cargo = null;
		lista = new ArrayList<RepresentacaoSetor>();
		limparListas();
		registroInicial = 0;
	}
	
	public void consultar() {

		try {
			
			limparListas();

			if ( this.cargo == null )
				throw new SRHRuntimeException("Selecione um cargo.");

			count = representacaoSetorService.count(this.cargo.getId());

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
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void salvar() {

		try {

			representacaoSetorService.salvar(entidade);
			setEntidade( new RepresentacaoSetor() );
			getEntidade().setAtivo(true);

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

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

			representacaoSetorService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new RepresentacaoSetor() );
		getEntidade().setAtivo(true);

		consultar();
	}

	public List<RepresentacaoCargo> getComboCargo() {

        try {

        	if ( this.comboCargo == null )
        		this.comboCargo = representacaoCargoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo cargo. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCargo;
	}

	public List<Setor> getComboSetor() {

        try {

        	if ( this.comboSetor == null )
        		this.comboSetor = setorService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboSetor;
	}

	public void relatorio() {
		
		try {

			if ( this.cargo == null )
				throw new SRHRuntimeException("Selecione o cargo.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.cargo != null && this.cargo.getId() != null) {
				String filtro = " WHERE repr.IDREPRESENTACAOCARGO = " + this.cargo.getId();
				parametros.put("FILTRO", filtro);				
			}

			relatorioUtil.relatorio("cargodeRepresentacaoSetor.jasper", parametros, "cargodeRepresentacaoSetor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public Long ocupados(RepresentacaoSetor representacaoSetor) {
		
		List<RepresentacaoFuncional> representacoesAtivas = representacaoFuncionalDAO.findByCargoSetor(representacaoSetor.getRepresentacaoCargo().getId(), representacaoSetor.getSetor().getId());
		if (representacoesAtivas == null){
			return 0L;
		}else{
			return (long) representacoesAtivas.size();
		}
	}

	public RepresentacaoCargo getCargo() {return cargo;}
	public void setCargo(RepresentacaoCargo cargo) {this.cargo = cargo;}

	public RepresentacaoSetor getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoSetor entidade) {this.entidade = entidade;}

	public List<RepresentacaoSetor> getLista(){return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RepresentacaoSetor>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if( registroInicial != getPrimeiroDaPagina() ) {
			registroInicial = getPrimeiroDaPagina();
			setPagedList(representacaoSetorService.search(this.cargo.getId(), registroInicial, dataModel.getPageSize()));
			if(count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;		
	}

	public List<RepresentacaoSetor> getPagedList() {return pagedList;}
	public void setPagedList(List<RepresentacaoSetor> pagedList) {this.pagedList = pagedList;}
	
	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
	//FIM PAGINAÇÃO

}