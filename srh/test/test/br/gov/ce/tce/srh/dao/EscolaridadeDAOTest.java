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

import br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl;
import br.gov.ce.tce.srh.domain.Escolaridade;

public class EscolaridadeDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Escolaridade entidade;
	private EscolaridadeDAOImpl dao = new EscolaridadeDAOImpl();

	private static int countTests = 0;


	public EscolaridadeDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Escolaridade.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new Escolaridade();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Escolaridade();
        entidade.setDescricao("Superior Incompleto");
        entidade.setOrdem(new Long(1));
        entidade.setObservacao("Escolaridade");
        entidade.setCodigoRais(new Long(1));

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Superior Incompleto");
		Assert.assertEquals( entidade.getOrdem(), new Long(1));
		Assert.assertEquals( entidade.getObservacao(), "Escolaridade");
		Assert.assertEquals( entidade.getCodigoRais(), new Long(1));

        // inserindo 2
		entidade = new Escolaridade();
		entidade.setDescricao("Superior Completo");
		entidade.setOrdem(new Long(2));
		entidade.setObservacao("Escolaridade");
		entidade.setCodigoRais(new Long(2));

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Superior Completo");
		Assert.assertEquals( entidade.getOrdem(), new Long(2));
		Assert.assertEquals( entidade.getObservacao(), "Escolaridade");
		Assert.assertEquals( entidade.getCodigoRais(), new Long(2));

		// alterando
		entidade.setDescricao("Superior Completo Update");
		entidade.setOrdem(new Long(3));
		entidade.setObservacao("Escolaridade Update");
		entidade.setCodigoRais(new Long(3));

		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Superior Completo Update");
		Assert.assertEquals( entidade.getOrdem(), new Long(3));
		Assert.assertEquals( entidade.getObservacao(), "Escolaridade Update");
		Assert.assertEquals( entidade.getCodigoRais(), new Long(3));

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new Escolaridade();
    	entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("ESCOLARIDADE"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Escolaridade> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("ESCOLARIDADE", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("ESCOLARIDADE 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("ESCOLARIDADE");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.getByOrdem
	 */
	public void testGetByOrdem() {

		++countTests;

		entidade = dao.getByOrdem(1l);
		Assert.assertNotNull(entidade);

		entidade = dao.getByOrdem(0l);
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.EscolaridadeDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

        List<Escolaridade> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
