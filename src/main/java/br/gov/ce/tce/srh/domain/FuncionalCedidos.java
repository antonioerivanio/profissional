package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import br.gov.ce.tce.srh.enums.StatusFuncional;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_FUNCIONAL
 * 
 * @since   : Out 3, 2011, 14:09:10 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Audited
@Entity
@SuppressWarnings("serial")
@Table(name="TB_FUNCIONALCEDIDOS", schema=DatabaseMetadata.SCHEMA_SRH)
public class FuncionalCedidos extends BasicEntity<Long> implements Serializable {
	
	@Column(name = "IDFUNCIONAL")
	private Funcional funcional;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="CODIGOCATEGORIA")
	private CodigoCategoria codigoCategoria;
	
	@Column(name="IDPESSOAJURIDICA")
	private PessoaJuridica pessoaJuridica;
	
	@Column(name="MATRICULAORIGEM")
	private String matricOrig;
	
	@Column(name="TIPOREGTRAB")
	private Integer tpRegTrab;
	
	@Column(name="TIPOREGPREV")
	private Integer tpRegPrev;


	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		// TODO Auto-generated method stub
		
	}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public CodigoCategoria getCodigoCategoria() {
		return codigoCategoria;
	}

	public void setCodigoCategoria(CodigoCategoria codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public String getMatricOrig() {
		return matricOrig;
	}

	public void setMatricOrig(String matricOrig) {
		this.matricOrig = matricOrig;
	}

	public Integer getTpRegTrab() {
		return tpRegTrab;
	}

	public void setTpRegTrab(Integer tpRegTrab) {
		this.tpRegTrab = tpRegTrab;
	}

	public Integer getTpRegPrev() {
		return tpRegPrev;
	}

	public void setTpRegPrev(Integer tpRegPrev) {
		this.tpRegPrev = tpRegPrev;
	}
	
}
