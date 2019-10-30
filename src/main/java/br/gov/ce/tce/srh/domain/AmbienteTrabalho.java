package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.enums.LocalAmbiente;

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_AMBIENTE_TRABALHO", schema = "SRH")
public class AmbienteTrabalho extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO")
	private String codigo;

	@Column(name = "NOMEAMBIENTE")
	private String nome;
	
	@Column(name = "DESCRICAOAMBIENTE")
	private String descricao;

	@Column(name = "LOCALAMBIENTE")
	private Integer localAmbiente;
	
	@ManyToOne
	@JoinColumn(name = "IDESTABELECIMENTO")	
	private Estabelecimento estabelecimento;

	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();
	

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	

	public LocalAmbiente getLocalAmbiente() {
		return LocalAmbiente.getByCodigo(this.localAmbiente);
	}

	public void setLocalAmbiente(LocalAmbiente local) {
		if(local != null)
			this.localAmbiente = local.getCodigo();
	}	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}
	

}