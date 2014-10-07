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

import br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 */
public class RepresentacaoSetorDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private RepresentacaoSetor entidade;
	private RepresentacaoSetorDAOImpl dao = new RepresentacaoSetorDAOImpl();

	private static int countTests = 0;


	public RepresentacaoSetorDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\RepresentacaoSetor.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();
        
        // excluindo
        transaction.begin();
        entidade = new RepresentacaoSetor();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new RepresentacaoSetor();
        entidade.setRepresentacaoCargo(new RepresentacaoCargo());
        entidade.getRepresentacaoCargo().setId(new Long(1));
        entidade.setSetor(new Setor());
        entidade.getSetor().setId(1l);
        entidade.setQuantidade(1l);
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setAtivo(true);
        entidade.setObservacao("teste");
        entidade.setHierarquia(1l);

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getId(), new Long(1));
		Assert.assertEquals( entidade.getRepresentacaoCargo().getId(), new Long(1));
		Assert.assertEquals( entidade.getSetor().getId(), new Long(1));
		Assert.assertEquals( entidade.getQuantidade(), new Long(1));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertTrue( entidade.isAtivo() );
		Assert.assertEquals( entidade.getObservacao(), "teste");
		Assert.assertEquals( entidade.getHierarquia(), new Long(1) );

        // inserindo 2
        entidade = new RepresentacaoSetor();
        entidade.setRepresentacaoCargo(new RepresentacaoCargo());
        entidade.getRepresentacaoCargo().setId(new Long(2));
        entidade.setSetor(new Setor());
        entidade.getSetor().setId(2l);
        entidade.setQuantidade(2l);
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setAtivo(false);
        entidade.setObservacao("teste 2");
        entidade.setHierarquia(2l);

		entidade = dao.salvar(entidade);

		Assert.assertNotNull( entidade );
		Assert.assertEquals( entidade.getId(), new Long(2));
		Assert.assertEquals( entidade.getRepresentacaoCargo().getId(), new Long(2));
		Assert.assertEquals( entidade.getSetor().getId(), new Long(2));
		Assert.assertEquals( entidade.getQuantidade(), new Long(2));
		Assert.assertNotNull( entidade.getInicio() );
		Assert.assertNotNull( entidade.getFim() );
		Assert.assertFalse( entidade.isAtivo() );
		Assert.assertEquals( entidade.getObservacao(), "teste 2");
		Assert.assertEquals( entidade.getHierarquia(), new Long(2) );

	
		// alterando
		entidade.setObservacao("Teste Update");
		dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getObservacao(), "Teste Update" );

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.salvar
	 */
    @Test(expected=SRHRuntimeException.class)
    public void testSalvarExistente() {

    	++countTests;

        entidade = new RepresentacaoSetor();
        entidade.setRepresentacaoCargo(new RepresentacaoCargo());
        entidade.getRepresentacaoCargo().setId(new Long(1));
        entidade.setSetor(new Setor());
        entidade.getSetor().setId(1l);
        entidade.setQuantidade(1l);
        entidade.setInicio(new Date());
        entidade.setFim(new Date());
        entidade.setAtivo(true);
        entidade.setObservacao("teste");
        entidade.setHierarquia(1l);

        try {

			entidade = dao.salvar(entidade);
			closeEntityManager(countTests);
	        fail("Passou existente!!!");

		} catch (SRHRuntimeException e) {

		}

        closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new RepresentacaoSetor();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<RepresentacaoSetor> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.getByCargoSetor
	 */
    @Test
    public void testGetByCargoSetor() {

    	++countTests;

    	RepresentacaoSetor entidade = dao.getByCargoSetor(1l, 2l);
    	Assert.assertNull( entidade );

    	entidade = dao.getByCargoSetor(1l, 1l);
    	Assert.assertNotNull( entidade );

    	closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.RepresentacaoSetorDAOImpl.findBySetorAtivo
	 */
    @Test
    public void testFindBySetorAtivo() {

    	++countTests;

    	List<RepresentacaoSetor> lista = dao.findBySetorAtivo(2l, true);
    	Assert.assertEquals( lista.size(), 0l);

    	lista = dao.findBySetorAtivo(1l, true);
    	Assert.assertEquals( lista.size(), 1l );

    	closeEntityManager(countTests);
    }

}
