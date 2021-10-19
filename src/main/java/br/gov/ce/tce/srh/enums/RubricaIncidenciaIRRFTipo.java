package br.gov.ce.tce.srh.enums;

import java.util.List;

public enum RubricaIncidenciaIRRFTipo {
	
	//INCIDENCIA_TRIBUTARIA ("Código de incidência tributária da rubrica para o IRRF"),
	INCIDENCIA_TRIBUTARIA ("Outros"),
	BASE_DE_CALCULO ("Rendimentos tributáveis - base de cálculo do IRRF"),
	RETENCOES ("Retenções do IRRF efetuadas"),
	DEDUCOES ("Deduções da base de cálculo do IRRF"),
	ISENCOES ("Rendimento não tributável ou isento do IRRF"),
	/* DEMANDAS_JUDICIAIS("Demandas Judiciais"), */
	EXIGIBILIDADE_BASE_DE_CALCULO("Exigibilidade suspensa - Rendimento tributável"),
	EXIGIBILIDADE_RETENCAO("Exigibilidade suspensa - Retenção do IRRF"),
	EXIGIBILIDADE_DEDUCAO("Exigibilidade suspensa - Dedução da base de cálculo do IRRF"),
	COMPENSACAO_JUDICIAL("Compensação judicial");
	
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
