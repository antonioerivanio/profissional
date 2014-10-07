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

import br.gov.ce.tce.srh.dao.EspecialidadeCargoDAOImpl;
import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Ocupacao;

public class EspecialidadeCargoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private EspecialidadeCargo entidade;
	private EspecialidadeCargoDAOImpl dao = new EspecialidadeCargoDAOImpl();

	private static int countTests = 0;


	public EspecialidadeCargoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\EspecialidadeCargo.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.EspecialidadeCargoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new EspecialidadeCargo();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new EspecialidadeCargo();
        entidade.setOcupacao( new Ocupacao() );
        entidade.getOcupacao().setId(1l);
        entidade.setEspecialidade( new Especialidade() );
        entidade.getEspecialidade().setId(1l);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getEspecialidade().getId(), new Long(1));
		Assert.assertEquals( entidade.getOcupacao().getId(), new Long(1));

        // inserindo 2
        entidade = new EspecialidadeCargo();
        entidade.setOcupacao( new Ocupacao() );
        entidade.getOcupacao().setId(1l);
        entidade.setEspecialidade( new Especialidade() );
        entidade.getEspecialidade().setId(2l);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getEspecialidade().getId(), new Long(2));
		Assert.assertEquals( entidade.getOcupacao().getId(), new Long(1));

		// alterando
		entidade = dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.EspecialidadeCargoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        entidade = new EspecialidadeCargo();
        entidade.setId(1l);

        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }

    
	/**
	 * Test of br.gov.ce.tce.srh.dao.EspecialidadeCargoDAOImpl.excluirAll
	 */
    @Test
    public void testExcluirAll() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        dao.excluirAll( 1l );
        transaction.commit();

		closeEntityManager(countTests);
    }
    

	/**
	 * Test of br.gov.ce.tce.srh.dao.EspecialidadeCargoDAOImpl.findByOcupacao
	 */
    @Test
    public void testFindByOcupacao() {

    	++countTests;

    	List<EspecialidadeCargo> lista = dao.findByOcupacao(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByOcupacao(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
