package br.gov.ce.tce.srh.enums;

public enum RubricaIncidenciaFGTS {
	
	RIFGTS00("00", "Não é base de cálculo do FGTS"),
	RIFGTS11("11", "Base de cálculo do FGTS mensal"),
	RIFGTS12("12", "Base de cálculo do FGTS 13° salário"),
	RIFGTS21("21", "Base de cálculo do FGTS aviso prévio indenizado"),
	RIFGTS91("91", "Incidência suspensa em decorrência de decisão judicial - FGTS mensal"),
	RIFGTS92("92", "Incidência suspensa em decorrência de decisão judicial - FGTS 13º salário"),
	RIFGTS93("93", "Incidência suspensa em decorrência de decisão judicial - FGTS aviso prévio indenizado");
	
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
