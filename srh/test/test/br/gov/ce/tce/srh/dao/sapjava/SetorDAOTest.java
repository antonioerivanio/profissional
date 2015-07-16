package test.br.gov.ce.tce.srh.dao.sapjava;

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

import br.gov.ce.tce.srh.sapjava.dao.SetorDAOImpl;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public class SetorDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private static EntityManagerFactory emf;
	private static EntityManager em;

	private SetorDAOImpl dao = new SetorDAOImpl();

	private static int countTests = 0;


	public SetorDAOTest() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
		dao.setEntityManager(em);	
	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {

		@SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection(
        		"jdbc:oracle:thin:@localhost:1521:XE", "SAPJAVA", "sapjava");
        jdbcConnection.setAutoCommit(true);

		return new DatabaseConnection(jdbcConnection);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\sapjava\\Setor.xml"));
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
	 * Test of br.gov.ce.tce.srh.sapjava.dao.SetorDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	Setor entidade = new Setor();
    	entidade.setId(new Long(1));
    	entidade.setNome("INFORMATICA");
    	entidade.setTipo(1l);

    	List<Setor> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 3);

    	Setor entidade1 = lista.get(0);
    	Assert.assertEquals( entidade1.getId(), new Long(2));
    	Assert.assertEquals( entidade1.getNome(), "EXECUTIVO");
    	Assert.assertEquals( entidade.getTipo(), new Long(1));

    	closeEntityManager(++countTests);
    }

}
