package br.gov.ce.tce.srh.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum das leis de incorporacao
 * 
 * @since   : Dez 12, 2011, 09:32:29 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
public enum LeiIncorporacao {

	LEI_INCOPORACAO_01("11.171/1986"),
	LEI_INCOPORACAO_02("11.847/1991");

	private String descricao;
	
	LeiIncorporacao(String descricao){
		this.descricao = descricao;
	}
	
	public static List<LeiIncorporacao> findAll(){
		List<LeiIncorporacao> entidades = new ArrayList<LeiIncorporacao>();
		
		for (LeiIncorporacao entidade : LeiIncorporacao.values()) {
			entidades.add(entidade);
		}
		return entidades;
	}
	
	public static LeiIncorporacao getByDescricao(String descricao){
		for (LeiIncorporacao leiIncorporacao : LeiIncorporacao.values()) {
			if(leiIncorporacao.getDescricao().equals(descricao)){
				return leiIncorporacao;
			}
		}
		return null;
	}
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

}
