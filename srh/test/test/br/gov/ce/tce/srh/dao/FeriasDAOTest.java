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

import br.gov.ce.tce.srh.dao.FeriasDAOImpl;
import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.domain.TipoPublicacao;

public class FeriasDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Ferias entidade;
	private FeriasDAOImpl dao = new FeriasDAOImpl();

	private static int countTests = 0;


	public FeriasDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Ferias.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Ferias();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Ferias();
        entidade.setFuncional( new Funcional() );
        entidade.getFuncional().setId(1l);
        entidade.setTipoFerias( new TipoFerias() );
        entidade.getTipoFerias().setId(1l);
        entidade.setAnoReferencia( 2012l );
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setDataDoAto(new Date());
        entidade.setDataPublicacao(new Date());
        entidade.setObservacao("teste");
        entidade.setQtdeDias(20l);
        entidade.setPeriodo( 1l );
        entidade.setTipoPublicacao( new TipoPublicacao() );
        entidade.getTipoPublicacao().setId(1l);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getFuncional().getId(), new Long(1));
		Assert.assertEquals( entidade.getTipoFerias().getId(), new Long(1));
		Assert.assertEquals( entidade.getAnoReferencia(), new Long(2012));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertNotNull( entidade.getDataDoAto() );
		Assert.assertNotNull( entidade.getDataPublicacao() );
		Assert.assertEquals( entidade.getObservacao(), "teste");
		Assert.assertEquals( entidade.getQtdeDias(), new Long(20) );
		Assert.assertEquals( entidade.getPeriodo(), new Long(1));
		Assert.assertEquals( entidade.getTipoPublicacao().getId(), new Long(1));

        // inserindo 2
        entidade = new Ferias();
        entidade.setFuncional( new Funcional() );
        entidade.getFuncional().setId(1l);
        entidade.setTipoFerias( new TipoFerias() );
        entidade.getTipoFerias().setId(1l);
        entidade.setAnoReferencia( 2013l );
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setDataDoAto(new Date());
        entidade.setDataPublicacao(new Date());
        entidade.setObservacao("teste 2");
        entidade.setQtdeDias(30l);
        entidade.setPeriodo( 2l );
        entidade.setTipoPublicacao( new TipoPublicacao() );
        entidade.getTipoPublicacao().setId(1l);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getFuncional().getId(), new Long(1));
		Assert.assertEquals( entidade.getTipoFerias().getId(), new Long(1));
		Assert.assertEquals( entidade.getAnoReferencia(), new Long(2013));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertNotNull( entidade.getDataDoAto() );
		Assert.assertNotNull( entidade.getDataPublicacao() );
		Assert.assertEquals( entidade.getObservacao(), "teste 2");
		Assert.assertEquals( entidade.getQtdeDias(), new Long(30) );
		Assert.assertEquals( entidade.getPeriodo(), new Long(2));
		Assert.assertEquals( entidade.getTipoPublicacao().getId(), new Long(1));

		// alterando
		entidade.setObservacao("update");
		entidade.setQtdeDias(35l);
		entidade.setPeriodo(1l);

		dao.salvar(entidade);
		
		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        entidade = new Ferias();
        entidade.setId(1l);

        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals(dao.count(10l), 0);
    	Assert.assertEquals(dao.count(1l), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<Ferias> lista = dao.search(10l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.findByPessoal
	 */
    @Test
    public void testFindByPessoal() {

    	++countTests;

    	List<Ferias> lista = dao.findByPessoal(10l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoal(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.findByPessoalPeriodoReferencia
	 */
    @Test
    public void testFindByPessoalPeriodoReferencia() {

    	++countTests;

    	List<Ferias> lista = dao.findByPessoalPeriodoReferencia(10l, 1l, 2012l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoalPeriodoReferencia(1l, 1l, 2012l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.FeriasDAOImpl.findByPessoalTipo
	 */
    @Test
    public void testFindByPessoalTipo() {

    	++countTests;

    	List<Ferias> lista = dao.findByPessoalTipo(10l, 1l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoalTipo(1l, 1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
