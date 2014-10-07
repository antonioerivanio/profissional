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

import br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 */
public class RepresentacaoValorDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private RepresentacaoValor entidade;
	private RepresentacaoValorDAOImpl dao = new RepresentacaoValorDAOImpl();

	private static int countTests = 0;


	public RepresentacaoValorDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\RepresentacaoValor.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new RepresentacaoValor();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new RepresentacaoValor();
        entidade.setRepresentacaoCargo(new RepresentacaoCargo());
        entidade.getRepresentacaoCargo().setId(new Long(1));
        entidade.setValorVencimento(1000l);
        entidade.setValorRepresentacao(2000l);
        entidade.setValorDedicacaoExclusiva(5000l);
        entidade.setObservacao("OBS 1");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getRepresentacaoCargo().getId(), new Long(1));
		Assert.assertEquals( entidade.getValorVencimento(), new Long(1000));
		Assert.assertEquals( entidade.getValorRepresentacao(), new Long(2000) );
		Assert.assertEquals( entidade.getValorDedicacaoExclusiva(), new Long(5000) );
		Assert.assertEquals( entidade.getObservacao(), "OBS 1");
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );

        // inserindo 2
        entidade = new RepresentacaoValor();
        entidade.setRepresentacaoCargo(new RepresentacaoCargo());
        entidade.getRepresentacaoCargo().setId(new Long(1));
        entidade.setValorVencimento(2000l);
        entidade.setValorRepresentacao(3000l);
        entidade.setValorDedicacaoExclusiva(4000l);
        entidade.setObservacao("OBS 2");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());

		entidade = dao.salvar(entidade);

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getRepresentacaoCargo().getId(), new Long(1));
		Assert.assertEquals( entidade.getValorVencimento(), new Long(2000));
		Assert.assertEquals( entidade.getValorRepresentacao(), new Long(3000) );
		Assert.assertEquals( entidade.getValorDedicacaoExclusiva(), new Long(4000) );
		Assert.assertEquals( entidade.getObservacao(), "OBS 2");
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );

		// alterando
		entidade.setObservacao("Teste Update");
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getObservacao(), "Teste Update" );

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl.salvar
	 */
    @Test(expected=SRHRuntimeException.class)
    public void testSalvarExistente() {

    	++countTests;

        entidade = new RepresentacaoValor();
        entidade.setRepresentacaoCargo(new RepresentacaoCargo());
        entidade.getRepresentacaoCargo().setId(new Long(1));
        entidade.setValorVencimento(1000l);
        entidade.setValorRepresentacao(2000l);
        entidade.setValorDedicacaoExclusiva(5000l);
        entidade.setObservacao("OBS 1");
        entidade.setInicio(new Date());

        try {

			entidade = dao.salvar(entidade);
			closeEntityManager(countTests);
	        fail("Passou existente!!!");

		} catch (SRHRuntimeException e) {

		}

        closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new RepresentacaoValor();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<RepresentacaoValor> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoValorDAOImpl.getByCargo
	 */
    @Test
    public void testGetByCargo() {

    	++countTests;

    	RepresentacaoValor entidade = dao.getByCargo(2l);
    	Assert.assertNull( entidade );

    	entidade = dao.getByCargo(1l);
    	Assert.assertNotNull( entidade );

    	closeEntityManager(countTests);
    }

}
