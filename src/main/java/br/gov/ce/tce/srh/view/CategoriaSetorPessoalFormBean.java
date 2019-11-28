package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.CategoriaSetorPessoal;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.service.CategoriaSetorPessoalService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("categoriaSetorPessoalFormBean")
@Scope("view")
public class CategoriaSetorPessoalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(CategoriaSetorPessoalFormBean.class);

	@Autowired
	private CategoriaSetorPessoalService categoriaSetorPessoalService;	
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;
	@Autowired
	private SetorService setorService;

	private CategoriaSetorPessoal entidade = new CategoriaSetorPessoal();
	private Funcional funcional;

	private boolean passouConsultar = false;
	private boolean alterar = false;

	private List<Setor> comboSetor;
	private boolean setoresAtivos = true;
	private Setor setor;
	private List<CategoriaFuncionalSetor> categorias = null;

	private String matricula = new String();
	private String nome = new String();
	private boolean ativa;

	@PostConstruct
	public void init() {
		CategoriaSetorPessoal flashParameter = (CategoriaSetorPessoal)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new CategoriaSetorPessoal());
		
		if(this.entidade.getId() != null) {
			this.alterar = true;
			this.passouConsultar = true;
			
			entidade.setCategoriaFuncionalSetor(categoriaFuncionalSetorService.findById(entidade.getCategoriaFuncionalSetor().getId()));
	
			this.funcional = funcionalService.getByPessoaAtivo(getEntidade().getPessoal().getId());
	
			this.matricula = this.funcional.getMatricula();
			this.nome = getEntidade().getPessoal().getNomeCompleto();			
			this.setor = entidade.getCategoriaFuncionalSetor().getSetor();
		}
	}

	private void limpar() {

		setEntidade(new CategoriaSetorPessoal());
		getEntidade().setCategoriaFuncionalSetor(new CategoriaFuncionalSetor());
		getEntidade().setPessoal(new Pessoal());
		getEntidade().getCategoriaFuncionalSetor().setSetor(new Setor());
		getEntidade().getCategoriaFuncionalSetor().setCategoriaFuncional(new CategoriaFuncional());

		this.alterar = false;
		this.passouConsultar = false;
		this.ativa = true;

		this.matricula = new String();
		this.nome = new String();

		this.comboSetor = null;
		this.setor = null;
		this.categorias = new ArrayList<CategoriaFuncionalSetor>();
		this.setoresAtivos = true;
		this.funcional = null;

	}

	public void salvar() {
		try {
			
//			entidade.setCategoriaFuncionalSetor(categoriaFuncionalSetorService.findById(entidade.getCategoriaFuncionalSetor().getId()));

			if (ativa) {
				entidade.setAtiva(1L);
			} else {
				entidade.setAtiva(0L);
			}
			categoriaSetorPessoalService.salvar(entidade);
			
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

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

	public void carregarCategorias() {
		if (categorias != null) {

			if (entidade.getCategoriaFuncionalSetor().getId() != null && entidade.getCategoriaFuncionalSetor().getId() != 0) {
				CategoriaFuncionalSetor categoriaFuncionalSetor = categoriaFuncionalSetorService.findById(entidade.getCategoriaFuncionalSetor().getId());
				getEntidade().setCategoriaFuncionalSetor(categoriaFuncionalSetor);

				categorias = categoriaFuncionalSetorService.findBySetor(entidade.getCategoriaFuncionalSetor().getSetor());

			}

		}
	}

	public CategoriaSetorPessoal getEntidade() {return entidade;}
	public void setEntidade(CategoriaSetorPessoal entidade) {this.entidade = entidade;}

	public boolean isPassouConsultar() {return passouConsultar;}
	public void setPassouConsultar(boolean passouConsultar) {this.passouConsultar = passouConsultar;}

	public boolean isAlterar() {return alterar;}
	public void setAlterar(boolean alterar) {this.alterar = alterar;}

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if (!this.matricula.equalsIgnoreCase(matricula)) {
			this.matricula = matricula;

			try {

				this.funcional = funcionalService
						.getCpfAndNomeByMatriculaAtiva(this.matricula);
				if (this.funcional != null) {
					this.nome = this.funcional.getNomeCompleto();
					getEntidade().setPessoal(this.funcional.getPessoal());
				} else {
					FacesUtil
							.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil
						.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}
	
	public boolean isSetoresAtivos() {return this.setoresAtivos;}
	public void setSetoresAtivos(boolean setoresAtivos) {this.setoresAtivos = setoresAtivos;}
	
	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public List<CategoriaFuncionalSetor> getCategorias() {
		if(setor != null){
			categorias = categoriaFuncionalSetorService.findBySetor(setor);
		}		
		return categorias;
	}
	public void setCategorias(List<CategoriaFuncionalSetor> categorias) {this.categorias = categorias;}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public boolean isAtiva() {return ativa;}
	public void setAtiva(boolean ativa) {this.ativa = ativa;}

}
