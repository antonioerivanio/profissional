package br.gov.ce.tce.srh.enums;

public enum PcdContrato {

	DISPENSADO("Dispensado de acordo com a lei", 0),
	DISPENSADO_PARCIALMENTE("Dispensado, mesmo que parcialmente, em virtude de processo judicial", 1),
	EXIGIBILIDADE_SUSPENSA("Com exigibilidade suspensa, mesmo que parcialmente em virtude de\n"
			+ "Termo de Compromisso firmado com o Ministério do Trabalho", 2),
	OBRIGADO("Obrigado", 9);

	private String descricao;
	private Integer codigo;

	private PcdContrato(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static PcdContrato getByCodigo(Integer codigo) {

		for (PcdContrato parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código PCD Contrato inválido");
	}
}
