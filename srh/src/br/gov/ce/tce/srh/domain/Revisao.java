package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.service.sca.RevisaoListener;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "TB_REVISAO", schema="SRH")
@RevisionEntity(RevisaoListener.class)
public class Revisao extends BasicEntity<Long> implements Serializable, Comparable<Revisao>{

	@Id
	@SequenceGenerator(name = "revisao_id", sequenceName = "SRH.revisao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revisao_id")
	@RevisionNumber
	@Column(name = "CD_REVISAO")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@RevisionTimestamp
	@Column(name = "DATA_AUDITORIA")
	private Date dataAuditoria;

	@NotAudited
	@ManyToOne
	@JoinColumn(name = "CD_USUARIO")
	private Usuario usuario;
	
	@Transient
	private TipoRevisao tipoRevisao;
	
	@Transient
	private Class<?> entidade;	
	
	@Transient
	private Restricao restricao;
	
	@Transient
	private Object valorColuna;

	@Transient
	private Date periodoInicial;
	
	@Transient
	private Date periodoFinal;
	
	@Transient
	private Long idRegistro;
	
	@Transient
	private Pessoal pessoal;
	
	@Transient
	private List<Funcional> funcionais;	

		
	@Override
	public Long getId() {return id;}
	@Override
	public void setId(Long id) {this.id = id;}

	public Date getDataAuditoria() {return dataAuditoria;}
	public void setDataAuditoria(Date dataAuditoria) {this.dataAuditoria = dataAuditoria;}

	public Usuario getUsuario() {return usuario;}
	public void setUsuario(Usuario usuario) {this.usuario = usuario;}

	public void setEntidade(Class<?> entidade) {this.entidade = entidade;}
	public Class<?> getEntidade() {return entidade;}

	public void setTipoRevisao(TipoRevisao tipoRevisao) {this.tipoRevisao = tipoRevisao;}
	public TipoRevisao getTipoRevisao() {return tipoRevisao;}

	public Restricao getRestricao() {return restricao;}
	public void setRestricao(Restricao restricao) {this.restricao = restricao;}	
	
	public Object getValorColuna() {return valorColuna;}
	public void setValorColuna(Object valorColuna) {this.valorColuna = valorColuna;}

	public Date getPeriodoInicial() {return periodoInicial;}
	public void setPeriodoInicial(Date periodoInicial) {this.periodoInicial = periodoInicial;}

	public Date getPeriodoFinal() {return periodoFinal;}
	public void setPeriodoFinal(Date periodoFinal) {this.periodoFinal = periodoFinal;}
	
	public Long getIdRegistro() {return idRegistro;}
	public void setIdRegistro(Long idRegistro) {this.idRegistro = idRegistro;}
	
	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}
	
	public List<Funcional> getFuncionais() {return funcionais;}
	public void setFuncionais(List<Funcional> funcionais) {this.funcionais = funcionais;}

	
	@Override
	public Revisao clone() {
		Revisao revisao = new Revisao();
		revisao.setDataAuditoria(getDataAuditoria());
		revisao.setId(getId());
		revisao.setUsuario(getUsuario());
		return revisao;
	}
	
	
	@Override
	public int compareTo(Revisao r) {
		return -this.getDataAuditoria().compareTo(r.getDataAuditoria());
	}

	
	
	public static class Restricao implements Serializable, Comparable<Restricao> {
		
		private static final long serialVersionUID = 1955725361200101617L;
		
		private String nome;
		private Class<?> tipo;
		private Object valor;
		
		public Restricao(String nome, Class<?> tipo, Object valor) {
			super();
			this.nome = nome;
			this.tipo = tipo;
			this.valor = valor;
		}

		public String getNome() {return nome;}
		public void setNome(String nome) {this.nome = nome;}

		public Class<?> getTipo() {return tipo;}
		public void setTipo(Class<?> tipo) {this.tipo = tipo;}

		public Object getValor() {return valor;}
		public void setValor(Object valor) {this.valor = valor;}
		
		public Object getValorAsString() {
			if (valor instanceof Date) {
				return SRHUtils.formataData(SRHUtils.FORMATO_DATA, (Date) valor);
			}
			return valor;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
			result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
			result = prime * result + ((valor == null) ? 0 : valor.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Restricao other = (Restricao) obj;
			if (nome == null) {
				if (other.nome != null)
					return false;
			} else if (!nome.equals(other.nome))
				return false;
			if (tipo == null) {
				if (other.tipo != null)
					return false;
			} else if (!tipo.equals(other.tipo))
				return false;
			if (valor == null) {
				if (other.valor != null)
					return false;
			} else if (!valor.equals(other.valor))
				return false;
			return true;
		}

		@Override
		public int compareTo(Restricao o) {
			return this.getNome().compareTo(o.getNome());
		}
	}
}
