package br.gov.ce.tce.srh.enums;

public enum TipoRubrica {
	
	VENCIMENTO_PROVENTO_PENSAO(1, "Vencimento, provento ou pensão"),
	DESCONTO(2, "Desconto"),
	INFORMATIVA(3, "Informativa"),
	INFORMATIVA_DEDUTORA(4, "Informativa dedutora");
	
	private String descricao;
	private Integer codigo;

	private TipoRubrica(Integer codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static TipoRubrica getByCodigo(Integer codigo) {
		
		if (codigo == null) {
			return null;
		}

		for (TipoRubrica parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Tipo de rubrica inválido");
	}

}
