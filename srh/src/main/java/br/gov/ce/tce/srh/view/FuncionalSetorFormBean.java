package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.FuncionalSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC042_Manter Lotação do Servidor
* 
* @since   : Dez 19, 2011, 17:59:02 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("funcionalSetorFormBean")
@Scope("session")
public class FuncionalSetorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoFormBean.class);

	@Autowired
	private FuncionalSetorService funcionalSetorService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private SetorService setorService;


	// entidades das telas
	private FuncionalSetor entidade = new FuncionalSetor();	

	private String matricula = new String();
	private String nome = new String();

	private boolean alterar = false;

	// combos
	private List<Setor> comboSetor;
	private boolean setoresAtivos = true;



	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {
		this.alterar = true;

		try {

			this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();
			this.matricula = entidade.getFuncional().getMatricula();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			funcionalSetorService.salvar(entidade);
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());		
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Combo Setor
	 * 
	 * @return
	 */
	public List<Setor> getComboSetor() {
			
		try {

			if ( this.setoresAtivos )
				this.comboSetor = setorService.findTodosAtivos();
	       	else
	       		this.comboSetor = setorService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
	       	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	    return this.comboSetor;
	}


	/**
	 * Limpar form
	 */
	private void limpar() {

		this.alterar = false;

		this.matricula = new String();
		this.nome = new String();

		setEntidade( new FuncionalSetor() );
		this.comboSetor = null;
	}
	
	public String limpaTelaForm() {
		limpar();
		return "listar";
	}


	/**
	 * Gets and Sets
	 */
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}

	public FuncionalSetor getEntidade() {return entidade;}
    public void setEntidade(FuncionalSetor entidade) {this.entidade = entidade;}

	public boolean isAlterar() {return alterar;}
	
	public boolean isSetoresAtivos() {return this.setoresAtivos;}
	public void setSetoresAtivos(boolean setoresAtivos) {this.setoresAtivos = setoresAtivos;}

}