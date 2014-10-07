package test.br.gov.ce.tce.srh.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
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

import br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl;
import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.domain.Competencia;

/**
 *
 * @author robstown
 */
public class AreaSetorCompetenciaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private AreaSetorCompetencia entidade;
	private AreaSetorCompetenciaDAOImpl dao = new AreaSetorCompetenciaDAOImpl();

	private static int countTests = 0;


	public AreaSetorCompetenciaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\AreaSetorCompetencia.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.salvar
	 * 
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		GregorianCalendar inicio = new GregorianCalendar(2011, 8, 24);
		String dataInicio = format.format(inicio.getTime());

		GregorianCalendar fim = new GregorianCalendar(2011, 8, 26);
		String dataFim = format.format(fim.getTime());

    	
        // excluindo
        transaction.begin();

        entidade = new AreaSetorCompetencia();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();

        entidade = new AreaSetorCompetencia();
        entidade.setAreaSetor(new AreaSetor());
        entidade.getAreaSetor().setId(new Long(1));
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(new Long(1));
        entidade.setDataInicio(new Date(inicio.getTimeInMillis()));
        entidade.setMotivoInicio("Motivo Inicio Teste");
        entidade.setDataFim(new Date(fim.getTimeInMillis()));
        entidade.setMotivoFim("Motivo Fim Teste");

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getAreaSetor().getId(), new Long(1));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(1));
		Assert.assertEquals( entidade.getDataInicio().toString(), dataInicio);
		Assert.assertEquals( entidade.getMotivoInicio(), "Motivo Inicio Teste" );
		Assert.assertEquals( entidade.getDataFim().toString(), dataFim );
		Assert.assertEquals( entidade.getMotivoFim(), "Motivo Fim Teste" );


        // inserindo 2
		entidade = new AreaSetorCompetencia();
        entidade.setAreaSetor(new AreaSetor());
        entidade.getAreaSetor().setId(new Long(2));
        entidade.setCompetencia(new Competencia());
        entidade.getCompetencia().setId(new Long(1));
        entidade.setDataInicio(new Date(inicio.getTimeInMillis()));
        entidade.setMotivoInicio("Motivo Inicio Teste 2");
        entidade.setDataFim(new Date(fim.getTimeInMillis()));
        entidade.setMotivoFim("Motivo Fim Teste 2");

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getAreaSetor().getId(), new Long(2));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(1));
		Assert.assertEquals( entidade.getDataInicio().toString(), dataInicio);
		Assert.assertEquals( entidade.getMotivoInicio(), "Motivo Inicio Teste 2" );
		Assert.assertEquals( entidade.getDataFim().toString(), dataFim );
		Assert.assertEquals( entidade.getMotivoFim(), "Motivo Fim Teste 2" );

		// alterando
		entidade.setMotivoInicio("Motivo Inicio Update");
		entidade.setMotivoFim("Motivo Fim Update");

		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getMotivoInicio(), "Motivo Inicio Update" );
		Assert.assertEquals( entidade.getMotivoFim(), "Motivo Fim Update" );

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new AreaSetorCompetencia();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals( dao.count(0l), 0);
    	Assert.assertEquals( dao.count(1l), 1);

    	closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.count
	 */
    @Test
    public void testCountCompetencia() {

    	++countTests;

    	Assert.assertEquals( dao.count(0l, 0l), 0);
    	Assert.assertEquals( dao.count(1l, 1l), 1);

    	closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<AreaSetorCompetencia> lista = dao.search(0l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.search
	 */
    @Test
    public void testSearchCompetencia() {

    	++countTests;

    	List<AreaSetorCompetencia> lista = dao.search(0l, 0l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.findByArea
	 */
    @Test
    public void testFindByArea() {

    	++countTests;

    	List<AreaSetorCompetencia> lista = dao.findByArea(new Long(10));
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByArea(new Long(1));
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }    


	/**
	 * Test of br.gov.ce.tce.srh.dao.AreaSetorCompetenciaDAOImpl.findByAreaCompetencia
	 */
    @Test
    public void testFindByAreaCompetencia() {

    	++countTests;

    	List<AreaSetorCompetencia> lista = dao.findByAreaCompetencia(new Long(0), new Long(0));
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByAreaCompetencia(new Long(1), new Long(1));
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
