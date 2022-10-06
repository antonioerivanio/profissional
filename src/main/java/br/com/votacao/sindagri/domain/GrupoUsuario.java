package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "GRUPOUSUARIO", schema = "sindagri")
@BatchSize(size = 1000)
public class GrupoUsuario implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  private Long id;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "GRUPO")
  private Grupo grupo;
  
  @Column(name = "USUARIO")
  private Long usuario;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Grupo getGrupo() {
    return this.grupo;
  }
  
  public void setGrupo(Grupo grupo) {
    this.grupo = grupo;
  }
  
  public Long getUsuario() {
    return this.usuario;
  }
  
  public void setUsuario(Long usuario) {
    this.usuario = usuario;
  }
}
