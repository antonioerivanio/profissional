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

import br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl;
import br.gov.ce.tce.srh.domain.TipoLicenca;

public class TipoLicencaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private TipoLicenca entidade;
	private TipoLicencaDAOImpl dao = new TipoLicencaDAOImpl();

	private static int countTests = 0;


	public TipoLicencaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\TipoLicenca.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "TIPO LICENCA 1" );
		Assert.assertEquals( entidade.getFundamentacao(), "LEI TAL");
		Assert.assertEquals( entidade.getSexoValido(), "A");
		Assert.assertEquals( entidade.getQtdeMaximoDias(), new Long(100));
		Assert.assertFalse( entidade.isEspecial() );

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("TIPO LICENCA 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("TIPO");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new TipoLicenca();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new TipoLicenca();
        entidade.setDescricao("Tipo Licenca 1");
        entidade.setFundamentacao("Lei 1");
        entidade.setSexoValido("A");
        entidade.setQtdeMaximoDias(1l);
        entidade.setEspecial(true);

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Licenca 1");
		Assert.assertEquals( entidade.getFundamentacao(), "Lei 1");
		Assert.assertEquals( entidade.getSexoValido(), "A");
		Assert.assertEquals( entidade.getQtdeMaximoDias(), new Long(1l));
		Assert.assertTrue( entidade.isEspecial() );

        // inserindo 2
		entidade = new TipoLicenca();
		entidade.setDescricao("Tipo Licenca 2");
		entidade.setFundamentacao("Lei 2");
		entidade.setSexoValido("M");
		entidade.setQtdeMaximoDias(10l);
		entidade.setEspecial(false);

		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Licenca 2");
		Assert.assertEquals( entidade.getFundamentacao(), "Lei 2");
		Assert.assertEquals( entidade.getSexoValido(), "M");
		Assert.assertEquals( entidade.getQtdeMaximoDias(), new Long(10l));
		Assert.assertFalse( entidade.isEspecial() );

		// alterando
		entidade.setDescricao("Tipo Licenca Update");
		entidade.setFundamentacao("Lei 3");
		entidade.setSexoValido("F");
		entidade.setQtdeMaximoDias(100l);

		dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Tipo Licenca Update");
		Assert.assertEquals( entidade.getFundamentacao(), "Lei 3");
		Assert.assertEquals( entidade.getSexoValido(), "F");
		Assert.assertEquals( entidade.getQtdeMaximoDias(), new Long(100));

		transaction.commit();
		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.excluir
	 */
    @Test
    public void testExcluir(){

    	++countTests;

    	entidade = new TipoLicenca();
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
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("TIPO"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<TipoLicenca> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("TIPO", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.TipoLicencaDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

    	List<TipoLicenca> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
