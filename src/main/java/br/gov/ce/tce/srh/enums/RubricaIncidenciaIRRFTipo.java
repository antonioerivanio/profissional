package br.gov.ce.tce.srh.enums;

import java.util.List;

public enum RubricaIncidenciaIRRFTipo {
	
	INCIDENCIA_TRIBUTARIA ("Código de incidência tributária da rubrica para o IRRF"),
	BASE_DE_CALCULO ("Rendimentos tributáveis - base de cálculo do IRRF"),
	RETENCOES ("Retenções do IRRF efetuadas"),
	DEDUCOES ("Deduções da base de cálculo do IRRF"),
	ISENCOES ("Isenções do IRRF"),
	DEMANDAS_JUDICIAIS("Demandas Judiciais"),
	INCIDENCIA_SUSPENSA("Incidência Suspensa decorrente de decisão judicial, relativas a base de cálculo do IRRF");
	
	private String descricao;
	
	private RubricaIncidenciaIRRFTipo (String descricao) {
		this.descricao = descricao;	
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public List<RubricaIncidenciaIRRFCodigo> getCodigos(){			
		return RubricaIncidenciaIRRFCodigo.getByTipo(this);
	}

}
