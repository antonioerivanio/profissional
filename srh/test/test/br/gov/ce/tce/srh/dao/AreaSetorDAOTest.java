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

import br.gov.ce.tce.srh.dao.AreaSetorDAOImpl;
import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

/**
 *
 * @author robstown
 */
public class AreaSetorDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private AreaSetor entidade;
	private AreaSetorDAOImpl dao = new AreaSetorDAOImpl();

	private static int countTests = 0;


	public AreaSetorDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\AreaSetor.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorDAOImpl.getByDescricao
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getBySetorDescricao(1l, "GERAL");
		Assert.assertNotNull(entidade);

		entidade = dao.getBySetorDescricao(37l, "GERAL");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new AreaSetor();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new AreaSetor();
        entidade.setSetor(new Setor());
        entidade.getSetor().setId(new Long(1));
        entidade.setDescricao("Geral Teste");
        entidade.setCompetenciaMinima(true);
        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getSetor().getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "Geral Teste" );
		Assert.assertEquals( entidade.getCompetenciaMinima(), true);

        // inserindo 2
		entidade = new AreaSetor();
		entidade.setSetor(new Setor());
        entidade.getSetor().setId(new Long(2));
		entidade.setDescricao("Especifico Teste");
		entidade.setCompetenciaMinima(false);
		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getSetor().getId(), new Long(2));
		Assert.assertEquals( entidade.getDescricao(), "Especifico Teste" );
		Assert.assertEquals( entidade.getCompetenciaMinima(), false);

		// alterando
		entidade.setDescricao("Especifico Teste Update");
		entidade.setCompetenciaMinima(true);
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getDescricao(), "Especifico Teste Update" );
		Assert.assertEquals( entidade.getCompetenciaMinima(), true);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new AreaSetor();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l, "TESTE"), 0);
		Assert.assertEquals( dao.count(1l, "GERAL"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<AreaSetor> lista = dao.search(2l, "TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, "GERAL", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorDAOImpl.findBySetor
	 */
    @Test
    public void testFindBySetor() {

    	List<AreaSetor> lista = dao.findBySetor(12l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findBySetor(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(++countTests);
    }

}
