package br.gov.ce.tce.srh.enums;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public enum EnumCarreira {

	CONTROLE_EXTERNO(1, "Controle Externo"),
	ADMINISTRACAO(2, "Administração");
	
	private Integer id;
	private String descricao;
	
	private EnumCarreira( Integer id, String descricao ) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public static EnumCarreira getEnumCarreira(Integer idCarreira) {
		
		if(idCarreira == null)
			return null;
		
		for (EnumCarreira enumCarreira: EnumCarreira.values()) {
			if(enumCarreira.getId() == idCarreira)
				return enumCarreira;
		}
		
		throw new SRHRuntimeException("Carreira inválida.");
	}	
	
}
