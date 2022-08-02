package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_DMDEV", schema=DatabaseMetadata.SCHEMA_SRH)
public class DemonstrativosDeValores  extends BasicEntity<Long> implements Serializable, Cloneable {
	
	@Id
	@Column(name = "ID")	
	private Long id;
	
	@Column(name = "IDE_DM_DEV")
	private String ideDmDev;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDREMUNERACAOTRABALHADOR")
	private RemuneracaoTrabalhador remuneracaoTrabalhador;
	
	@Column(name = "COD_CATEG")
	private Integer codCateg;
	
	@Column(name = "COD_CBO")
	private String codCBO;
	
	@Column(name = "NAT_ATIVIDADE")
	private Byte natAtividade;
	
	@Column(name = "QTD_DIAS_TRAB")
	private Byte qtdDiasTrab;
	
	@Column(name="FLINFOREMUNPERANTERIORES")
	private Integer flInfoRemunPerAnteriores;
	
	@Transient
	private InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnteriores;
	
	@Transient
	private InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao;
	
	@Transient
	private List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList;
	
	public String getIdeDmDev() {
		return ideDmDev;
	}

	public void setIdeDmDev(String ideDmDev) {
		this.ideDmDev = ideDmDev;
	}

	public Integer getCodCateg() {
		return codCateg;
	}

	public void setCodCateg(Integer codCateg) {
		this.codCateg = codCateg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemuneracaoTrabalhador getRemuneracaoTrabalhador() {
		return remuneracaoTrabalhador;
	}

	public void setRemuneracaoTrabalhador(RemuneracaoTrabalhador remuneracaoTrabalhador) {
		this.remuneracaoTrabalhador = remuneracaoTrabalhador;
	}

	public String getCodCBO() {
		return codCBO;
	}

	public void setCodCBO(String codCBO) {
		this.codCBO = codCBO;
	}

	public Byte getNatAtividade() {
		return natAtividade;
	}

	public void setNatAtividade(Byte natAtividade) {
		this.natAtividade = natAtividade;
	}

	public Byte getQtdDiasTrab() {
		return qtdDiasTrab;
	}

	public void setQtdDiasTrab(Byte qtdDiasTrab) {
		this.qtdDiasTrab = qtdDiasTrab;
	}

	public Integer getFlInfoRemunPerAnteriores() {
		return flInfoRemunPerAnteriores;
	}

	public void setFlInfoRemunPerAnteriores(Integer flInfoRemunPerAnteriores) {
		this.flInfoRemunPerAnteriores = flInfoRemunPerAnteriores;
	}

	public InfoRemuneracaoPeriodoAnteriores getInfoRemuneracaoPeriodoAnteriores() {
		return infoRemuneracaoPeriodoAnteriores;
	}

	public void setInfoRemuneracaoPeriodoAnteriores(InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnteriores) {
		this.infoRemuneracaoPeriodoAnteriores = infoRemuneracaoPeriodoAnteriores;
	}

	public List<ItensRemuneracaoTrabalhador> getItensRemuneracaoTrabalhadorList() {
		return itensRemuneracaoTrabalhadorList;
	}

	public void setItensRemuneracaoTrabalhadorList(List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList) {
		this.itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorList;
	}	

	public InfoRemuneracaoPeriodoApuracao getInfoRemuneracaoPeriodoApuracao() {
		return infoRemuneracaoPeriodoApuracao;
	}

	public void setInfoRemuneracaoPeriodoApuracao(InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao) {
		this.infoRemuneracaoPeriodoApuracao = infoRemuneracaoPeriodoApuracao;
	}

	@Override
    public DemonstrativosDeValores clone() throws CloneNotSupportedException {
        return (DemonstrativosDeValores) super.clone();
    }

}
