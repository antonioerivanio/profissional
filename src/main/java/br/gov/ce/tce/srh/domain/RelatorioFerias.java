package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;

@SqlResultSetMapping(name = "RelatorioFerias", entities = { @EntityResult(entityClass = br.gov.ce.tce.srh.domain.RelatorioFerias.class, fields = {
		@FieldResult(name = "id", column = "IDNUM"),
		@FieldResult(name = "nomeCompleto", column = "NOMECOMPLETO"),
		@FieldResult(name = "anoReferencia", column = "ANOREFERENCIA"),
		@FieldResult(name = "inicio", column = "INICIO"),
		@FieldResult(name = "fim", column = "FIM"),
		@FieldResult(name = "tipoFerias", column = "TIPOFERIAS"),
		@FieldResult(name = "qtdeDias", column = "QTDEDIAS"),
		@FieldResult(name = "periodo", column = "PERIODO"),}) })
@Entity
public class RelatorioFerias implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 177402744595189814L;

	@Id
	private long id;

	private String nomeCompleto;

	private Long anoReferencia;

	private Date inicio;

	private Date fim;

	private String tipoFerias;
	
	private Long qtdeDias;
	
	private Long periodo;
	
	@Transient
	private Integer dias;

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public Long getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(Long anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public String getTipoFerias() {
		return tipoFerias;
	}

	public void setTipoFerias(String tipoFerias) {
		this.tipoFerias = tipoFerias;
	}	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getDias() {
		return dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public Long getQtdeDias() {
		return qtdeDias;
	}

	public void setQtdeDias(Long qtdeDias) {
		this.qtdeDias = qtdeDias;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}
	

}
