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

import br.gov.ce.tce.srh.dao.AtestoPessoaDAOImpl;
import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;

public class AtestoPessoaDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private AtestoPessoa entidade;
	private AtestoPessoaDAOImpl dao = new AtestoPessoaDAOImpl();

	private static int countTests = 0;


	public AtestoPessoaDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\AtestoPessoa.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.AtestoPessoaDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new AtestoPessoa();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new AtestoPessoa();
        entidade.setPessoal( new Pessoal() );
        entidade.getPessoal().setId(1l);
        entidade.setCompetencia( new Competencia() );
        entidade.getCompetencia().setId(1l);
        entidade.setResponsavel( new RepresentacaoFuncional() );
        entidade.getResponsavel().setId(1l);
        entidade.setDataInicio(new Date());
        entidade.setDataFim(new Date());

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(1));
		Assert.assertEquals( entidade.getResponsavel().getId(), new Long(1));
		Assert.assertNotNull( entidade.getDataInicio() );
		Assert.assertNotNull( entidade.getDataFim() );

        // inserindo 2
        entidade = new AtestoPessoa();
        entidade.setPessoal( new Pessoal() );
        entidade.getPessoal().setId(1l);
        entidade.setCompetencia( new Competencia() );
        entidade.getCompetencia().setId(2l);
        entidade.setResponsavel( new RepresentacaoFuncional() );
        entidade.getResponsavel().setId(1l);
        entidade.setDataInicio(new Date());

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getPessoal().getId(), new Long(1));
		Assert.assertEquals( entidade.getCompetencia().getId(), new Long(2));
		Assert.assertEquals( entidade.getResponsavel().getId(), new Long(1));
		Assert.assertNotNull( entidade.getDataInicio() );
		Assert.assertNull( entidade.getDataFim() );

		// alterando
		entidade.setDataFim(new Date());
		dao.salvar(entidade);

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.AtestoPessoaDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        // excluindo
        EntityTransaction transaction = em.getTransaction();

        entidade = new AtestoPessoa();
        entidade.setId(1l);

        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }



	/**
	 * Test of br.gov.ce.tce.srh.dao.AtestoPessoaDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.AtestoPessoaDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<AtestoPessoa> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.AtestoPessoaDAOImpl.findByPessoaCompetencia
	 */
    @Test
    public void testFindByPessoaCompetencia() {

    	++countTests;

    	List<AtestoPessoa> lista = dao.findByPessoaCompetencia(1l, 5l);
    	Assert.assertEquals(lista.size(), 0);

    	lista = dao.findByPessoaCompetencia(1l, 1l);
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
