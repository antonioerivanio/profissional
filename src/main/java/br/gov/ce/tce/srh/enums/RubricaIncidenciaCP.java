package br.gov.ce.tce.srh.enums;

public enum RubricaIncidenciaCP {
	
	RICP00("00", "Não é base de cálculo"),
	RICP01("01", "Não é base de cálculo em função de acordos internacionais de previdência social"),
	RICP11("11", "Mensal"),
	RICP12("12", "13º Salário"),
	RICP13("13", "Exclusiva do Empregador - mensal"),
	RICP14("14", "Exclusiva do Empregador - 13º salário"),
	RICP15("15", "Exclusiva do segurado - mensal"),
	RICP16("16", "Exclusiva do segurado - 13º salário"),
	RICP21("21", "Salário maternidade mensal pago pelo Empregador"),
	RICP22("22", "Salário maternidade - 13º Salário, pago pelo Empregador"),
	RICP23("23", "Auxilio doença mensal - Regime Próprio de Previdência Social"),
	RICP24("24", "Auxilio doença 13º salário doença - Regime próprio de previdência social"),
	RICP25("25", "Salário maternidade mensal pago pelo INSS"),
	RICP26("26", "Salário maternidade - 13º salário, pago pelo INSS"),
	RICP31("31", "Mensal"),
	RICP32("32", "13º Salário"),
	RICP34("34", "SEST"),
	RICP35("35", "SENAT"),
	RICP51("51", "Salário-família"),
	RICP61("61", "Complemento de salário-mínimo - Regime próprio de previdência social"),
	RICP91("91", "Mensal"),
	RICP92("92", "13º Salário"),
	RICP93("93", "Salário maternidade"),
	RICP94("94", "Salário maternidade - 13º salário"),
	RICP95("95", "Exclusiva do Empregador - mensal"),
	RICP96("96", "Exclusiva do Empregador - 13º salário"),
	RICP97("97", "Exclusiva do Empregador - Salário maternidade"),
	RICP98("98", "Exclusiva do Empregador - Salário maternidade 13º salário");
	
	private String descricao;
	private String codigo;

	private RubricaIncidenciaCP (String codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static RubricaIncidenciaCP getByCodigo(String codigo) {
		
		if (codigo == null) {
			return null;
		}
		
		for (RubricaIncidenciaCP parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}

}
