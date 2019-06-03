package br.gov.ce.tce.srh.enums;

public enum IndicativoMateria {
	
	TRIBUTARIA(1, "Exclusivamente tributária ou tributária e FGTS"),
	MENOR(2, "Autorização de trabalho de menor"),
	PCD(3, "Dispensa, ainda que parcial, de contratação de pessoa com deficiência (PCD)"),
	APRENDIZ(4, "Dispensa, ainda que parcial, de contratação de aprendiz"),
	SST(5, "Segurança e Saúde no Trabalho"),
	ACIDENTE(6, "Conversão de Licença Saúde em Acidente de Trabalho"),
	EX_FGTS(7, "Exclusivamente FGTS e/ou Contribuição Social Rescisória (Lei Complementar 110/2001)"),
	SINDICAL(8, "Contribuição sindical"),
	OUTROS(99, "Outros assuntos");
	
	private Integer codigo;
	private String descricao;

	private IndicativoMateria(Integer codigo, String descricao) {
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

	public static IndicativoMateria toEnum(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (IndicativoMateria tipo : IndicativoMateria.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Indicativo de Matéria inválido: " + codigo);
	}

}
