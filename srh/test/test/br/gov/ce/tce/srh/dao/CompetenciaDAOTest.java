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

import br.gov.ce.tce.srh.dao.CompetenciaDAOImpl;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 */
public class CompetenciaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Competencia entidade;
	private CompetenciaDAOImpl dao = new CompetenciaDAOImpl();

	private static int countTests = 0;


	public CompetenciaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Competencia.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "COMPETENCIA 1" );
		Assert.assertEquals( entidade.getAtivo(), false);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("COMPETENCIA 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("COMPETENCIA");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}

	
	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Competencia();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Competencia();
        entidade.setDescricao("Competencia Teste 1");
        entidade.setAtivo(true);
        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Competencia Teste 1" );
		Assert.assertEquals( entidade.getAtivo(), true);

        // inserindo 2
		entidade = new Competencia();
		entidade.setDescricao("Competencia Teste 2");
		entidade.setAtivo(false);
		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Competencia Teste 2" );
		Assert.assertEquals( entidade.getAtivo(), false);

		// alterando
		entidade.setDescricao("Competencia Update");
		entidade.setAtivo(true);
		dao.salvar(entidade);

		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Competencia Update" );
		Assert.assertEquals( entidade.getAtivo(), true);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.excluir
	 * 
	 * @throws SRHRuntimeException 
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new Competencia();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();
        entidade = dao.getById(1l);

		Assert.assertNull(entidade);

		closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("COMPETENCIA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Competencia> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("COMPETENCIA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.CompetenciaDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

        List<Competencia> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
