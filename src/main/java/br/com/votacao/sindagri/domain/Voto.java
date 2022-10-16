package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import br.com.votacao.sindagri.enums.TipoVoto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Audited
@Entity
@Table(name = "votos")
@NamedQueries({
		@NamedQuery(name = "Voto.findByVotousuario", query = "SELECT v FROM Voto v WHERE v.usuario = :usuario"),
		@NamedQuery(name = "Voto.findByVotomatricula", query = "SELECT v FROM Voto v WHERE v.usuario.matricula = :matricula"),
		@NamedQuery(name = "Voto.findAll", query = "SELECT v FROM Voto v")})
public class Voto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "votos_id_seq", sequenceName = "votos_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votos_id_seq")
	@Column(name = "id")
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column
	private TipoVoto tipo;

	@NotAudited
	@OneToOne
	@JoinColumn(name="usuario_id", unique = true)
	private Usuario usuario;
	
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar datavotacao;
	
    
	@Transient
	private int quantidade;
	
	@Transient
	private int quantidadeBranco;
	@Transient
	private int quantidadeChapa;
	@Transient
	private int quantidadeNulo;
	@Transient
	private Long total;

	public Voto(TipoVoto tipoVoto, Usuario usuario, Calendar dataVotacao) {
		super();		
		this.tipo = tipoVoto;
		this.usuario = usuario;	
		this.datavotacao = dataVotacao;
	}

}
