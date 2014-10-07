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

import br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAOImpl;
import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetorPk;

public class FuncionalAreaSetorDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private FuncionalAreaSetor entidade;
	private FuncionalAreaSetorDAOImpl dao = new FuncionalAreaSetorDAOImpl();

	private static int countTests = 0;


	public FuncionalAreaSetorDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\FuncionalAreaSetor.xml"));
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
		if (countTests == 3) {
			em.close();
			emf.close();
		}
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new FuncionalAreaSetor();
        entidade.setPk(new FuncionalAreaSetorPk());
        entidade.getPk().setAreaSetor(1l);
        entidade.getPk().setFuncional(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new FuncionalAreaSetor();
        entidade.setPk(new FuncionalAreaSetorPk());
        entidade.setAreaSetor(new AreaSetor());
        entidade.getAreaSetor().setId(1l);
        entidade.setFuncional(new Funcional());
        entidade.getFuncional().setId(1l);
        entidade.getPk().setAreaSetor(1l);
        entidade.getPk().setFuncional(1l);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getAreaSetor().getId(), new Long(1));
		Assert.assertEquals( entidade.getFuncional().getId(), new Long(1));
		Assert.assertEquals( entidade.getPk().getAreaSetor(), new Long(1));
		Assert.assertEquals( entidade.getPk().getFuncional(), new Long(1));

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalAreaSetorlDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new FuncionalAreaSetor();
        entidade.setPk(new FuncionalAreaSetorPk());
        entidade.getPk().setAreaSetor(1l);
        entidade.getPk().setFuncional(1l);
        dao.excluir(entidade);
        transaction.commit();

        List<FuncionalAreaSetor> lista = dao.findByFuncional(1l);

		Assert.assertEquals(lista.size(), 0l);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals(dao.count(2l), 0);
    	Assert.assertEquals(dao.count(1l), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<FuncionalAreaSetor> lista = dao.search(2l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAOImpl.findByFuncional
	 */
    @Test
    public void testFindByFuncional() {

    	++countTests;

    	List<FuncionalAreaSetor> lista = dao.findByFuncional(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByFuncional(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAOImpl.findyByAreaSetor
	 */
    @Test
    public void testFindyByAreaSetor() {

    	++countTests;

    	List<FuncionalAreaSetor> lista = dao.findyByAreaSetor(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findyByAreaSetor(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
