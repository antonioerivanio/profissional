package br.com.votacao.sindagri.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "pessoas")
@NamedQueries({@NamedQuery(name = "Pessoal.findByMatricula", query = "SELECT p FROM Pessoal p WHERE p.matricula = :matricula"), @NamedQuery(name = "Pessoal.findByCpf", query = "SELECT p FROM Pessoal p WHERE UPPER(p.cpf) = UPPER(:cpf)")})
public class Pessoal implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pessoal_id")
  private Integer id;
  
  @Column
  private String matricula;
  
  @Column
  private String cpf;
  
  @Column(name = "isMembroComissao", columnDefinition = "boolean default true", nullable = false)
  private boolean isMembroComissao;
  
  @Column
  private String funcao;
 
  public String toString() {
    return "Pessoal(id=" + getId() + ", matricula=" + getMatricula() + ", cpf=" + getCpf() + ", isMembroComissao=" + isMembroComissao() + ", funcao=" + getFuncao() + ")";
  }
}
