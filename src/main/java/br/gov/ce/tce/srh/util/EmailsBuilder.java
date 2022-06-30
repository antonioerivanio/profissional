package br.gov.ce.tce.srh.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.exception.eTCEException;
import br.gov.ce.tce.srh.sca.service.UsuarioService;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.service.EmailService;

@Component
public class EmailsBuilder {

  private final Logger logger = Logger.getLogger(EmailsBuilder.class);
  
  public static final String NAME_SRH = "SRH";
  public static final String EMAIL_SRH = "sistema.srh@tce.ce.gov.br";
  public static final String ASSUNTO = "SRH - Situação atual do auxilio-saúde solicitado";

  private AuxilioSaudeRequisicao auxilioSaudeRequisicao;
 
  private UsuarioService usuarioService;
  
  private AmbienteService ambienteService;
 
  private EmailService emailService;

  List<?> anexos = new ArrayList<>();  

  public EmailsBuilder builder(AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
    // this.inicializarContexto();
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
    /// saneamentoAtoService.findSaneametoById(saneamentoAto.getId());
    return this;
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
      enviarViaMap();
      //this.adicionarUsuarioCadastro().enviarViaMap();

      logger.info(String.format("Disparo de email de saneamento do Ato %d concluído.", auxilioSaudeRequisicao.getId()));
    }else {
      enviarViaMap();
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
    //  if (auxilioSaudeRequisicao != null && auxilioSaudeRequisicao.getUsuario().getEmail() != null && !usuario.getEmail().isEmpty()) {
        //mapaEmails.put("usuario.getNome()", "usuario.getEmail()");
      mapaEmails.put("Erivanio", "erivanio.cruz@tce.ce.gov.br");
      //}
      mapaEmails.put(EmailsBuilder.NAME_SRH, EmailsBuilder.EMAIL_SRH);

    } else {
      logger.info(String.format("\tEnvio de e-mail da PCG interrompido em %s", this.ambienteService.ambiente().getDescricao()));
    }

    return mapaEmails;
  }


  private void enviarViaMap() {
    Map<String, String> emailsConfirmacao = this.buildMap();

    SRHAuxilioSaudeTemplate template = new SRHAuxilioSaudeTemplate("erivanio.cruz@tce.ce.gov.br", auxilioSaudeRequisicao);

    String assunto = ASSUNTO;

    for (Map.Entry<String, String> mapa : emailsConfirmacao.entrySet()) {
      if (mapa.getValue() != null && !mapa.getValue().isEmpty()) {
        List<String> emails = new ArrayList<String>();

        template.setDestinatario(mapa.getKey());
        emails.add(mapa.getValue());

        logger.info(String.format("\tEnviando e-mail para %s %s ...", mapa.getValue(), mapa.getKey()));

        try {

          emailService.enviarEmail(emails, assunto, template, null, 2); // anexos null
          // emailService.enviarEmail(emails, assunto, template, getAnexos(), 2);
          logger.info("\tE-mail enviado.");
        } catch (Exception e) {
          logger.info(String.format("\tErro ao enviar e-mail para %s %s ...", mapa.getValue(), mapa.getKey()));
          logger.info(String.format("\tMensagem do erro: %s", e.getCause().getMessage()));
          e.printStackTrace();
        }

      }
    }
  }

  
  /*
   * private void inicializarContexto() { this.usuarioService =
   * AutowiringSpringBeanJobF.getContext().getBean(UsuarioService.class); this.ambienteService =
   * AutowiringSpringBeanJobFactory.getContext().getBean(AmbienteService.class); this.emailService =
   * AutowiringSpringBeanJobFactory.getContext().getBean(EmailService.class); }
   */
  
  
}
