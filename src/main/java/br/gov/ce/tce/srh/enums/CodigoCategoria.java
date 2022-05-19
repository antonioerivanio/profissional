package br.gov.ce.tce.srh.enums;

public enum CodigoCategoria {
	
	CATEG101("101", "Empregado - Geral, inclusive o empregado público da administração direta ou indireta contratado pela CLT"),
	CATEG301("301", "Servidor público titular de cargo efetivo, magistrado, ministro de Tribunal de Contas, conselheiro de Tribunal de Contas e membro do Ministério Público."),
	CATEG307("307", "Militar"),
	CATEG410("410", "Trabalhador cedido/exercício em outro órgão/juiz auxiliar - Informação prestada pelo cessionário/destino."),
	CATEG701("701", "Contribuinte individual - Autônomo em geral, exceto se enquadrado em uma das demais categorias de contribuinte.");
	
	private String codigo;
	private String descricao;

	private CodigoCategoria(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static CodigoCategoria getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}

		for (CodigoCategoria tipo : CodigoCategoria.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Tipo de VinculoRGPS inválido: " + codigo);
	}

}
