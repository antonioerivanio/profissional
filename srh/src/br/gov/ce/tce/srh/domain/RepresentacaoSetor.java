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

import br.gov.ce.tce.srh.domain.sapjava.Setor;

/**
 * Referente a tabela: TB_REPRESENTACAOSETOR
 * 
 * @since   : Out 19, 2011, 9:01:13 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_REPRESENTACAOSETOR", schema="SRH")
public class RepresentacaoSetor extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

	@ManyToOne
	@JoinColumn(name = "IDSETOR")
    private Setor setor;

    @ManyToOne
    @JoinColumn(name = "IDREPRESENTACAOCARGO")
    private RepresentacaoCargo representacaoCargo;

    @Column(name = "QUANTIDADE", nullable=false)
    private Long quantidade;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO", nullable=false)    
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "FIM")
    private Date fim;

    @Column(name = "ATIVO", nullable=false)
    private boolean ativo;

    @Column(name = "OBSERVACAO", length=100)
    private String observacao;

    @Column(name = "HIERARQUIA", nullable=false)
    private Long hierarquia;


	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public RepresentacaoCargo getRepresentacaoCargo() {return representacaoCargo;}
	public void setRepresentacaoCargo(RepresentacaoCargo representacaoCargo) {this.representacaoCargo = representacaoCargo;}

	public Long getQuantidade() {return quantidade;}
	public void setQuantidade(Long quantidade) {this.quantidade = quantidade;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public boolean isAtivo() {return ativo;}
	public void setAtivo(boolean ativo) {this.ativo = ativo;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}

	public Long getHierarquia() {return hierarquia;}
	public void setHierarquia(Long hierarquia) {this.hierarquia = hierarquia;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}