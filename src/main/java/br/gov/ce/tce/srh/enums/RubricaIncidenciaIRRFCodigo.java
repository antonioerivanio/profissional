package br.gov.ce.tce.srh.enums;

import java.util.ArrayList;
import java.util.List;

public enum RubricaIncidenciaIRRFCodigo {
	
	/* RIIRRF00("00", "Rendimento não tributável", RubricaIncidenciaIRRFTipo.INCIDENCIA_TRIBUTARIA),
	RIIRRF01("01", "Rendimento não tributável em função de acordos internacionais de bitributação", RubricaIncidenciaIRRFTipo.INCIDENCIA_TRIBUTARIA), */
	/* RIIRRF09("09", "Outras verbas não consideradas como base de cálculo ou rendimento", RubricaIncidenciaIRRFTipo.INCIDENCIA_TRIBUTARIA), */
	RIIRRF09("09", "Verba transitada pela folha de pagamento de natureza diversa de rendimento ou retenção/isenção/dedução de IR", RubricaIncidenciaIRRFTipo.INCIDENCIA_TRIBUTARIA),

	RIIRRF11("11", "Remuneração mensal", RubricaIncidenciaIRRFTipo.BASE_DE_CALCULO),
	RIIRRF12("12", "13º Salário", RubricaIncidenciaIRRFTipo.BASE_DE_CALCULO),
	RIIRRF13("13", "Férias", RubricaIncidenciaIRRFTipo.BASE_DE_CALCULO),
	RIIRRF14("14", "PLR", RubricaIncidenciaIRRFTipo.BASE_DE_CALCULO),
	/* RIIRRF15("15", "Rendimentos Recebidos Acumuladamente - RRA", RubricaIncidenciaIRRFTipo.BASE_DE_CALCULO), */

	RIIRRF31("31", "Remuneração mensal", RubricaIncidenciaIRRFTipo.RETENCOES),
	RIIRRF32("32", "13º Salário", RubricaIncidenciaIRRFTipo.RETENCOES),
	RIIRRF33("33", "Férias", RubricaIncidenciaIRRFTipo.RETENCOES),
	RIIRRF34("34", "PLR", RubricaIncidenciaIRRFTipo.RETENCOES),
	/* RIIRRF35("35", "RRA", RubricaIncidenciaIRRFTipo.RETENCOES), */

