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
		Query query = entityManager.createQuery("Select max(pj.id) from PessoaJuridica pj ");
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
		entityManager.createQuery("DELETE FROM PessoaJuridica pj WHERE pj.id=:id ").setParameter("id", entidade.getId())
				.executeUpdate();
	}

	@Override
	public int count(String cnpj, String razaoSocial, String nomeFantasia) {
		System.out.println("CHEGOU AQUI2: ------->>>" + cnpj + razaoSocial + nomeFantasia);
		StringBuilder consulta = new StringBuilder("Select count (pj) FROM PessoaJuridica pj WHERE 1=1 ");

		if (cnpj != null && !(cnpj.isEmpty())) {
			consulta.append("AND pj.cnpj LIKE :cnpj ");
		}

		if (razaoSocial != null && !(razaoSocial.isEmpty())) {
			consulta.append("AND pj.razaoSocial LIKE :razaoSocial ");
		}

		if (nomeFantasia != null && !(nomeFantasia.isEmpty())) {
			consulta.append("AND pj.nomeFantasia LIKE :nomeFantasia ");
		}

		consulta.append("ORDER BY pj.razaoSocial");

		Query query = entityManager.createQuery(consulta.toString());

		if (cnpj != null && !(cnpj.isEmpty())) {
			query.setParameter("cnpj", "%" + cnpj + "%");
		}

		if (razaoSocial != null && !(razaoSocial.isEmpty())) {
			query.setParameter("razaoSocial", "%" + razaoSocial.toUpperCase() + "%");
		}

		if (nomeFantasia != null && !(nomeFantasia.isEmpty())) {
			query.setParameter("nomeFantasia", "%" + nomeFantasia.toUpperCase() + "%");
		}

		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PessoaJuridica> search(String cnpj, String razaoSocial, String nomeFantasia, int first, int rows) {
		StringBuilder consulta = new StringBuilder("Select pj FROM PessoaJuridica pj WHERE 1=1 ");

		if (cnpj != null && !(cnpj.isEmpty())) {
			consulta.append(" AND pj.cnpj LIKE :cnpj ");
		}

		if (razaoSocial != null && !(razaoSocial.isEmpty())) {
			consulta.append(" AND pj.razaoSocial LIKE :razaoSocial ");
		}

		if (nomeFantasia != null && !(nomeFantasia.isEmpty())) {
			consulta.append(" AND pj.nomeFantasia LIKE :nomeFantasia ");
		}

		consulta.append(" ORDER BY pj.razaoSocial");

		Query query = entityManager.createQuery(consulta.toString());

		if (cnpj != null && !(cnpj.isEmpty())) {
			query.setParameter("cnpj", "%" + cnpj + "%");
		}

		if (razaoSocial != null && !(razaoSocial.isEmpty())) {
			query.setParameter("razaoSocial", "%" + razaoSocial.toUpperCase() + "%");
		}

		if (nomeFantasia != null && !(nomeFantasia.isEmpty())) {
			query.setParameter("nomeFantasia", "%" + nomeFantasia.toUpperCase() + "%");
		}

		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public PessoaJuridica getBycnpj(String cnpj) {

		try {
			Query query = entityManager.createQuery("Select pj from PessoaJuridica pj where pj.cnpj = :cnpj ");
			query.setParameter("cnpj", cnpj);
			return (PessoaJuridica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public PessoaJuridica getByrazaoSocial(String razaoSocial) {
		try {
			Query query = entityManager
					.createQuery("Select pj from PessoaJuridica pj where pj.razaoSocial = :razaoSocial ");
			query.setParameter("razaoSocial", razaoSocial.toUpperCase());
			return (PessoaJuridica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public PessoaJuridica getBynomeFantasia(String nomeFantasia) {
		try {
			Query query = entityManager
					.createQuery("Select pj from PessoaJuridica pj where pj.nomeFantasia = :nomeFantasia ");
			query.setParameter("nomeFantasia", nomeFantasia.toUpperCase());
			return (PessoaJuridica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PessoaJuridica> findAll() {
		return entityManager.createQuery("SELECT pj FROM PessoaJuridica pj ORDER BY pj.id").getResultList();
	}
}
