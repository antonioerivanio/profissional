package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CursoProfissional;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class CursoProfissionalDAOImpl implements CursoProfissionalDAO {

	static Logger logger = Logger.getLogger(CursoProfissionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CursoProfissional getById(Long id) {
		return entityManager.find(CursoProfissional.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(c.id) FROM CursoProfissional c");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public CursoProfissional salvar(CursoProfissional entidade) {

		//entidade.setPessoal(1l);
		
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(CursoProfissional entidade) {
		Query query = entityManager.createQuery("DELETE FROM CursoProfissional c WHERE c.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("SELECT count (c) FROM CursoProfissional c WHERE upper( c.descricao ) LIKE :descricao ");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	public int count(Long area, String descricao) {
		Query query = entityManager.createQuery("SELECT count (c) FROM CursoProfissional c WHERE c.area.id = :area AND upper( c.descricao ) LIKE :descricao ");
		query.setParameter("area", area);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT c FROM CursoProfissional c WHERE upper( c.descricao ) LIKE :descricao ORDER BY c.area.descricao, c.descricao ");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		
		if (first >= 0 && rows >= 0) {
			query.setFirstResult(first);
			query.setMaxResults(rows);
		}
			
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> search(Long area, String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT c FROM CursoProfissional c WHERE c.area.id = :area AND upper( c.descricao ) LIKE :descricao ORDER BY c.descricao ");
		query.setParameter("area", area);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
				
		if (first >= 0 && rows >= 0) {
			query.setFirstResult(first);
			query.setMaxResults(rows);
		}	
		
		return query.getResultList();
	}


	@Override
	public CursoProfissional getByCursoAreaInstituicao(String curso, Long area, Long instituicao) {
		try {
			Query query = entityManager.createQuery("SELECT c FROM CursoProfissional c join fetch c.area join fetch c.instituicao WHERE upper( c.descricao ) = :descricao AND c.area.id = :area AND c.instituicao.id = :instituicao");
			query.setParameter("descricao", curso.toUpperCase() );
			query.setParameter("area", area);
			query.setParameter("instituicao", instituicao);
			return (CursoProfissional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> findByArea(Long area) {
		Query query = entityManager.createQuery("SELECT c FROM CursoProfissional c join fetch c.area  WHERE c.area.id = :area ORDER BY c.descricao ");
		query.setParameter("area", area);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> findAll() {
		return entityManager.createQuery("SELECT c FROM CursoProfissional c join fetch c.area join fetch c.instituicao ORDER BY c.area.descricao, c.descricao ").getResultList();
	}

}
