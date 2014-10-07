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

import br.gov.ce.tce.srh.dao.RubricaDAOImpl;
import br.gov.ce.tce.srh.domain.Rubrica;

public class RubricaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Rubrica entidade;
	private RubricaDAOImpl dao = new RubricaDAOImpl();

	private static int countTests = 0;


	public RubricaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Rubrica.xml"));
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
		if (countTests == 7) {
			em.close();
			emf.close();
		}
	}




	/**
	 * Test of br.gov.ce.tce.srh.dao.RubricaDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByDescricao("RUBRICA 1");
		Assert.assertNotNull(entidade);

		entidade = dao.getByDescricao("RUBRICA");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RubricaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Rubrica();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Rubrica();
        entidade.setCodigo("100");
        entidade.setOrdem(new Long(1));
        entidade.setTipo("V");
        entidade.setDescricao("Rubrica 1");
        entidade.setEmprestimo(true);
        entidade.setConsignacao(true);
        entidade.setSupsec(true);
        entidade.setIrrf(true);
        entidade.setVerbaExtra(true);
        entidade.setVerificaTeto(true);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getCodigo(), "100");
		Assert.assertEquals( entidade.getOrdem(), new Long(1));
		Assert.assertEquals( entidade.getTipo(), "V");
		Assert.assertEquals( entidade.getDescricao(), "Rubrica 1");
		Assert.assertTrue(entidade.isEmprestimo());
		Assert.assertTrue(entidade.isConsignacao());
		Assert.assertTrue(entidade.isSupsec());
		Assert.assertTrue(entidade.isIrrf());
		Assert.assertTrue(entidade.isVerbaExtra());
		Assert.assertTrue(entidade.isVerificaTeto());

        // inserindo 2
		entidade = new Rubrica();
        entidade.setCodigo("200");
        entidade.setOrdem(new Long(2));
        entidade.setTipo("D");
		entidade.setDescricao("RUBRICA 2");
        entidade.setEmprestimo(false);
        entidade.setConsignacao(false);
        entidade.setSupsec(false);
        entidade.setIrrf(false);
        entidade.setVerbaExtra(false);
        entidade.setVerificaTeto(false);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getCodigo(), "200");
		Assert.assertEquals( entidade.getOrdem(), new Long(2));
		Assert.assertEquals( entidade.getTipo(), "D");
		Assert.assertEquals( entidade.getDescricao(), "RUBRICA 2");
		Assert.assertFalse(entidade.isEmprestimo());
		Assert.assertFalse(entidade.isConsignacao());
		Assert.assertFalse(entidade.isSupsec());
		Assert.assertFalse(entidade.isIrrf());
		Assert.assertFalse(entidade.isVerbaExtra());
		Assert.assertFalse(entidade.isVerificaTeto());

		// alterando
		entidade.setDescricao("Rubrica Update");
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Rubrica Update");

		transaction.commit();
		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RubricaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new Rubrica();
    	entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RubricaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("RUBRICA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RubricaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Rubrica> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("RUBRICA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RubricaDAOImpl.getByCodigo
	 */
    @Test
    public void testGetByCodigo() {

    	++countTests;

    	entidade = dao.getByCodigo("1");
    	Assert.assertNull(entidade);

    	entidade = dao.getByCodigo("100");
    	Assert.assertNotNull(entidade);

    	closeEntityManager(countTests);
    }

}
