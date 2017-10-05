package br.gov.ce.tce.srh.enums;

public enum EnumTipoBeneficio {
	
	VOLUNTARIA(1L, "Voluntária"),
	PROPORCIONAL(2L, "Proporcional"),
	INVALIDEZ(3L, "Invalidez");
	
	private Long id;
	private String descricao;
	
	private EnumTipoBeneficio (Long id, String descricao) {
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
