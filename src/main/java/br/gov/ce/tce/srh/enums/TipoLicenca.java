package br.gov.ce.tce.srh.enums;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tipo de licença permitidas para o cadastro do afastamento
 * @author erivanio.cruz
 * @since 13/04/2022
 *
 */
public enum TipoLicenca {

	LICENCA_SEM_REMUNERACAO_PARA_SERVIDOR_ESTATUTARIO(1, "Licença Especial"),
	LICENCA_SAUDE(3, "Licença Saúde"),
	LICENCA_GESTANTE(4, "Licença Gestante"),
	LICENCA_PARA_ACOMPANHAMENTO_MEMBRO_DA_FAMILIA_ENFERMO (6, "Licença por Motivo de Doença em Pessoa da Família"),	
	LICENCA_PATERNIDADE (10, "Licença Paternidade");	
	
	
	private Integer codigo;
	private String descricao;

	private TipoLicenca(Integer codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static TipoLicenca getByCodigo(Integer codigo) {
		
		if (codigo == null) {
			return null;
		}
		
		for (TipoLicenca parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Tipo de Licença inválido");
	}
	
	public static ArrayList<Integer> getTodosCodigos() {
		ArrayList<Integer> codigos = new ArrayList<Integer>();
		for (TipoLicenca parametro : values()) {			
				codigos.add(parametro. getCodigo());
		}
		
		return codigos;
	}
}
