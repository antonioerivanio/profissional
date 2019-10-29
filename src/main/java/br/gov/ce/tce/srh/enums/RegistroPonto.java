package br.gov.ce.tce.srh.enums;

public enum RegistroPonto {

	NAO_UTILIZA("Não utiliza", 0), MANUAL("Manual", 1), MECANICO("Mecânico", 2), ELETRONICO("Eletrônico", 3),
	NAO_ELETRONICO("Não eletrônico alternativo", 4), ELETRONICO_ALTERNATIVO("Eletrônico alternativo", 5),
	ELETRONICO_OUTROS("Eletrônico outros", 6);

	private String descricao;
	private Integer codigo;

	private RegistroPonto(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static RegistroPonto getByCodigo(Integer codigo) {

		for (RegistroPonto parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Registro de Ponto inválido");
	}
}
