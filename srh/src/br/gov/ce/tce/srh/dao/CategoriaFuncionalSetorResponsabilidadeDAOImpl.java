package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetorResponsabilidade;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Repository
public class CategoriaFuncionalSetorResponsabilidadeDAOImpl implements CategoriaFuncionalSetorResponsabilidadeDAO{

	static Logger logger = Logger.getLogger(CategoriaFuncionalSetorResponsabilidadeDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(c.id) FROM CategoriaFuncionalSetorResponsabilidade c ORDER BY c.id");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}
	
	@Override
	public int count(Setor setor, int opcaoAtiva) {
		
		String filtro = "";
		
		if(opcaoAtiva == 1){
			filtro = "AND c.fim is null";
		}else if(opcaoAtiva == 2){
			filtro = "AND c.fim is not null";
		}
			
		Query query = entityManager.createQuery("Select count(c) from CategoriaFuncionalSetorResponsabilidade c where c.categoriaFuncionalSetor.setor.id = :setor " + filtro);
		query.setParameter("setor", setor.getId());
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> search(Setor setor, int opcaoAtiva, int first, int rows) {
		
		String filtro = "";
		
		if(opcaoAtiva == 1){
			filtro = "AND c.fim is null";
		}else if(opcaoAtiva == 2){
			filtro = "AND c.fim is not null";
		}
		
		Query query = entityManager.createQuery("Select c from CategoriaFuncionalSetorResponsabilidade c where c.categoriaFuncionalSetor.setor.id = :setor "
				+ filtro + " ORDER BY c.categoriaFuncionalSetor.categoriaFuncional.descricao, c.tipo, c.inicio DESC, c.id DESC");
		query.setParameter("setor", setor.getId());
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Transactional
	@Override
	public CategoriaFuncionalSetorResponsabilidade salvar(CategoriaFuncionalSetorResponsabilidade entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}
		return entityManager.merge(entidade);
	}

	@Transactional
	@Override
	public void excluir(CategoriaFuncionalSetorResponsabilidade entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	@Override
	public CategoriaFuncionalSetorResponsabilidade findById(Long id) {
		return entityManager.find(CategoriaFuncionalSetorResponsabilidade.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> findAll() {
		return entityManager.createQuery("SELECT c FROM CategoriaFuncionalSetorResponsabilidade c ORDER BY c.categoriaFuncionalSetor.setor.nome, c.inicio DESC").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> findBySetor(Setor setor) {
		Query query = entityManager.createQuery("Select c from CategoriaFuncionalSetorResponsabilidade c where c.categoriaFuncionalSetor.setor.id = :setorId ORDER BY c.inicio DESC");
		query.setParameter("setorId", setor.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> findAtivaByCategoriaFuncionalSetor(CategoriaFuncionalSetor categoriaFuncionalSetor) {
		Query query = entityManager.createQuery("Select c from CategoriaFuncionalSetorResponsabilidade c "
				+ "where c.categoriaFuncionalSetor.id = :categoriaFuncionalSetorId "
				+ "and c.fim is null ORDER BY c.inicio DESC");
		query.setParameter("categoriaFuncionalSetorId", categoriaFuncionalSetor.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetorResponsabilidade> findByCategoriaFuncionalSetor(CategoriaFuncionalSetor categoriaFuncionalSetor) {
		Query query = entityManager.createQuery("Select c from CategoriaFuncionalSetorResponsabilidade c "
				+ "where c.categoriaFuncionalSetor.id = :categoriaFuncionalSetorId "
				+ "ORDER BY c.inicio DESC");
		query.setParameter("categoriaFuncionalSetorId", categoriaFuncionalSetor.getId());
		return query.getResultList();
	}
}
