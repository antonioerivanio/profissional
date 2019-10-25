package br.gov.ce.tce.srh.enums;

public enum RubricaIncidenciaSIND {
	
	RISIND00("00", "Não é base de cálculo"),
	RISIND11("11", "Base de Cálculo"),	
	RISIND31("31", "Valor da contribuição sindical laboral descontada"),
	RISIND91("91", "Incidência suspensa em decorrência de decisão judicial");

	private String descricao;
	private String codigo;

	private RubricaIncidenciaSIND (String codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static RubricaIncidenciaSIND getByCodigo(String codigo) {

		for (RubricaIncidenciaSIND parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}
	
}
