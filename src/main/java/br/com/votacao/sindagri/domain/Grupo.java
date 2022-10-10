package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "GRUPOS")
@NamedQueries({@NamedQuery(name = "Grupo.findByGruponome", query = "SELECT gru FROM Grupo gru WHERE UPPER(gru.nome) = UPPER(:nome)")})
public class Grupo implements Serializable {
  private static final long serialVersionUID = 937394715296920532L;
  
  @Id
  @SequenceGenerator(name = "grupos_id_seq", sequenceName = "grupos_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupos_id_seq")  
  private Long id;
  
  @Column(name = "NOME")
  private String nome;
  
  @Column
  private String descricao;  
  
  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "grupoUsuario")
  Set<Usuario> usuarios;
}
