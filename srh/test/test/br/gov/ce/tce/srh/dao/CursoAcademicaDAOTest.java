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

import br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl;
import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.domain.CursoAcademica;

/**
 *
 * @author robstown
 */
public class CursoAcademicaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private CursoAcademica entidade;
	private CursoAcademicaDAOImpl dao = new CursoAcademicaDAOImpl();

	private static int countTests = 0;


	public CursoAcademicaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\CursoAcademica.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getArea().getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "ENGENHARIA DE SOFTWARE" );

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByAreaDescricao(1l, "ENGENHARIA DE SOFTWARE");
		Assert.assertNotNull(entidade);

		entidade = dao.getByAreaDescricao(1l, "AREA");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new CursoAcademica();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new CursoAcademica();
        entidade.setArea(new AreaAcademica());
        entidade.getArea().setId(new Long(1));
        entidade.setDescricao("Geral Teste");

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getArea().getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "Geral Teste");

        // inserindo 2
		entidade = new CursoAcademica();
		entidade.setArea(new AreaAcademica());
        entidade.getArea().setId(new Long(2));
		entidade.setDescricao("Especifico Teste");

		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getArea().getId(), new Long(2));
		Assert.assertEquals( entidade.getDescricao(), "Especifico Teste");

		// alterando
		entidade.setDescricao("Especifico Teste Update");
		dao.salvar(entidade);

		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Especifico Teste Update" );

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new CursoAcademica();
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
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("ENGENHARIA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.count
	 */
	public void testCountArea() {

		++countTests;

		Assert.assertEquals( dao.count(0l, "TESTE"), 0);
		Assert.assertEquals( dao.count(1l, "ENGENHARIA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<CursoAcademica> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("ENGENHARIA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.search
	 */
	public void testSearchArea() {

		++countTests;

		List<CursoAcademica> lista = dao.search(0l, "TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, "ENGENHARIA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoAcademicaDAOImpl.findAll
	 */
	public void testFindAll() {

		++countTests;

		List<CursoAcademica> lista = dao.findAll();
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}

}
