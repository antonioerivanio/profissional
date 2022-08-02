package br.gov.ce.tce.srh.domain;

/***
 * 
 * @author erivanio.cruz
 *
 */
public class Texto {

  public static String getDeclaracao() {
    StringBuilder declaracao = new StringBuilder("Declaro que estou ciente que a inveracidade da informação ");
    declaracao.append("contida neste documento, por mim firmado, constitui prática de ");
    declaracao.append("infração disciplinar, passível de punição na forma da lei, e ");
    declaracao.append("que não recebo auxílio-saúde semelhante nem possuo programa de ");
    declaracao.append("assistência à saúde custeado integral ou parcialmente pelos cofres públicos ");
    
    return declaracao.toString();
  }
  
  
  public static String getAviso() {    
    StringBuilder aviso = new StringBuilder("Prezado(a) servidor (a), o requerimento para pagamento do auxílio-saúde ");
    aviso.append("deve ser proposto até o dia 10 de cada mês, acompanhado dos respectivos documentos comprobatórios. ");
    aviso.append("Requerimentos realizados após o dia 10 somente serão incluídos na folha de pagamento do mês subsequente. ");    
    
    return aviso.toString();
  }

}
