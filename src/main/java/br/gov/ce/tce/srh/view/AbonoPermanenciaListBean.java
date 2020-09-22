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

import br.gov.ce.tce.srh.domain.AbonoPermanencia;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.StatusFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.AbonoPermanenciaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;
import br.gov.ce.tce.srh.util.SRHUtils;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("serial")
@Component("abonoPermanenciaListBean")
@Scope("view")
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
	
	private String cpf = new String();
	private String nome = new String();
	private Pessoal pessoal;
	private Boolean servidorAtivo;
	
	// entidades das telas
	private AbonoPermanencia entidade = new AbonoPermanencia();
	
	//paginação
	private int count;
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AbonoPermanencia> pagedList = new ArrayList<AbonoPermanencia>();
	private int flagRegistroInicial = 0;
	private Integer pagina = 1;
	
	@PostConstruct
	public void init() {
		servidorAtivo = false;
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());
			consultar();
		}
	}	
	
	public void consultar() {

		try {
			
			limparListas();

			if (count() == 0) {
				FacesUtil.addInfoMessage("O servidor selecionado não possui abono de permanência cadastrado.");
				logger.info("O servidor selecionado não possui abono de permanência cadastrado.");
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
	
	public void relatorio() {

		try {
			
			List<AbonoPermanencia> listaAbono = getAbonoList();	
			
			if (listaAbono.size() == 0){
				FacesUtil.addInfoMessage("O servidor selecionado não possui abono de permanência cadastrado.");
				logger.info("O servidor selecionado não possui abono de permanência cadastrado.");
				return;
			}

			Map<String, Object> parametros = new HashMap<String, Object>();			
			
			parametros.put("listaAbono", new JRBeanCollectionDataSource(listaAbono));
						
			relatorioUtil.relatorio("abonoPermanencia.jasper", parametros, "abonoPermanencia.pdf", null);

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}		
	
	public void excluir() {

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
		
		consultar();
	}
	
		
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		this.cpf = SRHUtils.removerMascara(cpf);
		if ( !this.cpf.isEmpty() ) {
			try {
				
				List<Pessoal> list = pessoalService.findServidorEfetivoByNomeOuCpf(null, this.cpf, false);
				
				if (list.isEmpty()) {
					this.cpf = new String();
					FacesUtil.addInfoMessage("CPF não encontrado.");
				} else {
					this.pessoal = list.get(0);
					this.nome = this.pessoal.getNomeCompleto();
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
			
			if(servidorAtivo) {
				List<AbonoPermanencia> abonoPermanenciaList = new ArrayList<>();
				
				for (AbonoPermanencia abonoPermanencia : abonos) {
					if(!StatusFuncional.INATIVO.getId().equals(abonoPermanencia.getFuncional().getStatus())) {
						abonoPermanenciaList.add(abonoPermanencia);
					}
				}	
				abonos = abonoPermanenciaList;
			}
			
		} else {
			AbonoPermanencia abono = abonoPermanenciaService.getByPessoalId(this.pessoal.getId());
			if (abono != null)
				abonos.add(abono);
		}
		
		return abonos;
	}

	public Boolean getServidorAtivo() {
		return servidorAtivo;
	}

	public void setServidorAtivo(Boolean servidorAtivo) {
		this.servidorAtivo = servidorAtivo;
	}
	
	

}