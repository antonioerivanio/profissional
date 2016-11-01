package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.LicencaEspecial;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class LicencaEspecialDAOImpl implements LicencaEspecialDAO {

	static Logger logger = Logger.getLogger(LicencaEspecialDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(l.id) FROM LicencaEspecial l");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public LicencaEspecial salvar(LicencaEspecial entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(LicencaEspecial entidade) {
		Query query = entityManager.createQuery("DELETE FROM LicencaEspecial l WHERE l.id = :id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public LicencaEspecial getById(Long id) {
		return entityManager.find(LicencaEspecial.class, id);
	}


	@Override
	public int count(Long pessoal) {
		Query query = entityManager.createQuery("Select count (l) from LicencaEspecial l where l.pessoal.id = :pessoal ORDER BY l.anoinicial DESC ");
		query.setParameter("pessoal", pessoal );
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<LicencaEspecial> search(Long pessoal, int first, int rows) {
		Query query = entityManager.createQuery("Select l from LicencaEspecial l where l.pessoal.id = :pessoal ORDER BY l.anoinicial DESC ");
		query.setParameter("pessoal", pessoal );
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public void ajustarSaldo(Long idLicencaEspecial, Long saldodias) {
		Query query = entityManager.createQuery("UPDATE LicencaEspecial SET saldodias = :saldo WHERE id = :id");
		query.setParameter("id", idLicencaEspecial);
		query.setParameter("saldo", saldodias);
		query.executeUpdate();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<LicencaEspecial> findByPessoal(Long pessoal) {
		Query query = entityManager.createQuery("Select l from LicencaEspecial l where l.pessoal.id = :pessoal ORDER BY l.anoinicial DESC ");
		query.setParameter("pessoal", pessoal );
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<LicencaEspecial> findByPessoalComSaldo(Long pessoal) {
		Query query = entityManager.createQuery("Select l from LicencaEspecial l where l.pessoal.id = :pessoal and l.saldodias > 0 ORDER BY l.anoinicial DESC");
		query.setParameter("pessoal", pessoal);
		return query.getResultList();
	}

}
