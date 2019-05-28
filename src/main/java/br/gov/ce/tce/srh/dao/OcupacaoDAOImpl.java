package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Ocupacao;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class OcupacaoDAOImpl implements OcupacaoDAO {

	static Logger logger = Logger.getLogger(OcupacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(o.id) from Ocupacao o ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Ocupacao salvar(Ocupacao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Ocupacao entidade) {
		entityManager.createQuery("DELETE FROM Ocupacao o WHERE o.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String nomenclatura) {
		Query query = entityManager.createQuery("Select count (o) from Ocupacao o where upper( o.nomenclatura ) LIKE :nomenclatura ORDER BY o.id");
		query.setParameter("nomenclatura", "%" + nomenclatura.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	public int count(String nomenclatura, Long situacao) {
		Query query = entityManager.createQuery("Select count (o) from Ocupacao o where upper( o.nomenclatura ) LIKE :nomenclatura and o.situacao = :situacao ORDER BY o.id");
		query.setParameter("nomenclatura", "%" + nomenclatura.toUpperCase() + "%");
		query.setParameter("situacao", situacao);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Ocupacao> search(String nomenclatura, int first, int rows) {
		Query query = entityManager.createQuery("Select o from Ocupacao o join fetch o.escolaridade join fetch o.tipoOcupacao where upper( o.nomenclatura ) LIKE :nomenclatura ORDER BY o.id");
		query.setParameter("nomenclatura", "%" + nomenclatura.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Ocupacao> search(String nomenclatura, Long situacao, int first, int rows) {
		Query query = entityManager.createQuery("Select o from Ocupacao o join fetch o.escolaridade join fetch o.tipoOcupacao where upper( o.nomenclatura ) LIKE :nomenclatura and o.situacao = :situacao ORDER BY o.id");
		query.setParameter("nomenclatura", "%" + nomenclatura.toUpperCase() + "%");
		query.setParameter("situacao", situacao);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	
	@Override
	public Ocupacao getById(Long id) {
		return entityManager.find(Ocupacao.class, id);
	}


	@Override
	public Ocupacao getByNomenclatura(String nomenclatura) {
		try {
			Query query = entityManager.createQuery("Select o from Ocupacao o join fetch o.escolaridade join fetch o.tipoOcupacao where upper( o.nomenclatura ) = :nomenclatura ");
			query.setParameter("nomenclatura", nomenclatura.toUpperCase() );
			return (Ocupacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Ocupacao> findByTipoOcupacao(Long tipoOcupacao) {
		Query query = entityManager.createQuery("Select distinct o from Ocupacao o join fetch o.escolaridade join fetch o.tipoOcupacao where o.tipoOcupacao.id=:tipoOcupacao ORDER BY o.nomenclatura asc");
		query.setParameter("tipoOcupacao", tipoOcupacao);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Ocupacao> findAll() {
		return entityManager.createQuery("SELECT o FROM Ocupacao o join fetch o.escolaridade join fetch o.tipoOcupacao ORDER BY o.nomenclatura asc").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ocupacao> findByPessoa(Long idPessoal) {
		Query query = entityManager.createQuery("Select o from Funcional f join f.ocupacao o where f.pessoal.id = :idPessoal ORDER BY o.nomenclatura asc");
		query.setParameter("idPessoal", idPessoal);
		return query.getResultList();
	}
}
