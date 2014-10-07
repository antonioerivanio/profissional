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

import br.gov.ce.tce.srh.dao.LicencaDAOImpl;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.domain.TipoPublicacao;

public class LicencaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Licenca entidade;
	private LicencaDAOImpl dao = new LicencaDAOImpl();

	private static int countTests = 0;


	public LicencaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Licenca.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getTipoLicenca().getId(), new Long(5));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertFalse( entidade.isExcluitemposerv() );
		Assert.assertFalse( entidade.isExcluifinanceiro() );
		Assert.assertEquals( entidade.getLicencaEspecial().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getNrprocesso(), "1234");
		Assert.assertEquals( entidade.getObs(), "teste");
		Assert.assertNotNull( entidade.getDoe() );
		Assert.assertEquals( entidade.getTipoPublicacao().getId(), new Long(1));
		Assert.assertEquals( entidade.getDias(), 10l);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new Licenca();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Licenca();
        entidade.setPessoal( new Pessoal() );
        entidade.getPessoal().setId(1l);
        entidade.setTipoLicenca( new TipoLicenca() );
        entidade.getTipoLicenca().setId(5l);
        entidade.setLicencaEspecial( new LicencaEspecial() );
        entidade.getLicencaEspecial().setId(1l);
        entidade.setNrprocesso("1234");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setExcluifinanceiro(true);
        entidade.setExcluitemposerv(true);
        entidade.setObs("teste");
        entidade.setTipoPublicacao( new TipoPublicacao() );
        entidade.getTipoPublicacao().setId(1l);
        entidade.setDoe(new Date());

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getTipoLicenca().getId(), new Long(5));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertTrue( entidade.isExcluitemposerv() );
		Assert.assertTrue( entidade.isExcluifinanceiro() );
		Assert.assertEquals( entidade.getLicencaEspecial().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getNrprocesso(), "1234");
		Assert.assertEquals( entidade.getObs(), "teste");
		Assert.assertNotNull( entidade.getDoe() );
		Assert.assertEquals( entidade.getTipoPublicacao().getId(), new Long(1));

        // inserindo 2
        entidade = new Licenca();
        entidade.setPessoal( new Pessoal() );
        entidade.getPessoal().setId(1l);
        entidade.setTipoLicenca( new TipoLicenca() );
        entidade.getTipoLicenca().setId(5l);
        entidade.setLicencaEspecial( new LicencaEspecial() );
        entidade.getLicencaEspecial().setId(1l);
        entidade.setNrprocesso("5678");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setExcluifinanceiro(false);
        entidade.setExcluitemposerv(false);
        entidade.setObs("teste 2");
        entidade.setTipoPublicacao( new TipoPublicacao() );
        entidade.getTipoPublicacao().setId(1l);
        entidade.setDoe(new Date());

		entidade = dao.salvar(entidade);
        entidade = dao.getById( entidade.getId() );

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getTipoLicenca().getId(), new Long(5));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertFalse( entidade.isExcluitemposerv() );
		Assert.assertFalse( entidade.isExcluifinanceiro() );
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getNrprocesso(), "5678");
		Assert.assertEquals( entidade.getObs(), "teste 2");
		Assert.assertNotNull( entidade.getDoe() );

		// alterando
		entidade.setTipoPublicacao(new TipoPublicacao());
		entidade.getTipoPublicacao().setId(1l);
		dao.salvar(entidade);
		
		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        entidade = new Licenca();
        entidade.setId(1l);

        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

        entidade = dao.getById(1l);

		Assert.assertNull(entidade);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.count
	 */
	public void testCountTipoLicenca() {

		++countTests;

		Assert.assertEquals( dao.count(2l, 1l), 0);
		Assert.assertEquals( dao.count(1l, 5l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Licenca> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.search
	 */
	public void testSearchTipoLicenca() {

		++countTests;

		List<Licenca> lista = dao.search(2l, 1l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 5l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.findByPessoa
	 */
    @Test
    public void testFindByPessoa() {

    	++countTests;

    	List<Licenca> lista = dao.findByPessoa(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoa(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.LicencaDAOImpl.findByPessoaLicencaEspecial
	 */
    @Test
    public void testFindByPessoaLicencaEspecial() {

    	++countTests;

    	List<Licenca> lista = dao.findByPessoaLicencaEspecial(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoaLicencaEspecial(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
