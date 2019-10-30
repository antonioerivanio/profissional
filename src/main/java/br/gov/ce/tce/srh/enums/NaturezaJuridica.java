package br.gov.ce.tce.srh.enums;

public enum NaturezaJuridica {

	ORGAO_PUBLICO("Órgão Público do Poder Legislativo Estadual ou do Distrito Federal", "1058");

	private String descricao;
	private String codigo;

	private NaturezaJuridica(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static NaturezaJuridica getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (NaturezaJuridica parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Natureza Jurídica inválido");
	}
}
