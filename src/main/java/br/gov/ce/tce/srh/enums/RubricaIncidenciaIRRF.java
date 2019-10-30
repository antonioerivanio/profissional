package br.gov.ce.tce.srh.enums;

public enum RubricaIncidenciaIRRF {
	
	RIIRRF00("00", "Rendimento não tributável"),
	RIIRRF01("01", "Rendimento não tributável em função de acordos internacionais de bitributação"),
	RIIRRF09("09", "Outras verbas não consideradas como base de cálculo ou rendimento"),
	RIIRRF11("11", "Remuneração mensal"),
	RIIRRF12("12", "13º Salário"),
	RIIRRF13("13", "Férias"),
	RIIRRF14("14", "PLR"),
	RIIRRF15("15", "Rendimentos Recebidos Acumuladamente - RRA"),
	RIIRRF31("31", "Remuneração mensal"),
	RIIRRF32("32", "13º Salário"),
	RIIRRF33("33", "Férias"),
	RIIRRF34("34", "PLR"),
	RIIRRF35("35", "RRA"),
	RIIRRF41("41", "Previdência Social Oficial - PSO - Remuner. mensal"),
	RIIRRF42("42", "PSO - 13º salário"),
	RIIRRF43("43", "PSO - Férias"),
	RIIRRF44("44", "PSO - RRA"),
	RIIRRF46("46", "Previdência Privada - salário mensal"),
	RIIRRF47("47", "Previdência Privada - 13º salário"),
	RIIRRF51("51", "Pensão Alimentícia - Remuneração mensal"),
	RIIRRF52("52", "Pensão Alimentícia - 13º salário"),
	RIIRRF53("53", "Pensão Alimentícia - Férias"),
	RIIRRF54("54", "Pensão Alimentícia - PLR"),
	RIIRRF55("55", "Pensão Alimentícia - RRA"),
	RIIRRF61("61", "Fundo de Aposentadoria Programada Individual - FAPI - Remuneração mensal"),
	RIIRRF62("62", "Fundo de Aposentadoria Programada Individual - FAPI - 13º salário"),
	RIIRRF63("63", "Fundação de Previdência Complementar do Servidor Público - Funpresp - Remuneração mensal"),
	RIIRRF64("64", "Fundação de Previdência Complementar do Servidor Público - Funpresp - 13º salário"),	
	RIIRRF70("70", "Parcela Isenta 65 anos - Remuneração mensal"),
	RIIRRF71("71", "Parcela Isenta 65 anos - 13º salário"),
	RIIRRF72("72", "Diárias"),
	RIIRRF73("73", "Ajuda de custo"),
	RIIRRF74("74", "Indenização e rescisão de contrato, inclusive a título de PDV e acidentes de trabalho"),
	RIIRRF75("75", "Abono pecuniário"),
	RIIRRF76("76", "Pensão, aposentadoria ou reforma por moléstia grave ou acidente em serviço - Remuneração Mensal"),
	RIIRRF77("77", "Pensão, aposentadoria ou reforma por moléstia grave ou acidente em serviço - 13º salário"),
	RIIRRF78("78", "Valores pagos a titular ou sócio de microempresa ou empresa de pequeno porte, exceto pró-labore e alugueis"),
	RIIRRF79("79", "Outras isenções (o nome da rubrica deve ser claro para identificação da natureza dos valores)"),
	RIIRRF81("81", "Depósito judicial"),
	RIIRRF82("82", "Compensação judicial do ano calendário"),
	RIIRRF83("83", "Compensação judicial de anos anteriores"),
	RIIRRF91("91", "Remuneração mensal"),
	RIIRRF92("92", "13º salário"),
	RIIRRF93("93", "Férias"),
	RIIRRF94("94", "PLR"),
	RIIRRF95("95", "RRA");
	
	
	
	private String descricao;
	private String codigo;

	private RubricaIncidenciaIRRF (String codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static RubricaIncidenciaIRRF getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (RubricaIncidenciaIRRF parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}

}
