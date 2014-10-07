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

import br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademicaPk;

public class PessoalCursoAcademicaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private PessoalCursoAcademica entidade;
	private PessoalCursoAcademicaDAOImpl dao = new PessoalCursoAcademicaDAOImpl();

	private static int countTests = 0;


	public PessoalCursoAcademicaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\PessoalCursoAcademica.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new PessoalCursoAcademica();
        entidade.setPk( new PessoalCursoAcademicaPk() );
        entidade.getPk().setCursoAcademico(1l);
        entidade.getPk().setPessoal(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        entidade = new PessoalCursoAcademica();
        entidade.setPk(new PessoalCursoAcademicaPk());
        entidade.setCursoAcademica(new CursoAcademica());
        entidade.getCursoAcademica().setId(1l);
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setInstituicao(new Instituicao());
        entidade.getInstituicao().setId(1l);
        entidade.getPk().setPessoal(1l);
        entidade.getPk().setCursoAcademico(1l);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getCursoAcademica().getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getPk().getCursoAcademico(), new Long(1));
		Assert.assertEquals( entidade.getPk().getPessoal(), new Long(1));
		Assert.assertEquals( entidade.getInstituicao().getId(), new Long(1));

        // inserindo 2
        entidade = new PessoalCursoAcademica();
        entidade.setPk(new PessoalCursoAcademicaPk());
        entidade.setCursoAcademica(new CursoAcademica());
        entidade.getCursoAcademica().setId(2l);
        entidade.setPessoal(new Pessoal());
        entidade.getPessoal().setId(1l);
        entidade.setInstituicao(new Instituicao());
        entidade.getInstituicao().setId(1l);
        entidade.getPk().setPessoal(1l);
        entidade.getPk().setCursoAcademico(2l);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getCursoAcademica().getId(), new Long(2));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getPk().getCursoAcademico(), new Long(2));
		Assert.assertEquals( entidade.getPk().getPessoal(), new Long(1));
		Assert.assertEquals( entidade.getInstituicao().getId(), new Long(1));

		// alterando
		entidade = dao.salvar(entidade);


		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        entidade = new PessoalCursoAcademica();
        entidade.setPk( new PessoalCursoAcademicaPk() );
        entidade.getPk().setCursoAcademico(1l);
        entidade.getPk().setPessoal(1l);
        dao.excluir(entidade);
        transaction.commit();

        List<PessoalCursoAcademica> lista = dao.findByPessoa(1l);

		Assert.assertEquals( lista.size(), 0l);

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.count
	 */
    @Test
    public void testCount() {

    	++countTests;

    	Assert.assertEquals(dao.count(2l), 0);
    	Assert.assertEquals(dao.count(1l), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.search
	 */
    @Test
    public void testSearch() {

    	++countTests;

    	List<PessoalCursoAcademica> lista = dao.search(2l, 0, 10);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.search(1l, 0, 10);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.findByPessoa
	 */
    @Test
    public void testFindByPessoa() {

    	++countTests;

    	List<PessoalCursoAcademica> lista = dao.findByPessoa(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoa(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.getByCursoPessoa
	 */
    @Test
    public void testGetByCursoPessoa() {

    	++countTests;

    	PessoalCursoAcademica entidadeJaExiste = dao.getByCursoPessoa(2l, 1l);
    	Assert.assertNull( entidadeJaExiste );

    	entidadeJaExiste = dao.getByCursoPessoa(1l, 1l);
    	Assert.assertNotNull(entidadeJaExiste);

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalCursoAcademicaDAOImpl.findByCursoAcademica
	 */
    @Test
    public void testFindByCursoAcademica() {

    	++countTests;

    	List<PessoalCursoAcademica> lista = dao.findByCursoAcademica(2l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByCursoAcademica(1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
