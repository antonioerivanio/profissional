package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.gov.ce.tce.srh.enums.BaseCalculoValorRestituido;

/***
 * 
 * @author erivanio.cruz
 * @since 05/07/2022
 *
 */
public class AuxilioSaudeBaseCalculo implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  
  private String intervaloIdade;  
  private BaseCalculoValorRestituido baseCalculoValorRestituido; 
  
  private List<AuxilioSaudeBaseCalculo>  auxilioSaudeBaseCalculos;

  
  public AuxilioSaudeBaseCalculo() {
    super();
  }

  public AuxilioSaudeBaseCalculo(String intervaloIdade, BaseCalculoValorRestituido baseCalculoValorRestituido) {
    super();    
    this.intervaloIdade = intervaloIdade;    
    this.baseCalculoValorRestituido = baseCalculoValorRestituido;
  }

 
  public String getIntervaloIdade() {
    return intervaloIdade;
  }

  public void setIntervaloIdade(String intervaloIdade) {
    this.intervaloIdade = intervaloIdade;
  }

  public List<AuxilioSaudeBaseCalculo> getAuxilioSaudeBaseCalculos() {   
    return auxilioSaudeBaseCalculos;
  }

  public void setAuxilioSaudeBaseCalculos(List<AuxilioSaudeBaseCalculo> auxilioSaudeBaseCalculos) {
    this.auxilioSaudeBaseCalculos = auxilioSaudeBaseCalculos;
  }

  public BaseCalculoValorRestituido getBaseCalculoValorRestituido() {
    return baseCalculoValorRestituido;
  }

  public void setBaseCalculoValorRestituido(BaseCalculoValorRestituido baseCalculoValorRestituido) {
    this.baseCalculoValorRestituido = baseCalculoValorRestituido;
  }

  public void gerarTabelaAuxilioSaudeBase() {    
    List<AuxilioSaudeBaseCalculo> auxilioSaudeBaseCalculoList = new ArrayList<AuxilioSaudeBaseCalculo>();
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("ATÉ 30", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_ATE_30ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("31-40", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_31_ATE_40ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("41-50", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_41_ATE_50ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("51-60", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_51_ATE_60ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("A PARTIR DE 61", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_SUPERIOR_60ANOS));
    
    setAuxilioSaudeBaseCalculos(auxilioSaudeBaseCalculoList);
  }
}

