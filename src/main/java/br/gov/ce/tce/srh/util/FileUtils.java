package br.gov.ce.tce.srh.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/***
 * Classe para trabalhar com upload, dowload, de arquivos
 * @author erivanio.cruz
 *
 */
public class FileUtils {

  private String caminhoFile;
  private String nomeArquivo;


  public void downloadPDF(FacesContext facesContext, File file) throws IOException {

    // Prepare.
    // FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

    BufferedInputStream input = null;
    BufferedOutputStream output = null;


    try {
      // Open file.
     // input = new BufferedInputStream(new FileInputStream(file), file.length());

      // Init servlet response.
      /*
       * response.reset(); response.setHeader("Content-Type", "application/pdf");
       * response.setHeader("Content-Length", String.valueOf(file.length()));
       * response.setHeader("Content-Disposition", "inline; filename=\"" + caminhoFile + "\""); output =
       * new BufferedOutputStream(response.getOutputStream(), file.length());
       * 
       * // Write file contents to response. byte[] buffer = new byte[tamanhoArquivo]; int length; while
       * ((length = input.read(buffer)) > 0) { output.write(buffer, 0, length); }
       * 
       * // Finalize task. output.flush();
       */
    } finally {
      // Gently close streams.
      close(output);
      close(input);
    }

    // Inform JSF that it doesn't need to handle response.
    // This is very important, otherwise you will get the following exception in the logs:
    // java.lang.IllegalStateException: Cannot forward after response has been committed.
    facesContext.responseComplete();
  }

  public String getCaminhoFile() {
    return caminhoFile;
  }

  public void setCaminhoFile(String caminhoFile) {
    this.caminhoFile = caminhoFile;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }


  // Helpers (can be refactored to public utility class) ----------------------------------------

  private static void close(Closeable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (IOException e) {
        // Do your thing with the exception. Print it, log it or mail it. It may be useful to
        // know that this will generally only be thrown when the client aborted the download.
        e.printStackTrace();
      }
    }
  }

  
  public static void upload(String caminhoArquivo, byte[] dadosArquivos) throws Exception {
    File file = new File(caminhoArquivo);
    FileOutputStream fop;
    fop = new FileOutputStream(file);
    fop.write(dadosArquivos);
    fop.flush();
    fop.close();
  }

  public static void visualizar(String pathArquivo, RelatorioUtil relatorioUtil) throws Exception {    
      InputStream in = new FileInputStream(pathArquivo);
      byte[] comprovanteBytes = IOUtils.toByteArray(in);

      relatorioUtil.openPdf(comprovanteBytes, pathArquivo);

      IOUtils.closeQuietly(in);
  }
  
  public static void criarDiretorio(String pathDiretorio) throws IOException {
    Path caminhoDiretorioNovo = Paths.get(pathDiretorio);
    Files.createDirectories(caminhoDiretorioNovo);      
  }
  
  public static void moverArquivoParaUmNovoDiretorio(String caminhoOrigem, String caminhoSestino) {
  //mover arquivo para um nova pasta  nome da pessoa/id
    try {
      Files.move(Paths.get(caminhoOrigem), Paths.get(caminhoSestino), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}

