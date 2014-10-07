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

import br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl;
import br.gov.ce.tce.srh.domain.TipoMovimento;

public class TipoMovimentoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private TipoMovimento entidade;
	private TipoMovimentoDAOImpl dao = new TipoMovimentoDAOImpl();

	private static int countTests = 0;


	public TipoMovimentoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\TipoMovimento.xml"));
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
		if (countTests == 6) {
			em.close();
			emf.close();
		}
	}

	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.getById
	 */
	public void testGetById() {

		++countTests;
		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "TIPO MOVIMENTO ENTRADA" );
		Assert.assertEquals( entidade.getTipo(), new Long(1));
		Assert.assertEquals( entidade.getAbreviatura(), "TIPO MOVIMENTO");

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("TIPO MOVIMENTO ENTRADA");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("TIPO");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.salvar
	 */
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new TipoMovimento();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new TipoMovimento();
        entidade.setDescricao("Tipo Movimento 1");
        entidade.setTipo(1l);
        entidade.setAbreviatura("Tipo Movimento 1");

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Movimento 1");
		Assert.assertEquals( entidade.getTipo(), new Long(1));
		Assert.assertEquals( entidade.getAbreviatura(), "Tipo Movimento 1");

        // inserindo 2
		entidade = new TipoMovimento();
		entidade.setDescricao("Tipo Movimento 2");
		entidade.setTipo(2l);
		entidade.setAbreviatura("Tipo Movimento 2");

		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Movimento 2");
		Assert.assertEquals( entidade.getTipo(), new Long(2));
		Assert.assertEquals( entidade.getAbreviatura(), "Tipo Movimento 2");

		// alterando
		entidade.setDescricao("Tipo Movimento Update");
		entidade.setTipo(3l);
		entidade.setAbreviatura("Tipo Movimento Up");

		dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Movimento Update");
		Assert.assertEquals( entidade.getTipo(), new Long(3));
		Assert.assertEquals( entidade.getAbreviatura(), "Tipo Movimento Up");

		transaction.commit();
		closeEntityManager(countTests);
    }
 

	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.excluir
	 */
    public void testExcluir() {

    	++countTests;

    	entidade = new TipoMovimento();
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
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("MOVIMENTO"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<TipoMovimento> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("MOVIMENTO", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.findByTipo
	 */
    public void testFindByTipo() {

    	++countTests;

    	List<TipoMovimento> lista = dao.findByTipo(0l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByTipo(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


    /**
	 * Test of br.gov.ce.tce.srh.dao.TipoMovimentoDAOImpl.findAll
	 */
    public void testFindAll() throws Exception {

    	++countTests;

        List<TipoMovimento> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
