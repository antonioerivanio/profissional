package br.gov.ce.tce.srh.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import br.gov.ce.tce.srh.exception.eTCEException;
import br.gov.ce.tce.srh.util.EmailTemplate;



public interface EmailService {
	
	void enviarEmail(List<String> destinatarios, String assunto, String conteudo, Map<String, InputStream> anexos, Integer tipoEmail) throws eTCEException;
	
	void enviarEmail(List<String> destinatarios, String assunto, EmailTemplate template, Map<String, InputStream> anexos, Integer tipoEmail) throws eTCEException;

}
