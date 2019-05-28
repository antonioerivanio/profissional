package br.gov.ce.tce.srh.enums;

public enum SituacaoCarreira {

	CRIACAO(1, "Criação"), EXTINCAO(2, "Extinção"), REESTRUTURACAO(3, "Reestruturação");

	private Integer codigo;
	private String descricao;

	private SituacaoCarreira(Integer codigo, String descricao) {
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

	public static SituacaoCarreira toEnum(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (SituacaoCarreira situacao : SituacaoCarreira.values()) {
			if (codigo.equals(situacao.getCodigo())) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Código de Situação da Carreira inválido: " + codigo);
	}

}
