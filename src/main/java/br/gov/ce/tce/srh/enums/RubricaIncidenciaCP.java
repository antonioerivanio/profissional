package br.gov.ce.tce.srh.enums;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<RubricaIncidenciaCP> naoEhBase(){
		List<RubricaIncidenciaCP> list = new ArrayList<>();
		list.add(RICP00);
		list.add(RICP01);
		return list;
	}
	
	public static List<RubricaIncidenciaCP> base(){
		List<RubricaIncidenciaCP> list = new ArrayList<>();
		list.add(RICP11);
		list.add(RICP12);
		list.add(RICP13);
		list.add(RICP14);
		list.add(RICP15);
		list.add(RICP16);
		list.add(RICP21);
		list.add(RICP22);
		list.add(RICP23);
		list.add(RICP24);
		list.add(RICP25);
		list.add(RICP26);
		return list;
	}
	
	public static List<RubricaIncidenciaCP> contribuicao(){
		List<RubricaIncidenciaCP> list = new ArrayList<>();
		list.add(RICP31);
		list.add(RICP32);
		list.add(RICP34);
		list.add(RICP35);
		return list;
	}
	
	public static List<RubricaIncidenciaCP> outros(){
		List<RubricaIncidenciaCP> list = new ArrayList<>();
		list.add(RICP51);
		list.add(RICP61);
		return list;
	}
	
	public static List<RubricaIncidenciaCP> suspensao(){
		List<RubricaIncidenciaCP> list = new ArrayList<>();
		list.add(RICP91);
		list.add(RICP92);
		list.add(RICP93);
		list.add(RICP94);
		list.add(RICP95);
		list.add(RICP96);
		list.add(RICP97);
		list.add(RICP98);
		return list;
	}

}
