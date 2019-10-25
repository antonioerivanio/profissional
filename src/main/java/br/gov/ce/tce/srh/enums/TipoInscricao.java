package br.gov.ce.tce.srh.enums;

public enum TipoInscricao {
	
	CNPJ(1),
	CPF(2),
	CAEPF(3),
	CNO(4),
	CGC(5);
	
	private Integer codigo;

	private TipoInscricao(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public static TipoInscricao getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (TipoInscricao tipo : TipoInscricao.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Tipo de Inscrição inválido: " + codigo);
	}

}
