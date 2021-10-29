package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ComprovanteVinculoSocietario;

@Repository
public class ComprovanteVinculoSocietarioDAO {

	static Logger logger = Logger.getLogger(ComprovanteVinculoSocietarioDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ComprovanteVinculoSocietario getById(Long id) {
		return entityManager.find(ComprovanteVinculoSocietario.class, id);
	}
	
	public ComprovanteVinculoSocietario salvar(ComprovanteVinculoSocietario entidade) {		
		return entityManager.merge(entidade);
	}
	
	public void excluir(ComprovanteVinculoSocietario entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}
	
	public List<ComprovanteVinculoSocietario> getByPessoalId(Long id) {
		
		try {
			TypedQuery<ComprovanteVinculoSocietario> query = entityManager.createQuery("Select c from ComprovanteVinculoSocietario c "
					+ "JOIN c.pessoal p where p.id = :id "
					+ "ORDER BY c.nomeArquivo", ComprovanteVinculoSocietario.class)
					.setParameter("id", id);				
			
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
		
	}	
}
