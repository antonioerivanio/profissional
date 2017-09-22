package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalAnotacao;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.service.FuncionalAnotacaoService;
import br.gov.ce.tce.srh.service.FuncionalSetorService;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.service.ReferenciaFuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : 
* 
* @since   : Fev 09, 2012, 11:16:00
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("emitirDossieServidorFormBean")
@Scope("session")
public class EmitirDossieServidorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoFormBean.class);

	@Autowired
	private FuncionalSetorService funcionalSetorService;

	@Autowired
	private ReferenciaFuncionalService referenciaFuncionalService;

	@Autowired
	private FeriasService feriasService;
	
	@Autowired
	private LicencaService licencaService;
	
	@Autowired
	private FuncionalAnotacaoService funcionalAnotacaoService;
	
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	

	// entidades das telas
	private Funcional entidade  = new Funcional();
	
	//Listas
	private List<ReferenciaFuncional> listaReferenciaFuncional;
	private List<FuncionalSetor> listaFuncionalSetor;
	private List<Ferias> listaFerias;
	private List<Licenca> listaLicenca;
	private List<FuncionalAnotacao>listaFuncionalAnotacao;
	private List<RepresentacaoFuncional> listaRepresentacaoFuncional;


	/**
	 * Realizar antes de carregar tela de visualizacao
	 * 
	 * @return
	 */
	public String visualizar() {

		try {
		
			this.listaReferenciaFuncional = referenciaFuncionalService.findByPessoa(getEntidade().getPessoal().getId());
			this.listaFuncionalSetor = funcionalSetorService.findByPessoal( entidade.getPessoal().getId() );
			this.listaFerias = feriasService.findByPessoal( entidade.getPessoal().getId() );
			this.listaLicenca = licencaService.findByPessoa( entidade.getPessoal().getId() );
			this.listaFuncionalAnotacao = funcionalAnotacaoService.findByPessoal( entidade.getPessoal().getId() );
			this.listaRepresentacaoFuncional = representacaoFuncionalService.findByPessoal(entidade.getPessoal().getId());

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} 
			
		return "incluirAlterar";
	}


	/**
	 * Gets and Sets
	 */
	public Funcional getEntidade() {return entidade;}
    public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public List<ReferenciaFuncional> getListaReferenciaFuncional() {return listaReferenciaFuncional;}
	public List<FuncionalSetor> getListaFuncionalSetor() {return listaFuncionalSetor;}
	public List<Ferias> getListaFerias() {return listaFerias;}
	public List<Licenca> getListaLicenca() {return listaLicenca;}
	public List<FuncionalAnotacao> getListaFuncionalAnotacao() {return listaFuncionalAnotacao;}
	public List<RepresentacaoFuncional> getListaRepresentacaoFuncional() {return listaRepresentacaoFuncional;}
	
}