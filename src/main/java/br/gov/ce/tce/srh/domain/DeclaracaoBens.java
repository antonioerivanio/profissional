package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@SuppressWarnings("serial")
@Table(name="TB_DECLARACAOBENS", schema=DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name="SQ_DECLARACAOBENS", sequenceName="SQ_DECLARACAOBENS", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
public class DeclaracaoBens extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="SQ_DECLARACAOBENS")
	private Long id;
	
	@Column(name="CAMINHO")
	private String caminho;
	
	@Column(name="NOMEARQUIVO")
	private String nomeArquivo;
	
	@Column(name="EXERCICIO")
	private Integer exercicio;
	
	@Column(name="ANOCALENDARIO")
	private Integer anoCalendario;
	
	@ManyToOne
	@JoinColumn(name="IDPESSOAL")
	private Pessoal pessoal;
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Integer getExercicio() {
		return exercicio;
	}

	public void setExercicio(Integer exercicio) {
		this.exercicio = exercicio;
	}

	public Integer getAnoCalendario() {
		return anoCalendario;
	}

	public void setAnoCalendario(Integer anoCalendario) {
		this.anoCalendario = anoCalendario;
	}

	public Pessoal getPessoal() {
		return pessoal;
	}

	public void setPessoal(Pessoal pessoal) {
		this.pessoal = pessoal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((caminho == null) ? 0 : caminho.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeclaracaoBens other = (DeclaracaoBens) obj;
		if (caminho == null) {
			if (other.caminho != null)
				return false;
		} else if (!caminho.equals(other.caminho))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}		

}