package br.gov.ce.tce.srh.enums;

public enum SituacaoPj {

	NORMAL("Situação Normal", 0), EXTINCAO("Extinção", 1), FUSAO("Fusão", 2), CISAO("Cisão", 3),
	INCORPORACAO("Incorporação", 4);

	private String descricao;
	private Integer codigo;

	private SituacaoPj(String descricao, Integer codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static SituacaoPj getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (SituacaoPj parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Situação Pessoa Jurídica inválido");
	}
}
