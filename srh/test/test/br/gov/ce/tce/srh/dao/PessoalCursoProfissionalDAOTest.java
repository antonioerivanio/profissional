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

import br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissionalPk;

public class PessoalCursoProfissionalDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private PessoalCursoProfissional entidade;
	private PessoalCursoProfissionalDAOImpl dao = new PessoalCursoProfissionalDAOImpl();

	private static int countTests = 0;


	public PessoalCursoProfissionalDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\PessoalCursoProfissional.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        dao.excluir(1l);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new PessoalCursoProfissional();
        entidade.setPk(new PessoalCursoProfissionalPk());
        entidade.setCursoProfissional(new CursoProfissional());
        entidade.getCursoProfissional().setId(1l);
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.getPk().setPessoal(1l);
        entidade.getPk().setCursoProfissional(1l);
        entidade.setAreaAtuacao(true);
        entidade.setTempoPromocao(true);

        entidade = dao.salvar(entidade);
        entidade = dao.getByCurso(1l);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getCursoProfissional().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getPk().getCursoProfissional(), new Long(1));
		Assert.assertEquals( entidade.getPk().getPessoal(), new Long(1));
		Assert.assertTrue( entidade.isAreaAtuacao() );
		Assert.assertTrue( entidade.isTempoPromocao() );

        // inserindo 2
        entidade = new PessoalCursoProfissional();
        entidade.setPk( new PessoalCursoProfissionalPk() );

        entidade.setCursoProfissional(new CursoProfissional());
        entidade.getCursoProfissional().setId(2l);
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        
        entidade.getPk().setPessoal(1l);
        entidade.getPk().setCursoProfissional(2l);

        entidade.setAreaAtuacao(false);
        entidade.setTempoPromocao(false);

		entidade = dao.salvar(entidade);
        entidade = dao.getByCurso(2l);

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getCursoProfissional().getId(), new Long(2));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getPk().getCursoProfissional(), new Long(2));
		Assert.assertEquals( entidade.getPk().getPessoal(), new Long(1));
		Assert.assertFalse( entidade.isAreaAtuacao() );
		Assert.assertFalse( entidade.isTempoPromocao() );

		// alterando
		entidade = dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(1l);
        transaction.commit();

        entidade = dao.getByCurso(1l);

		Assert.assertNull(entidade);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals(dao.count(2l, ""), 0);
    	Assert.assertEquals(dao.count(1l, ""), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<String> lista = dao.search(2l, "", 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, "", 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl.getByCurso
	 */
    @Test
    public void testGetByCurso() {

    	++countTests;

    	entidade = dao.getByCurso(2l);
    	Assert.assertNull( entidade );

    	entidade = dao.getByCurso(1l);
    	Assert.assertNotNull( entidade );

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAOImpl.findByCurso
	 */
    @Test
    public void testFindByCurso() {

    	++countTests;

    	List<PessoalCursoProfissional> lista = dao.findByCurso(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByCurso(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
