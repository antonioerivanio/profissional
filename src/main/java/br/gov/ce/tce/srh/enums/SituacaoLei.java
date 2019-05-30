package br.gov.ce.tce.srh.enums;

public enum SituacaoLei {

	CRIACAO(1, "Criação"), EXTINCAO(2, "Extinção"), REESTRUTURACAO(3, "Reestruturação");

	private Integer codigo;
	private String descricao;

	private SituacaoLei(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static SituacaoLei toEnum(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (SituacaoLei situacao : SituacaoLei.values()) {
			if (codigo.equals(situacao.getCodigo())) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Código de Situação da Carreira inválido: " + codigo);
	}

}
