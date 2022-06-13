package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/***
 * 
 * @author erivanio.cruz
 *
 * @param <T>
 */
public abstract class DAO<T> {
  protected static final Logger LOGGER = LogManager.getLogger(DAO.class);

  protected Class<T> modelClass;
  
  public void setmodelClass(Class<T> modelClass) {
    this.modelClass = modelClass;
  }
  
  @PersistenceContext
  private EntityManager entityManager;
  
  public EntityManager getEntityManager() {
    return entityManager;
  }

  public final void salvar(T obj) {
      getEntityManager().persist(obj);      
  }
  
  public final void deletar(T obj) {
      getEntityManager().remove(obj);
  }
  
  public final void deletarById(Long id) {
    Object beanEntity = getEntityManager().find(modelClass, id);
    
    if(beanEntity != null){
      getEntityManager().remove(beanEntity);
    }
  }
  
  public final void update(Long id) {
    Object beanEntity = getEntityManager().find(modelClass, id);
    
    //Call remove method to remove the entity
    if(beanEntity != null){
      getEntityManager().merge(beanEntity);
    }

  }
  
  public T getById(Long id) {
      return getById(id);
  }
  
  @SuppressWarnings("unchecked")
  public List<T> getAll() {
      return getEntityManager().createQuery("from " + 
          modelClass.getName()).getResultList();
  }
  
  public abstract List<T> search(T bean);
  
}