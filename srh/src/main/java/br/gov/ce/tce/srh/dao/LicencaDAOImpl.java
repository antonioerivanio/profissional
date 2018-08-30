package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.TipoLicenca;

/**
 * 
 * @author robstown
 *
 */
@Repository
public class LicencaDAOImpl implements LicencaDAO {

	static Logger logger = Logger.getLogger(LicencaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public Licenca getById(Long id) {
		return entityManager.find(Licenca.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(l.id) from Licenca l ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Licenca salvar(Licenca entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Licenca entidade) {
//		Query query = entityManager.createQuery("DELETE FROM Licenca l WHERE l.id=:id");
//		query.setParameter("id", entidade.getId());
//		query.executeUpdate();
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	public int count(Long idPessoa) {
		Query query = entityManager.createQuery("Select count (l) as total from Licenca l where l.pessoal.id = :pessoal order by l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public int count(Long idPessoa, Long tipoLicenca) {
		Query query = entityManager.createQuery("SELECT count (l) as total FROM Licenca l WHERE l.pessoal.id = :pessoal AND l.tipoLicenca.id = :tipo  order by l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		query.setParameter("tipo", tipoLicenca);
		return ((Long) query.getSingleResult()).intValue();
	}	


	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> search(Long idPessoa, int first, int rows) {
		Query query = entityManager.createQuery("Select l from Licenca l where l.pessoal.id = :pessoal order by l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> search(Long idPessoa, Long tipoLicenca, int first, int rows) {
		Query query = entityManager.createQuery("SELECT l FROM Licenca l WHERE l.pessoal.id = :pessoal AND l.tipoLicenca.id = :tipo ORDER BY l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		query.setParameter("tipo", tipoLicenca);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> search(String nome, int first, int rows) {
		Query query = entityManager.createQuery("SELECT l FROM Licenca l WHERE l.pessoal.nomePesquisa LIKE :nome AND (l.tipoLicenca.id = 1 OR l.tipoLicenca.id = 2) ORDER BY l.pessoal.nome, l.inicio desc ");
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> search(String nome, TipoLicenca tipoLicenca, int first, int rows) {
		Query query = entityManager.createQuery("SELECT l FROM Licenca l WHERE l.pessoal.nomePesquisa LIKE :nome AND l.tipoLicenca.id = :tipo ORDER BY l.pessoal.nome, l.inicio desc ");
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		query.setParameter("tipo", tipoLicenca.getId());
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> findByPessoa(Long idPessoa) {
		Query query = entityManager.createQuery("Select l from Licenca l where l.pessoal.id = :pessoal order by l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> findByPessoaLicencaEspecial(Long idPessoa) {
		Query query = entityManager.createQuery("Select l from Licenca l where l.pessoal.id = :pessoal and l.licencaEspecial.id is not null and l.excluitemposerv = 0 and l.licencaEspecial.contaremdobro > 0 ORDER BY l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Licenca> findByPessoaLicencaExcluidaTempoServico(Long idPessoa) {
		Query query = entityManager.createQuery("Select l from Licenca l where l.pessoal.id = :pessoal and l.excluitemposerv = 1 ORDER BY l.inicio desc ");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}
}

