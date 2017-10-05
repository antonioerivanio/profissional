package br.gov.ce.tce.srh.enums;

public enum EnumStatusFuncional {
	
	ATIVO(1L, "Ativo"),
	ESTAGIARIO(2L, "Estagiário"),
	INATIVO(5L, "Inativo"),
	PENSAO_ALIMENTICIA_ATIVOS(8L, "Pensão Alimentícia Ativos"),
	PENSAO_ALIMENTICIA_INATIVOS(9L, "Pensão Alimentícia Inativos");
	
	private Long id;
	private String descricao;
	
	private EnumStatusFuncional( Long id, String descricao ) {
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
