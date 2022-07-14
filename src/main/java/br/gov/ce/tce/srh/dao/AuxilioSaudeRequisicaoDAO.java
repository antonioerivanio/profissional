package br.gov.ce.tce.srh.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoBase;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoBase.FlagAtivo;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.BeanEntidade;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class AuxilioSaudeRequisicaoDAO {

  @PersistenceContext
  private EntityManager entityManager;


  private static final String NOME = "nome";
  private static final String ID = "id";
  private static final String IDFUNCIONAL = "idFuncional";
  private static final String CPF = "cpf";
  private static final String DATA_INICIO = "dataInicioRequisicao";
  private static final String DATA_FIM = "dataFimRequisicao";
  private static final String FLG_ATIVO = "flgAtivo";
  private static final String FLG_DELETADO = "flgDeletado";
  

  public static final Double TRES_POR_CENTO = 0.03;
  public static final Double TRES_PONTO_CINCO_POR_CENTO = 0.035;
  public static final Double QUATRO_POR_CENTO = 0.04;
  public static final Double QUATRO_PONTO_CINCO_POR_CENTO = 0.045;
  public static final Double CINCO_POR_CENTO = 0.05;

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public final void salvar(BeanEntidade obj) {
    getEntityManager().persist(obj);
  }

  public final void atualizar(BeanEntidade obj) {
    getEntityManager().merge(obj);
  }

  public final BeanEntidade find(BeanEntidade obj) {
    if (obj instanceof AuxilioSaudeRequisicao) {
      return getEntityManager().find(AuxilioSaudeRequisicao.class, (((AuxilioSaudeRequisicao) obj).getId()));
    } else if (obj instanceof AuxilioSaudeRequisicaoDependente) {
      return getEntityManager().find(AuxilioSaudeRequisicaoDependente.class, (((AuxilioSaudeRequisicaoDependente) obj).getId()));
    } else if (obj instanceof AuxilioSaudeRequisicaoDocumento) {
      return getEntityManager().find(AuxilioSaudeRequisicaoDocumento.class, (((AuxilioSaudeRequisicaoDocumento) obj).getId()));
    } else if (obj instanceof AuxilioSaudeRequisicaoBase) {
      return getEntityManager().find(AuxilioSaudeRequisicaoBase.class, (((AuxilioSaudeRequisicaoBase) obj).getId()));
    }

    return obj;
  }

  public final AuxilioSaudeRequisicaoDependente find(AuxilioSaudeRequisicaoDependente obj) {
    return getEntityManager().find(AuxilioSaudeRequisicaoDependente.class, obj.getId());
  }

  public final AuxilioSaudeRequisicaoDocumento find(AuxilioSaudeRequisicaoDocumento obj) {
    return getEntityManager().find(AuxilioSaudeRequisicaoDocumento.class, obj.getId());
  }

  
  public int count(AuxilioSaudeRequisicao bean) {
    Query query = null;
    StringBuilder sql = new StringBuilder();
    sql.append("select count(asr) from AuxilioSaudeRequisicao asr where 1=1 ");
    
    setFiltrosWhere(sql, bean);    
    query = entityManager.createQuery(sql.toString());
    setParametroQuery(query, bean);
    
    long countResult = (long) query.getSingleResult();

    return (int) countResult;
  }
  
  private void setFiltrosWhere(StringBuilder sql, AuxilioSaudeRequisicao bean) {
    if (isNomeNotNull(bean)) {
      sql.append(" and upper(asr.funcional.nome) like :nome ");
    } else if (isCpfNotNull(bean)) {
      sql.append(" and asr.funcional.pessoal.cpf =:cpf ");
    } else if (isNomeNotNull(bean) && isCpfNotNull(bean)) {
      sql.append("and upper(asr.funcional.nome) like :nome and asr.funcional.cpf=:cpf");
    } else if (isDataInicioNotNull(bean)) {
      sql.append("and asr.dataInicioRequisicao= :dataInicioRequisicao ");
    } else if (isDataFimNotNull(bean)) {
      sql.append("and asr.dataFimRequisicao= :dataFimRequisicao ");
    } else if (isDataInicioNotNull(bean) && isDataFimNotNull(bean)) {      
        sql.append("and asr.dataInicioRequisicao= :dataInicioRequisicao or asr.dataFimRequisicao= :dataFimRequisicao ");        
    }
    
    sql.append("and asr.isDeletado = 0 ");
  }


  private void setParametroQuery(Query query, AuxilioSaudeRequisicao bean) {    
    if (isNomeNotNull(bean)) {
      query.setParameter(NOME, "%" + bean.getFuncional().getPessoal().getNome() + "%");
    }
    else if (isCpfNotNull(bean)) {
      query.setParameter(CPF, SRHUtils.removerMascara(bean.getFuncional().getPessoal().getCpf()));
    } else if (isNomeNotNull(bean) && isCpfNotNull(bean)) {
      query.setParameter(NOME, "%" + bean.getFuncional().getPessoal().getNomeCompleto() + "%");
      query.setParameter(CPF, SRHUtils.removerMascara(bean.getFuncional().getPessoal().getCpf()));
    }

    else if (isDataInicioNotNull(bean)) {
      query.setParameter(DATA_INICIO, bean.getDataInicioRequisicao(), TemporalType.DATE);
    }
    else if (isDataFimNotNull(bean)) {
      query.setParameter(DATA_FIM, bean.getDataFimRequisicao(), TemporalType.DATE);
    }
    
    else if (isDataInicioNotNull(bean) && isDataFimNotNull(bean)) {
      query.setParameter(DATA_INICIO, bean.getDataInicioRequisicao(), TemporalType.DATE);
      query.setParameter(DATA_FIM, bean.getDataFimRequisicao(), TemporalType.DATE);
    }
  }

  @SuppressWarnings("unchecked")
  public List<AuxilioSaudeRequisicao> search(AuxilioSaudeRequisicao bean, Integer first, Integer rows) {
    Query query = null;
    StringBuilder sql = new StringBuilder();
    sql.append("from AuxilioSaudeRequisicao asr where 1=1 ");
    
    setFiltrosWhere(sql, bean);    
    query = entityManager.createQuery(sql.toString());
    setParametroQuery(query, bean);
    
    if (first != null && first >= 0)
      query.setFirstResult(first);
    if (rows != null && rows > 0)
      query.setMaxResults(rows);

    return query.getResultList();
  }

  public List<AuxilioSaudeRequisicaoDocumento> getListaAnexos(BeanEntidade beanEntidade) {
    Query query = null;
    if(beanEntidade instanceof AuxilioSaudeRequisicao) {
      query = entityManager.createQuery("from AuxilioSaudeRequisicaoDocumento asd where asd.auxilioSaudeRequisicao.id =:id ", AuxilioSaudeRequisicaoDocumento.class);
      query.setParameter(ID, (((AuxilioSaudeRequisicao) beanEntidade)).getId());
    }
    
    if(beanEntidade instanceof AuxilioSaudeRequisicaoDependente) {
      query = entityManager.createQuery("from AuxilioSaudeRequisicaoDocumento asd where asd.auxilioSaudeRequisicaoDependente.id =:id ", AuxilioSaudeRequisicaoDocumento.class);
      query.setParameter(ID, (((AuxilioSaudeRequisicaoDependente) beanEntidade)).getId());
    }
    
    return query.getResultList();
  }

  public BigDecimal getValorSalarioComBaseIdadePorPercentual(Double percentual) {
    if (percentual.equals(TRES_POR_CENTO)) {
      return getSalarioColaboradorComIdadeAte30Anos();
    }
    if (percentual.equals(TRES_PONTO_CINCO_POR_CENTO)) {
      return getSalarioColaboradorComIdade30a40Anos();
    }
    if (percentual.equals(QUATRO_POR_CENTO)) {
      return getSalarioColaboradorComIdade41a50Anos();
    }
    if (percentual.equals(QUATRO_PONTO_CINCO_POR_CENTO)) {
      return getSalarioColaboradorComIdade51a60Anos();
    }
    if (percentual.equals(CINCO_POR_CENTO)) {
      return getSalarioColaboradorComIdadeAcima60Anos();
    }

    return BigDecimal.ZERO;
  }

  private BigDecimal getSalarioColaboradorComIdadeAte30Anos() {
    Query query = entityManager.createNativeQuery("select 0.03 * (select max(ACE23) from fp_vencimentoscargos where ace23 is not null) valor_por_percentual from dual");
    return (BigDecimal) query.getSingleResult();
  }

  private BigDecimal getSalarioColaboradorComIdade30a40Anos() {
    Query query = entityManager.createNativeQuery(" select 0.35 * (select max(ACE23) from fp_vencimentoscargos where ace23 is not null) valor_por_percentual from dual");
    return (BigDecimal) query.getSingleResult();
  }

  private BigDecimal getSalarioColaboradorComIdade41a50Anos() {
    Query query = entityManager.createNativeQuery("select 0.04 * (select max(ACE23) from fp_vencimentoscargos where ace23 is not null) valor_por_percentual from dual");
    return (BigDecimal) query.getSingleResult();
  }

  private BigDecimal getSalarioColaboradorComIdade51a60Anos() {
    Query query = entityManager.createNativeQuery("select 0.045 * (select max(ACE23) from fp_vencimentoscargos where ace23 is not null) valor_por_percentual from dual");
    return (BigDecimal) query.getSingleResult();
  }

  private BigDecimal getSalarioColaboradorComIdadeAcima60Anos() {
    Query query = entityManager.createNativeQuery("select 0.05 * (select max(ACE23) from fp_vencimentoscargos where ace23 is not null) from fp_cargos");
    return (BigDecimal) query.getSingleResult();
  }

  private boolean isDataInicioNotNull(AuxilioSaudeRequisicao bean) {
    if (bean.getDataInicioRequisicao() != null)
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  private boolean isDataFimNotNull(AuxilioSaudeRequisicao bean) {
    if (bean.getDataFimRequisicao() != null)
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  private boolean isNomeNotNull(AuxilioSaudeRequisicao bean) {
    if (bean.getFuncional().getPessoal().getNome() != null && !bean.getFuncional().getPessoal().getNome().isEmpty())
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  private boolean isCpfNotNull(AuxilioSaudeRequisicao bean) {
    if (bean.getFuncional().getPessoal().getCpf() != null && !bean.getFuncional().getPessoal().getCpf().isEmpty())
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  /**
   * @autor Antonio Erivanio F. Cruz
   * @param pessoal
   * @param flagAtivo
   * @return AuxilioSaudeRequisicaoBase
   */
  public AuxilioSaudeRequisicaoBase getAuxilioSaudeBasePorPessoaIdeAtivo(Pessoal pessoal, FlagAtivo flagAtivo) {
    try {
      Query query = entityManager.createQuery(" from AuxilioSaudeRequisicaoBase asb where asb.pessoal.id=:id and asb.flgAtivo=:flgAtivo", AuxilioSaudeRequisicaoBase.class);
      query.setParameter(ID, pessoal.getId());
      query.setParameter(FLG_ATIVO, flagAtivo);
      return (AuxilioSaudeRequisicaoBase) query.getSingleResult();
    }catch (Exception e) {
      return null;
    }
  }
  
  public List<AuxilioSaudeRequisicaoDependente> getAuxilioSaudeRequisicaoDependentePorId(Long id){    
      final boolean  flagDeletado = Boolean.TRUE;
      Query query = entityManager.createQuery(" from AuxilioSaudeRequisicaoDependente asd where asd.auxilioSaudeRequisicao.id=:id and asd.flgDeletado != :flgDeletado ", AuxilioSaudeRequisicaoDependente.class);
      query.setParameter(ID, id);  
      query.setParameter(FLG_DELETADO, flagDeletado);
      return query.getResultList();
  }
  
}

