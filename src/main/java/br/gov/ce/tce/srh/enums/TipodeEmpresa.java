package br.gov.ce.tce.srh.enums;

import java.util.ArrayList;
import java.util.List;

/****
 * 
 * @author erivanio.cruz campos da tabela TB_PESSOAJURIDICA
 */
public enum TipodeEmpresa {
  
  EMPRESA_EM_GERAL(0, "Empresa em geral"), 
  INSTITUICAO_ENSINO(1, "Instituição de Ensino"), 
  PLANOS_SAUDE(2, "Planos de Saúde");
  
  
  private Integer codigo;
  private String descricao;

  private TipodeEmpresa(Integer codigo, String descricao) {
    this.codigo = codigo;
    this.descricao = descricao;
  }


  public Integer getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public static TipodeEmpresa getByCodigo(Integer codigo) {

    if (codigo == null) {
      return null;
    }

    for (TipodeEmpresa parametro : values()) {
      if (parametro.getCodigo().equals(codigo)) {
        return parametro;
      }
    }
    throw new IllegalArgumentException("Código Tipo de Licença inválido");
  }

  public static ArrayList<Integer> getTodosCodigos() {
    ArrayList<Integer> codigos = new ArrayList<Integer>();
    for (TipodeEmpresa parametro : values()) {
      codigos.add(parametro.getCodigo());
    }

    return codigos;
  }


}

