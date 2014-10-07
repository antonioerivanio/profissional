package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.LicencaDAO;
import br.gov.ce.tce.srh.dao.LicencaEspecialDAO;
import br.gov.ce.tce.srh.dao.TipoLicencaDAO;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl;
import br.gov.ce.tce.srh.service.LicencaServiceImpl;
import br.gov.ce.tce.srh.service.TipoLicencaServiceImpl;

public class LicencaServiceTest {

	private TipoLicencaDAO tipoLicencaDAO;
	private TipoLicencaServiceImpl tipoLicencaService;

	private LicencaEspecialDAO licencaEspecialDAO;
	private LicencaEspecialServiceImpl licencaEspecialService;

	private LicencaDAO licencaDAO;
	private LicencaServiceImpl licencaService;

	@Before
	public void beforeTest() {

		licencaEspecialDAO = createMock(LicencaEspecialDAO.class);
		licencaEspecialService = new LicencaEspecialServiceImpl();
		licencaEspecialService.setDAO(licencaEspecialDAO);

		tipoLicencaDAO = createMock(TipoLicencaDAO.class);
		tipoLicencaService = new TipoLicencaServiceImpl();
		tipoLicencaService.setDAO(tipoLicencaDAO);

		licencaDAO = createMock(LicencaDAO.class);
		licencaService = new LicencaServiceImpl();

		licencaService.setDAO(licencaDAO);
		licencaService.setLicencaEspecialService(licencaEspecialService);
		licencaService.setTipoLicencaService(tipoLicencaService);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Licenca entidade = new Licenca();

		// validando o funcionario NULO
		try {

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPessoal( new Pessoal() );
		}


		// validando tipo de licenca NULA
		try {

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com licenca nula!!!");

		} catch (SRHRuntimeException e) {}

		TipoLicenca tipoLicenca = new TipoLicenca();
		tipoLicenca.setId(5l);
		entidade.setTipoLicenca( tipoLicenca );
		
		expect( tipoLicencaDAO.getById(5l)).andReturn(null);
		replay(tipoLicencaDAO);

		// validando licenca especial NULA
		try {


			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com licenca especial nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setLicencaEspecial( new LicencaEspecial() );
		}


		// validando o data inicio NULA
		try {

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio nulo!!!");

		} catch (SRHRuntimeException e) {
			entidade.setInicio( new Date() );
		}
		

		// validando o data fim NULA
		try {

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data fim nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setFim( new Date() );
		}


		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		// validando o data inicio maior que a data fim
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("10/12/2011") );

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		// validando o data de publicacao maior que a data de inicio
		try {

			entidade.setDoe( dateFormate.parse("15/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data inicio maior que a fim!!!");

		} catch (SRHRuntimeException e) {

		} catch (ParseException e) {
			Assert.fail("Erro no parser!!!");
			e.printStackTrace();
		}


		// validando tipo de publicacao nula e data nao nula
		try {

			entidade.setDoe( dateFormate.parse("10/12/2011") );
			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com tipo de publicacao nula e data nao nula!!!");

		} catch (SRHRuntimeException e) { 

			entidade.getLicencaEspecial().setId(1l);

		} catch (ParseException e) {
			e.printStackTrace();
		}


		// validando tipo de publicacao vazia e data nao nula
		try {

			entidade.setDoe( dateFormate.parse("10/12/2011") );
			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com tipo de publicacao vazia e data nao nula!!!");

		} catch (SRHRuntimeException e) { 

		} catch (ParseException e) {
			e.printStackTrace();
		}


		// validando data publicacao nula e tipo de publicacao nao nula
		try {

			entidade.setDoe( null );
			entidade.setTipoPublicacao( new TipoPublicacao() );
			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com data nula e tipo de publicacao nao nula!!!");

		} catch (SRHRuntimeException e) { 

			entidade.getTipoPublicacao().setId(0l);

		}


		// validando nr processo
		try {

			entidade.getTipoPublicacao().setId(null);
			entidade.setNrprocesso("1234567890123");

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com nr do processo invalido!!!");

		} catch (SRHRuntimeException e) { 
			entidade.setNrprocesso("2000033532");
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataInicioIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		Licenca entidade = new Licenca();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setTipoLicenca( new TipoLicenca() );
		entidade.getTipoLicenca().setId(1l);

		Licenca existente = new Licenca();
		existente.setId(2l);
		existente.setPessoal(new Pessoal());
		existente.getPessoal().setId(1l);


		// validando data inicio no intervalo
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );
			
			existente.setInicio( dateFormate.parse("15/12/2011") );
			existente.setFim( dateFormate.parse("25/12/2011") );

			List<Licenca> lista = new ArrayList<Licenca>();
			lista.add(existente);

			expect(licencaDAO.findByPessoa(1l)).andReturn(lista);
			expect(licencaDAO.salvar(entidade)).andReturn(null);
			replay(licencaDAO);

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data inicio no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataFimIntervalo() throws SRHRuntimeException {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		Licenca entidade = new Licenca();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setTipoLicenca( new TipoLicenca() );
		entidade.getTipoLicenca().setId(1l);
		entidade.setTipoPublicacao( new TipoPublicacao() );

		Licenca existente = new Licenca();
		existente.setId(2l);
		existente.setPessoal(new Pessoal());
		existente.getPessoal().setId(1l);


		// validando data fim no intervalo
		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );
			
			existente.setInicio( dateFormate.parse("15/12/2011") );
			existente.setFim( dateFormate.parse("25/12/2011") );

			List<Licenca> lista = new ArrayList<Licenca>();
			lista.add(existente);

			expect(licencaDAO.findByPessoa(1l)).andReturn(lista);
			expect(licencaDAO.salvar(entidade)).andReturn(null);
			replay(licencaDAO);

			licencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data fim no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		TipoLicenca tipoLicenca = new TipoLicenca();
		tipoLicenca.setId(5l);
		tipoLicenca.setSexoValido("A");
		tipoLicenca.setQtdeMaximoDias(100l);
		
		Licenca entidade = new Licenca();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setTipoLicenca( tipoLicenca );
		entidade.setInicio(new Date());
		entidade.setFim(new Date());

		expect(tipoLicencaDAO.getById(tipoLicenca.getId())).andReturn(tipoLicenca);
		expect(licencaDAO.findByPessoa(1l)).andReturn(new ArrayList<Licenca>());
		expect(licencaDAO.salvar(entidade)).andReturn(null);

		replay(tipoLicencaDAO);
		replay(licencaDAO);

		licencaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarComLicencaEspecial() throws SRHRuntimeException {

		TipoLicenca tipoLicenca = new TipoLicenca();
		tipoLicenca.setId(5l);
		tipoLicenca.setSexoValido("A");
		tipoLicenca.setQtdeMaximoDias(100l);

		Licenca entidade = new Licenca();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setTipoLicenca( tipoLicenca );
		entidade.setLicencaEspecial( new LicencaEspecial() );
		entidade.getLicencaEspecial().setId(1l);
		entidade.setInicio(new Date());
		entidade.setFim(new Date());
		//entidade.setNrprocesso("66526/9063-2");

		LicencaEspecial existente = new LicencaEspecial();
		existente.setId(2l);
		existente.setPessoal(new Pessoal());
		existente.getPessoal().setId(1l);
		existente.setSaldodias(60l);

		expect(tipoLicencaDAO.getById(tipoLicenca.getId())).andReturn(tipoLicenca);
		expect(licencaEspecialDAO.getById(1l)).andReturn(existente);
		licencaEspecialDAO.ajustarSaldo(1l, 59l);
		expect(licencaDAO.findByPessoa(1l)).andReturn(new ArrayList<Licenca>());
		expect(licencaDAO.salvar(entidade)).andReturn(null);

		replay(tipoLicencaDAO);
		replay(licencaEspecialDAO);
		replay(licencaDAO);

		licencaService.salvar(entidade);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		Licenca entidade = new Licenca();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setTipoLicenca( new TipoLicenca() );
		entidade.getTipoLicenca().setId(1l);

		licencaDAO.excluir(entidade);
		replay(licencaDAO);

		try {

			licencaService.excluir(entidade);

		} catch (SRHRuntimeException e) {
			Assert.fail("Ocorreu algum erro!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.excluir
	 */
	@Test
	public void testExcluirLicencaEspecial() {

		DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

		Licenca entidade = new Licenca();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setTipoLicenca( new TipoLicenca() );
		entidade.getTipoLicenca().setId(5l);
		entidade.setLicencaEspecial( new LicencaEspecial());
		entidade.getLicencaEspecial().setId(1l);

		LicencaEspecial existente = new LicencaEspecial();
		existente.setId(2l);
		existente.setPessoal(new Pessoal());
		existente.getPessoal().setId(1l);
		existente.setSaldodias(60l);

		expect(licencaEspecialDAO.getById(1l)).andReturn(existente);
		licencaEspecialDAO.ajustarSaldo(1l, 69l);
		licencaDAO.excluir(entidade);

		replay(licencaEspecialDAO);
		replay(licencaDAO);

		try {

			entidade.setInicio( dateFormate.parse("12/12/2011") );
			entidade.setFim( dateFormate.parse("20/12/2011") );

			licencaService.excluir(entidade);
			
		} catch (SRHRuntimeException e) {
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.findByPessoa
	 */
	@Test
	public void testFindByPessoa() {
		expect(licencaDAO.findByPessoa(1l)).andReturn(null);
		replay(licencaDAO);
		List<Licenca> lista = licencaService.findByPessoa(1l);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(licencaDAO.count(1l)).andReturn(1);
		replay(licencaDAO);

		Assert.assertEquals( licencaService.count(1l), 1);
	}

	
	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.count
	 */
	@Test
	public void testCountPessoal() {
		expect(licencaDAO.count(1l, 1l)).andReturn(1);
		replay(licencaDAO);

		Assert.assertEquals( licencaService.count(1l, 1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(licencaDAO.search(1l, 0, 10)).andReturn(null);
		replay(licencaDAO);

		Assert.assertNull( licencaService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.search
	 */
	@Test
	public void testSearchPessoal() {
		expect(licencaDAO.search(1l, 1l, 0, 10)).andReturn(null);
		replay(licencaDAO);

		Assert.assertNull( licencaService.search(1l, 1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaServiceImpl.findByPessoaLicencaEspecial
	 */
	@Test
	public void testFindByPessoaLicencaEspecial() {
		expect(licencaDAO.findByPessoaLicencaEspecial(1l)).andReturn(null);
		replay(licencaDAO);

		Assert.assertNull( licencaService.findByPessoaLicencaEspecial(1l) );
	}

}
