package br.gov.ce.tce.srh.exception;

import java.util.List;

/**
 * Excecao para propagar mensagens de validacao de regras de negacao Como a
 * excecao herda de RuntimeException, o container faz rollback da transacao
 * corrente automaticamente.
 *
 * @author Ivia
 */
public class eTCEException extends RuntimeException {

	private static final long serialVersionUID = 8715912847813835227L;
	private List<String> messages;

	public eTCEException(Throwable throwable) {
		super(throwable);
	}

	public eTCEException(String message) {
		super(message);
	}

	public eTCEException(String message, Exception exception) {
		super(message, exception);
	}

	public eTCEException(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
