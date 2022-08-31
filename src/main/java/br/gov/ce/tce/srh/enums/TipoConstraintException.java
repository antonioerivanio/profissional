package br.gov.ce.tce.srh.enums;
/**
 * 
 * @author erivanio.cruz
 *
 */
public enum TipoConstraintException {
  CONSTRAINT_UNIQUE_DESLIGAMENTO("UN_ESOCIAL_DESLIGAMENTO", "Ops!. Este servidor já foi cadastrado para o evento 2299!"),
  CONSTRAINT_UNIQUE_TERMINOVINCULO("UN_ESOCIAL_TERMINOVINCULO", "Ops!. Este servidor já foi cadastrado para o evento 2399!");
  
  private String nome;
  private String mensageError;
  
  TipoConstraintException(String nome, String mensageError) {
    this.nome = nome;
    this.mensageError = mensageError;
  }

  public String getNome() {
    return nome;
  }

  public String getMensageError() {
    return mensageError;
  }
  
   
  
}
