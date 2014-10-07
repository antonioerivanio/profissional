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

import br.gov.ce.tce.srh.dao.FuncionalSetorDAOImpl;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

public class FuncionalSetorDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private FuncionalSetor entidade;
	private FuncionalSetorDAOImpl dao = new FuncionalSetorDAOImpl();

	private static int countTests = 0;


	public FuncionalSetorDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\FuncionalSetor.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.FuncionalSetorDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new FuncionalSetor();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new FuncionalSetor();
        entidade.setFuncional(new Funcional());
        entidade.getFuncional().setId(1l);
        entidade.setSetor(new Setor());
        entidade.getSetor().setId(1l);
        entidade.setObservacao("teste 1");
        entidade.setDataInicio( new Date() );
        entidade.setDataFim( new Date() );

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getFuncional().getId(), new Long(1));
		Assert.assertEquals( entidade.getSetor().getId(), new Long(1));
		Assert.assertEquals( entidade.getObservacao(), "teste 1" );
		Assert.assertNotNull( entidade.getDataInicio() );
		Assert.assertNotNull( entidade.getDataFim() );

        // inserindo 2
        entidade = new FuncionalSetor();
        entidade.setFuncional(new Funcional());
        entidade.getFuncional().setId(2l);
        entidade.setSetor(new Setor());
        entidade.getSetor().setId(1l);
        entidade.setObservacao("teste 1");
        entidade.setDataInicio( new Date() );
        entidade.setDataFim( new Date() );

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getFuncional().getId(), new Long(2));
		Assert.assertEquals( entidade.getSetor().getId(), new Long(1));
		Assert.assertEquals( entidade.getObservacao(), "teste 1" );
		Assert.assertNotNull( entidade.getDataInicio() );
		Assert.assertNotNull( entidade.getDataFim() );

		// alterando
		entidade = dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalSetorlDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new FuncionalSetor();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        List<FuncionalSetor> lista = dao.findByPessoal(1l);

		Assert.assertEquals(lista.size(), 0l);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalSetorlDAOImpl.excluirAll
	 */
    @Test
    public void testExcluirAll() {

    	++countTests;

        EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        dao.excluirAll(1l);
        transaction.commit();

        List<FuncionalSetor> lista = dao.findByPessoal(1l);

		Assert.assertEquals(lista.size(), 0l);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalSetorDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals(dao.count(2l), 0);
    	Assert.assertEquals(dao.count(1l), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalSetorDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<FuncionalSetor> lista = dao.search(2l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FuncionalSetorDAOImpl.findByPessoal
	 */
    @Test
    public void testFindByPessoal() {

    	++countTests;

    	List<FuncionalSetor> lista = dao.findByPessoal(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoal(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
