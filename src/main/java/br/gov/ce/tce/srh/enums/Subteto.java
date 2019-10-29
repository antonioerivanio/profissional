package br.gov.ce.tce.srh.enums;

public enum Subteto {

	EXECUTIVO("Executivo", 1), JUDICIARIO("Judiciário", 2), LEGISLATIVO("Legislativo", 3), TODOS("Todos os poderes", 9);

	private String descricao;
	private Integer codigo;

	private Subteto(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static Subteto getByCodigo(Integer codigo) {

		for (Subteto parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Subteto inválido");
	}
}
