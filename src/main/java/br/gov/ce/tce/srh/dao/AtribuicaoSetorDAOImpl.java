package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AtribuicaoSetor;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Repository
public class AtribuicaoSetorDAOImpl implements AtribuicaoSetorDAO{
	
	static Logger logger = Logger.getLogger(AtribuicaoSetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(a.id) FROM AtribuicaoSetor a ORDER BY a.id");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}	
	
	@Override
	public int count(Setor setor, int opcaoAtiva) {
		
		String filtro = "";
		
		if(opcaoAtiva == 1){
			filtro = "AND a.fim is null";
		}else if(opcaoAtiva == 2){
			filtro = "AND a.fim is not null";
		}
		
		Query query = entityManager.createQuery("Select count(a) from AtribuicaoSetor a where a.setor.id = :setorId " + filtro);			
		query.setParameter("setorId", setor.getId());
					
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AtribuicaoSetor> search(Setor setor, int opcaoAtiva, int first,	int rows) {
		
		String filtro = "";
		
		if(opcaoAtiva == 1){
			filtro = "AND a.fim is null";
		}else if(opcaoAtiva == 2){
			filtro = "AND a.fim is not null";
		}
		
		Query query = entityManager.createQuery("Select a from AtribuicaoSetor a where a.setor.id = :setorId " + filtro + " ORDER BY a.tipo, a.inicio DESC, a.id desc");
		
		query.setParameter("setorId", setor.getId());				
		query.setFirstResult(first);
		query.setMaxResults(rows);
		
		return query.getResultList();
	}	
	
	@Override
	public AtribuicaoSetor salvar(AtribuicaoSetor entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}
		return entityManager.merge(entidade);
	}

	@Override
	public void excluir(AtribuicaoSetor entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	@Override
	public AtribuicaoSetor findById(Long id) {
		return entityManager.find(AtribuicaoSetor.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtribuicaoSetor> findAll() {
		return entityManager.createQuery("SELECT a FROM AtribuicaoSetor a ORDER BY a.tipo, a.inicio DESC, a.id desc, a.setor.nome").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtribuicaoSetor> findBySetor(Setor setor) {
		Query query = entityManager.createQuery("Select a from AtribuicaoSetor a where a.setor.id = :setorId ORDER BY a.tipo, a.inicio DESC, a.id desc");
		query.setParameter("setorId", setor.getId());
		return query.getResultList();
	}	
}
