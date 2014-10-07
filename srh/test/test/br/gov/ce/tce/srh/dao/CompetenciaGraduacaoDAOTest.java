package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAOImpl;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.domain.Pessoal;

/**
 *
 * @author robstown
 */
public class CompetenciaGraduacaoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private CompetenciaGraduacao entidade;
	private CompetenciaGraduacaoDAOImpl dao = new CompetenciaGraduacaoDAOImpl();

	private static int countTests = 0;


	public CompetenciaGraduacaoDAOTest() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
		dao.setEntityManager(em);	
	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {

		@SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection(
        		"jdbc:oracle:thin:@localhost:1521:XE", "SRH", "srh");
        jdbcConnection.setAutoCommit(true);

		return new DatabaseConnection(jdbcConnection);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\CompetenciaGraduacao.xml"));
	}

	/**
	 * Returns the database operation executed in test setup.
	 */
	protected DatabaseOperation getSetUpOperation() throws Exception
	{
	    return DatabaseOperation.CLEAN_INSERT;
	}

	/**
	 * Returns the database operation executed in test cleanup.
	 */
	protected DatabaseOperation getTearDownOperation() throws Exception
	{
	    return DatabaseOperation.NONE;
	}

	/**
	 * Metodo para fechar o entityManager
	 */
	private void closeEntityManager(int countTests) {
		if (countTests == 4) {
			em.close();
			emf.close();
		}
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getCursoAcademica().getId(), new Long(1));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(1));
		Assert.assertNotNull( entidade.getInicioCompetencia() );

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        dao.excluir(1l, 1l);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new CompetenciaGraduacao();
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setCursoAcademica(new CursoAcademica());
        entidade.getCursoAcademica().setId(1l);
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(1l);
        entidade.setInicioCompetencia(new Date());

        dao.salvar(entidade);
        entidade = dao.getById(1l);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getCursoAcademica().getId(), new Long(1));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(1));
		Assert.assertNotNull( entidade.getInicioCompetencia() );

        // inserindo 2
        entidade = new CompetenciaGraduacao();
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setCursoAcademica(new CursoAcademica());
        entidade.getCursoAcademica().setId(2l);
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(2l);
        entidade.setInicioCompetencia(new Date());

		dao.salvar(entidade);
		entidade = dao.getById(2l);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getCursoAcademica().getId(), new Long(2));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(2));
		Assert.assertNotNull( entidade.getInicioCompetencia() );

		// alterando
		dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        dao.excluir(1l, 1l);
        transaction.commit();

        entidade = dao.getById(1l);

		Assert.assertNull(entidade);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAOImpl.findByPessoaCurso
	 */
    @Test
    public void testFindByPessoaCurso() {

    	++countTests;

    	List<CompetenciaGraduacao> lista = dao.findByPessoaCurso(1l, 2l);
    	Assert.assertEquals( lista.size(), 0);

    	lista = dao.findByPessoaCurso(1l, 1l);
    	Assert.assertEquals( lista.size(), 1 );

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAOImpl.getByPessoalCompetencia
	 */
    @Test
    public void testGetByPessoalCompetencia() {

    	++countTests;

    	Assert.assertNull( dao.getByPessoalCompetencia(2l, 2l));
    	Assert.assertNotNull( dao.getByPessoalCompetencia(1l, 1l));

    	closeEntityManager(countTests);
    }

}
