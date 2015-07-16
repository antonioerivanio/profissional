package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

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

import br.gov.ce.tce.srh.dao.ParametroDAOImpl;
import br.gov.ce.tce.srh.domain.Parametro;

public class ParametroDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private static EntityManagerFactory emf;
	private static EntityManager em;

	private ParametroDAOImpl dao = new ParametroDAOImpl();

	private static int countTests = 0;


	public ParametroDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Parametro.xml"));
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
		if (countTests == 1) {
			em.close();
			emf.close();
		}
	}

	/**
	 * Test of br.gov.ce.tce.srh.sapjava.dao.ParametroDAOImpl.getByNome
	 */
    @Test
    public void testGetByNome() throws Exception {

    	++countTests;

    	Parametro parametro = dao.getByNome("CAMARQPUB");
    	Assert.assertNotNull(parametro);

    	parametro = dao.getByNome("nome");
    	Assert.assertNull(parametro);

    	closeEntityManager(++countTests);
    }

}
