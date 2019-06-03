package br.gov.ce.tce.srh.enums;

public enum IndicativoSuspensao {
	
	i01("01", "Liminar em Mandado de Segurança"),
	i02("02", "Depósito Judicial do Montante Integral"),
	i03("03", "Depósito Administrativo do Montante Integral"),
	i04("04", "Antecipação de Tutela"),
	i05("05", "Liminar em Medida Cautelar"),
	i08("08", "Sentença em Mandado de Segurança Favorável ao Contribuinte"),
	i09("09", "Sentença em Ação Ordinária Favorável ao Contribuinte e Confirmada pelo TRF"),
	i10("10", "Acórdão do TRF Favorável ao Contribuinte"),
	i11("11", "Acórdão do STJ em Recurso Especial Favorável ao Contribuinte"),
	i12("12", "Acórdão do STF em Recurso Extraordinário Favorável ao Contribuinte"),
	i13("13", "Sentença 1ª instância não transitada em julgado com efeito suspensivo"),
	i14("14", "Contestação Administrativa FAP"),
	i90("90", "Decisão Definitiva a favor do contribuinte"),
	i92("92", "Sem suspensão da exigibilidade");
	
	private String codigo;
	private String descricao;

	private IndicativoSuspensao(String codigo, String descricao) {
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

	public static IndicativoSuspensao toEnum(String codigo) {

		if (codigo == null) {
			return null;
		}

		for (IndicativoSuspensao tipo : IndicativoSuspensao.values()) {
			if (codigo.equals(tipo.getCodigo())) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Código de Indicativo de Suspensão inválido: " + codigo);
	}

}
