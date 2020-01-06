package br.gov.ce.tce.srh.enums;

public enum ContagemEspecial {
	
	NAO(1, "Não"),
	PROFESSOR(2, "Professor (Infantil, Fundamental e Médio)"),
	MEMBRO_TC(3, "Professor de Ensino Superior, Magistrado, Membro de Ministério Público, Membro do Tribunal de Contas (com ingresso anterior a 16/12/1998 EC nr. 20/98)");
//	RISCO(4, "Atividade de risco");
	
	private Integer codigo;
	private String descricao;

	private ContagemEspecial(Integer codigo, String descricao) {
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

	public static ContagemEspecial getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (ContagemEspecial tipo : ContagemEspecial.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Contagem de Tempo Especial inválido: " + codigo);
	}

}
