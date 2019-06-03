package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.IndicativoAutoria;
import br.gov.ce.tce.srh.enums.IndicativoMateria;
import br.gov.ce.tce.srh.enums.TipoProcesso;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_PROCESSO", schema = "SRH")
public class ProcessoESocial extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "TIPOPROCESSO")
	private Integer tipoProcesso;
	
	@Column(name = "NUMEROPROCESSO")
	private String numero;

	@Column(name = "AUTORIA")
	private Integer indicativoAutoria;
	
	@Column(name = "MATERIA")
	private Integer indicativoMateria;
		
	@Column(name = "OBSERVACAO")
	private String observacao;	
	
	@ManyToOne
	@JoinColumn(name="MUNICIPIOVARA")	
	private Municipio municipioVara;
	
	@Column(name = "IDVARA")
	private Integer idVara;

	@Column(name = "INICIOVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date inicioValidade;

	@Column(name = "FIMVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date fimValidade;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Municipio getMunicipioVara() {
		return municipioVara;
	}

	public void setMunicipioVara(Municipio municipioVara) {
		this.municipioVara = municipioVara;
	}

	public Integer getIdVara() {
		return idVara;
	}

	public void setIdVara(Integer idVara) {
		if (idVara > 0)
			this.idVara = idVara;
		else
			this.idVara = null;
	}

	public TipoProcesso getTipoProcesso() {
		return TipoProcesso.toEnum(this.tipoProcesso);
	}

	public void setTipoProcesso(TipoProcesso tipo) {
		if(tipo != null)
			this.tipoProcesso = tipo.getCodigo();
	}
	
	public IndicativoAutoria getIndicativoAutoria() {
		return IndicativoAutoria.toEnum(this.indicativoAutoria);
	}

	public void setIndicativoAutoria(IndicativoAutoria indicativo) {
		if(indicativo != null)
			this.indicativoAutoria = indicativo.getCodigo();
	}
	
	public IndicativoMateria getIndicativoMateria() {
		return IndicativoMateria.toEnum(this.indicativoMateria);
	}

	public void setIndicativoMateria(IndicativoMateria indicativo) {
		if(indicativo != null)
			this.indicativoMateria = indicativo.getCodigo();
	}

	public Date getInicioValidade() {
		return inicioValidade;
	}

	public void setInicioValidade(Date inicioValidade) {
		this.inicioValidade = inicioValidade;
	}

	public Date getFimValidade() {
		return fimValidade;
	}

	public void setFimValidade(Date fimValidade) {
		this.fimValidade = fimValidade;
	}	

}