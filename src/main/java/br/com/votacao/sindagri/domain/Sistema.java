package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SISTEMA", schema = "sindagri")
public class Sistema implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  private Long id;
  
  @Column(name = "NOME")
  private String nome;
  
  @Column(name = "SIGLA")
  private String sigla;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getNome() {
    return this.nome;
  }
  
  public void setNome(String nome) {
    this.nome = nome;
  }
  
  public String getSigla() {
    return this.sigla;
  }
  
  public void setSigla(String sigla) {
    this.sigla = sigla;
  }
}
