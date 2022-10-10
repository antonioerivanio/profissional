package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

import lombok.Data;

@Data
@Entity
@Table(name = "GRUPOS_USUARIOS")
@BatchSize(size = 1000)
public class GrupoUsuario implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  private Long id;
  
  @ManyToOne  
  @JoinColumn(name = "grupo_id")
  private Grupo grupo;
  
  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;
  
}
