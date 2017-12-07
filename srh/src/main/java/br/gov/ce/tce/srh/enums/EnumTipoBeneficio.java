package br.gov.ce.tce.srh.enums;

import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public enum EnumTipoBeneficio {
	
	VOLUNTARIA(1, "Voluntária"),
	PROPORCIONAL(2, "Proporcional"),
	INVALIDEZ(3, "Invalidez");
	
	private Integer id;
	private String descricao;
	
	private EnumTipoBeneficio (Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}	
	
	public static EnumTipoBeneficio getEnumTipoBeneficio(Integer idTipoBeneficio) {
		
		if(idTipoBeneficio == null)
			return null;
		
		for (EnumTipoBeneficio enumTipoBeneficio: EnumTipoBeneficio.values()) {
			if(enumTipoBeneficio.getId() == idTipoBeneficio)
				return enumTipoBeneficio;
		}
		
		throw new SRHRuntimeException("Tipo Benefício inválido.");
	}

}
