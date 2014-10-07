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

import br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.Pessoal;

public class LicencaEspecialDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private LicencaEspecial entidade;
	private LicencaEspecialDAOImpl dao = new LicencaEspecialDAOImpl();

	private static int countTests = 0;


	public LicencaEspecialDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\LicencaEspecial.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new LicencaEspecial();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new LicencaEspecial();
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setQtdedias(4l);
        entidade.setAnoinicial(2000l);
        entidade.setAnofinal(2005l);
        entidade.setDescricao("Teste");
        entidade.setSaldodias(4l);
        entidade.setContaremdobro(true);

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getQtdedias(), new Long(4));
		Assert.assertEquals( entidade.getAnoinicial(), new Long(2000));
		Assert.assertEquals( entidade.getAnofinal(), new Long(2005));
		Assert.assertEquals( entidade.getDescricao(), "Teste");
		Assert.assertEquals( entidade.getSaldodias(), new Long(4));
		Assert.assertTrue( entidade.isContaremdobro() );

        // inserindo 2
        entidade = new LicencaEspecial();
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setQtdedias(2l);
        entidade.setAnoinicial(2010l);
        entidade.setAnofinal(2015l);
        entidade.setDescricao("Teste 2");
        entidade.setSaldodias(2l);
        entidade.setContaremdobro(false);

		entidade = dao.salvar(entidade);
        entidade = dao.getById( entidade.getId() );

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getQtdedias(), new Long(2));
		Assert.assertEquals( entidade.getAnoinicial(), new Long(2010));
		Assert.assertEquals( entidade.getAnofinal(), new Long(2015));
		Assert.assertEquals( entidade.getDescricao(), "Teste 2");
		Assert.assertEquals( entidade.getSaldodias(), new Long(2));
		Assert.assertFalse( entidade.isContaremdobro() );

		// alterando
		entidade = dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.findByPessoa
	 */
    @Test
    public void testFindByPessoa() {

    	++countTests;

    	List<LicencaEspecial> lista = dao.findByPessoal(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoal(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.findByPessoaComRestricoes
	 */
    @Test
    public void testFindByPessoaComRestricoes() {

    	++countTests;

    	List<LicencaEspecial> lista = dao.findByPessoalComSaldo(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoalComSaldo(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        entidade = new LicencaEspecial();
        entidade.setId(1l);

        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

        entidade = dao.getById(1l);

		Assert.assertNull(entidade);

		closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<LicencaEspecial> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaEspecialDAOImpl.ajustarSaldo
	 */
    @Test
    public void testAjustarSaldo() {

    	++countTests;

        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        dao.ajustarSaldo(1l, 3l);
        transaction.commit();

		closeEntityManager(countTests);
    }

}
