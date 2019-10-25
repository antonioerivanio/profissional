package br.gov.ce.tce.srh.enums;

public enum DesoneracaoFolha {

	NAO("Não Aplicável", 0), SIM("Aplicável", 1);

	private String descricao;
	private Integer codigo;

	private DesoneracaoFolha(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static DesoneracaoFolha getByCodigo(Integer codigo) {

		for (DesoneracaoFolha parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Desoneração Folha inválido");
	}
}
