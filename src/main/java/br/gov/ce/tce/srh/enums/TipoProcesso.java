package br.gov.ce.tce.srh.enums;

public enum TipoProcesso {
	
	ADMINISTRATIVO(1, "Administrativo"),
	JUDICIAL(2, "Judicial"),
	/* NB_INSS(3, "Número de Benefício (NB) do INSS"), */
	FAP(4, "Processo FAP de exercício anterior a 2019");
	
	private Integer codigo;
	private String descricao;

	private TipoProcesso(Integer codigo, String descricao) {
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

	public static TipoProcesso toEnum(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (TipoProcesso tipo : TipoProcesso.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Tipo de Processo inválido: " + codigo);
	}

}
