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

import br.gov.ce.tce.srh.sca.domain.Usuario;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_GTR", schema=DatabaseMetadata.SCHEMA_SRH)
public class GTR extends BasicEntity<Long> implements Serializable{
	
	@Id
	@Column(name="ID")
	private Long id;
	
    @ManyToOne
    @JoinColumn(name = "IDFUNCIONAL")
    private Funcional funcional;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO")
    private Date inicio;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FIM")
    private Date fim;
    
    @ManyToOne
    @JoinColumn(name = "IDUSUARIOALTERACAO")
    private Usuario usuarioAlteracao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DTALTERACAO")
    private Date dtAlteracao;
    
    
    //Getters e Setters
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	
	

}
