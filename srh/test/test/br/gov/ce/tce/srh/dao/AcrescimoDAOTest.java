package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
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

import br.gov.ce.tce.srh.dao.AcrescimoDAOImpl;
import br.gov.ce.tce.srh.domain.Acrescimo;
import br.gov.ce.tce.srh.domain.Pessoal;

public class AcrescimoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Acrescimo entidade;
	private AcrescimoDAOImpl dao = new AcrescimoDAOImpl();

	private static int countTests = 0;


	public AcrescimoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Acrescimo.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Acrescimo();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Acrescimo();
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setDescricao("teste");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setQtdeDias(1l);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "teste");
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertEquals( entidade.getQtdeDias(), new Long(1));


        // inserindo 2
        entidade = new Acrescimo();
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setDescricao("teste 2");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setQtdeDias(2l);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "teste 2");
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertEquals( entidade.getQtdeDias(), new Long(2));


		// alterando
		entidade = dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        entidade = new Acrescimo();
        entidade.setId(1l);

        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }
    
    
	/**
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.count
	 */
    @Test
    public void count() {

    	++countTests;

    	Assert.assertEquals(dao.count(2l), 0);
    	Assert.assertEquals(dao.count(1l), 1);

    	closeEntityManager(countTests);
    }
    

	/**
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.search
	 */
    @Test
    public void search() {

    	++countTests;

    	List<Acrescimo> lista = dao.search(2l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals(dao.count(2l), 0);
    	Assert.assertEquals(dao.count(1l), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<Acrescimo> lista = dao.search(2l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AcrescimoDAOImpl.findByPessoal
	 */
    @Test
    public void testFindByPessoal() {

    	++countTests;

    	List<Acrescimo> lista = dao.findByPessoal(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoal(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
