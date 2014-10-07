package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
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

import br.gov.ce.tce.srh.dao.RacaDAOImpl;
import br.gov.ce.tce.srh.domain.Raca;

public class RacaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Raca entidade;
	private RacaDAOImpl dao = new RacaDAOImpl();

	private static int countTests = 0;


	public RacaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Raca.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.RacaDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("RACA 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("RACA");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RacaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Raca();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Raca();
        entidade.setDescricao("Raca 1");

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Raca 1");

        // inserindo 2
		entidade = new Raca();
		entidade.setDescricao("Raca 2");

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Raca 2");

		// alterando
		entidade.setDescricao("Raca Update");
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Raca Update");

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RacaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new Raca();
    	entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.RacaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("RACA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RacaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Raca> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("RACA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.RacaDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

    	List<Raca> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
