package br.gov.ce.tce.srh.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.enums.EnumTipoCursoProfissional;
import br.gov.ce.tce.srh.sapjava.domain.Setor;


@Repository
public class CursoServidorDAOImpl implements CursoServidorDAO {

	static Logger logger = Logger.getLogger(PessoalCursoProfissionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public PessoalCursoProfissional salvar(PessoalCursoProfissional entidade) {
		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Long curso) {
		Query query = entityManager.createQuery("DELETE FROM PessoalCursoProfissional p WHERE p.pk.cursoProfissional = :curso ");
		query.setParameter("curso", curso );
		query.executeUpdate();
	}


	@Override
	public int count(Long area, String curso) {
		Query query = entityManager.createQuery("Select count(*) from CursoProfissional c where c.area.id = :area AND upper( c.descricao ) like :curso ORDER BY c.descricao ");
		query.setParameter("area", area);
		query.setParameter("curso", "%" + curso.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}	
	

	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> search(Long area, String curso, int first, int rows) {
		Query query = entityManager.createQuery("SELECT new CursoProfissional(c.id, c.area, c.descricao, c.inicio, c.fim) FROM CursoProfissional c WHERE c.area.id = :area AND upper(c.descricao) LIKE :curso ORDER BY c.descricao");
		query.setParameter("area", area);
		query.setParameter("curso", "%" + curso.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> search(String curso) {
		Query query = entityManager.createQuery("SELECT new CursoProfissional(c.id, c.area, c.descricao, c.inicio, c.fim) FROM CursoProfissional c WHERE upper(c.descricao) LIKE :curso ORDER BY c.inicio DESC, TRIM(c.descricao)");
		query.setParameter("curso", "%" + curso.toUpperCase() + "%");
		return query.getResultList();
	}


	@Override
	public PessoalCursoProfissional getByCurso(Long curso) {
		try {
			Query query = entityManager.createQuery("Select e from PessoalCursoProfissional e where e.pk.cursoProfissional = :curso ");
			query.setParameter("curso", curso );
			return (PessoalCursoProfissional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional) {
		Query query = entityManager.createQuery("Select e from PessoalCursoProfissional e where e.pk.cursoProfissional = :curso");
		query.setParameter("curso", cursoProfissional);
		return query.getResultList();
	}
	
	
	@Override
	public int count(Long pessoal, boolean areaAtuacao, EnumTipoCursoProfissional tipoCurso, boolean somentePosGraduacao, Date inicio, Date fim) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("Select count(*) from PessoalCursoProfissional e, Funcional f where e.pk.pessoal = :pessoal ");
		
		sql.append("AND e.pessoal.id = f.pessoal.id ");
		sql.append("AND f.saida IS NULL AND f.situacao.id = 1 ");
		
		if (areaAtuacao)
			sql.append(" AND e.areaAtuacao = :areaAtuacao");
		if (tipoCurso != null)
			sql.append(" AND e.cursoProfissional.posGraduacao = :tipoCurso");		
		if (somentePosGraduacao)
			sql.append(" AND e.cursoProfissional.posGraduacao != :extensao");		
		if (inicio != null ) 	
			sql.append(" AND e.cursoProfissional.fim >= :inicio");		
		if (fim != null) 
			sql.append(" AND e.cursoProfissional.fim <= :fim ");
		
		sql.append(" ORDER BY e.cursoProfissional.inicio desc");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", pessoal);
		
		if(areaAtuacao)
			query.setParameter("areaAtuacao", areaAtuacao);		
		if(tipoCurso != null)
			query.setParameter("tipoCurso", tipoCurso);
		if(somentePosGraduacao)
			query.setParameter("extensao", EnumTipoCursoProfissional.EXTENSAO);
		if (inicio != null ) 	
			query.setParameter("inicio", inicio);		
		if (fim != null) 
			query.setParameter("fim", fim);		
		
		return ((Long) query.getSingleResult()).intValue();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> search(Long pessoal, boolean areaAtuacao, EnumTipoCursoProfissional tipoCurso, boolean somentePosGraduacao, Date inicio, Date fim, int first, int rows) {
		StringBuilder sql = new StringBuilder();
		sql.append("Select e from PessoalCursoProfissional e, Funcional f where e.pk.pessoal = :pessoal ");
		
		sql.append("AND e.pessoal.id = f.pessoal.id ");
		sql.append("AND f.saida IS NULL AND f.situacao.id = 1 ");
		
		if(areaAtuacao)
			sql.append(" AND e.areaAtuacao = :areaAtuacao");		
		if (tipoCurso != null)
			sql.append(" AND e.cursoProfissional.posGraduacao = :tipoCurso");
		if (somentePosGraduacao)
			sql.append(" AND e.cursoProfissional.posGraduacao != :extensao");
		if (inicio != null ) 	
			sql.append(" AND e.cursoProfissional.fim >= :inicio");		
		if (fim != null) 
			sql.append(" AND e.cursoProfissional.fim <= :fim ");
		
		sql.append(" ORDER BY e.cursoProfissional.inicio desc");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", pessoal);
		
		if(areaAtuacao)
			query.setParameter("areaAtuacao", areaAtuacao);		
		if(tipoCurso != null)
			query.setParameter("tipoCurso", tipoCurso);	
		if(somentePosGraduacao)
			query.setParameter("extensao", EnumTipoCursoProfissional.EXTENSAO);
		if (inicio != null ) 	
			query.setParameter("inicio", inicio);		
		if (fim != null) 
			query.setParameter("fim", fim);	
		
		if( first >= 0 && rows >= 0 ) {
			query.setFirstResult(first);
			query.setMaxResults(rows);
		}
		
		return query.getResultList();
	}	

	@Override
	public int count(Date inicio, Date fim, boolean areaAtuacao, EnumTipoCursoProfissional tipoCurso, boolean somentePosGraduacao, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso) {
		
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");		
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(*) ");		
		
		sql.append("FROM TB_FUNCIONAL ");
		sql.append("INNER JOIN TB_PESSOAL ON TB_FUNCIONAL.IDPESSOAL = TB_PESSOAL.ID ");
		sql.append("INNER JOIN TB_PESSOALCURSOPROF ON TB_PESSOAL.ID = TB_PESSOALCURSOPROF.IDPESSOAL ");
		sql.append("INNER JOIN TB_CURSOPROFISSIONAL ON TB_PESSOALCURSOPROF.IDCURSOPROFISSIONAL = TB_CURSOPROFISSIONAL.ID ");
		sql.append("LEFT JOIN TB_OCUPACAO ON TB_FUNCIONAL.IDOCUPACAO = TB_OCUPACAO.ID ");
		sql.append("LEFT JOIN TB_TIPOOCUPACAO ON TB_TIPOOCUPACAO.ID = TB_OCUPACAO.TIPOOCUPACAO ");
		sql.append("LEFT JOIN SAPJAVA.SETOR ON DECODE(TB_FUNCIONAL.IDSETORDESIGNADO, NULL, TB_FUNCIONAL.IDSETOR, TB_FUNCIONAL.IDSETORDESIGNADO) = SAPJAVA.SETOR.IDSETOR ");

		sql.append(" WHERE TB_FUNCIONAL.DATASAIDA IS NULL AND TB_FUNCIONAL.IDSITUACAO = 1 " );
		
		if(inicio != null)
			sql.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') >= To_Date('"+formatador.format(inicio)+"','dd/mm/yyyy') " );
		
		if(fim != null)
			sql.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') <= To_Date('"+formatador.format(fim)+"','dd/mm/yyyy') ");
		
		if(areaAtuacao)
			sql.append(" AND TB_PESSOALCURSOPROF.AREAATUACAO = 1 ");			
		
		if(tipoCurso != null)
			sql.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO = " + tipoCurso.ordinal());
		
		if(somentePosGraduacao)
			sql.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO > 0" );
		
		if (tipoOcupacao != null && tipoOcupacao.getId() != null)				
			sql.append(" AND TB_TIPOOCUPACAO.ID = " + tipoOcupacao.getId() );
				
		if (setor != null && setor.getId() != null)				
			sql.append(" AND SAPJAVA.SETOR.IDSETOR = " + setor.getId() );
		
		if (idCurso != null && idCurso.intValue() != 0)				
			sql.append(" AND TB_CURSOPROFISSIONAL.ID = " + idCurso );
		
		Query query = entityManager.createNativeQuery(sql.toString());		
		
		return ((BigDecimal) query.getSingleResult()).intValue();
	}	

	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> search(Date inicio, Date fim, boolean areaAtuacao, EnumTipoCursoProfissional tipoCurso, boolean somentePosGraduacao, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso, int first, int rows) {
		
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");				
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT TB_PESSOALCURSOPROF.* ");		
		
		sql.append(" FROM TB_FUNCIONAL ");
		sql.append(" INNER JOIN TB_PESSOAL ON TB_FUNCIONAL.IDPESSOAL = TB_PESSOAL.ID ");
		sql.append(" INNER JOIN TB_PESSOALCURSOPROF ON TB_PESSOAL.ID = TB_PESSOALCURSOPROF.IDPESSOAL ");
		sql.append(" INNER JOIN TB_CURSOPROFISSIONAL ON TB_PESSOALCURSOPROF.IDCURSOPROFISSIONAL = TB_CURSOPROFISSIONAL.ID ");
		sql.append(" LEFT JOIN TB_OCUPACAO ON TB_FUNCIONAL.IDOCUPACAO = TB_OCUPACAO.ID ");
		sql.append(" LEFT JOIN TB_TIPOOCUPACAO ON TB_TIPOOCUPACAO.ID = TB_OCUPACAO.TIPOOCUPACAO ");
		sql.append(" LEFT JOIN SAPJAVA.SETOR ON DECODE(TB_FUNCIONAL.IDSETORDESIGNADO, NULL, TB_FUNCIONAL.IDSETOR, TB_FUNCIONAL.IDSETORDESIGNADO) = SAPJAVA.SETOR.IDSETOR ");

		sql.append(" WHERE TB_FUNCIONAL.DATASAIDA IS NULL AND TB_FUNCIONAL.IDSITUACAO = 1 " );
		
		if(inicio != null)
			sql.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') >= To_Date('"+formatador.format(inicio)+"','dd/mm/yyyy') " );
		
		if(fim != null)
			sql.append(" AND To_Date(To_Char(TB_CURSOPROFISSIONAL.FIM,'dd/mm/yyyy'),'dd/mm/yyyy') <= To_Date('"+formatador.format(fim)+"','dd/mm/yyyy') ");
		
		if(areaAtuacao)
			sql.append(" AND TB_PESSOALCURSOPROF.AREAATUACAO = 1 ");			
		
		if(tipoCurso != null)
			sql.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO = " + tipoCurso.ordinal());
		
		if(somentePosGraduacao)
			sql.append(" AND TB_CURSOPROFISSIONAL.POSGRADUACAO > 0 ");
		
		if (tipoOcupacao != null && tipoOcupacao.getId() != null)				
			sql.append(" AND TB_TIPOOCUPACAO.ID = " + tipoOcupacao.getId() );
				
		if (setor != null && setor.getId() != null)				
			sql.append(" AND SAPJAVA.SETOR.IDSETOR = " + setor.getId() );	
			
		if (idCurso != null && idCurso.intValue() != 0)				
			sql.append(" AND TB_CURSOPROFISSIONAL.ID = " + idCurso );
		
		if(somentePosGraduacao) {
			sql.append(" ORDER BY TB_PESSOAL.NOMECOMPLETO, TB_CURSOPROFISSIONAL.POSGRADUACAO, TB_CURSOPROFISSIONAL.INICIO DESC, TB_CURSOPROFISSIONAL.ID DESC");			
		} else {
			sql.append(" ORDER BY TB_CURSOPROFISSIONAL.INICIO DESC, TB_CURSOPROFISSIONAL.ID DESC, TB_PESSOAL.NOMECOMPLETO");
		}
		
		
		Query query = entityManager.createNativeQuery(sql.toString(), PessoalCursoProfissional.class);		
		
		query.setFirstResult(first);
		query.setMaxResults(rows);
		
		return query.getResultList();		
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> getCursos(Date inicio, Date fim,boolean areaAtuacao, EnumTipoCursoProfissional tipoCurso) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("Select e from PessoalCursoProfissional e where e.cursoProfissional.inicio >= :inicio AND e.cursoProfissional.fim <= :fim ");
		
		if(areaAtuacao)
			sql.append(" AND e.areaAtuacao = :areaAtuacao");		
		if (tipoCurso != null)
			sql.append(" AND e.cursoProfissional.posGraduacao = :tipoCurso");

		sql.append(" ORDER BY  e.pessoal.nomeCompleto, e.cursoProfissional.inicio desc");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		
		if(areaAtuacao)
			query.setParameter("areaAtuacao", areaAtuacao);		
		if(tipoCurso != null)
			query.setParameter("tipoCurso", tipoCurso.ordinal());		
		
		return query.getResultList();
	}
}
