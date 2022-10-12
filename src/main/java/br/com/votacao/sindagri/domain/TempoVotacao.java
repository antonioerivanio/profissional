package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "tempo_votacao")
@NamedQueries({
		@NamedQuery(name = "TempoVotacao.findByTempoVotacao", query = "SELECT t FROM TempoVotacao t ") })
public class TempoVotacao  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "tempo_votacao_id_seq", sequenceName = "tempo_votacao_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tempo_votacao_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name="data_inicio", columnDefinition = "TIMESTAMP")
	private LocalDateTime  dataInicio;
	
	@Column(name="data_fim", columnDefinition = "TIMESTAMP")	
	private LocalDateTime  dataFim;
}
