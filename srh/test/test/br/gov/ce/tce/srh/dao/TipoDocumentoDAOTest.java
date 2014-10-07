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

import br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl;
import br.gov.ce.tce.srh.domain.TipoDocumento;

public class TipoDocumentoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private TipoDocumento entidade;
	private TipoDocumentoDAOImpl dao = new TipoDocumentoDAOImpl();

	private static int countTests = 0;


	public TipoDocumentoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\TipoDocumento.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("TIPO DOCUMENTO 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("TIPO");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new TipoDocumento();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new TipoDocumento();
        entidade.setDescricao("Tipo Documento 1");
        entidade.setEsfera(1l);
        entidade.setDocumentoFuncional(true);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Documento 1");
		Assert.assertEquals( entidade.getEsfera(), new Long(1));
		Assert.assertTrue(entidade.isDocumentoFuncional());

        // inserindo 2
		entidade = new TipoDocumento();
		entidade.setDescricao("Tipo Documento 2");
		entidade.setEsfera(2l);
		entidade.setDocumentoFuncional(false);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Documento 2");
		Assert.assertEquals( entidade.getEsfera(), new Long(2));
		Assert.assertFalse(entidade.isDocumentoFuncional());

		// alterando
		entidade.setDescricao("Tipo Documento Update");
		entidade.setEsfera(3l);
		entidade.setDocumentoFuncional(true);

		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Documento Update");
		Assert.assertEquals( entidade.getEsfera(), new Long(3));
		Assert.assertTrue(entidade.isDocumentoFuncional());

		transaction.commit();
		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new TipoDocumento();
    	entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("DOCUMENTO"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<TipoDocumento> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("DOCUMENTO", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.findByEsfera
	 */
    @Test
    public void testFindByEsfera() {

    	++countTests;

    	List<TipoDocumento> lista = dao.findByEsfera(10l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByEsfera(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.findByDocFuncional
	 */
    @Test
    public void testFindByDocFuncional() {

    	++countTests;

    	List<TipoDocumento> lista = dao.findByDocFuncional(false);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByDocFuncional(true);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


    /**
	 * Test of br.gov.ce.tce.srh.dao.TipoDocumentoDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

    	List<TipoDocumento> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
