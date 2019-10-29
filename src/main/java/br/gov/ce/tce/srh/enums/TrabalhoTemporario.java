package br.gov.ce.tce.srh.enums;

public enum TrabalhoTemporario {

	SIM("Empresa de Trabalho Temporário", "S"), NAO("Não é Empresa de Trabalho Temporário", "N");

	private String descricao;
	private String sigla;

	private TrabalhoTemporario(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static TrabalhoTemporario getBySigla(String sigla) {

		for (TrabalhoTemporario parametro : values()) {
			if (parametro.getSigla().equals(sigla)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Sigla Trabalho Temporário inválido");
	}
}
