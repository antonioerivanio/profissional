package br.gov.ce.tce.srh.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.Parametro;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class ParametroDAOImpl implements ParametroDAO {

  static Logger logger = Logger.getLogger(ParametroDAOImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  @Override
  public Parametro getByNome(String nome) {
    try {
      Query query = entityManager.createQuery("Select e from Parametro e where upper( e.nome ) = :nome ");
      query.setParameter("nome", nome.toUpperCase());
      return (Parametro) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
