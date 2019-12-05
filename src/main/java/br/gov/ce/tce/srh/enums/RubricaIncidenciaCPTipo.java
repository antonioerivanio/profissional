package br.gov.ce.tce.srh.enums;

import java.util.List;

public enum RubricaIncidenciaCPTipo {
	
	NAO_EH_BASE ("Código de incidência tributária da rubrica para a Previdência Social"),
	BASE ("Base de cálculo das contribuições sociais - Salário de Contribuição"),
	CONTRIBUICAO ("Contribuição descontada do Segurado sobre salário de contribuição"),
	OUTROS ("Outros"),
	SUSPENSAO ("Suspensão de incidência sobre Salário de Contribuição em decorrência de decisão judicial");
	
	private String descricao;
	
	private RubricaIncidenciaCPTipo (String descricao) {
		this.descricao = descricao;	
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public List<RubricaIncidenciaCPCodigo> getCodigos(){			
		return RubricaIncidenciaCPCodigo.getByTipo(this);
	}

}
