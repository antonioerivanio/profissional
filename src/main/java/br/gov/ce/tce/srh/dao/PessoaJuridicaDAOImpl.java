package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.PessoaJuridica;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since : Dez 08, 2021, 09:24:00 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */

@Repository
public class PessoaJuridicaDAOImpl implements PessoaJuridicaDAO {

	static Logger logger = Logger.getLogger(PessoaJuridicaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from PessoaJuridica e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Override
	public PessoaJuridica salvar(PessoaJuridica entidade) {
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Override
	public void excluir(PessoaJuridica entidade) {
		entityManager.createQuery("DELETE FROM PessoaJuridica e WHERE e.id=:id").setParameter("id", entidade.getId())
				.executeUpdate();
	}

	@Override
	public int count(String cnpj) {
		Query query = entityManager
				.createQuery("Select count (e) from PessoaJuridica e where e.cnpj LIKE :cnpj ORDER BY e.razaoSocial");
		query.setParameter("cnpj", cnpj);
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PessoaJuridica> search(String cnpj, int first, int rows) {
		Query query = entityManager
				.createQuery("Select e from PessoaJuridica e where e.cnpj LIKE :cnpj ORDER BY e.razaoSocial");
		query.setParameter("cnpj", cnpj);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public PessoaJuridica getBycnpj(String cnpj) {
		try {
			Query query = entityManager.createQuery("Select e from PessoaJuridica e where e.cnpj = :cnpj ");
			query.setParameter("cnpj", cnpj);
			return (PessoaJuridica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public PessoaJuridica getByrazaoSocial(String razaoSocial) {
		try {
			Query query = entityManager.createQuery("Select e from PessoaJuridica e where e.razaoSocial = :razaoSocial ");
			query.setParameter("razaoSocial", razaoSocial.toUpperCase());
			return (PessoaJuridica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PessoaJuridica> findAll() {
		return entityManager.createQuery("SELECT e FROM PessoaJuridica e ORDER BY e.id").getResultList();
	}
}
