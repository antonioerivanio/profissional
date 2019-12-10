package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.enums.SimNao;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_GRADE_HORARIO", schema=DatabaseMetadata.SCHEMA_SRH)
public class GradeHorario extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGO")
	private String codigo;
	
	@Column(name="HORAENTRADA")
	private String horaEntrada;
	
	@Column(name="HORASAIDA")
	private String horaSaida;
	
	@Column(name="DURACAOJORNADA")
	private Integer duracaoJornada;
	
	@Column(name = "FLEXIVEL")
	@Enumerated(EnumType.STRING)
	private SimNao flexivel;
	
	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}

	public Integer getDuracaoJornada() {
		return duracaoJornada;
	}

	public void setDuracaoJornada(Integer duracaoJornada) {
		this.duracaoJornada = duracaoJornada;
	}

	public SimNao getFlexivel() {
		return flexivel;
	}

	public void setFlexivel(SimNao flexivel) {
		this.flexivel = flexivel;
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}

	
	public String getReferenciaESocial() {
		return this.codigo;
	}
}
