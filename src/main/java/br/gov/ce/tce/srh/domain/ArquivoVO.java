package br.gov.ce.tce.srh.domain;

import java.io.File;
import br.gov.ce.tce.srh.util.SRHUtils;

public class ArquivoVO {  
  private String nome;
  private String descricacao;
  private String caminhoCompletoArquivo;
  private byte[] comprovante;
  public static final String PDF = ".pdf";
  public static final String UNDERLINE = "_";
  // pegando o caminho do arquivo no servidor
  public static final String CAMINHO_PARA_SALVAR_ARQUIVO =
                            SRHUtils.getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");
  
  public ArquivoVO(String nome, String descricacao, String caminhoCompletoArquivo, byte[] comprovante, int contador) {
    super();
    this.nome = nome + UNDERLINE +  contador + PDF;
    this.descricacao = descricacao;
    this.caminhoCompletoArquivo = caminhoCompletoArquivo;
    this.comprovante = comprovante;    
  }
  
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
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

