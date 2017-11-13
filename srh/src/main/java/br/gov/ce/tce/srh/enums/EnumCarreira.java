package br.gov.ce.tce.srh.enums;

public enum EnumCarreira {

	CONTROLE_EXTERNO(1L, "Controle Externo"),
	ADMINISTRACAO(2L, "Administração");
	
	private Long id;
	private String descricao;
	
	private EnumCarreira( Long id, String descricao ) {
		this.id = id;
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}	
	
}
