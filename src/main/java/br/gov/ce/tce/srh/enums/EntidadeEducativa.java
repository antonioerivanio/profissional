package br.gov.ce.tce.srh.enums;

public enum EntidadeEducativa {

	SIM("Sim", "S"), NAO("Não", "N");

	private String descricao;
	private String sigla;

	private EntidadeEducativa(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static EntidadeEducativa getBySigla(String sigla) {

		if (sigla == null) {
			return null;
		}
		
		for (EntidadeEducativa parametro : values()) {
			if (parametro.getSigla().equals(sigla)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Sigla Entidade Educativa inválido");
	}
}
