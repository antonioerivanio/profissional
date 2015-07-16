package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class PessoalCursoAcademicaDAOImpl implements PessoalCursoAcademicaDAO {

	static Logger logger = Logger.getLogger(PessoalCursoAcademicaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	PessoalService pessoalService;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public PessoalCursoAcademica salvar(PessoalCursoAcademica entidade) {
		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(PessoalCursoAcademica entidade) {
		Query query = entityManager.createQuery("DELETE FROM PessoalCursoAcademica r WHERE r.pk.pessoal=:pessoa AND r.pk.cursoAcademico = :curso ");
		query.setParameter("pessoa", entidade.getPk().getPessoal() );
		query.setParameter("curso", entidade.getPk().getCursoAcademico());
		query.executeUpdate();
	}


	@Override
	public int count(Long pessoa) {
		Query query = entityManager.createQuery("Select count (p.cursoAcademica) as total from PessoalCursoAcademica p where p.pessoal.id = :pessoal");
		query.setParameter("pessoal", pessoa);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	public int count(Usuario usuarioLogado) {
		return count((pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf()))).getId());
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoAcademica> search(Long pessoa, int first, int rows) {
		Query query = entityManager.createQuery("Select p from PessoalCursoAcademica p where p.pessoal.id = :pessoal ");
		query.setParameter("pessoal", pessoa);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	public List<PessoalCursoAcademica> search(Usuario usuarioLogado, int first, int rows) {
		return search((pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf()))).getId(), first, rows);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoAcademica> findByPessoa(Long pessoal) {
		Query query = entityManager.createQuery("Select p from PessoalCursoAcademica p where p.pessoal.id = :pessoal ");
		query.setParameter("pessoal", pessoal);
		return query.getResultList();
	}


	@Override
	public PessoalCursoAcademica getByCursoPessoa(Long curso, Long pessoal) {
		try {
			Query query = entityManager.createQuery("Select p from PessoalCursoAcademica p where p.cursoAcademica.id = :curso AND p.pessoal.id = :pessoal ");
			query.setParameter("curso", curso);
			query.setParameter("pessoal", pessoal);
			return (PessoalCursoAcademica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoAcademica> findByCursoAcademica(Long cursoAcademica) {
		Query query = entityManager.createQuery("Select p from PessoalCursoAcademica p where p.cursoAcademica.id = :cursoAcademica ");
		query.setParameter("cursoAcademica", cursoAcademica);
		return query.getResultList();
	}

}
