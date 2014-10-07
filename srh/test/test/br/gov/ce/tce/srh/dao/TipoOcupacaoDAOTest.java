package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoOcupacaoDAOImpl;
import br.gov.ce.tce.srh.domain.TipoOcupacao;

public class TipoOcupacaoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private TipoOcupacao entidade;
	private TipoOcupacaoDAOImpl dao = new TipoOcupacaoDAOImpl();

	private static int countTests = 0;


	public TipoOcupacaoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\TipoOcupacao.xml"));
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
		if (countTests == 2) {
			em.close();
			emf.close();
		}
	}

	/**
	 * Test of br.gov.ce.tce.srh.dao.TipoOcupacaoDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "Membro" );

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.TipoOcupacaoDAOImpl.findAll
	 */
    public void testFindAll() throws Exception {

    	++countTests;

        List<TipoOcupacao> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	entidade = lista.get(0);

    	Assert.assertEquals( entidade.getId(), new Long(1));
    	Assert.assertEquals( entidade.getDescricao(), "Membro");

    	entidade = new TipoOcupacao();
    	entidade.setId(2l);
    	entidade.setDescricao("outros");

    	Assert.assertEquals( entidade.getId(), new Long(2));
    	Assert.assertEquals( entidade.getDescricao(), "outros");

    	closeEntityManager(countTests);
    }

}
