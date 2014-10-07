 package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC034_Manter Exoneração do Servidor
* 
* @since   : Dez 19, 2011, 17:09:00 
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("exoneracaoListBean")
@Scope("session")
public class ExoneracaoListBean implements Serializable {

	static Logger logger = Logger.getLogger(ExoneracaoListBean.class);

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String matricula = new String();
	private String cpf = new String();
	private String nome = new String();

	// entidades das telas
	private List<Funcional> listaEfetivo;
	private List<RepresentacaoFuncional> listaRepresentacao;

	private Funcional entidade;
	private Long tipo;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			passouConsultar = true;

			// validando campos da entidade
			if ( getEntidade() == null )
				throw new SRHRuntimeException("Selecione um funcionário.");
			if ( getTipo() == null || getTipo().equals(0l) )
				throw new SRHRuntimeException("Selecione um tipo de exoneração.");

			listaEfetivo = new ArrayList<Funcional>();
			listaRepresentacao = new ArrayList<RepresentacaoFuncional>();

			// consultando conforme o tipo cargo efetivo
			if ( this.tipo.equals(1l) ) {
				listaEfetivo = funcionalService.findByPessoal(entidade.getPessoal().getId(), "DESC");
				listaRepresentacao = null;
			}

			// consultando conforme o tipo representacao
			if ( this.tipo.equals(2l) ) {
				listaRepresentacao = representacaoFuncionalService.getByMatricula( entidade.getMatricula() );
				listaEfetivo = null;
			}

			if ( (listaEfetivo != null && listaEfetivo.size() == 0)
					|| (listaRepresentacao != null && listaRepresentacao.size() == 0) ) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}

	public String limpaTela() {
		setEntidade(new Funcional());
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

				setEntidade( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula )); 
				if ( getEntidade() != null) {
					this.nome = getEntidade().getNomeCompleto();
					this.cpf = getEntidade().getPessoal().getCpf();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}
				
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				setEntidade( funcionalService.getMatriculaAndNomeByCpfAtiva( this.cpf ));
				if ( getEntidade() != null) {
					this.nome = getEntidade().getNomeCompleto();
					this.matricula = getEntidade().getMatricula();
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}

	public List<Funcional> getListaEfetivo() {return listaEfetivo;}
	public void setListaEfetivo(List<Funcional> listaEfetivo) {this.listaEfetivo = listaEfetivo;}

	public List<RepresentacaoFuncional> getListaRepresentacao() {return listaRepresentacao;}
	public void setListaRepresentacao(List<RepresentacaoFuncional> listaRepresentacao) {this.listaRepresentacao = listaRepresentacao;}

	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( null );
			matricula = new String();
			cpf = new String();
			nome = new String();
			tipo = null;
			listaEfetivo = null;
			listaRepresentacao = null;
		}
		passouConsultar = false;
		return form;
	}

}