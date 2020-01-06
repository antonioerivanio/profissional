package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Estabelecimento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EstabelecimentoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("estabelecimentoListBean")
@Scope("view")
public class EstabelecimentoListBean implements Serializable{

	static Logger logger = Logger.getLogger(EstabelecimentoListBean.class);

	@Autowired
	private EstabelecimentoService service;

	private Estabelecimento entidade = new Estabelecimento();

	private List<Estabelecimento> estabelecimentoList = new ArrayList<Estabelecimento>();	

	@PostConstruct
	public void consultar() {
		
		FacesUtil.setFlashParameter("entidade", null);

		try {
			
			estabelecimentoList = service.findAll();

			if (estabelecimentoList.size() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
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

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		this.consultar();
		
	}

	private void limparListas() {
		estabelecimentoList = new ArrayList<Estabelecimento>();
	}	

	public Estabelecimento getEntidade() {
		return entidade;
	}

	public void setEntidade(Estabelecimento entidade) {
		this.entidade = entidade;
	}

	public List<Estabelecimento> getEstabelecimentoList() {
		return estabelecimentoList;
	}
	
}
