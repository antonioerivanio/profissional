package br.com.votacao.sindagri.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SECAO", schema = "sindagri")
public class Secao {
  @Id
  private Long id;
  
  @Column(name = "NOME")
  private String nome;
  
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
}
