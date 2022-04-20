package br.gov.ce.tce.srh.enums;

public enum TipoVinculoRGPS {
	
	ABAIXO_TETO_RGPS(2, "Contribuinte recebe em outra fonte abaixo do teto máximo do RGPS"),
	ACIMA_TETO_RGPS(3, "Contribuinte recebe em outra fonte acima do teto máximo do RGPS");
	
	private Integer codigo;
	private String descricao;

	private TipoVinculoRGPS(Integer codigo, String descricao) {
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

	public static TipoVinculoRGPS getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (TipoVinculoRGPS tipo : TipoVinculoRGPS.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Tipo de VinculoRGPS inválido: " + codigo);
	}

}
