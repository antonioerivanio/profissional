package br.gov.ce.tce.srh.enums;

public enum EnumTipoCursoProfissional {
	
	EXTENSAO("Curso de extensão"),
	ESPECIALIZACAO("Pós-Graduação a nível de Especialização"),
	MESTRADO("Pós-Graduação a nível de Mestrado"),
	DOUTORADO("Pós-Graduação a nível de Doutorado");
	
	private String descricao;
	
	private EnumTipoCursoProfissional (String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}		
	
}
