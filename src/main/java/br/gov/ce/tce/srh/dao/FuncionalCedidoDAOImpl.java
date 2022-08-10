package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.domain.VinculoRGPS;


/**
 * @author robson.castro
 *
 */
@Repository
public class FuncionalCedidoDAOImpl implements FuncionalCedidoDAO {

  static Logger logger = Logger.getLogger(FuncionalCedidoDAOImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void excluir(FuncionalCedido entidade) {    
    FuncionalCedido bean = getById(entidade.getId());
    entityManager.remove(bean);
  }

  private Long getMaxId() {
    Query query = entityManager.createQuery("SELECT MAX(v.id) FROM FuncionalCedido  v");
    return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
  }

  public FuncionalCedido salvar(FuncionalCedido entidade) {

    if (entidade.getId() == null || entidade.getId() == 0) {
      entidade.setId(getMaxId());
    }

    return entityManager.merge(entidade);
  }

  @Override
  public FuncionalCedido getById(Long id) {
    return entityManager.find(FuncionalCedido.class, id);
  }

  @Override
  public List<FuncionalCedido> search(String matricula, int first, int rows) {
    try {
      StringBuffer sql = new StringBuffer();
      sql.append(" SELECT f FROM FuncionalCedido f ");
      sql.append("  WHERE  1=1 ");
      if (matricula != null) {
        sql.append("  and f.matricOrig = :matricula ");
      }
      sql.append("         ORDER BY f.matricOrig DESC ");
      Query query = entityManager.createQuery(sql.toString());

      if (matricula != null) {
        query.setParameter("matricula", matricula);
      }
      query.setFirstResult(first);
      query.setMaxResults(rows);

      return query.getResultList();

    } catch (NoResultException e) {
      return null;
    }
  }

  public int count(String matricula) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT count (f) FROM FuncionalCedido f ");
    sql.append("  WHERE  1=1 ");

    if (matricula != null) {
      sql.append("     and   f.matricOrig = :pessoal ");
    }

    Query query = entityManager.createQuery(sql.toString());

    if (matricula != null) {
      query.setParameter("pessoal", matricula);
    }

    int i = ((Long) query.getSingleResult()).intValue();
    return i;
  }

  @Override
  public FuncionalCedido getByCodigo(Long codigo) {
    try {
      Query query = entityManager.createQuery("Select c from FuncionalCedido c where c.codigo = :codigo");
      query.setParameter("codigo", codigo);
      return (FuncionalCedido) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }


  @Override
  @SuppressWarnings("unchecked")
  public List<FuncionalCedido> findAll() {
    return entityManager.createQuery("SELECT c FROM FuncionalCedido c ORDER BY c.id").getResultList();
  }


  @Override
  @SuppressWarnings("unchecked")
  public List<FuncionalCedido> findByCodigo(Long codigo) {
    Query query = entityManager.createQuery("Select c from FuncionalCedido c where c.nivel = :nivel AND c.codigo like :codigo order by c.codigo");
    query.setParameter("codigo", codigo + "%");
    return query.getResultList();
  }

}
