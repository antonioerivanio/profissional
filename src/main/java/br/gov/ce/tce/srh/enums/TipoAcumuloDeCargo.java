package br.gov.ce.tce.srh.enums;

public enum TipoAcumuloDeCargo {
	
	NAO_ACUMULAVEL(1, "Não acumulável"),
	SAUDE(2, "Profissional de Saúde"),
	PROFESSOR(3, "Professor"),
	TECNICO(4, "Técnico/Científico");
	
	private Integer codigo;
	private String descricao;

	private TipoAcumuloDeCargo(Integer codigo, String descricao) {
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

	public static TipoAcumuloDeCargo getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (TipoAcumuloDeCargo tipo : TipoAcumuloDeCargo.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Tipo de Acúmulo de Cargo inválido: " + codigo);
	}

}
