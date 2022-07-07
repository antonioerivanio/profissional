package br.gov.ce.tce.srh.util;

import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;

public class SRHAuxilioSaudeTemplate extends EmailTemplate {
	
	private String destinatario;
	
	private AuxilioSaudeRequisicao auxilioSaudeRequisicao;

	private static final String TEMPLATE_AUXILIO_SAUDE_SRH = "srh_auxilio_saude_email_para_servidor.vm";
	private static final String KEY="srh";
	

	public SRHAuxilioSaudeTemplate(String destinatario, AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
		super(KEY, TEMPLATE_AUXILIO_SAUDE_SRH);
		
		this.destinatario = destinatario;
		this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
		this.templateName = TEMPLATE_AUXILIO_SAUDE_SRH;		
	}	

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
}
