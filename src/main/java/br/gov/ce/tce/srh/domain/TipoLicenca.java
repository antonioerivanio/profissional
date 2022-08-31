package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_TIPOLICENCA
 * 
 * @since : Sep 1, 2011, 18:19:23 AM
 * @author : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "TB_TIPOLICENCA", schema = DatabaseMetadata.SCHEMA_SRH)
public class TipoLicenca extends BasicEntity<Long> implements Serializable {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "DESCRICAO", length = 60, nullable = false)
  private String descricao;

  @Column(name = "FUNDAMENTACAO", length = 255, nullable = false)
  private String fundamentacao;

  @Column(name = "SEXOVALIDO", length = 1, nullable = false)
  private String sexoValido;

  @Column(name = "QTDEMAXIMODIAS", length = 4, nullable = false)
  private Long qtdeMaximoDias;

  @Column(name = "ESPECIAL", nullable = false)
  private boolean especial;

  @Column(name = "CODIGOESOCIAL", nullable = false)
  private Integer codigoEsocial;


  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getFundamentacao() {
    return fundamentacao;
  }

  public void setFundamentacao(String fundamentacao) {
    this.fundamentacao = fundamentacao;
  }

  public String getSexoValido() {
    return sexoValido;
  }

  public void setSexoValido(String sexoValido) {
    this.sexoValido = sexoValido;
  }

  public Long getQtdeMaximoDias() {
    return qtdeMaximoDias;
  }

  public void setQtdeMaximoDias(Long qtdeMaximoDias) {
    this.qtdeMaximoDias = qtdeMaximoDias;
  }

  public boolean isEspecial() {
    return especial;
  }

  public void setEspecial(boolean especial) {
    this.especial = especial;
  }

  public Integer getCodigoEsocial() {
    return codigoEsocial;
  }

  public void setCodigoEsocial(Integer codigoEsocial) {
    this.codigoEsocial = codigoEsocial;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

}
