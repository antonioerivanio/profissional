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

import br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.Simbolo;

public class ClasseReferenciaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private ClasseReferencia entidade;
	private ClasseReferenciaDAOImpl dao = new ClasseReferenciaDAOImpl();

	private static int countTests = 0;


	public ClasseReferenciaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\ClasseReferencia.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl.getBySimboloReferencia
	 */
	public void testGetBySimboloReferencia() {

		++countTests;

		entidade = dao.getBySimboloReferencia(1l, 1l);
		Assert.assertNotNull(entidade);

		entidade = dao.getBySimboloReferencia(2l, 2l);
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new ClasseReferencia();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new ClasseReferencia();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setReferencia(new Long(1));
        entidade.setClasse("A");
		entidade.setSimbolo(new Simbolo());
		entidade.getSimbolo().setId(new Long(1));
		entidade.setHorasExigidas(new Long(360));

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getReferencia(), new Long(1));
		Assert.assertEquals( entidade.getClasse(), "A");
		Assert.assertEquals( entidade.getSimbolo().getId(), new Long(1));
		Assert.assertEquals( entidade.getHorasExigidas(), new Long(360));

        // inserindo 2
        entidade = new ClasseReferencia();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setReferencia(new Long(2));
        entidade.setClasse("B");
		entidade.setSimbolo(new Simbolo());
		entidade.getSimbolo().setId(new Long(1));
		entidade.setHorasExigidas(new Long(100));

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getReferencia(), new Long(2));
		Assert.assertEquals( entidade.getClasse(), "B");
		Assert.assertEquals( entidade.getSimbolo().getId(), new Long(1));
		Assert.assertEquals( entidade.getHorasExigidas(), new Long(100));

		// alterando
		entidade.setClasse("C");
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getClasse(), "C");

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new ClasseReferencia();
    	entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<ClasseReferencia> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.ClasseReferenciaDAOImpl.findByCargo
	 */
    @Test
    public void testFindByCargo() {

    	++countTests;

    	List<ClasseReferencia> lista = dao.findByCargo(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByCargo(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
