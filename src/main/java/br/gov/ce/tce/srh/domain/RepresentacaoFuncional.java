package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_REPRESENTACAOFUNCIONAL
 * 
 * @since   : Jan 1, 2012, 10:03:54 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_REPRESENTACAOFUNCIONAL", schema=DatabaseMetadata.SCHEMA_SRH)
public class RepresentacaoFuncional extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID", nullable=false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDFUNCIONAL")
    private Funcional funcional;

	@NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPODOCUMENTO")
    private TipoDocumento tipoDocumento;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO", nullable=false)
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "FIM")
    private Date fim;

    @Column(name = "NUMERODOCUMENTO", length=10)
    private String numerodocumento;

    @Column(name = "CONTATEMPO", nullable=false)
    private boolean contaTempo;

    @Column(name = "ATIVO", nullable=false)
    private boolean ativo;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDREPRESENTACAOCARGO")
    private RepresentacaoCargo representacaoCargo;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDSETOR")
    private Setor setor;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPOMOVIMENTOFIM")
    private TipoMovimento tipoMovimentoFim;

    @Temporal(TemporalType.DATE)
    @Column(name = "DOEINICIO", nullable=false)
    private Date doeInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "DOEFIM")
    private Date doeFim;

    @Column(name = "TIPONOMEACAO", nullable=false)
    private Long tipoNomeacao;

    @NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDTIPOPUBLICACAONOMEACAO")
	private TipoPublicacao tipoPublicacaoNomeacao;
	
    @NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDTIPOPUBLICACAOSAIDA")
	private TipoPublicacao tipoPublicacaoSaida;

	@Transient
	private Integer dias;
	
	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public TipoDocumento getTipoDocumento() {return tipoDocumento;}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {this.tipoDocumento = tipoDocumento;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public String getNumerodocumento() {return numerodocumento;}
	public void setNumerodocumento(String numerodocumento) {this.numerodocumento = numerodocumento;}

	public boolean isContaTempo() {return contaTempo;}
	public void setContaTempo(boolean contaTempo) {this.contaTempo = contaTempo;}

	public boolean isAtivo() {return ativo;}
	public void setAtivo(boolean ativo) {this.ativo = ativo;}

	public RepresentacaoCargo getRepresentacaoCargo() {return representacaoCargo;}
	public void setRepresentacaoCargo(RepresentacaoCargo representacaoCargo) {this.representacaoCargo = representacaoCargo;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public TipoMovimento getTipoMovimentoFim() {return tipoMovimentoFim;}
	public void setTipoMovimentoFim(TipoMovimento tipoMovimentoFim) {this.tipoMovimentoFim = tipoMovimentoFim;}

	public Date getDoeInicio() {return doeInicio;}
	public void setDoeInicio(Date doeInicio) {this.doeInicio = doeInicio;}

	public Date getDoeFim() {return doeFim;}
	public void setDoeFim(Date doeFim) {this.doeFim = doeFim;}

	public Long getTipoNomeacao() {return tipoNomeacao;}
	public void setTipoNomeacao(Long tipoNomeacao) {this.tipoNomeacao = tipoNomeacao;}

	public TipoPublicacao getTipoPublicacaoNomeacao() {return tipoPublicacaoNomeacao;}
	public void setTipoPublicacaoNomeacao(TipoPublicacao tipoPublicacaoNomeacao) {this.tipoPublicacaoNomeacao = tipoPublicacaoNomeacao;}

	public TipoPublicacao getTipoPublicacaoSaida() {return tipoPublicacaoSaida;}
	public void setTipoPublicacaoSaida(TipoPublicacao tipoPublicacaoSaida) {this.tipoPublicacaoSaida = tipoPublicacaoSaida;}

	public Integer getDias() {
		Date dfim = (getFim()==null?new Date():getFim());		
		Date dini = getInicio();
		dias = SRHUtils.dataDiff(dini, dfim);
		return dias;
	}
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
