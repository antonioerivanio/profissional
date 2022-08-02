package br.gov.ce.tce.srh.enums;

/****
 * 
 * Tabela de base de calculo dos valores a ser restituídos sobre o valor solicitado
 * na tela Auxilio-Saúde
 * @author erivanio.cruz
 *
 */
public enum BaseCalculoValorRestituido {
  VALOR_MAXIMO_PARA_PESSOA_IDADE_ATE_30ANOS("3,00%", 814.12),
  VALOR_MAXIMO_PARA_PESSOA_IDADE_31_ATE_40ANOS("3.5%",949.80),
  VALOR_MAXIMO_PARA_PESSOA_IDADE_41_ATE_50ANOS("4,00%", 1085.49),
  VALOR_MAXIMO_PARA_PESSOA_IDADE_51_ATE_60ANOS("4.50%", 1221.17),
  VALOR_MAXIMO_PARA_PESSOA_IDADE_SUPERIOR_60ANOS("5,00%" , 1356.86);
  
  private String percentual;
  private Double valor;

  BaseCalculoValorRestituido(String percentural,double valor) {
    this.percentual = percentural;
   this.valor = valor;
  }

  public String getPercentual() {
    return percentual;
  }

  public Double getValor() {
    return valor;
  }

}
