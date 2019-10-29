package br.gov.ce.tce.srh.enums;

public enum Carreira {

	NAO_SE_APLICA("Não se aplica"),
	CONTROLE_EXTERNO("Controle Externo");
	
	private String descricao;
	
	private Carreira( String descricao ) {
		this.descricao = descricao;
	}	
	
	public String getDescricao() {
		return descricao;
	}		
	
}
