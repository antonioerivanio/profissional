package br.gov.ce.tce.srh.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_FUNCIONALAREASETOR
 * 
 * @since   : Dez 2, 2011, 15:14:08 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@Table(name = "TB_FUNCIONALAREASETOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class FuncionalAreaSetor {

	@EmbeddedId
	protected FuncionalAreaSetorPk pk;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDFUNCIONAL", insertable = false, updatable = false)
	private Funcional funcional;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDAREASETOR", insertable = false, updatable = false)
	private AreaSetor areaSetor;


	public FuncionalAreaSetorPk getPk() {return pk;}
	public void setPk(FuncionalAreaSetorPk pk) {this.pk = pk;}

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public AreaSetor getAreaSetor() {return areaSetor;}
	public void setAreaSetor(AreaSetor areaSetor) {this.areaSetor = areaSetor;}

}
