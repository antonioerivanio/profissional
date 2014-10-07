package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Referente a tabela: TB_FUNCIONALAREASETOR
 * 
 * @since   : Dez 2, 2011, 15:30:28 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Embeddable
@SuppressWarnings("serial")
public class FuncionalAreaSetorPk implements Serializable {

    @Column(name = "IDFUNCIONAL", nullable=false)
    private Long funcional;

    @Column(name = "IDAREASETOR", nullable=false)
    private Long areaSetor;

	public Long getFuncional() {return funcional;}
	public void setFuncional(Long funcional) {this.funcional = funcional;}

	public Long getAreaSetor() {return areaSetor;}
	public void setAreaSetor(Long areaSetor) {this.areaSetor = areaSetor;}

}
