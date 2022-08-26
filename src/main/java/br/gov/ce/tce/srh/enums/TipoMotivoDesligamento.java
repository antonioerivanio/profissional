package br.gov.ce.tce.srh.enums;

/***
 *  @FONTE - eSocial versão S-1.0 - Tabelas (cons. até NT 06/2022)
 *  Tabela 19 - Motivos de Desligamento
 * @author erivanio.cruz
 *
 */
public enum TipoMotivoDesligamento {  
  APOSENTADORIA_COMPUSORIA(38, "Aposentadoria, exceto por invalidez"), 
  APOSENTADORIA_POR_IDADE(39, "Aposentadoria de servidor estatutário, por invalidez"),
  EXONERACAO(23, "Exoneração"), 
  RESCISAO_POR_FALECIMENTO_EMPREGADO(10, "Rescisão por falecimento do empregado"),
  RESCISAO_TERMINO_CONTRATO_A_TERMO(06, "Rescisão por término do contrato a termo"),
  RESCISAO_ANTECIPADA_CONTRATO_INICIADA_EMPREGADOR(03, "Rescisão antecipada do contrato a termo por iniciativa do empregador"),
  RESCISAO_ANTECIPADA_CONTRATO_INICIADA_PELO_EMPREGADO(04, "Rescisão antecipada do contrato a termo por iniciativa do empregado");
   
  
  private Integer codigo;
  private String descricao;
  
  private TipoMotivoDesligamento(Integer codigo, String descricao) {
    this.descricao = descricao;
    this.codigo = codigo;    
  }

  public Integer getCodigo() {
    return codigo;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public static TipoMotivoDesligamento getByCodigo(String codigo) {
    if (codigo == null) {
      return null;
    }

    for (TipoMotivoDesligamento parametro : values()) {
      if (parametro.getCodigo().equals(codigo)) {
        return parametro;
      }
    }
    throw new IllegalArgumentException("Código de incidência inválido");
  }  

}
