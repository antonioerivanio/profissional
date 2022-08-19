package br.gov.ce.tce.srh.enums;

/***
 * @FONTE - eSocial versão S-1.0 - Tabelas (cons. até NT 06/2022)
 * Tabela 02 - Financiamento da Aposentadoria Especial e Redução do Tempo de Contribuição
 * @author erivanio.cruz
 *
 */
public enum NaturezaRubricaFolhaPagamento {

  NAO_ENSEJA_APOSENTADORIA_ESPECIAL(1, "Não ensejador de aposentadoria especial"),
  ENSEJA_APOSENTADORIA_ESPECIAL_FAE15_12(2, "Ensejador de aposentadoria especial - FAE15_12% (15 anos de contribuição e alíquota de 12%)"),
  ENSEJA_APOSENTADORIA_ESPECIAL_FAE20_09(2, "Ensejador de aposentadoria especial - FAE15_12% (15 anos de contribuição e alíquota de 12%)"),
  ENSEJA_APOSENTADORIA_ESPECIAL_FAE25_06(2, "Ensejador de aposentadoria especial - FAE15_12% (15 anos de contribuição e alíquota de 12%)");

  private Integer codigo;
  private String descricao;
  

  private NaturezaRubricaFolhaPagamento(Integer codigo, String desc) {
    this.codigo = codigo;
    this.descricao = desc;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }


  public static NaturezaRubricaFolhaPagamento getByCodigo(Integer codigo) {

    if (codigo == null) {
      return null;
    }

    for (NaturezaRubricaFolhaPagamento tipo : NaturezaRubricaFolhaPagamento.values()) {
      if (codigo.equals(tipo.getCodigo())) {
        return tipo;
      }
    }

    throw new IllegalArgumentException("Código de Tipo de Inscrição inválido: " + codigo);
  }

}
