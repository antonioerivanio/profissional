package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.enums.EnumCarreira;

@SuppressWarnings("serial")
@Entity
@Table(name="TB_CARREIRAPESSOAL", schema="SRH")
public class CarreiraPessoal extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDPESSOAL")	
	private Pessoal pessoal;
	
	@Column(name = "IDCARREIRA")
    @Enumerated(EnumType.ORDINAL)
    private EnumCarreira carreira;
	
	@ManyToOne
	@JoinColumn(name = "IDOCUPACAO")	
	private Ocupacao ocupacao;
	
	
	

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

}
