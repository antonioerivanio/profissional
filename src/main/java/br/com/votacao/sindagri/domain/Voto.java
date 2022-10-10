package br.com.votacao.sindagri.domain;

import java.io.Serializable;

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
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.envers.Audited;

import br.com.votacao.sindagri.enums.TipoVoto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Audited
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "votos")
@NamedQueries({
		@NamedQuery(name = "Voto.findByVotousuario", query = "SELECT v FROM Voto v WHERE v.usuario = :usuario"),
		@NamedQuery(name = "Voto.findByUserid", query = "SELECT v FROM Voto v WHERE v.usuario.id = :usuarioId") })
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

	
	@NotFound
	@OneToOne
	@JoinColumn(name="usuario_id", unique = true)
	private Usuario usuario;
	
	@Transient
	private Long quantidade;
	@Transient
	private Long total;

	public Voto(TipoVoto tipoVoto, Usuario usuario) {
		super();		
		this.tipo = tipoVoto;
		this.usuario = usuario;	
	}

}
