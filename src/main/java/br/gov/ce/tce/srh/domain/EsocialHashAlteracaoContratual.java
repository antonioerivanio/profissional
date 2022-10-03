package br.gov.ce.tce.srh.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/***
 * Entidade que será usada para armezado o hash dos do eventos 2200 e 2300, depois será comparado com o evento 2205
 * @author erivanio.cruz
 *
 */

@Entity
@Table(name = "ESOCIAL_HASH_ALTERACAOCONTRAT", schema=DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_HASH_ALTERACAOCONTRAT", sequenceName = "SEQ_HASH_ALTERACAOCONTRAT", allocationSize = 1)
public class EsocialHashAlteracaoContratual {
	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	
	@Column(name = "CODIGOHASH")
	private String codigoHash;
	
	@Column(name = "ORIGEM")
	private String origim;
	

	public EsocialHashAlteracaoContratual() {
		super();
	}

	public EsocialHashAlteracaoContratual(Funcional funcional, String codigoHash, String origim) {
		super();		
		this.funcional = funcional;
		this.codigoHash = codigoHash;
		this.origim = origim;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public String getCodigoHash() {
		return codigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	public String getOrigim() {
		return origim;
	}

	public void setOrigim(String origim) {
		this.origim = origim;
	}

	
	
	
}
