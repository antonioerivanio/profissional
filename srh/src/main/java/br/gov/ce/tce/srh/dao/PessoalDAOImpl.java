package br.gov.ce.tce.srh.dao;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class PessoalDAOImpl implements PessoalDAO {

	static Logger logger = Logger.getLogger(PessoalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public Pessoal getById(Long id) {
		return entityManager.find(Pessoal.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(p.id) from Pessoal p ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Pessoal salvar(Pessoal entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}		
		
		return entityManager.merge(entidade);
	}


	public Boolean verificarNomeExistente(Long id, String nome) {
		
		String select = "Select count (e) from Pessoal e where upper( e.nomeCompleto ) = :nome ";
		
		if(id != null)
			select += "AND e.id <> :id";
		
		Query query = entityManager.createQuery(select);
		query.setParameter("nome", nome.toUpperCase() );
		
		if(id != null)
			query.setParameter("id", id);
		
		Long count = (Long) query.getSingleResult();
		
		return count > 0;
	}


	@Override
	public void excluir(Pessoal entidade) {
//		entityManager.createQuery("DELETE FROM Pessoal p WHERE p.id=:id").setParameter("id", entidade.getId()).executeUpdate();	
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	@SuppressWarnings("unchecked")
	public Pessoal getByCPf(String cpf) {

		// com mascara
		Query query = entityManager.createQuery("Select p from Pessoal p where p.cpf LIKE :cpf ");
		query.setParameter("cpf", "%" + cpf + "%");
		List<Pessoal> lista = query.getResultList();
		if (lista.size() > 0)
			return lista.get(0);

		// sem mascara
		query = entityManager.createQuery("Select p from Pessoal p where p.cpf LIKE :cpf ");
		query.setParameter("cpf", "%"+ SRHUtils.removerMascara( cpf ) + "%");
		lista = query.getResultList();
		if (lista.size() > 0)
			return lista.get(0);

		return null;
	}


	@Override
	@SuppressWarnings("unchecked")
	public Pessoal getByPasep(String pasep) {

		// com mascara
		Query query = entityManager.createQuery("Select p from Pessoal p where p.pasep = :pasep ");
		query.setParameter("pasep", pasep );
		List<Pessoal> lista = query.getResultList();
		if (lista.size() > 0)
			return lista.get(0);

		// sem mascara
		query = entityManager.createQuery("Select p from Pessoal p where p.pasep LIKE :pasep ");
		query.setParameter("pasep", "%" + SRHUtils.removerMascara( pasep ) + "%" );
		lista = query.getResultList();
		if (lista.size() > 0)
			return lista.get(0);

		return null;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoal> findByNome(String nome) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT e FROM Pessoal e ");
		sql.append("    left join fetch   e.escolaridade ");
		sql.append("    left join fetch   e.estadoCivil");
		sql.append("    left join fetch   e.raca");
		sql.append("    left join fetch   e.ufEndereco");
		sql.append("    left join fetch   e.uf");
		sql.append("    left join fetch   e.ufEmissorRg");
		sql.append("    left join fetch   e.categoria");
		sql.append("    WHERE             e.nomePesquisa LIKE :nome ");
		sql.append("    ORDER BY          e.nomePesquisa ");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		return query.getResultList();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoal> findServidorByNome(String nome) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT e FROM Pessoal e ");
		sql.append("    left join fetch   e.escolaridade ");
		sql.append("    left join fetch   e.estadoCivil");
		sql.append("    left join fetch   e.raca");
		sql.append("    left join fetch   e.ufEndereco");
		sql.append("    left join fetch   e.uf");
		sql.append("    left join fetch   e.ufEmissorRg");
		sql.append("    left join fetch   e.categoria");
		sql.append("    WHERE             e.nomePesquisa LIKE :nome");
		sql.append("    AND               e.categoria.id = 1");
		sql.append("    ORDER BY          e.nomePesquisa");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		return query.getResultList();
	}
			

	@Override
	public int count(String nome, String cpf) {

		StringBuffer select = new StringBuffer();
		select.append("SELECT count (e) FROM Pessoal e WHERE e.id > 0 ");

		if (nome != null && !nome.equalsIgnoreCase(""))
			select.append(" AND e.nomePesquisa LIKE :nome ");

		if (cpf != null && !cpf.equalsIgnoreCase(""))
			select.append(" AND e.cpf LIKE :cpf ");

		select.append(" ORDER BY e.id ");

		// montando a query
		Query query = entityManager.createQuery(select.toString());

		if (nome != null && !nome.equalsIgnoreCase(""))
			query.setParameter("nome", "%" + nome.toLowerCase() + "%");

		if (cpf != null && !cpf.equalsIgnoreCase(""))
			query.setParameter("cpf", "%" + SRHUtils.removerMascara( cpf ) + "%");
		
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	public int count(Usuario usuarioLogado) {
		return count(null, usuarioLogado.getCpf());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoal> search(String nome, String cpf, int first, int rows) {

		StringBuffer select = new StringBuffer();
		select.append("SELECT new Pessoal(e.id, e.cpf, e.nomeCompleto) FROM Pessoal e WHERE e.id > 0 ");

		if (nome != null && !nome.equalsIgnoreCase(""))
			select.append(" AND e.nomePesquisa LIKE :nome ");

		if (cpf != null && !cpf.equalsIgnoreCase(""))
			select.append(" AND e.cpf LIKE :cpf ");

		select.append(" ORDER BY e.id ");

		// montando a query
		Query query = entityManager.createQuery(select.toString());

		if (nome != null && !nome.equalsIgnoreCase(""))
			query.setParameter("nome", "%" + nome.toLowerCase() + "%");

		if (cpf != null && !cpf.equalsIgnoreCase(""))
			query.setParameter("cpf", "%" + SRHUtils.removerMascara( cpf ) + "%");

		query.setFirstResult(first);
		query.setMaxResults(rows);

		return query.getResultList();
	}
	
	@Override
	public List<Pessoal> search(Usuario usuarioLogado, int first, int rows) {
		return search(null, usuarioLogado.getCpf(), first, rows);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoal> findAllComFuncional() {		
		return entityManager.createQuery("SELECT DISTINCT p FROM Funcional f INNER JOIN f.pessoal p ORDER BY p.nome").getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoal> findServidorEfetivoByNome(String nome) {		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT p FROM Funcional f ");
		sql.append("INNER JOIN f.pessoal p ");
		sql.append("INNER JOIN f.ocupacao o ");
		sql.append("INNER JOIN o.tipoOcupacao toc ");
		sql.append("WHERE toc.id IN (:tipos) ");
		sql.append("AND p.nomePesquisa LIKE :nome ");
		sql.append("ORDER BY p.nomeCompleto");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("nome", "%" + nome.toLowerCase() + "%");
		List<Long> ids = Arrays.asList(1L, 2L, 3L);
		query.setParameter("tipos", ids);
		
		return query.getResultList();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoal> findByCategoria(Long idCategoria) {
		Query query = entityManager.createQuery("SELECT e FROM Pessoal e WHERE e.categoria.id = :idCategoria ORDER BY e.nomeCompleto");
		query.setParameter("idCategoria", idCategoria);
		return query.getResultList();
	}


}
