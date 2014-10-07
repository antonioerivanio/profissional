package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CboDAOImpl;
import br.gov.ce.tce.srh.domain.Cbo;

public class CboDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Cbo entidade = new Cbo();
	private CboDAOImpl dao = new CboDAOImpl();

	private static int countTests = 0;


	public CboDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Cbo.xml"));
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
		if (countTests == 2) {
			em.close();
			emf.close();
		}
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CboDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getCodigo(), "1");
		Assert.assertEquals( entidade.getDescricao(), "CBO" );
		Assert.assertEquals( entidade.getNivel(), new Long(1));

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.CboDAOImpl.getByCodigo
	 */
    @Test
    public void testGetByCodigo() throws Exception {

    	++countTests;

    	Assert.assertNull( dao.getByCodigo("10") );
    	Assert.assertNotNull( dao.getByCodigo("1") );

    	closeEntityManager(countTests);
    }


    /**
	 * Test of br.gov.ce.tce.srh.dao.CboDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

        List<Cbo> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CboDAOImpl.findByNivel
	 */
	@Test
	public void testFindByNivel() {

    	++countTests;

        List<Cbo> lista = dao.findByNivel(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CboDAOImpl.findByNivelCodigo
	 */
	@Test
	public void testfindByNivelCodigo() {

    	++countTests;

    	List<Cbo> lista = dao.findByNivelCodigo(1l, "1");
    	Assert.assertEquals( lista.size(), 1);

    	closeEntityManager(countTests);
	}

}
