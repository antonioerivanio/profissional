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

import br.gov.ce.tce.srh.dao.OcupacaoDAOImpl;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.TipoOcupacao;

public class OcupacaoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Ocupacao entidade;
	private OcupacaoDAOImpl dao = new OcupacaoDAOImpl();

	private static int countTests = 0;


	public OcupacaoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Ocupacao.xml"));
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
		if (countTests == 6) {
			em.close();
			emf.close();
		}
	}

	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);
		Assert.assertNotNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.getByNomenclatura
	 */
	public void testGetByMoneclatura() {

		++countTests;

		entidade = dao.getByNomenclatura("nomenclatura");
		Assert.assertNotNull(entidade);

		entidade = dao.getByNomenclatura("nomenclatura FALSA");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Ocupacao();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Ocupacao();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setTipoOcupacao(new TipoOcupacao());
        entidade.getTipoOcupacao().setId(1L);
        entidade.setQuantidade(new Long(10));
        entidade.setSituacao(new Long(0));
        entidade.setReferenciaInicial(new Long(0));
        entidade.setReferenciaFinal(new Long(10));
        entidade.setNomenclatura("nomenclatura 1");
        entidade.setCargoIsolado( false );

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getTipoOcupacao().getId(), new Long(1));
		Assert.assertEquals( entidade.getQuantidade(), new Long(10));
		Assert.assertEquals( entidade.getSituacao(), new Long(0));
		Assert.assertEquals( entidade.getReferenciaInicial(), new Long(0));
		Assert.assertEquals( entidade.getReferenciaFinal(), new Long(10));
		Assert.assertEquals( entidade.getNomenclatura(), "nomenclatura 1");
		Assert.assertFalse( entidade.isCargoIsolado() );

        // inserindo 2
        entidade = new Ocupacao();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setTipoOcupacao(new TipoOcupacao());
        entidade.getTipoOcupacao().setId(1l);
        entidade.setQuantidade(new Long(20));
        entidade.setSituacao(new Long(1));
        entidade.setReferenciaInicial(new Long(10));
        entidade.setReferenciaFinal(new Long(20));
        entidade.setNomenclatura("nomenclatura 2");
        entidade.setCargoIsolado( true );

		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getTipoOcupacao().getId(), new Long(1));
		Assert.assertEquals( entidade.getQuantidade(), new Long(20));
		Assert.assertEquals( entidade.getSituacao(), new Long(1));
		Assert.assertEquals( entidade.getReferenciaInicial(), new Long(10));
		Assert.assertEquals( entidade.getReferenciaFinal(), new Long(20));
		Assert.assertEquals( entidade.getNomenclatura(), "nomenclatura 2");
		Assert.assertTrue( entidade.isCargoIsolado() );

		// alterando
		entidade.setNomenclatura("nomenclatura Update");
		dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getNomenclatura(), "nomenclatura Update");

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new Ocupacao();
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
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("nomenclatura"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.count
	 */
	public void testCountSituacao() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE", 0l), 0);
		Assert.assertEquals( dao.count("nomenclatura", 0l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Ocupacao> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("nomenclatura", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.search
	 */
	public void testSearchSituacao() {

		++countTests;

		List<Ocupacao> lista = dao.search("TESTE", 0l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("nomenclatura", 0l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

    	List<Ocupacao> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.OcupacaoDAOImpl.findByTipoOcupacao
	 */
    @Test
    public void testFindByTipoOcupacao() {

    	++countTests;

    	List<Ocupacao> lista = dao.findByTipoOcupacao(10l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByTipoOcupacao(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
