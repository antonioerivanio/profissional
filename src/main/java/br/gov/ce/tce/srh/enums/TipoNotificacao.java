package br.gov.ce.tce.srh.enums;

public enum TipoNotificacao {

	N("Notificação"),
	H("Histórico");
	
	private String descricao;
	
	private TipoNotificacao (String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}		
}
