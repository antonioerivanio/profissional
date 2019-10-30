package br.gov.ce.tce.srh.enums;

public enum ClassificacaoTributaria {

	ADMINISTRACAO_DIRETA(
			"Administração Direta da União, Estados, Distrito Federal e Municípíos; Autarquias e Fundações Públicas",
			85);

	private String descricao;
	private Integer codigo;

	private ClassificacaoTributaria(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static ClassificacaoTributaria getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (ClassificacaoTributaria parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		return null;
	}
}
