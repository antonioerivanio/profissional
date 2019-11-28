package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetorPk;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.service.FuncionalAreaSetorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("funcionalAreaSetorFormBean")
@Scope("view")
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

	// entidades das telas
	private FuncionalAreaSetor entidade = new FuncionalAreaSetor();
	
	private String matricula = new String();
	private String nome = new String();

	private Funcional funcional = new Funcional();
	private AreaSetor areaSetor = new AreaSetor();
	private Setor setor = new Setor();
	private List<AreaSetor> comboArea;

	private List<FuncionalAreaSetor> lista;

	public void consultar() {

		try {

			// validando campos da entidade
			if ( getFuncional() == null ){
				throw new SRHRuntimeException("Selecione um funcionário.");
			}
			this.setFuncional( funcionalService.getById( getFuncional().getId() ) );

			lista = funcionalAreaSetorService.findByFuncionalComSetorAtual( getFuncional().getId() );			
	
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public void adicionar() {

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

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao adicionar a área do servidor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void excluir() {

		try {

			funcionalAreaSetorService.excluir( getEntidade() );
			lista = funcionalAreaSetorService.findByFuncional( getFuncional().getId() );

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao remover a área do servidor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void carregaArea() {
		this.setComboArea(getComboArea());
	}

	public List<Setor> getComboSetor() {
		return setorService.findAll();
	}

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
