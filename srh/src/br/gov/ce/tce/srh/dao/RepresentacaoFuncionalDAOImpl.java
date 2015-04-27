package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.domain.sca.Usuario;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class RepresentacaoFuncionalDAOImpl implements RepresentacaoFuncionalDAO {

	static Logger logger = Logger.getLogger(RepresentacaoFuncionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PessoalService pessoalService;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from RepresentacaoFuncional r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public void salvar(RepresentacaoFuncional entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		entityManager.merge(entidade);
	}


	@Override
	public void excluir(RepresentacaoFuncional entidade) {
//		Query query = entityManager.createQuery("DELETE FROM RepresentacaoFuncional r WHERE r.id=:id");
//		query.setParameter("id", entidade.getId());
//		query.executeUpdate();
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	public int count(Long pessoal) {
		Query query = entityManager.createQuery("Select count (r) from RepresentacaoFuncional r where r.funcional.pessoal.id = :pessoal ORDER BY r.inicio DESC");
		query.setParameter("pessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> search(Long pessoal, int first, int rows) {
		Query query = entityManager.createQuery("Select r from RepresentacaoFuncional r where r.funcional.pessoal.id = :pessoal ORDER BY r.inicio DESC");
		query.setParameter("pessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public RepresentacaoFuncional getById(Long id) {
		return entityManager.find(RepresentacaoFuncional.class, id);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RepresentacaoFuncional> getByMatricula(String matricula) {
		Query query = entityManager
				.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.funcional.matricula = :matricula ORDER BY r.id DESC");
		query.setParameter("matricula", matricula);
		return query.getResultList();

	}


	@Override
	public RepresentacaoFuncional getByFuncionalTipo(Long funcional, Long tipoNomeacao) {
		try {
			Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.funcional.id = :funcional AND r.tipoNomeacao = :tipo AND r.fim IS NULL ");
			query.setParameter("funcional", funcional);
			query.setParameter("tipo", tipoNomeacao);
			return (RepresentacaoFuncional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	public RepresentacaoFuncional getByCpf(String cpf) {
		try {
			Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.funcional.pessoal.cpf = '" + SRHUtils.removerMascara( cpf ) + "'");
			return (RepresentacaoFuncional) query.getResultList().get(0);
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> findByNome(String nome) {
		Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.funcional.pessoal.nomePesquisa like :nome and r.fim is null");
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		return query.getResultList();
	}
	
	
	@Override
	public List<RepresentacaoFuncional> findByUsuarioLogado(Usuario usuarioLogado) {
		return findByNome(pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf())).getNomePesquisa());
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> findByNomeSetor(String nome, Long idSetor) {
		Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.funcional.pessoal.nomePesquisa like :nome AND r.setor.id = :setor AND r.fim IS NULL ");
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		query.setParameter("setor", idSetor);
		return query.getResultList();
	}
	
	@Override
	public List<RepresentacaoFuncional> findByUsuarioLogadoSetor(Usuario usuarioLogado, Long idSetor) {
		return findByNomeSetor(pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf())).getNomePesquisa(), idSetor);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> findByPessoal(Long idPessoa) {
		Query query = entityManager.createQuery("Select r from RepresentacaoFuncional r where r.funcional.pessoal.id = :pessoal ORDER BY r.inicio DESC");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> findByFuncional(Long idFuncional) {
		Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.funcional.id = :idFuncional order by r.id desc");
		query.setParameter("idFuncional", idFuncional);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> findByTipoNomeacaoCargoSetor(Long tipoNomeacao, Long cargo, Long setor) {
		Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.tipoNomeacao = :nomeacao AND r.representacaoCargo.id = :cargo AND r.setor.id = :setor AND ativo = true ");
		query.setParameter("nomeacao", tipoNomeacao );
		query.setParameter("cargo", cargo);
		query.setParameter("setor", setor);
		return query.getResultList();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoFuncional> findByCargoSetor(Long cargo, Long setor) {
		Query query = entityManager.createQuery("SELECT r FROM RepresentacaoFuncional r WHERE r.representacaoCargo.id = :cargo AND r.setor.id = :setor AND ativo = true ");
		query.setParameter("cargo", cargo);
		query.setParameter("setor", setor);
		return query.getResultList();
	}

}
