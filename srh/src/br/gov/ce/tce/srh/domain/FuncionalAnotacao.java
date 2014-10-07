package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Referente a tabela: TB_FUNCIONALANOTACAO
 * 
 * @since   : Mar 7, 2012, 16:39:00 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_FUNCIONALANOTACAO", schema="SRH")
public class FuncionalAnotacao extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA" , nullable=false)
	private Date data;

	@Column(name="ANOTACAO" , nullable=false)
	private String anotacao;


	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public Date getData() {return data;}
	public void setData(Date data) {this.data = data;}

	public String getAnotacao() {return anotacao;}
	public void setAnotacao(String anotacao) {this.anotacao = anotacao;}

}
