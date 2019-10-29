package br.gov.ce.tce.srh.enums;

public enum EnteFederativoRpps {

	SIM("Possui RPPS", "S"), NAO("Não possui RPPS", "N");

	private String descricao;
	private String sigla;

	private EnteFederativoRpps(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static EnteFederativoRpps getBySigla(String sigla) {

		for (EnteFederativoRpps parametro : values()) {
			if (parametro.getSigla().equals(sigla)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Sigla Ente Federativo inválido");
	}
}
