package br.gov.ce.tce.srh.enums;

public enum RegistroEmpregados {

	NAO("Não optou pelo registro eletrônico de empregados", 0), SIM("Optou pelo registro eletrônico de empregados", 1);

	private String descricao;
	private Integer codigo;

	private RegistroEmpregados(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static RegistroEmpregados getByCodigo(Integer codigo) {

		for (RegistroEmpregados parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Registro de Tempo inválido");
	}
}
