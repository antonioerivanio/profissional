package br.gov.ce.tce.srh.domain;

import br.gov.ce.tce.srh.util.SRHUtils;

public class ArquivoVO {
  private int contador = 1; 
  private String nomeTemp;
  private String descricacao;
  private String caminhoCompletoArquivo;  
  private byte[] comprovante;
  public static String PDF = ".pdf";
  public static String UNDERLINE = "_";
  // pegando o caminho do arquivo no servidor
  public static String CAMINHO_PARA_SALVAR_ARQUIVO =
                            SRHUtils.getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");
  
  public static String NOME_ARQUIVO_BENEFICIARIO ="comprovante_beneficiario";
  public static String NOME_ARQUIVO_DEPENDENTE ="comprovante_dependente";
  
  public ArquivoVO(String nomeTemp, String descricacao, String caminhoCompletoArquivo, byte[] comprovante) {
    super();
    this.nomeTemp = nomeTemp + UNDERLINE +  contador + PDF;
    this.descricacao = descricacao;
    this.caminhoCompletoArquivo = caminhoCompletoArquivo;
    this.comprovante = comprovante;
  }
  
  public String getNomeTemp() {
    return nomeTemp;
  }
  public void setNomeTemp(String nomeTemp) {
    this.nomeTemp = nomeTemp;
  }
  public String getDescricacao() {
    return descricacao;
  }
  public void setDescricacao(String descricacao) {
    this.descricacao = descricacao;
  }
  public String getCaminhoCompletoArquivo() {
    return caminhoCompletoArquivo;
  }
  public void setCaminhoCompletoArquivo(String caminhoCompletoArquivo) {
    this.caminhoCompletoArquivo = caminhoCompletoArquivo;
  }

  public byte[] getComprovante() {
    return comprovante;
  }

  public void setComprovante(byte[] comprovante) {
    this.comprovante = comprovante;
  }
  
  
  
}
