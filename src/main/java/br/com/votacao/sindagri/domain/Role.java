package br.com.votacao.sindagri.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String name;
  
  private String description;
  
   public String toString() {
    return "Role(id=" + getId() + ", name=" + getName() + ", description=" + getDescription() + ")";
  }
}
