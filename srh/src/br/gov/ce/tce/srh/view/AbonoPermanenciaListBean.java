package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AbonoPermanencia;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.AbonoPermanenciaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("serial")
@Component("abonoPermanenciaListBean")
@Scope("session")
public class AbonoPermanenciaListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(DependenteListBean.class);
	
	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private AbonoPermanenciaService abonoPermanenciaService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	
	private HtmlForm form;
	private boolean passouConsultar = false;
	
	private String cpf = new String();
	private String nome = new String();
	private Pessoal pessoal;
	
	// entidades das telas
	private AbonoPermanencia entidade = new AbonoPermanencia();
	
	//paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AbonoPermanencia> pagedList = new ArrayList<AbonoPermanencia>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;
	
	
	public String consultar() {

		try {
			
			limparListas();

			if (count() == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			flagRegistroInicial = -1;
			
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
	
	public String relatorio() {

		try {
			
			List<AbonoPermanencia> listaAbono = getAbonoList();	
			
			if (listaAbono.size() == 0){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
				return null;
			}

			Map<String, Object> parametros = new HashMap<String, Object>();			
			
			parametros.put("listaAbono", new JRBeanCollectionDataSource(listaAbono));
						
			relatorioUtil.relatorioComEmptyDataSource("abonoPermanencia.jasper", parametros, "abonoPermanencia.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
		
	public String limpaTela() {		
		setEntidade(new AbonoPermanencia());
		return "listar";
	}
	
	
	public String excluir() {

		try {

			abonoPermanenciaService.excluir(this.entidade);			
			
			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return consultar();
	}
	
		
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {
				this.pessoal = pessoalService.getByCpf(this.cpf);
				
				if (this.pessoal != null) {
					this.nome = pessoal.getNomeCompleto();					
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
	}
	

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public AbonoPermanencia getEntidade() {return entidade;}
	public void setEntidade(AbonoPermanencia entidade) {this.entidade = entidade;}
	
	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			
			try {
				setCpf(authenticationService.getUsuarioLogado().getCpf());
				count = count();	
				limparListas();
				flagRegistroInicial = -1;				
				
			} catch (Exception e) {
				limparListas();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}			
			
		}else if (!passouConsultar) {
			
			entidade = new AbonoPermanencia();
			
			cpf = new String();
			nome = new String();
			pessoal = null;
			
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	
//	PAGINAÇÃO
	
	private void limparListas() {
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AbonoPermanencia>();
		pagina = 1;
	}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getPrimeiroDaPagina() ) {
			flagRegistroInicial = getPrimeiroDaPagina();
			
			setPagedList(abonoPermanenciaService.search(cpf, flagRegistroInicial, dataModel.getPageSize()));
						
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AbonoPermanencia> getPagedList() {return pagedList;}
	public void setPagedList(List<AbonoPermanencia> pagedList) {this.pagedList = pagedList;}

	public Integer getPagina() {return pagina;}
	public void setPagina(Integer pagina) {this.pagina = pagina;}
	
	private int getPrimeiroDaPagina() {return dataModel.getPageSize() * (pagina - 1);}
		
//	FIM PAGINAÇÃO	
	
	private int count(){
		count = getAbonoList().size(); 
		return count;
	}
	
	private List<AbonoPermanencia> getAbonoList(){
		List<AbonoPermanencia> abonos = new ArrayList<>();
		if (this.pessoal == null) {
			abonos = abonoPermanenciaService.findAll();
		} else {
			AbonoPermanencia abono = abonoPermanenciaService.getByPessoalId(this.pessoal.getId());
			if (abono != null)
				abonos.add(abono);
		}
		
		return abonos;
	}

}