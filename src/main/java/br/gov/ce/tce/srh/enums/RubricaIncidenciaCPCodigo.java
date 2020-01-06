package br.gov.ce.tce.srh.enums;

import java.util.ArrayList;
import java.util.List;

public enum RubricaIncidenciaCPCodigo {
	
	RICP00("00", "Não é base de cálculo", RubricaIncidenciaCPTipo.NAO_EH_BASE),
	RICP01("01", "Não é base de cálculo em função de acordos internacionais de previdência social", RubricaIncidenciaCPTipo.NAO_EH_BASE),
	RICP11("11", "Mensal", RubricaIncidenciaCPTipo.BASE),
	RICP12("12", "13º Salário", RubricaIncidenciaCPTipo.BASE),
	RICP13("13", "Exclusiva do Empregador - mensal", RubricaIncidenciaCPTipo.BASE),
	RICP14("14", "Exclusiva do Empregador - 13º salário", RubricaIncidenciaCPTipo.BASE),
	RICP15("15", "Exclusiva do segurado - mensal", RubricaIncidenciaCPTipo.BASE),
	RICP16("16", "Exclusiva do segurado - 13º salário", RubricaIncidenciaCPTipo.BASE),
	RICP21("21", "Salário maternidade mensal pago pelo Empregador", RubricaIncidenciaCPTipo.BASE),
	RICP22("22", "Salário maternidade - 13º Salário, pago pelo Empregador", RubricaIncidenciaCPTipo.BASE),
	RICP23("23", "Auxilio doença mensal - Regime Próprio de Previdência Social", RubricaIncidenciaCPTipo.BASE),
	RICP24("24", "Auxilio doença 13º salário doença - Regime próprio de previdência social", RubricaIncidenciaCPTipo.BASE),
	RICP25("25", "Salário maternidade mensal pago pelo INSS", RubricaIncidenciaCPTipo.BASE),
	RICP26("26", "Salário maternidade - 13º salário, pago pelo INSS", RubricaIncidenciaCPTipo.BASE),
	RICP31("31", "Mensal", RubricaIncidenciaCPTipo.CONTRIBUICAO),
	RICP32("32", "13º Salário", RubricaIncidenciaCPTipo.CONTRIBUICAO),
	RICP34("34", "SEST", RubricaIncidenciaCPTipo.CONTRIBUICAO),
	RICP35("35", "SENAT", RubricaIncidenciaCPTipo.CONTRIBUICAO),
	RICP51("51", "Salário-família", RubricaIncidenciaCPTipo.OUTROS),
	RICP61("61", "Complemento de salário-mínimo - Regime próprio de previdência social", RubricaIncidenciaCPTipo.OUTROS),
	RICP91("91", "Mensal", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP92("92", "13º Salário", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP93("93", "Salário maternidade", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP94("94", "Salário maternidade - 13º salário", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP95("95", "Exclusiva do Empregador - mensal", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP96("96", "Exclusiva do Empregador - 13º salário", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP97("97", "Exclusiva do Empregador - Salário maternidade", RubricaIncidenciaCPTipo.SUSPENSAO),
	RICP98("98", "Exclusiva do Empregador - Salário maternidade 13º salário", RubricaIncidenciaCPTipo.SUSPENSAO);
	
	private String descricao;
	private String codigo;
	private RubricaIncidenciaCPTipo tipo;

	private RubricaIncidenciaCPCodigo (String codigo, String descricao, RubricaIncidenciaCPTipo tipo) {
		this.descricao = descricao;
		this.codigo = codigo;
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public RubricaIncidenciaCPTipo getTipo() {
		return tipo;
	}

	public static RubricaIncidenciaCPCodigo getByCodigo(String codigo) {
		
		if (codigo == null) {
			return null;
		}
		
		for (RubricaIncidenciaCPCodigo parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}
	
	public static List<RubricaIncidenciaCPCodigo> getByTipo(RubricaIncidenciaCPTipo tipo) {
		
		if (tipo == null) {
			return null;
		}
		
		List<RubricaIncidenciaCPCodigo> list = new ArrayList<>();		
		
		for (RubricaIncidenciaCPCodigo parametro : values()) {
			if (parametro.getTipo().equals(tipo)) {
				list.add(parametro);
			}
		}
		
		return list;
	}
	

}
