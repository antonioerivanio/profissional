package br.gov.ce.tce.srh.enums;

public enum AprendizContrato {

	DISPENSADO("Dispensado de acordo com a lei", 0),
	DISPENSADO_PARCIALMENTE("Dispensado, mesmo que parcialmente, em virtude de processo judicial", 1),
	OBRIGADO("Obrigado", 2);

	private String descricao;
	private Integer codigo;

	private AprendizContrato(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static AprendizContrato getByCodigo(Integer codigo) {
		
		if (codigo == null) {
			return null;
		}
		
		for (AprendizContrato parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Aprendiz Contrato inválido");
	}
}
