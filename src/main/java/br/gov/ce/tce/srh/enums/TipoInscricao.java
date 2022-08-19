package br.gov.ce.tce.srh.enums;

public enum TipoInscricao {

  CNPJ(1, "CNPJ"), CPF(2, "CPF"),
  // CAEPF(3),
  CNO(4, "CNO")/*
                * , CGC(5)
                */;

  private Integer codigo;
  private String descricao;
  

  private TipoInscricao(Integer codigo, String desc) {
    this.codigo = codigo;
    this.descricao = desc;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }


  public static TipoInscricao getByCodigo(Integer codigo) {

    if (codigo == null) {
      return null;
    }

    for (TipoInscricao tipo : TipoInscricao.values()) {
      if (codigo.equals(tipo.getCodigo())) {
        return tipo;
      }
    }

    throw new IllegalArgumentException("Código de Tipo de Inscrição inválido: " + codigo);
  }

}
