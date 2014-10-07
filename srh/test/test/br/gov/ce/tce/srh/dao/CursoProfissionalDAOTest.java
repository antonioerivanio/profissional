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

import br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl;
import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Instituicao;

/**
 *
 * @author robstown
 */
public class CursoProfissionalDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private CursoProfissional entidade;
	private CursoProfissionalDAOImpl dao = new CursoProfissionalDAOImpl();

	private static int countTests = 0;


	public CursoProfissionalDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\CursoProfissional.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getArea().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "ENGENHARIA CIVIL" );
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertEquals( entidade.getCargaHoraria(), new Long(360));
		Assert.assertTrue( entidade.isAreaAtuacao() );
		Assert.assertTrue( entidade.isPosGraduacao() );
		Assert.assertTrue( entidade.isPresencial() );
		Assert.assertTrue( entidade.isTempoPromocao() );
		Assert.assertTrue( entidade.isTreinamento() );

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new CursoProfissional();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new CursoProfissional();
        entidade.setArea(new AreaProfissional());
        entidade.getArea().setId(new Long(1));
        entidade.setPessoal(1l);
        entidade.setDescricao("ENGENHARIA ELETRICA");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setCargaHoraria(200l);
        entidade.setInstituicao( new Instituicao() );
        entidade.getInstituicao().setId(1l);
        entidade.setDsinstituicao("UNIFOR");
        entidade.setAreaAtuacao(false);
        entidade.setPosGraduacao(false);
        entidade.setPresencial(false);
        entidade.setTempoPromocao(false);
        entidade.setTreinamento(false);

        entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getArea().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "ENGENHARIA ELETRICA" );
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertEquals( entidade.getCargaHoraria(), new Long(200));
		Assert.assertEquals( entidade.getInstituicao().getId(), new Long(1));
		Assert.assertEquals( entidade.getDsinstituicao(), "UNIFOR" );
		Assert.assertFalse( entidade.isAreaAtuacao() );
		Assert.assertFalse( entidade.isPosGraduacao() );
		Assert.assertFalse( entidade.isPresencial() );
		Assert.assertFalse( entidade.isTempoPromocao() );
		Assert.assertFalse( entidade.isTreinamento() );

        // inserindo 2
        entidade = new CursoProfissional();
        entidade.setArea(new AreaProfissional());
        entidade.getArea().setId(new Long(1));
        entidade.setPessoal(1l);
        entidade.setDescricao("ENGENHARIA DE SOFTWARE");
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setCargaHoraria(100l);
        
        entidade.setInstituicao( new Instituicao() );
        entidade.getInstituicao().setId(2l);
        entidade.setAreaAtuacao(true);
        entidade.setPosGraduacao(false);
        entidade.setPresencial(true);
        entidade.setTempoPromocao(false);
        entidade.setTreinamento(true);


		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getArea().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal(), new Long(1));
		Assert.assertEquals( entidade.getDescricao(), "ENGENHARIA DE SOFTWARE" );
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertEquals( entidade.getCargaHoraria(), new Long(100));
		Assert.assertTrue( entidade.isAreaAtuacao() );
		Assert.assertFalse( entidade.isPosGraduacao() );
		Assert.assertTrue( entidade.isPresencial() );
		Assert.assertFalse( entidade.isTempoPromocao() );
		Assert.assertTrue( entidade.isTreinamento() );

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
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new CursoProfissional();
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
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count("TESTE"), 0);
		Assert.assertEquals( dao.count("ENGENHARIA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.count
	 */
	public void testCountArea() {

		++countTests;

		Assert.assertEquals( dao.count(1l, "TESTE"), 0);
		Assert.assertEquals( dao.count(1l, "ENGENHARIA"), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<CursoProfissional> lista = dao.search("TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search("ENGENHARIA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.search
	 */
	public void testSearchArea() {

		++countTests;

		List<CursoProfissional> lista = dao.search(1l, "TESTE", 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, "ENGENHARIA", 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.getByCursoAreaInstituicao
	 */
    @Test
    public void testGetByCursoAreaInstituicao() {

    	++countTests;

    	CursoProfissional entidade = dao.getByCursoAreaInstituicao("teste", 1l, 1l);
    	Assert.assertNull(entidade);

    	entidade = dao.getByCursoAreaInstituicao("ENGENHARIA CIVIL", 1l, 2l);
    	Assert.assertNotNull(entidade);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.findByArea
	 */
	public void testFindByArea() {

		++countTests;

		List<CursoProfissional> lista = dao.findByArea(2l);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.findByArea(1l);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


    /**
	 * Test of br.gov.ce.tce.srh.dao.CursoProfissionalDAOImpl.findAll
	 */
    @Test
    public void testFindAll() throws Exception {

    	++countTests;

        List<CursoProfissional> lista = dao.findAll();
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
