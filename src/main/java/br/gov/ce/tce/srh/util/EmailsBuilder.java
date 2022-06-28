package br.gov.ce.tce.srh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.exception.eTCEException;
import br.gov.ce.tce.srh.sca.service.UsuarioService;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.service.EmailService;

@Component
public class EmailsBuilder {

  private final Logger logger = Logger.getLogger(EmailsBuilder.class);

  public static final String NAME_SRP = "SRP";
  public static final String EMAIL_SRP = "sistema.srp@tce.ce.gov.br";
  public static final String ASSUNTO = "SRP - Confirmação envio resposta de saneamento";

  private AuxilioSaudeRequisicao auxilioSaudeRequisicao;

  @Autowired
  private UsuarioService usuarioService;
  @Autowired
  private AmbienteService ambienteService;
  @Autowired
  private EmailService emailService;

  // List<EcontasDocumentoMunicipal> anexos = new ArrayList<>();

  public EmailsBuilder(AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
    this.inicializarContexto();
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
    /// saneamentoAtoService.findSaneametoById(saneamentoAto.getId());
  }

  /*
   * public EmailsBuilder adicionarAnexo(EcontasDocumentoMunicipal relatorio) { if(relatorio != null
   * && !anexos.contains(relatorio)) { anexos.add(relatorio); }
   * 
   * return this; }
   */

  public void buildAndSend() {
    if (auxilioSaudeRequisicao != null) {
      logger.info(String.format("Disparando envio de email de saneamento do Ato %d ...", auxilioSaudeRequisicao.getId()));

      this.adicionarUsuarioCadastro().enviarViaMap();

      logger.info(String.format("Disparo de email de saneamento do Ato %d concluído.", auxilioSaudeRequisicao.getId()));
    }
  }
  /*
   * private EmailsBuilder adicionarUsuarioCadastro() { if (auxilioSaudeRequisicao != null) { if
   * (saneamentoAto.getUsuarioSaneador() != null) { usuario =
   * usuarioService.pesquisarPorId(String.valueOf(saneamentoAto.getUsuarioSaneador())); } else {
   * usuario = usuarioService.pesquisarPorId("330"); } }
   * 
   * return this; }
   */


  private Map<String, String> buildMap() {
    Map<String, String> mapaEmails = new HashMap<>();

    if (!this.ambienteService.isAmbienteProducao()) {
      if (auxilioSaudeRequisicao != null && auxilioSaudeRequisicao.getUsuario().getEmail() != null && !usuario.getEmail().isEmpty()) {
        mapaEmails.put(usuario.getNome(), usuario.getEmail());
      }
      mapaEmails.put(EmailsBuilder.NAME_SRP, EmailsBuilder.EMAIL_SRP);

    } else {
      logger.info(String.format("\tEnvio de e-mail da PCG interrompido em %s", this.ambienteService.ambiente().getDescricao()));
    }

    return mapaEmails;
  }


  private void enviarViaMap() {
    Map<String, String> emailsConfirmacao = this.buildMap();

    SRHAuxilioSaudeTemplate template = new SRHAuxilioSaudeTemplate("", auxilioSaudeRequisicao);
    
    String assunto = ASSUNTO;
    
    for (Map.Entry<String, String> mapa : emailsConfirmacao.entrySet()) {
      if (mapa.getValue() != null && !mapa.getValue().isEmpty()) {
        List<String> emails = new ArrayList<String>();

        template.setDestinatario(mapa.getKey());
        emails.add(mapa.getValue());

        logger.info(String.format("\tEnviando e-mail para %s %s ...", mapa.getValue(), mapa.getKey()));

        try {
          
          emailService.enviarEmail(emails, assunto, template, null, 2); //anexos null
         // emailService.enviarEmail(emails, assunto, template, getAnexos(), 2);
          logger.info("\tE-mail enviado.");
        } catch (eTCEException e) {
          logger.info(String.format("\tErro ao enviar e-mail para %s %s ...", mapa.getValue(), mapa.getKey()));
          logger.info(String.format("\tMensagem do erro: %s", e.getCause().getMessage()));
          e.printStackTrace();
        }

      }
    }
  }


  /*
   * private Map<String, InputStream> getAnexos() { Map<String, InputStream> anexosMap = null; if
   * (!this.anexos.isEmpty()) { anexosMap = new HashMap<>(); for (EcontasDocumentoMunicipal anexo :
   * anexos) { String nomeArquivo = anexo.getNomeArquivoOriginal();
   * 
   * String path = "";// entidadeExercicioMunicipal.getPastaRaiz(); path = path + File.separator +
   * nomeArquivo; File relatorioFile = new File(path);
   * 
   * try { anexosMap.put(anexo.getNomeArquivoOriginal(), new FileInputStream(relatorioFile)); } catch
   * (FileNotFoundException e) { e.printStackTrace(); } } }
   * 
   * return anexosMap != null && !anexosMap.isEmpty() ? anexosMap : null; }
   */

  private void inicializarContexto() {    
    //this.usuarioService = AutowiringSpringBeanJobFactory.getContext().getBean(UsuarioService.class);
    //this.ambienteService = AutowiringSpringBeanJobFactory.getContext().getBean(AmbienteService.class);
    //this.emailService = AutowiringSpringBeanJobFactory.getContext().getBean(EmailService.class);
  }

}
