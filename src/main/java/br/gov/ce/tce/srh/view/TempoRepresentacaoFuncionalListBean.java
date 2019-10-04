package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC040_Emitir Tempo Representação Funcional em Cargo Comissionado
* 
* @since   : Dez 19, 2012, 10:09:00 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("tempoRepresentacaoFuncionalListBean")
@Scope("session")
public class TempoRepresentacaoFuncionalListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(TempoRepresentacaoFuncionalListBean.class);

	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;

	@Autowired
	private OcupacaoService ocupacaoService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AuthenticationService authenticationService;


	// controle de acesso do formulario
    private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();
	private Ocupacao cargo;
	private List<Ocupacao> cargos;

	// entidades das telas
	private List<RepresentacaoFuncional> lista;
	private RepresentacaoFuncional entidade;


	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			if ( this.matricula.equals("") || 
				 this.cpf.equals("") ||
				 this.nome.equals("")) {
				throw new SRHRuntimeException("Selecione um funcionário.");
			}			
			
			if ( this.entidade == null ) {
				throw new SRHRuntimeException("Servidor não possui Representação.");
			}	
			
			
			lista = new ArrayList<RepresentacaoFuncional>();
			if(cargo !=null){
				List<Funcional> funcionais = funcionalService.findByPessoal(this.entidade.getFuncional().getPessoal().getId(), "desc");
				for (Funcional funcional : funcionais) {
					if(funcional.getOcupacao().equals(cargo)){
						this.entidade.setFuncional(funcional);
					}
				}
				lista = representacaoFuncionalService.findByFuncional(this.entidade.getFuncional().getId());
			}else{
				lista = representacaoFuncionalService.findByPessoal(this.entidade.getFuncional().getPessoal().getId());
			}
			
			
			lista = validaDataFimTempoRepresentacao(lista);
			passouConsultar = true;

		}  catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return "listar";
	}
	
	public List<RepresentacaoFuncional> validaDataFimTempoRepresentacao(List<RepresentacaoFuncional> listaRepresentacao) {
		
		for (RepresentacaoFuncional entidade : listaRepresentacao) {
			
			if (entidade.getFim() == null) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				entidade.setFim(calendar.getTime());
			}
			
		}		
		return listaRepresentacao;
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String visualizarFicha() {
		
		try {

			if ( this.entidade == null )
				throw new SRHRuntimeException("Selecione um Funcionário.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			String filtro = " WHERE repr.IDFUNCIONAL IN '" + getEntidade().getFuncional().getId() + "' ";
			if (cargo == null) {
				filtro = " WHERE p.ID IN '" + getEntidade().getFuncional().getPessoal().getId() + "' ";
			}
			parametros.put("FILTRO", filtro);

			relatorioUtil.relatorio("tempoRepresentacao.jasper", parametros, "tempoRepresentacao.pdf");

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
		setEntidade(new RepresentacaoFuncional());
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
				
				List<RepresentacaoFuncional> representacaoFuncionais =  representacaoFuncionalService.getByMatricula(matricula);
				
				if(representacaoFuncionais.size() > 0){
					setEntidade(representacaoFuncionais.get(representacaoFuncionais.size()-1));
				} else {
					FacesUtil.addErroMessage("Não foi encontrada Representação.");
				}
				
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
					this.cpf = getEntidade().getFuncional().getPessoal().getCpf();
					this.cargos = ocupacaoService.findByPessoa(getEntidade().getFuncional().getPessoal().getId());
				} 

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {
				List<RepresentacaoFuncional> representacaoFuncionais =  representacaoFuncionalService.getByCpf(cpf);
				
				if(representacaoFuncionais.size() > 0){
					setEntidade(representacaoFuncionais.get(representacaoFuncionais.size()-1));
				} else {
					FacesUtil.addErroMessage("Não foi encontrada Representação.");
				}
				
				if ( getEntidade() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
					this.matricula = getEntidade().getFuncional().getMatricula();	
					this.cargos = ocupacaoService.findByPessoa(getEntidade().getFuncional().getPessoal().getId());
				} 

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public List<RepresentacaoFuncional> getLista() {return lista;}
	public void setLista(List<RepresentacaoFuncional> lista) {this.lista = lista;}

	public RepresentacaoFuncional getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoFuncional entidade) {this.entidade = entidade;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
			setCpf(authenticationService.getUsuarioLogado().getCpf());
			consultar();
		} else if (!passouConsultar) {
			setEntidade( null );
			this.matricula = new String();
			this.cpf = new String();
			this.nome = new String();
			this.cargo = null;
			this.cargos = null;
			this.lista = new ArrayList<RepresentacaoFuncional>();
		}
		passouConsultar = false;
		return form;
	}

	public boolean isPassouConsultar() {
		return passouConsultar;
	}

	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}

	public Ocupacao getCargo() {
		return cargo;
	}

	public void setCargo(Ocupacao cargo) {
		this.cargo = cargo;
	}

	public List<Ocupacao> getCargos() {
		return cargos;
	}

	public void setCargos(List<Ocupacao> cargos) {
		this.cargos = cargos;
	}

}