	RIIRRF41("41", "Previdência Social Oficial - PSO - Remuner. mensal", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF42("42", "PSO - 13º salário", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF43("43", "PSO - Férias", RubricaIncidenciaIRRFTipo.DEDUCOES),
	/* RIIRRF44("44", "PSO - RRA", RubricaIncidenciaIRRFTipo.DEDUCOES), */
	RIIRRF46("46", "Previdência Privada - salário mensal", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF47("47", "Previdência Privada - 13º salário", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF48("48", "Previdência privada - Férias", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF51("51", "Pensão Alimentícia - Remuneração mensal", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF52("52", "Pensão Alimentícia - 13º salário", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF53("53", "Pensão Alimentícia - Férias", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF54("54", "Pensão Alimentícia - PLR", RubricaIncidenciaIRRFTipo.DEDUCOES),
	/* RIIRRF55("55", "Pensão Alimentícia - RRA", RubricaIncidenciaIRRFTipo.DEDUCOES), */
	RIIRRF61("61", "Fundo de Aposentadoria Programada Individual - FAPI - Remuneração mensal", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF62("62", "Fundo de Aposentadoria Programada Individual - FAPI - 13º salário", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF63("63", "Fundação de Previdência Complementar do Servidor Público - Funpresp - Remuneração mensal", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF64("64", "Fundação de Previdência Complementar do Servidor Público - Funpresp - 13º salário", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF65("65", "Fundação de previdência complementar do servidor público - Férias", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF66("66", "Fundo de Aposentadoria Programada Individual - FAPI - Férias", RubricaIncidenciaIRRFTipo.DEDUCOES),
	RIIRRF67("67", "Plano privado coletivo de assistência à saúde", RubricaIncidenciaIRRFTipo.DEDUCOES),	

	RIIRRF70("70", "Parcela Isenta 65 anos - Remuneração mensal", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF71("71", "Parcela Isenta 65 anos - 13º salário", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF72("72", "Diárias", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF73("73", "Ajuda de custo", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF74("74", "Indenização e rescisão de contrato, inclusive a título de PDV e acidentes de trabalho", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF75("75", "Abono pecuniário", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF76("76", "Pensão, aposentadoria ou reforma por moléstia grave ou acidente em serviço - Remuneração Mensal", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF77("77", "Pensão, aposentadoria ou reforma por moléstia grave ou acidente em serviço - 13º salário", RubricaIncidenciaIRRFTipo.ISENCOES),
	/* RIIRRF78("78", "Valores pagos a titular ou sócio de microempresa ou empresa de pequeno porte, exceto pró-labore e alugueis", RubricaIncidenciaIRRFTipo.ISENCOES), */
	RIIRRF700("700", "Auxílio moradia", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF701("701", "Parte não tributável do valor de serviço de transporte de passageiros ou cargas", RubricaIncidenciaIRRFTipo.ISENCOES),
	RIIRRF79("79", "Outras isenções (o nome da rubrica deve ser claro para identificação da natureza dos valores)", RubricaIncidenciaIRRFTipo.ISENCOES),
	/* RIIRRF81("81", "Depósito judicial", RubricaIncidenciaIRRFTipo.DEMANDAS_JUDICIAIS),
	RIIRRF82("82", "Compensação judicial do ano calendário", RubricaIncidenciaIRRFTipo.DEMANDAS_JUDICIAIS),
	RIIRRF83("83", "Compensação judicial de anos anteriores", RubricaIncidenciaIRRFTipo.DEMANDAS_JUDICIAIS),
	RIIRRF91("91", "Remuneração mensal", RubricaIncidenciaIRRFTipo.INCIDENCIA_SUSPENSA),
	RIIRRF92("92", "13º salário", RubricaIncidenciaIRRFTipo.INCIDENCIA_SUSPENSA),
	RIIRRF93("93", "Férias", RubricaIncidenciaIRRFTipo.INCIDENCIA_SUSPENSA),
	RIIRRF94("94", "PLR", RubricaIncidenciaIRRFTipo.INCIDENCIA_SUSPENSA),
	RIIRRF95("95", "RRA", RubricaIncidenciaIRRFTipo.INCIDENCIA_SUSPENSA); */

	RIIRRF9011("9011", "Remuneração mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_BASE_DE_CALCULO),
	RIIRRF9012("9012", "13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_BASE_DE_CALCULO),
	RIIRRF9013("9013", "Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_BASE_DE_CALCULO),
	RIIRRF9014("9014", "PLR", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_BASE_DE_CALCULO),

	RIIRRF9031("9031", "Remuneração mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9032("9032", "13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9033("9033", "Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9034("9034", "PLR", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9831("9831", "Depósito judicial - Mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9832("9832", "Depósito judicial - 13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9833("9833", "Depósito judicial - Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),
	RIIRRF9834("9834", "Depósito judicial - PLR", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_RETENCAO),

	RIIRRF9041("9041", "Previdência Social Oficial - PSO - Remuneração mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9042("9042", "PSO - 13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9043("9043", "PSO - Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9046("9046", "Previdência privada - Salário mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9047("9047", "Previdência privada - 13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9048("9048", "Previdência privada - Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9051("9051", "Pensão alimentícia - Remuneração mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9052("9052", "Pensão alimentícia - 13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9053("9053", "Pensão alimentícia - Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9054("9054", "Pensão alimentícia - PLR", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9061("9061", "Fundo de Aposentadoria Programada Individual - FAPI - Remuneração mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9062("9062", "Fundo de Aposentadoria Programada Individual - FAPI - 13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9063("9063", "Fundação de previdência complementar do servidor público - Remuneração mensal", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9064("9064", "Fundação de previdência complementar do servidor público - 13º salário", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9065("9065", "Fundação de previdência complementar do servidor público - Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9066("9066", "Fundo de Aposentadoria Programada Individual - FAPI - Férias", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),
	RIIRRF9067("9067", "Plano privado coletivo de assistência à saúde", RubricaIncidenciaIRRFTipo.EXIGIBILIDADE_DEDUCAO),

	RIIRRF9082("9082", "Compensação judicial do ano-calendário", RubricaIncidenciaIRRFTipo.COMPENSACAO_JUDICIAL),
	RIIRRF9083("9083", "Compensação judicial de anos anteriores", RubricaIncidenciaIRRFTipo.COMPENSACAO_JUDICIAL);
	
	private String descricao;
	private String codigo;
	private RubricaIncidenciaIRRFTipo tipo;

	private RubricaIncidenciaIRRFCodigo (String codigo, String descricao, RubricaIncidenciaIRRFTipo tipo) {
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
	
	public RubricaIncidenciaIRRFTipo getTipo() {
		return tipo;
	}

	public static RubricaIncidenciaIRRFCodigo getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (RubricaIncidenciaIRRFCodigo parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código de incidência inválido");
	}
	
	public static List<RubricaIncidenciaIRRFCodigo> getByTipo(RubricaIncidenciaIRRFTipo tipo) {
		
		if (tipo == null) {
			return null;
		}
		
		List<RubricaIncidenciaIRRFCodigo> list = new ArrayList<>();		
		
		for (RubricaIncidenciaIRRFCodigo parametro : values()) {
			if (parametro.getTipo().equals(tipo)) {
				list.add(parametro);
			}
		}
		
		return list;
	}

}
