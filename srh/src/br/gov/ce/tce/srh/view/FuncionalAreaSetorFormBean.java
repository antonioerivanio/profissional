package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetorPk;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.service.FuncionalAreaSetorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.sapjava.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
 * Use case : SRH_UC025_Manter Área do Setor do Servidor
 * 
 * @since  : Dez 3, 2011, 11:14:10 AM
 * @author : wesllhey.holanda@ivia.com.br
 */
@SuppressWarnings("serial")
@Component("funcionalAreaSetorFormBean")
@Scope("session")
public class FuncionalAreaSetorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(FuncionalAreaSetorFormBean.class);

	@Autowired
	private AreaSetorService areaSetorService;

	@Autowired
	private FuncionalAreaSetorService funcionalAreaSetorService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private SetorService setorService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;
	
	// entidades das telas
	private FuncionalAreaSetor entidade = new FuncionalAreaSetor();
	
	private String matricula = new String();
	private String nome = new String();

	private Funcional funcional = new Funcional();
	private AreaSetor areaSetor = new AreaSetor();
	private Setor setor = new Setor();
	private List<AreaSetor> comboArea;

	private List<FuncionalAreaSetor> lista;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			if ( getFuncional() == null ){
				throw new SRHRuntimeException("Selecione um funcionário.");
			}
			this.setFuncional( funcionalService.getById( getFuncional().getId() ) );

			lista = funcionalAreaSetorService.findByFuncionalComSetorAtual( getFuncional().getId() );
			this.passouConsultar = true;
	
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Reiniciar formulario
	 * 
	 * @return
	 */
	public String limparForm() {
		limpar();
		return null;
	}


	/**
	 * Adicionar Area
	 * 
	 * @return
	 */
	public String adicionar() {

		try {

			if( getFuncional() == null )
			    throw new SRHRuntimeException("Funcionario é obrigatório.");

			if ( getAreaSetor() == null || getAreaSetor().getId().equals(0l) )
				throw new SRHRuntimeException("Área é obrigatória.");

			for ( FuncionalAreaSetor entidades : lista ) {
				if ( entidades.getAreaSetor().getId().equals( getAreaSetor().getId() ) ) {
					throw new SRHRuntimeException("Já existe esta área na lista.");
				}
			}

			// setando as PKs
			entidade = new FuncionalAreaSetor();
			entidade.setPk( new FuncionalAreaSetorPk() );

			//entidade.setAreaSetor( areaSetorService.getById( getAreaSetor().getId() ));
			entidade.getPk().setFuncional( getFuncional().getId() );
			entidade.getPk().setAreaSetor( getAreaSetor().getId() );

			// add
			funcionalAreaSetorService.salvar( getEntidade() );
			lista = funcionalAreaSetorService.findByFuncional( getFuncional().getId() );

			return null;

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao adicionar a área do servidor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Remover a Area
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			funcionalAreaSetorService.excluir( getEntidade() );
			lista = funcionalAreaSetorService.findByFuncional( getFuncional().getId() );

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao remover a área do servidor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	public void carregaArea() {
		this.setComboArea(getComboArea());
	}

	public List<Setor> getComboSetor() {
		return setorService.findAll();
	}

	/**
	 * Combo Area
	 * 
	 * @return
	 */
	public List<AreaSetor> getComboArea() {

		this.comboArea = new ArrayList<AreaSetor>();
		try {

			//if ( getFuncional() != null && getFuncional().getSetor() != null) {
			//	return areaSetorService.findBySetor( getFuncional().getSetor().getId() );
			
			if (this.setor != null) {
				for(AreaSetor areaSetor : areaSetorService.findBySetor(getSetor().getId())) {
					comboArea.add(areaSetor);
				}
			
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo área. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return comboArea;
	}


	/**
	 * Limpar dados
	 * 
	 * @return
	 */
	private void limpar() {
		
		this.passouConsultar = false;

		this.entidade = new FuncionalAreaSetor();

		this.matricula = new String();
		this.nome = new String();

		this.funcional = new Funcional();
		this.areaSetor = new AreaSetor();

		this.lista = new ArrayList<FuncionalAreaSetor>();
	}


	/**
	 * Gets and Sets
	 */
	public FuncionalAreaSetor getEntidade() {return entidade;}
	public void setEntidade(FuncionalAreaSetor entidade) {this.entidade = entidade;}

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equalsIgnoreCase(matricula) ) {
			this.matricula = matricula;

			try {

				setFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula )); 
				if ( getFuncional() != null) {
					this.nome = getFuncional().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public AreaSetor getAreaSetor() {return areaSetor;}
	public void setAreaSetor(AreaSetor areaSetor) {this.areaSetor = areaSetor;}

	public List<FuncionalAreaSetor> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new FuncionalAreaSetor());
			this.matricula = new String();
			this.nome = new String();
			this.lista = new ArrayList<FuncionalAreaSetor>();
		}
		limpar();
		passouConsultar = false;
		return form;
	}

	public boolean isPassouConsultar() {return passouConsultar;}
	public void setPassouConsultar(boolean passouConsultar) {this.passouConsultar = passouConsultar;}


	public Setor getSetor() {
		return setor;
	}


	public void setSetor(Setor setor) {
		this.setor = setor;
	}


	public void setComboArea(List<AreaSetor> comboArea) {
		this.comboArea = comboArea;
	}
	
	
}
