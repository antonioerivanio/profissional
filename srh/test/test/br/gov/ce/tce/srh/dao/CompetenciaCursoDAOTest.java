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

import br.gov.ce.tce.srh.dao.CompetenciaCursoDAOImpl;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 */
public class CompetenciaCursoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private CompetenciaCurso entidade;
	private CompetenciaCursoDAOImpl dao = new CompetenciaCursoDAOImpl();

	private static int countTests = 0;


	public CompetenciaCursoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\CompetenciaCurso.xml"));
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
		if (countTests == 5) {
			em.close();
			emf.close();
		}
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaCursoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        dao.excluir(1l);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new CompetenciaCurso();
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(1l);
        entidade.setCursoProfissional(new CursoProfissional());
        entidade.getCursoProfissional().setId(1l);
        entidade.setIniciocompetencia(new Date());

        dao.salvar(entidade);
        entidade = dao.getById(1l);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(1));
		Assert.assertEquals( entidade.getCursoProfissional().getId(), new Long(1));
		Assert.assertNotNull( entidade.getIniciocompetencia() );

        // inserindo 2
        entidade = new CompetenciaCurso();
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(2l);
        entidade.setCursoProfissional(new CursoProfissional());
        entidade.getCursoProfissional().setId(1l);
        entidade.setIniciocompetencia(new Date());

		dao.salvar(entidade);
		entidade = dao.getById(2l);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(2));
		Assert.assertEquals( entidade.getCursoProfissional().getId(), new Long(1));
		Assert.assertNotNull( entidade.getIniciocompetencia() );

		// alterando
		dao.salvar(entidade);
		
		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaCursoDAOImpl.salvar
	 */
    @Test(expected=SRHRuntimeException.class)
    public void testSalvarExistente() {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

        entidade = new CompetenciaCurso();
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(1l);
        entidade.setCursoProfissional(new CursoProfissional());
        entidade.getCursoProfissional().setId(1l);
        entidade.setIniciocompetencia(new Date());

        try {

        	transaction.begin();
			dao.salvar(entidade);
			transaction.commit();
			closeEntityManager(countTests);
	        fail("Passou existente!!!");

		} catch (SRHRuntimeException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}

        closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaCursoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(1l);
        transaction.commit();
        entidade = dao.getById(1l);

		Assert.assertNull(entidade);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaCursoDAOImpl.findByCurso
	 */
    @Test
    public void testFindByCurso() {

    	++countTests;

    	List<CompetenciaCurso> lista = dao.findByCurso(2l);
    	Assert.assertEquals( lista.size(), 0);

    	lista = dao.findByCurso(1l);
    	Assert.assertEquals( lista.size(), 1 );

    	closeEntityManager(countTests);
    }

}
