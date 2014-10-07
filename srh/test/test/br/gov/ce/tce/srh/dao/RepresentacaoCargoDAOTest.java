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

import br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public class RepresentacaoCargoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private RepresentacaoCargo entidade;
	private RepresentacaoCargoDAOImpl dao = new RepresentacaoCargoDAOImpl();

	private static int countTests = 0;


	public RepresentacaoCargoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\RepresentacaoCargo.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl.salvar
	 */
    @Test(expected=SRHRuntimeException.class)
    public void testSalvar() throws Exception  {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new RepresentacaoCargo();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin(); 
        entidade = new RepresentacaoCargo();
    	entidade.setNomenclatura("Teste");
    	entidade.setOrdem(1l);
    	entidade.setSimbolo("DNS99");
    	entidade.setAtivo(true);
    	entidade.setObservacao("obs 1");

    	entidade = dao.salvar(entidade);

        Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getNomenclatura(), "Teste");
		Assert.assertEquals( entidade.getOrdem(), new Long(1));
		Assert.assertEquals( entidade.getSimbolo(), "DNS99");
		Assert.assertTrue( entidade.isAtivo() );
		Assert.assertEquals( entidade.getObservacao(), "obs 1");
		

		// inserindo 2
        entidade = new RepresentacaoCargo();
    	entidade.setNomenclatura("Teste 2");
    	entidade.setOrdem(2l);
    	entidade.setSimbolo("DNS00");
    	entidade.setAtivo(false);
    	entidade.setObservacao("obs 2");

    	entidade = dao.salvar(entidade);

        Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getNomenclatura(), "Teste 2");
		Assert.assertEquals( entidade.getOrdem(), new Long(2));
		Assert.assertEquals( entidade.getSimbolo(), "DNS00");
		Assert.assertFalse( entidade.isAtivo() );
		Assert.assertEquals( entidade.getObservacao(), "obs 2");

		// alterando
		entidade.setNomenclatura("Teste Update");
		dao.salvar(entidade);

		transaction.commit();
		
        closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new RepresentacaoCargo();
    	entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("Secretário"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<RepresentacaoCargo> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("Secretário", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl.getByNomenclaturaSimbolo
	 */
    @Test
    public void testGetByNomenclaturaSimbolo() throws Exception {

    	++countTests;

        entidade = dao.getByNomenclaturaSimbolo("diretor", "DN001");
    	Assert.assertNull(entidade);

        entidade = dao.getByNomenclaturaSimbolo("Secretário", "DNS01");
    	Assert.assertNotNull(entidade);

    	closeEntityManager(countTests);
    }


    /**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoCargoDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

        List<RepresentacaoCargo> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	entidade = lista.get(0);
		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getOrdem(), new Long(0));
		Assert.assertEquals( entidade.getSimbolo(), "DNS01");
		Assert.assertFalse( entidade.isAtivo() );
		Assert.assertEquals( entidade.getNomenclatura(), "Secretário");
    	
    	closeEntityManager(countTests);
    }
    
}
