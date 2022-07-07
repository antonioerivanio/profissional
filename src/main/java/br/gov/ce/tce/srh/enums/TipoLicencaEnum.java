package br.gov.ce.tce.srh.enums;

import java.util.ArrayList;

/**
 * Tipo de licença permitidas para o cadastro do afastamento
 * @author erivanio.cruz
 * @since 13/04/2022
 *
 */
public enum TipoLicencaEnum {	

	LICENCA_INTERESSE_PARTICULAR(2, "Interesse Particular"),	
	LICENCA_SAUDE(3, "Licença Saúde"),
	LICENCA_GESTANTE(4, "Licença Gestante"),
	LICENCA_ESPECIAL(5, "Licença Especial  Artigo 105, § 3º, da Lei 9826/74."),
	LICENCA_DOENCA_PESSOA_DA_FAMILIA_SERVIDOR_COM_REMONERACAO(6, "Licença Doença Pessoa da Família(servidores/com remuneração)   Arts. 80, nº III, e 99 da Lei nº 9.826/74."),
	LICENCA_INCENTIVO_FORMAÇÃO_PROFISSIONAL(8, "Licença de Incentivo à Formação Profissional  Art. 113 da Lei 9.826/74"),
	LICENCA_PARA_ACOMPANHAMENTO_CONJUGE (9, "Licença para Acompanhar o Cônjuge  Art. 103 da Lei 9.826/74"),	
	LICENCA_PATERNIDADE (10, "Licença Paternidade ADCT 10 § 1º e Lei Federal 13.257/2016. Resolução Administrativa 02/2018-TCE (DOTCE 24/04/2018)"),	
	ATESTADO_MEDICO (16, "Atestado médico - Servidor comissionado	CLT"),
	AFASTAMENTO_INTEGRAL_POS_GRADUACAO (17, "Afastamento Integral Pós-Graduação Resolução Administrativa 06/2019 DOTCE 26/07/2019"),
	LICENCA_DOENCA_PESSOA_DA_FAMILIA_SERVIDOR_SEM_REMONERACAO (19, "Licença Doença Pessoa da Família(servidores/sem remuneração)	Art. 99 §3º da Lei 9826/74"),
	LICENCA_DOENCA_PESSOA_DA_FAMILIA_MEMBRO_COM_REMONERACAO (20, "Licença Doença Pessoa da Família(Membro/com remuneração)	art. 69, II, da LC nº 35/79, c/c art. 268,  da Lei nº 12.342/94."),
	LICENCA_DOENCA_PESSOA_DA_FAMILIA_MEMBRO_SEM_REMONERACAO (21, "Licença Doença Pessoa da Família(Membro/sem remuneração)	art. 261, da Lei nº 12.342/94."),
	RECESSO_ESTAGIARIO (22, "Recesso estagiário	Lei Federal nº 11.788/2008");
	
	
	private Integer codigo;
	private String descricao;

	private TipoLicencaEnum(Integer codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public static TipoLicencaEnum getByCodigo(Integer codigo) {
		
		if (codigo == null) {
			return null;
		}
		
		for (TipoLicencaEnum parametro : values()) {
			if (parametro.getCodigo().equals(codigo)) {
				return parametro;
			}
		}
		throw new IllegalArgumentException("Código Tipo de Licença inválido");
	}
	
	public static ArrayList<Integer> getTodosCodigos() {
		ArrayList<Integer> codigos = new ArrayList<Integer>();
		for (TipoLicencaEnum parametro : values()) {			
				codigos.add(parametro. getCodigo());
		}
		
		return codigos;
	}
}
