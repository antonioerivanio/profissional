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
 * Referente a tabela: TB_PUBLICACAO
 * 
 * @since   : Out 20, 2011, 11:19:09 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_PUBLICACAO", schema="SRH")
public class Publicacao extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID", nullable=false)
	private Long id;

    @ManyToOne
	@JoinColumn(name = "IDTIPODOCUMENTO")
    private TipoDocumento tipoDocumento;

	@Column(name = "NUMERO")
	private Long numero;

	@Column(name = "ANO")
	private Long ano;

	@Column(name = "EMENTA", nullable=false, length=255)
	private String ementa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INICIOVIGENCIA", nullable=false)
	private Date vigencia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOE", nullable=false)
	private Date doe;

	@Column(name = "ARQUIVO", length=100)
	private String arquivo;

	@Column(name = "IDTIPOPUBLICACAO")
	private Long tipoPublicacao;


	public Long getNumero() {return numero;}
	public void setNumero(Long numero) {this.numero = numero;}

	public Long getAno() {return ano;}
	public void setAno(Long ano) {this.ano = ano;}

	public String getEmenta() {return ementa;}
	public void setEmenta(String ementa) {this.ementa = ementa;}

	public Date getVigencia() {return vigencia;}
	public void setVigencia(Date vigencia) {this.vigencia = vigencia;}

	public Date getDoe() {return doe;}
	public void setDoe(Date doe) {this.doe = doe;}

	public String getArquivo() {return arquivo;}
	public void setArquivo(String arquivo) {this.arquivo = arquivo;}

	public TipoDocumento getTipoDocumento() {return tipoDocumento;}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {this.tipoDocumento = tipoDocumento;}

	public Long getTipoPublicacao() {return tipoPublicacao;}
	public void setTipoPublicacao(Long tipoPublicacao) {this.tipoPublicacao = tipoPublicacao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}