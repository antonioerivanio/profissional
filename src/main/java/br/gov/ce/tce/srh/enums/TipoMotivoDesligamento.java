package br.gov.ce.tce.srh.enums;

/***
 *  @FONTE - eSocial versão S-1.0 - Tabelas (cons. até NT 06/2022)
 *  Tabela 19 - Motivos de Desligamento
 * @author erivanio.cruz
 *
 */
public enum TipoMotivoDesligamento {

  APOSENTADORIA_COMPUSORIA(18, "Aposentadoria compulsória", "Todos, exceto [104]"), 
  APOSENTADORIA_POR_IDADE(19, "Aposentadoria por idade", "[301, 302, 303, 306, 307, 309]"), 
  APOSENTADORIA_POR_IDADE_E_TEMPOCONTRIBUICAO(20,"Aposentadoria por idade e tempo de contribuição", "[301, 302, 303, 306, 307, 309]");

  private Integer codigo;
  private String descricao;
  private String categorias;

  private TipoMotivoDesligamento(Integer codigo, String descricao, String categorias) {
    this.descricao = descricao;
    this.codigo = codigo;
    this.categorias = categorias;
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

  public String getCategorias() {
    return categorias;
  }

  public void setCategorias(String categorias) {
    this.categorias = categorias;
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
