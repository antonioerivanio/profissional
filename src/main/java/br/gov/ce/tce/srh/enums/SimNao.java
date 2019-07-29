package br.gov.ce.tce.srh.enums;

public enum SimNao {
	
	S("Sim"),
	N("Não");
	
	private String descricao;

	private SimNao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
