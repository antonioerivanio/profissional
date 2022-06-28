package br.gov.ce.tce.srh.enums;

public enum Ambiente {
	
	DEV("Ambiente de Desenvolvimento"),
	HOM("Ambiente de Homologação"),
	PROD("Ambiente de Produção");
	
	private String descricao;
	
	private Ambiente(String descricao) {
		this.descricao = descricao;
	}

	public boolean isDesenvolvimento() {
		return this.equals(Ambiente.DEV);
	}
	
	public boolean isHomologacao() {
		return this.equals(Ambiente.HOM);
	}
	
	public boolean isProducao() {
		return this.equals(Ambiente.PROD);
	}

	public String getDescricao() {
		return this.descricao;
	}
}
