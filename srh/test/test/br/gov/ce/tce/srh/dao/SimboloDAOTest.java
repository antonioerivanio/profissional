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

import br.gov.ce.tce.srh.dao.SimboloDAOImpl;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;

/**
 *
 * @author robstown
 */
public class SimboloDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Simbolo entidade;
	private SimboloDAOImpl dao = new SimboloDAOImpl();

	private static int countTests = 0;


	public SimboloDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Simbolo.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.SimboloDAOImpl.getByOcupacaoSimbolo
	 */
	public void testGetByDescricao() {

		++countTests;

		entidade = dao.getByOcupacaoSimbolo(1l, "TCERH");
		Assert.assertNotNull(entidade);

		entidade = dao.getByOcupacaoSimbolo(1l, "TESTE");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.SimboloDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Simbolo();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Simbolo();
        entidade.setOcupacao(new Ocupacao());
        entidade.getOcupacao().setId(new Long(1));
        entidade.setSimbolo("TCE 1");

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getOcupacao().getId(), new Long(1));
		Assert.assertEquals( entidade.getSimbolo(), "TCE 1" );

        // inserindo 2
		entidade = new Simbolo();
        entidade = new Simbolo();
        entidade.setOcupacao(new Ocupacao());
        entidade.getOcupacao().setId(new Long(1));
        entidade.setSimbolo("TCE 2");

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getOcupacao().getId(), new Long(1));
		Assert.assertEquals( entidade.getSimbolo(), "TCE 2" );

		// alterando
		entidade.setSimbolo("TCEup");
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getSimbolo(), "TCEup" );

		transaction.commit();
		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.SimboloDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new Simbolo();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.SimboloDAOImpl.excluirAll
	 */
    @Test
    public void testExcluirAll(){

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluirAll(1l);
        transaction.commit();

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.SimboloDAOImpl.findByOcupacao
	 */
    @Test
    public void testFindByOcupacao() {

    	++countTests;

    	List<Simbolo> lista = dao.findByOcupacao(0l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByOcupacao(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
