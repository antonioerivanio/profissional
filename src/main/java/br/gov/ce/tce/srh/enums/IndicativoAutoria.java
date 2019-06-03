package br.gov.ce.tce.srh.enums;

public enum IndicativoAutoria {
	
	PROPRIA(1, "Próprio contribuinte"),
	OUTRA(2, "Outra entidade, empresa ou empregado");
	
	private Integer codigo;
	private String descricao;

	private IndicativoAutoria(Integer codigo, String descricao) {
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

	public static IndicativoAutoria toEnum(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (IndicativoAutoria tipo : IndicativoAutoria.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Indicativo de Autoria inválido: " + codigo);
	}

}
