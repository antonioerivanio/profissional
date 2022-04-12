package br.gov.ce.tce.srh.enums;

public enum RubricaContribuicaoRPPS {
	
	RINCCPRPS00("00", "Não é base de cálculo de contribuições devidas ao RPPS/regime militar"),
	RINCCPRPS11("11", "Base de cálculo de contribuições devidas ao RPPS/regime militar"),
	RINCCPRPS12("12", "Base de cálculo de contribuições devidas ao RPPS/regime militar - 13º salário"),
	RINCCPRPS31("31", "Contribuição descontada do segurado e beneficiário"),
	RINCCPRPS32("32", "Contribuição descontada do segurado e beneficiário - 13º salário"),
	RINCCPRPS91("91", "Suspensão de incidência em decorrência de decisão judicial");
	
	private String descricao;
	private String codigo;

	private RubricaContribuicaoRPPS (String codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static RubricaContribuicaoRPPS getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (RubricaContribuicaoRPPS parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}

}
