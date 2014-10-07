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
 * Referente a tabela: TB_REPRESENTACAOVALOR
 * 
 * @since   : Out 20, 2011, 10:11:33 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_REPRESENTACAOVALOR", schema="SRH")
public class RepresentacaoValor extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID", nullable=false)
	private Long id;

    @ManyToOne
	@JoinColumn(name = "IDREPRESENTACAOCARGO")
    private RepresentacaoCargo representacaoCargo;

    @Column(name = "VALORVENCIMENTO", nullable=false)
    private Long valorVencimento;

    @Column(name = "VALORREPRESENTACAO", nullable=false)
    private Long valorRepresentacao;

    @Column(name = "VALORDEDICACAOEXCLUSIVA", nullable=false)
    private Long valorDedicacaoExclusiva;

    @Column(name = "OBSERVACAO", length=100)
    private String observacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIOVIGENCIA", nullable=false)
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "FIMVIGENCIA")
    private Date fim;


	public RepresentacaoCargo getRepresentacaoCargo() {return representacaoCargo;}
	public void setRepresentacaoCargo(RepresentacaoCargo representacaoCargo) {this.representacaoCargo = representacaoCargo;}

	public Long getValorVencimento() {return valorVencimento;}
	public void setValorVencimento(Long valorVencimento) {this.valorVencimento = valorVencimento;}

	public Long getValorRepresentacao() {return valorRepresentacao;}
	public void setValorRepresentacao(Long valorRepresentacao) {this.valorRepresentacao = valorRepresentacao;}

	public Long getValorDedicacaoExclusiva() {return valorDedicacaoExclusiva;}
	public void setValorDedicacaoExclusiva(Long valorDedicacaoExclusiva) {this.valorDedicacaoExclusiva = valorDedicacaoExclusiva;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}