package br.gov.ce.tce.srh.util;

import org.apache.velocity.VelocityContext;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;

public class SRHAuxilioSaudeTemplate extends EmailTemplate {
	
	private String destinatario;
	
	private AuxilioSaudeRequisicao auxilioSaudeRequisicao;

	private final static String AUXILIO_SAUDE_TEMPLATE_EMAIL = "srh_auxilio_saude_email_para_servidor.vm";
	

	public SRHAuxilioSaudeTemplate(String destinatario, AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
		super(AUXILIO_SAUDE_TEMPLATE_EMAIL);
		
		this.destinatario = destinatario;
		this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
		this.templateName = "SRP_envio_saneamento_ao_TCE.vm";
		
	}

	@Override
	protected VelocityContext construirContexto() {
		VelocityContext velocityContext = new VelocityContext();	
		velocityContext.put("srp", this);
		
		return velocityContext;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
}
