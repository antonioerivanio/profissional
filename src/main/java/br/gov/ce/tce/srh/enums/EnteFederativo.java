package br.gov.ce.tce.srh.enums;

public enum EnteFederativo {

	SIM("É uma EFR", "S"), NAO("Não é EFR", "N");

	private String descricao;
	private String sigla;

	private EnteFederativo(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static EnteFederativo getBySigla(String sigla) {

		for (EnteFederativo parametro : values()) {
			if (parametro.getSigla().equals(sigla)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Sigla Ente Federativo inválido");
	}
}
