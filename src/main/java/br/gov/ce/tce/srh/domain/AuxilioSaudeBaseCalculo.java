package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/***
 * 
 * @author erivanio.cruz
 * @since 05/07/2022
 *
 */
public class AuxilioSaudeBaseCalculo implements Serializable {

  private String nomeColuna;
  private String intervaloIdade;
  private String percentual;
  private Double ValorCorrespondentes;  

  public AuxilioSaudeBaseCalculo(String nomeColuna, String intervaloIdade, String percentual, Double valorCorrespondentes) {
    super();
    this.nomeColuna = nomeColuna;
    this.intervaloIdade = intervaloIdade;
    this.percentual = percentual;
    ValorCorrespondentes = valorCorrespondentes;
  }

  public String getNomeColuna() {
    return nomeColuna;
  }

  public void setNomeColuna(String nomeColuna) {
    this.nomeColuna = nomeColuna;
  }

  public String getIntervaloIdade() {
    return intervaloIdade;
  }

  public void setIntervaloIdade(String intervaloIdade) {
    this.intervaloIdade = intervaloIdade;
  }

  public String getPercentual() {
    return percentual;
  }

  public void setPercentual(String percentual) {
    this.percentual = percentual;
  }

  public Double getValorCorrespondentes() {
    return ValorCorrespondentes;
  }

  public void setValorCorrespondentes(Double valorCorrespondentes) {
    ValorCorrespondentes = valorCorrespondentes;
  }
  
  


}

