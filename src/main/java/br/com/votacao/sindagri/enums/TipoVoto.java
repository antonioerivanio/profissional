package br.com.votacao.sindagri.enums;

public enum TipoVoto {
	ACHAPA_NAO_PARA("CHAPA \"A LUTA NÃO PODE PARAR\""), BRANCO("BRANCO"), NULO("NULO");

	private String descricao;

	TipoVoto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
