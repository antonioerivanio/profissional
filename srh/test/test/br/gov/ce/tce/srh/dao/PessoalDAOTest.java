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

import br.gov.ce.tce.srh.dao.PessoalDAOImpl;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.EstadoCivil;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.PessoalCategoria;
import br.gov.ce.tce.srh.domain.Raca;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public class PessoalDAOTest extends DatabaseTestCase {

	private static final String PERSISTENCE_UNIT_NAME = "maindatabase";
	private EntityManagerFactory emf;
	private EntityManager em;

	private Pessoal entidade;
	private PessoalDAOImpl dao = new PessoalDAOImpl();

	private static int countTests = 0;


	public PessoalDAOTest() {
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
		return new FlatXmlDataSetBuilder().build(new FileInputStream("test\\test\\br\\gov\\ce\\tce\\srh\\domain\\Pessoal.xml"));
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
		if (countTests == 7) {
			em.close();
			emf.close();
		}
	}

	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.getById
	 */
	@Test
	public void testGetById() {

		++countTests;

		entidade = dao.getById(1l);

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getEstadoCivil().getId(), new Long(1));
		Assert.assertEquals( entidade.getRaca().getId(), new Long(1));
		Assert.assertEquals( entidade.getCodCredorSic(), new Long(1));
		Assert.assertEquals( entidade.getNome(), "Robstown");

		Assert.assertEquals( entidade.getNomeCompleto(), "Francisco Robstown Nunes Holanda");
		Assert.assertEquals( entidade.getNomePesquisa(), "robstown");
		Assert.assertEquals( entidade.getAbreviatura(), "frnh");
		Assert.assertEquals( entidade.getNomePai(), "Hamilton de Holanda Cavalcante");
		Assert.assertEquals( entidade.getNomeMae(), "Ligia Mariusa Nunes Holanda");

		Assert.assertEquals( entidade.getEmail(), "robstownholanda@gmail.com");
		Assert.assertEquals( entidade.getSexo(), "M");
		Assert.assertNotNull( entidade.getNascimento() );
		Assert.assertNotNull( entidade.getObito() );
		Assert.assertEquals( entidade.getTipoSangue(), "O");

		Assert.assertEquals( entidade.getFatorRH(), "+");
		Assert.assertEquals( entidade.getEndereco(), "Rua Frei Mansueto");
		Assert.assertEquals( entidade.getNumero(), "233");
		Assert.assertEquals( entidade.getComplemento(), "Casa");
		Assert.assertEquals( entidade.getBairro(), "Varjota");

		Assert.assertEquals( entidade.getCep(), "60175555");
		Assert.assertEquals( entidade.getMunicipio(), "Fortaleza");
		Assert.assertEquals( entidade.getUfEndereco().getId(), "CE");
		Assert.assertEquals( entidade.getNaturalidade(), "Manaus");
		Assert.assertEquals( entidade.getUf().getId(), "CE");

		Assert.assertEquals( entidade.getPasep(), "12345678");
		Assert.assertEquals( entidade.getCpf(), "66526900077");
		Assert.assertEquals( entidade.getRg(), "98010290399");
		Assert.assertEquals( entidade.getEmissorRg(), "SSP");		
		Assert.assertNotNull( entidade.getDataEmissaoRg() );

		Assert.assertEquals( entidade.getUfEmissorRg().getId(), "CE");
		Assert.assertEquals( entidade.getTituloEleitoral(), "2222222");
		Assert.assertEquals( entidade.getZonaEleitoral(), "192");
		Assert.assertEquals( entidade.getSecaoEleitoral(), "2");
		Assert.assertEquals( entidade.getDocumentoMilitar(), "212121212121212");

		Assert.assertEquals( entidade.getTipoContaBbd(), new Long(1));
		Assert.assertEquals( entidade.getAgenciaBbd(), "1888");
		Assert.assertEquals( entidade.getContaBbd(), "690058");
		Assert.assertEquals( entidade.getQtdDepsf(), new Long(1));
		Assert.assertEquals( entidade.getQtdDepir(), new Long(1));

		Assert.assertEquals( entidade.getQtdDepprev(), new Long(1));
		Assert.assertEquals( entidade.getFoto(), "foto.jpg");
		Assert.assertEquals( entidade.getCategoria().getId(), new Long(1));
		Assert.assertNotNull( entidade.getAtualizacao() );
		Assert.assertEquals( entidade.getTelefone(), "32622088");
		Assert.assertEquals( entidade.getCelular(), "86776899");

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.salvar
	 */
    @Test
    public void testSalvar() throws Exception {

    	++countTests;

    	EntityTransaction transaction = em.getTransaction();

        // excluindo
        transaction.begin();
        entidade = new Pessoal();
        entidade.setId(1l);
        dao.excluir(entidade);
        transaction.commit();

        // inserindo 1
        transaction.begin();
        entidade = new Pessoal();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setEstadoCivil(new EstadoCivil());
        entidade.getEstadoCivil().setId(new Long(1));
        entidade.setRaca(new Raca());
        entidade.getRaca().setId(new Long(1));
		entidade.setCategoria( new PessoalCategoria() );
		entidade.getCategoria().setId( new Long(1) );
        entidade.setCodCredorSic(new Long(1));
        entidade.setNome("Robstown");
        entidade.setNomeCompleto("Francisco Robstown Nunes Holanda");
        entidade.setNomePesquisa("Robstown");
        entidade.setAbreviatura("frnh");
		entidade.setNomePai("Hamilton de Holanda Cavalcante");
		entidade.setNomeMae("Ligia Mariusa Nunes Holanda");
		entidade.setEmail("robstownholanda@gmail.com");
		entidade.setSexo("M");
		entidade.setNascimento(new Date());
		entidade.setObito(new Date());
		entidade.setTipoSangue("O");
		entidade.setFatorRH("+");
		entidade.setEndereco("Rua Frei Mansueto");
		entidade.setNumero("233");
		entidade.setComplemento("Casa");
		entidade.setBairro("Varjota");
		entidade.setCep("60175555");
		entidade.setMunicipio("Fortaleza");
		entidade.setUfEndereco(new Uf());
		entidade.getUfEndereco().setId("CE");
		entidade.setNaturalidade("Manaus");
		entidade.setUf(new Uf());
		entidade.getUf().setId("CE");
		entidade.setPasep("12345678");
		entidade.setCpf("66526900077");
		entidade.setRg("98010290399");
		entidade.setEmissorRg("SSP");
		entidade.setUfEmissorRg(new Uf());
		entidade.getUfEmissorRg().setId("CE");
		entidade.setDataEmissaoRg(new Date());
		entidade.setTituloEleitoral("2222222");
		entidade.setZonaEleitoral("192");
		entidade.setSecaoEleitoral("2");
		entidade.setDocumentoMilitar("212121212121212");
		entidade.setTipoContaBbd(new Long(1));
		entidade.setAgenciaBbd("1888");
		entidade.setContaBbd("690058");
		entidade.setQtdDepsf(new Long(1));
		entidade.setQtdDepir(new Long(1));
		entidade.setQtdDepprev(new Long(1));
		entidade.setFoto("foto.jpg");
		entidade.setAtualizacao(new Date());
		entidade.setTelefone("32622088");
		entidade.setCelular("86776899");

		entidade = dao.salvar(entidade);
        entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getEstadoCivil().getId(), new Long(1));
		Assert.assertEquals( entidade.getRaca().getId(), new Long(1));
		Assert.assertEquals( entidade.getCodCredorSic(), new Long(1));
		Assert.assertEquals( entidade.getNome(), "ROBSTOWN");

		Assert.assertEquals( entidade.getNomeCompleto(), "FRANCISCO ROBSTOWN NUNES HOLANDA");
		Assert.assertEquals( entidade.getNomePesquisa(), "robstown");
		Assert.assertEquals( entidade.getAbreviatura(), "frnh");
		Assert.assertEquals( entidade.getNomePai(), "Hamilton de Holanda Cavalcante");
		Assert.assertEquals( entidade.getNomeMae(), "Ligia Mariusa Nunes Holanda");

		Assert.assertEquals( entidade.getEmail(), "robstownholanda@gmail.com");
		Assert.assertEquals( entidade.getSexo(), "M");
		Assert.assertEquals( entidade.getTipoSangue(), "O");
		Assert.assertEquals( entidade.getFatorRH(), "+");
		Assert.assertEquals( entidade.getEndereco(), "Rua Frei Mansueto");

		Assert.assertEquals( entidade.getNumero(), "233");
		Assert.assertEquals( entidade.getComplemento(), "Casa");
		Assert.assertEquals( entidade.getBairro(), "Varjota");
		Assert.assertEquals( entidade.getCep(), "60175555");
		Assert.assertEquals( entidade.getMunicipio(), "Fortaleza");

		Assert.assertEquals( entidade.getUfEndereco().getId(), "CE");
		Assert.assertEquals( entidade.getNaturalidade(), "Manaus");
		Assert.assertEquals( entidade.getUf().getId(), "CE");
		Assert.assertEquals( entidade.getPasep(), "12345678");
		Assert.assertEquals( entidade.getCpf(), "66526900077");

		Assert.assertEquals( entidade.getRg(), "98010290399");
		Assert.assertEquals( entidade.getEmissorRg(), "SSP");
		Assert.assertEquals( entidade.getUfEmissorRg().getId(), "CE");
		Assert.assertEquals( entidade.getTituloEleitoral(), "2222222");
		Assert.assertEquals( entidade.getZonaEleitoral(), "192");

		Assert.assertEquals( entidade.getSecaoEleitoral(), "2");
		Assert.assertEquals( entidade.getDocumentoMilitar(), "212121212121212");
		Assert.assertEquals( entidade.getTipoContaBbd(), new Long(1));
		Assert.assertEquals( entidade.getAgenciaBbd(), "1888");
		Assert.assertEquals( entidade.getContaBbd(), "690058");
		Assert.assertEquals( entidade.getQtdDepsf(), new Long(1));

		Assert.assertEquals( entidade.getQtdDepir(), new Long(1));
		Assert.assertEquals( entidade.getQtdDepprev(), new Long(1));
		Assert.assertEquals( entidade.getFoto(), "foto.jpg");
		Assert.assertEquals( entidade.getCategoria().getId(), new Long(1));
		Assert.assertEquals( entidade.getTelefone(), "32622088");
		Assert.assertEquals( entidade.getCelular(), "86776899");


        // inserindo 2
        entidade = new Pessoal();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setEstadoCivil(new EstadoCivil());
        entidade.getEstadoCivil().setId(new Long(1));
        entidade.setRaca(new Raca());
        entidade.getRaca().setId(new Long(1));
		entidade.setCategoria( new PessoalCategoria() );
		entidade.getCategoria().setId( new Long(1) );
        entidade.setCodCredorSic(new Long(1));
        entidade.setNome("Ana");
        entidade.setNomeCompleto("Ana Delia");
        entidade.setNomePesquisa("Delia");
        entidade.setAbreviatura("an");
		entidade.setNomePai("Rodrigo");
		entidade.setNomeMae("Isabella");
		entidade.setEmail("anadelia@gmail.com");
		entidade.setSexo("F");
		entidade.setNascimento(new Date());
		entidade.setTipoSangue("O");
		entidade.setFatorRH("+");
		entidade.setEndereco("Rua Frei Mansueto");
		entidade.setNumero("233");
		entidade.setBairro("Varjota");
		entidade.setCep("60175555");
		entidade.setMunicipio("Fortaleza");
		entidade.setUfEndereco(new Uf());
		entidade.getUfEndereco().setId("CE");
		entidade.setNaturalidade("Manaus");
		entidade.setUf(new Uf());
		entidade.getUf().setId("CE");
		entidade.setPasep("12345679");
		entidade.setCpf("66526900088");
		entidade.setRg("98010290399");
		entidade.setEmissorRg("SSP");
		entidade.setUfEmissorRg(new Uf());
		entidade.getUfEmissorRg().setId("CE");
		entidade.setDataEmissaoRg(new Date());
		entidade.setTituloEleitoral("2222222");
		entidade.setZonaEleitoral("192");
		entidade.setSecaoEleitoral("2");
		entidade.setDocumentoMilitar("212121212121212");
		entidade.setTipoContaBbd(new Long(1));
		entidade.setAgenciaBbd("1888");
		entidade.setContaBbd("690058");
		entidade.setQtdDepsf(new Long(1));
		entidade.setQtdDepir(new Long(1));
		entidade.setQtdDepprev(new Long(1));
		entidade.setFoto("foto.jpg");
		entidade.setAtualizacao(new Date());
		entidade.setTelefone("32622088");
		entidade.setCelular("86776899");

		entidade = dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getEscolaridade().getId(), new Long(1));
		Assert.assertEquals( entidade.getEstadoCivil().getId(), new Long(1));
		Assert.assertEquals( entidade.getRaca().getId(), new Long(1));
		Assert.assertEquals( entidade.getCodCredorSic(), new Long(1));
		Assert.assertEquals( entidade.getNome(), "ANA");

		Assert.assertEquals( entidade.getNomeCompleto(), "ANA DELIA");
		Assert.assertEquals( entidade.getNomePesquisa(), "delia");
		Assert.assertEquals( entidade.getAbreviatura(), "an");
		Assert.assertEquals( entidade.getNomePai(), "Rodrigo");
		Assert.assertEquals( entidade.getNomeMae(), "Isabella");

		Assert.assertEquals( entidade.getEmail(), "anadelia@gmail.com");
		Assert.assertEquals( entidade.getSexo(), "F");
		Assert.assertEquals( entidade.getTipoSangue(), "O");
		Assert.assertEquals( entidade.getFatorRH(), "+");
		Assert.assertEquals( entidade.getEndereco(), "Rua Frei Mansueto");

		Assert.assertEquals( entidade.getNumero(), "233");
		Assert.assertEquals( entidade.getBairro(), "Varjota");
		Assert.assertEquals( entidade.getCep(), "60175555");
		Assert.assertEquals( entidade.getMunicipio(), "Fortaleza");
		Assert.assertEquals( entidade.getUfEndereco().getId(), "CE");

		Assert.assertEquals( entidade.getNaturalidade(), "Manaus");
		Assert.assertEquals( entidade.getUf().getId(), "CE");
		Assert.assertEquals( entidade.getPasep(), "12345679");
		Assert.assertEquals( entidade.getCpf(), "66526900088");
		Assert.assertEquals( entidade.getRg(), "98010290399");

		Assert.assertEquals( entidade.getEmissorRg(), "SSP");
		Assert.assertEquals( entidade.getUfEmissorRg().getId(), "CE");
		Assert.assertEquals( entidade.getTituloEleitoral(), "2222222");
		Assert.assertEquals( entidade.getZonaEleitoral(), "192");
		Assert.assertEquals( entidade.getSecaoEleitoral(), "2");

		Assert.assertEquals( entidade.getDocumentoMilitar(), "212121212121212");
		Assert.assertEquals( entidade.getTipoContaBbd(), new Long(1));
		Assert.assertEquals( entidade.getAgenciaBbd(), "1888");
		Assert.assertEquals( entidade.getContaBbd(), "690058");
		Assert.assertEquals( entidade.getQtdDepsf(), new Long(1));

		Assert.assertEquals( entidade.getQtdDepir(), new Long(1));
		Assert.assertEquals( entidade.getQtdDepprev(), new Long(1));
		Assert.assertEquals( entidade.getFoto(), "foto.jpg");
		Assert.assertEquals( entidade.getCategoria().getId(), new Long(1));
		Assert.assertEquals( entidade.getTelefone(), "32622088");
		Assert.assertEquals( entidade.getCelular(), "86776899");


		// alterando
		entidade.setNome("Ana update");
		dao.salvar(entidade);
		entidade = dao.getById(entidade.getId());

		Assert.assertNotNull(entidade);
		Assert.assertEquals( entidade.getNome(), "ANA UPDATE");

		transaction.commit();

		closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
    @Test(expected=SRHRuntimeException.class)
    public void testSalvarExistente() {

    	++countTests;

        entidade = new Pessoal();
        entidade.setEscolaridade(new Escolaridade());
        entidade.getEscolaridade().setId(new Long(1));
        entidade.setEstadoCivil(new EstadoCivil());
        entidade.getEstadoCivil().setId(new Long(1));
        entidade.setRaca(new Raca());
        entidade.getRaca().setId(new Long(1));
		entidade.setCategoria( new PessoalCategoria() );
		entidade.getCategoria().setId( new Long(1) );
        entidade.setCodCredorSic(new Long(1));
        entidade.setNome("Robstown");
        entidade.setNomeCompleto("Francisco Robstown Nunes Holanda");
        entidade.setNomePesquisa("Robstown");
        entidade.setAbreviatura("frnh");
		entidade.setNomePai("Hamilton de Holanda Cavalcante");
		entidade.setNomeMae("Ligia Mariusa Nunes Holanda");
		entidade.setEmail("robstownholanda@gmail.com");
		entidade.setSexo("M");
		entidade.setNascimento(new Date());
		entidade.setTipoSangue("O");
		entidade.setFatorRH("+");
		entidade.setEndereco("Rua Frei Mansueto");
		entidade.setNumero("233");
		entidade.setBairro("Varjota");
		entidade.setCep("60175555");
		entidade.setMunicipio("Fortaleza");
		entidade.setUfEndereco(new Uf());
		entidade.getUfEndereco().setId("CE");
		entidade.setNaturalidade("Manaus");
		entidade.setUf(new Uf());
		entidade.getUf().setId("CE");
		entidade.setPasep("12345678");
		entidade.setCpf("66526900077");
		entidade.setRg("98010290399");
		entidade.setEmissorRg("SSP");
		entidade.setUfEmissorRg(new Uf());
		entidade.getUfEmissorRg().setId("CE");
		entidade.setDataEmissaoRg(new Date());
		entidade.setTituloEleitoral("2222222");
		entidade.setZonaEleitoral("192");
		entidade.setSecaoEleitoral("2");
		entidade.setDocumentoMilitar("212121212121212");
		entidade.setTipoContaBbd(new Long(1));
		entidade.setAgenciaBbd("1888");
		entidade.setContaBbd("690058");
		entidade.setQtdDepsf(new Long(1));
		entidade.setQtdDepir(new Long(1));
		entidade.setQtdDepprev(new Long(1));
		entidade.setFoto("foto.jpg");
		entidade.setAtualizacao(new Date());
		entidade.setTelefone("32622088");
		entidade.setCelular("86776899");

        try {

			entidade = dao.salvar(entidade);
			closeEntityManager(countTests);
	        fail("Passou existente!!!");

		} catch (SRHRuntimeException e) {
			
		}

        closeEntityManager(countTests);
    }


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.excluir
	 */
    @Test
    public void testExcluir() {

    	++countTests;

    	entidade = new Pessoal();
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
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.getByCPf
	 */
	public void testGetByCPf() {

		++countTests;

		entidade = dao.getByCPf("665.269.000-77");
		Assert.assertNotNull(entidade);

		entidade = dao.getByCPf("2312312313");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.getByPasep
	 */
	public void testGetByPasep() {

		++countTests;

		entidade = dao.getByPasep("12345678");
		Assert.assertNotNull(entidade);

		entidade = dao.getByPasep("2312312313");
		Assert.assertNull(entidade);

		closeEntityManager(countTests);
	}


	/**
	 * Test of br.gov.ce.tce.srh.dao.PessoalDAOImpl.findByNome
	 */
    @Test
    public void testFindByNome() {

    	++countTests;

    	// trazendo todos
    	List<Pessoal> lista = dao.findByNome("teste");
    	Assert.assertEquals(lista.size(), 0);

    	// trazendo pelo nome
    	lista = dao.findByNome("robstown");
    	Assert.assertEquals(lista.size(), 1);

    	closeEntityManager(countTests);
    }

}
