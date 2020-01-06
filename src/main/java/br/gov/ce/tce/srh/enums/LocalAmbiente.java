package br.gov.ce.tce.srh.enums;

public enum LocalAmbiente {
	
	PROPRIO(1, "Estabelecimento do próprio empregador"),
	TERCEIROS(2, "Estabelecimento de terceiros"),
	OUTROS(3, "Prestação de serviços em instalações de terceiros não consideradas como lotações dos tipos 03 a 09 da Tabela 10");
	
	private Integer codigo;
	private String descricao;

	private LocalAmbiente(Integer codigo, String descricao) {
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

	public static LocalAmbiente getByCodigo(Integer codigo) {

		if (codigo == null) {
			return null;
		}

		for (LocalAmbiente tipo : LocalAmbiente.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Local de Ambiente inválido: " + codigo);
	}

}
