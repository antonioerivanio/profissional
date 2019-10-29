package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();
	
	
	@Transient
	private List<ProcessoESocialSuspensao> suspensoes = new ArrayList<>();

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

	public List<ProcessoESocialSuspensao> getSuspensoes() {
		return suspensoes;
	}

	public void setSuspensoes(List<ProcessoESocialSuspensao> suspensoes) {
		this.suspensoes = suspensoes;
	}
	
	public List<ProcessoESocialSuspensao> getSuspensoesAtualizadas() {
		List<ProcessoESocialSuspensao> listaAtualizada = new ArrayList<>();
		if (this.suspensoes != null) {
			for(ProcessoESocialSuspensao suspensao: this.suspensoes) {
				if(!suspensao.isExcluida())
					listaAtualizada.add(suspensao);
			}	
		}					
		return listaAtualizada;
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}

}