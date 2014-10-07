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

import br.gov.ce.tce.srh.dao.FolhaDAOImpl;
import br.gov.ce.tce.srh.domain.Folha;

/**
 *
 * @author robstown
 */
public class FolhaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Folha entidade;
	private FolhaDAOImpl dao = new FolhaDAOImpl();

	private static int countTests = 0;


	public FolhaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Folha.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("FOLHA 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("FOLHA");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}

	
	/**
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new Folha();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Folha();
        entidade.setCodigo("1");
        entidade.setDescricao("Folha Teste 1");
        entidade.setAtivo(true);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getCodigo(), "1");
		Assert.assertEquals( entidade.getDescricao(), "Folha Teste 1" );
		Assert.assertEquals( entidade.getAtivo(), true );

        // inserindo 2
		entidade = new Folha();
        entidade.setCodigo("2");
		entidade.setDescricao("Folha Teste 2");
		entidade.setAtivo(false);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getCodigo(), "2");
		Assert.assertEquals( entidade.getDescricao(), "Folha Teste 2" );
		Assert.assertEquals( entidade.getAtivo(), false);

		// alterando
		entidade.setCodigo("3");
		entidade.setDescricao("Folha Update");
		entidade.setAtivo(true);

		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Folha Update" );
		Assert.assertEquals( entidade.getAtivo(), true);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new Folha();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("FOLHA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Folha> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("FOLHA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.getByCodigo
	 */
    @Test
    public void testGetByCodigo() {

    	++countTests;

    	entidade = dao.getByCodigo("2");
    	Assert.assertNull(entidade);

    	entidade = dao.getByCodigo("1");
    	Assert.assertNotNull(entidade);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FolhaDAOImpl.findByAtivo
	 */
    @Test
    public void testFindByAtivo() {

    	++countTests;

    	List<Folha> lista = dao.findByAtivo(true);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByAtivo(false);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
