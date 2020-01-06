package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.TipoNotificacao;

/**
 * Referente a tabela: ESOCIAL_NOTIFICAO
 * 
 * @since : Tue 05, 2019, 14:40:56 PM
 * @author : esmayk.alves@tce.ce.gov.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_NOTIFICACAO", schema=DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name="SEQ_ESOCIAL_NOTIFICACAO", sequenceName="SEQ_ESOCIAL_NOTIFICACAO", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
public class Notificacao extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="SEQ_ESOCIAL_NOTIFICACAO")
	private Long id;

	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "DATA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(name = "TIPO")
	@Enumerated(EnumType.STRING)
	private TipoNotificacao tipo;
	
	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;
	
	@Column(name = "REFERENCIA")
	private String referencia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoNotificacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoNotificacao tipo) {
		this.tipo = tipo;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}
