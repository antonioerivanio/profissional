package br.gov.ce.tce.srh.enums;

public enum RubricaIncidenciaFGTS {
	
	RIFGTS00("00", "Não é Base de Cálculo do FGTS"),
	RIFGTS11("11", "Base de Cálculo do FGTS"),
	RIFGTS12("12", "Base de Cálculo do FGTS 13° salário"),
	RIFGTS21("21", "Base de Cálculo do FGTS Rescisório (aviso prévio)"),
	RIFGTS91("91", "Incidência suspensa em decorrência de decisão judicial");
	
	private String descricao;
	private String codigo;

	private RubricaIncidenciaFGTS (String codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static RubricaIncidenciaFGTS getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (RubricaIncidenciaFGTS parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}

}
