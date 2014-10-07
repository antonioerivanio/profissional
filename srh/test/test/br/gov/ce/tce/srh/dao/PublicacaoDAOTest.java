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

import br.gov.ce.tce.srh.dao.PublicacaoDAOImpl;
import br.gov.ce.tce.srh.domain.Publicacao;
import br.gov.ce.tce.srh.domain.TipoDocumento;

/**
 *
 * @author robstown
 */
public class PublicacaoDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Publicacao entidade;
	private PublicacaoDAOImpl dao = new PublicacaoDAOImpl();

	private static int countTests = 0;


	public PublicacaoDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Publicacao.xml"));
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
	 * Test of br.gov.ce.tce.srh.dao.VeiculoDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new Publicacao();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Publicacao();
        entidade.setTipoDocumento(new TipoDocumento());
        entidade.getTipoDocumento().setId(new Long(1));
        entidade.setNumero(new Long(999));
        entidade.setAno(new Long(2011));
        entidade.setEmenta("EMENTA");
        entidade.setVigencia(new Date());
        entidade.setDoe(new Date());
        entidade.setArquivo("TESTE.DOC");
        entidade.setTipoPublicacao(new Long(1));

        entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade );
		Assert.assertEquals( entidade.getTipoDocumento().getId(), new Long(1));
		Assert.assertEquals( entidade.getNumero(), new Long(999));
		Assert.assertEquals( entidade.getAno(), new Long(2011));
		Assert.assertEquals( entidade.getEmenta(), "EMENTA");
		Assert.assertNotNull( entidade.getDoe() );
		Assert.assertNotNull( entidade.getVigencia() );
		Assert.assertEquals( entidade.getArquivo(), "TESTE.DOC");
		Assert.assertEquals( entidade.getTipoPublicacao(), new Long(1));

        // inserindo 2
		entidade = new Publicacao();
        entidade.setTipoDocumento(new TipoDocumento());
        entidade.getTipoDocumento().setId(new Long(1));
        entidade.setNumero(new Long(111));
        entidade.setAno(new Long(2010));
        entidade.setEmenta("EMENTA 2");
        entidade.setVigencia(new Date());
        entidade.setDoe(new Date());
        entidade.setArquivo("TESTE 2.DOC");
        entidade.setTipoPublicacao(new Long(1));

		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getTipoDocumento().getId(), new Long(1));
		Assert.assertEquals( entidade.getNumero(), new Long(111));
		Assert.assertEquals( entidade.getAno(), new Long(2010));
		Assert.assertEquals( entidade.getEmenta(), "EMENTA 2");
		Assert.assertNotNull( entidade.getDoe() );
		Assert.assertNotNull( entidade.getVigencia() );
		Assert.assertEquals( entidade.getArquivo(), "TESTE 2.DOC");
		Assert.assertEquals( entidade.getTipoPublicacao(), new Long(1));

		// alterando
		entidade.setEmenta("EMENTA UPDATE");
		entidade.setArquivo("TESTE UPDATE.DOC");
		entidade = dao.salvar(entidade);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEmenta(), "EMENTA UPDATE" );
		Assert.assertEquals( entidade.getArquivo(), "TESTE UPDATE.DOC" );

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PublicacaoDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

        entidade = new Publicacao();
        entidade.setId(1l);

        // excluindo
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        dao.excluir(entidade);
        transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PublicacaoDAOImpl.count
	 */
	public void testCount() {

		++countTests;

		Assert.assertEquals( dao.count(2l), 0);
		Assert.assertEquals( dao.count(1l), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.PublicacaoDAOImpl.search
	 */
	public void testSearch() {

		++countTests;

		List<Publicacao> lista = dao.search(2l, 0, 10);
		Assert.assertEquals( lista.size(), 0);

		lista = dao.search(1l, 0, 10);
		Assert.assertEquals( lista.size(), 1);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.PublicacaoDAOImpl.getByEmenta
	 */
	public void testGetByEmenta() {

		++countTests;

		entidade = dao.getByEmenta("EMENTA");
		Assert.assertNotNull(entidade);

		entidade = dao.getByEmenta("TESTE");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}

}
