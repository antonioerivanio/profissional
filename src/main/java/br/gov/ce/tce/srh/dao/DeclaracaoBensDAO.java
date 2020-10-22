package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.DeclaracaoBens;


@Repository
public class DeclaracaoBensDAO {

	static Logger logger = Logger.getLogger(DeclaracaoBensDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public DeclaracaoBens getById(Long id) {
		return entityManager.find(DeclaracaoBens.class, id);
	}

	@Transactional
	public DeclaracaoBens salvar(DeclaracaoBens entidade) {		
		return entityManager.merge(entidade);
	}

	@Transactional
	public void excluir(DeclaracaoBens entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}
	
	public List<DeclaracaoBens> getByPessoalId(Long id) {
		return entityManager.createQuery("Select d from DeclaracaoBens d "
				+ "JOIN d.pessoal p where p.id = :id "
				+ "ORDER BY d.exercicio DESC, d.anoCalendario DESC, d.nomeArquivo", DeclaracaoBens.class)
				.setParameter("id", id)
				.getResultList();
	}
	
	@Transactional
	public void salvar(List<DeclaracaoBens> entidade) {		
		for (DeclaracaoBens declaracaoBens : entidade) {
			this.salvar(declaracaoBens);
		};
	}
	
	@Transactional
	public void excluir(List<DeclaracaoBens> entidade) {
		for (DeclaracaoBens declaracaoBens : entidade) {
			this.excluir(declaracaoBens);
		};
	}

}
