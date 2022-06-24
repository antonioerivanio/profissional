package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.BeanEntidade;
import br.gov.ce.tce.srh.sapjava.domain.Entidade;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class AuxilioSaudeRequisicaoDAO {

  @PersistenceContext
  private EntityManager entityManager;

  String NOME = "nome";
  String ID = "id";
  String CPF = "cpf";

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public final void salvar(BeanEntidade obj) {
    if (obj instanceof AuxilioSaudeRequisicao) {
      getEntityManager().persist(obj);
    }

    if (obj instanceof AuxilioSaudeRequisicaoDependente) {
      getEntityManager().persist(obj);
    }

    if (obj instanceof AuxilioSaudeRequisicaoDocumento) {
      getEntityManager().persist(obj);
    }
  }


  public final void atualizar(BeanEntidade obj) {
    if (obj instanceof AuxilioSaudeRequisicao) {
      getEntityManager().merge(obj);
    }

    if (obj instanceof AuxilioSaudeRequisicaoDependente) {
      getEntityManager().merge(obj);
    }
    if (obj instanceof AuxilioSaudeRequisicaoDocumento) {
      getEntityManager().merge(obj);
    }
  }

  public final BeanEntidade find(BeanEntidade obj) {

    if (obj instanceof AuxilioSaudeRequisicao) {
      return getEntityManager().find(AuxilioSaudeRequisicao.class, (((AuxilioSaudeRequisicao) obj).getId()));
    }

    else if (obj instanceof AuxilioSaudeRequisicaoDependente) {
      return (BeanEntidade) getEntityManager().find(AuxilioSaudeRequisicaoDependente.class, (((AuxilioSaudeRequisicaoDependente) obj).getId()));
    }

    else if (obj instanceof AuxilioSaudeRequisicaoDocumento) {
      return (BeanEntidade) getEntityManager().find(AuxilioSaudeRequisicaoDocumento.class, (((AuxilioSaudeRequisicaoDocumento) obj).getId()));
    }

    return obj;
  }

  public final AuxilioSaudeRequisicaoDependente find(AuxilioSaudeRequisicaoDependente obj) {
    return getEntityManager().find(AuxilioSaudeRequisicaoDependente.class, obj.getId());
  }

  public final AuxilioSaudeRequisicaoDocumento find(AuxilioSaudeRequisicaoDocumento obj) {
    return getEntityManager().find(AuxilioSaudeRequisicaoDocumento.class, obj.getId());
  }

  public int count(String nomeParam, String cpfParam) {
    Query query = null;


    if (nomeParam != null && !nomeParam.isEmpty()) {
      query = getQueryCount(NOME, nomeParam);
    }

    else if (cpfParam != null && !cpfParam.isEmpty()) {
      query = getQueryCountPorCdf(CPF, cpfParam);
    }

    else if (nomeParam != null && !nomeParam.isEmpty() && cpfParam != null && !cpfParam.isEmpty()) {
      query = getQueryPorNomeECpf(NOME, nomeParam, CPF, cpfParam);
    } else {
      query = getQueryCountAll();
    }

    long countResult = (long) query.getSingleResult();

    return (int) countResult;
  }

  @SuppressWarnings("unchecked")
  public List<AuxilioSaudeRequisicao> search(String nomeParam, String cpfParam, Integer first, Integer rows) {
    Query query = null;

    if (nomeParam != null && !nomeParam.isEmpty()) {
      query = getQueryPorNome(NOME, nomeParam);
    }

    else if (cpfParam != null && !cpfParam.isEmpty()) {
      query = getQueryPorCdf(CPF, cpfParam);
    } else {
      query = getQueryAll();
    }

    if (first != null && first >= 0)
      query.setFirstResult(first);
    if (rows != null && rows > 0)
      query.setMaxResults(rows);

    return query.getResultList();
  }

  private Query getQueryAll() {
    return entityManager.createQuery("select asr from AuxilioSaudeRequisicao asr ");
  }

  private Query getQueryCountAll() {
    Query query = entityManager.createQuery(" select count(asr) from AuxilioSaudeRequisicao asr ");
    return query;
  }

  private Query getQueryCount(String NOME, String nomeparam) {
    Query query = entityManager.createQuery(" select count(asr) from AuxilioSaudeRequisicao asr where upper(asr.funcional.nome) like :nome ");
    query.setParameter(NOME, "%" + nomeparam.toUpperCase() + "%");
    return query;
  }

  private Query getQueryPorNome(String NOME, String nomeparam) {
    Query query = entityManager.createQuery("select asr from AuxilioSaudeRequisicao asr where upper(asr.funcional.nome) like :nome ", AuxilioSaudeRequisicao.class);
    query.setParameter(NOME, "%" + nomeparam.toUpperCase() + "%");

    return query;
  }


  private Query getQueryCountPorCdf(String CPF, String cpfparam) {
    Query query = entityManager.createQuery("select count(asr) from AuxilioSaudeRequisicao asr where asr.funcional.pessoal.cpf =:cpf ");
    query.setParameter(CPF, SRHUtils.removerMascara(cpfparam));
    return query;
  }

  private Query getQueryPorCdf(String CPF, String cpfparam) {
    Query query = entityManager.createQuery("select asr from AuxilioSaudeRequisicao asr where asr.funcional.pessoal.cpf =:cpf ");
    query.setParameter(CPF, SRHUtils.removerMascara(cpfparam));
    return query;
  }

  private Query getQueryPorNomeECpf(String NOME, String nomeParam, String CPF, String cpfParam) {
    Query query = entityManager.createQuery("from AuxilioSaudeRequisicao asr where pper(asr.funcional.nome) like :nome and asr.funcional.cpf=:cpf", AuxilioSaudeRequisicao.class);
    query.setParameter(NOME, "%" + nomeParam.toUpperCase() + "%");
    query.setParameter(CPF, SRHUtils.removerMascara(cpfParam));
    return query;
  }

  public List<AuxilioSaudeRequisicaoDocumento> getListaAnexos(BeanEntidade beanEntidade) {
    Query query = entityManager.createQuery("from AuxilioSaudeRequisicaoDocumento asd where asd.auxilioSaudeRequisicao.id =:id ", AuxilioSaudeRequisicaoDocumento.class);
    query.setParameter(ID, (((AuxilioSaudeRequisicao) beanEntidade)).getId());
    return query.getResultList();
  }
}
