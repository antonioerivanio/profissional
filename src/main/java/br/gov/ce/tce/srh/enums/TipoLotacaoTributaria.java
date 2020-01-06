package br.gov.ce.tce.srh.enums;

public enum TipoLotacaoTributaria {
	
	TLT01("01", "Classificação da atividade econômica exercida pela Pessoa Jurídica para fins de atribuição de código FPAS, inclusive "
			+ "obras de construção civil própria, exceto: a) empreitada parcial ou sub-empreitada de obra de construção civil (utilizar opção 02); "
			+ "b) prestação de serviços em instalações de terceiros (utilizar opções 03 a 09); "
			+ "c) Embarcação inscrita no Registro Especial Brasileiro - REB (utilizar opção 10)", "Classificação da atividade econômica exercida pela Pessoa Jurídica para fins de atribuição de código FPAS"),	
	TLT04("04", "Pessoa Jurídica Tomadora de Serviços prestados mediante cessão de mão de obra, exceto contratante de cooperativa, nos termos da lei 8.212/1991",
			"Pessoa Jurídica Tomadora de Serviços prestados mediante cessão de mão de obra");
	
	private String descricao;
	private String simplificado;
	private String codigo;

	private TipoLotacaoTributaria (String codigo, String descricao, String simplificado) {
		this.descricao = descricao;
		this.codigo = codigo;
		this.simplificado = simplificado;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public String getSimplificado() {
		return simplificado;
	}

	public void setSimplificado(String simplificado) {
		this.simplificado = simplificado;
	}

	public String getCodigo() {
		return codigo;
	}

	public static TipoLotacaoTributaria getByCodigo(String codigo) {

		if (codigo == null) {
			return null;
		}
		
		for (TipoLotacaoTributaria parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Tipo de lotação de tributária inválido");
	}

}
