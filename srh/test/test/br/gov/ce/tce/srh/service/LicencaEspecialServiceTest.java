package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.LicencaEspecialDAO;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl;

public class LicencaEspecialServiceTest {

	private LicencaEspecialServiceImpl licencaEspecialService;
	private LicencaEspecialDAO licencaEspecialDAO;

	@Before
	public void beforeTest() {
		licencaEspecialDAO = createMock(LicencaEspecialDAO.class);
		licencaEspecialService = new LicencaEspecialServiceImpl();
		licencaEspecialService.setDAO(licencaEspecialDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setPessoal(new Pessoal());

		// validando o funcionario NULO
		try {

			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou a entidade com funcionario nulo!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarSaldoDiasNegativos() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setAnoinicial(1992l);
		entidade.setAnofinal(1997l);

		expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(null);
		expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
		replay(licencaEspecialDAO);

		// validando com saldo NULO
		try {

			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou com saldo nulo!!");

		}  catch (SRHRuntimeException e) {}


		entidade.setSaldodias(-10l);

		// validando com saldo NEGATIVO
		try {

			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou com saldo negativo!!");

		}  catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataInicioIntervalo() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setSaldodias(0l);

		LicencaEspecial existente = new LicencaEspecial();
		existente.setId(2l);
		existente.setPessoal(new Pessoal());
		existente.getPessoal().setId(1l);
		existente.setAnoinicial(1992l);
		existente.setAnofinal(1997l);

		// validando data inicio no intervalo
		try {

			entidade.setAnoinicial(1995l);
			entidade.setAnofinal(2000l);

			List<LicencaEspecial> lista = new ArrayList<LicencaEspecial>();
			lista.add(existente);

			expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(lista);
			expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
			replay(licencaEspecialDAO);

			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data inicio no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDataFimIntervalo() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setId(1l);
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setSaldodias(0l);

		LicencaEspecial existente = new LicencaEspecial();
		existente.setId(2l);
		existente.setPessoal(new Pessoal());
		existente.getPessoal().setId(1l);
		existente.setAnoinicial(1992l);
		existente.setAnofinal(1997l);

		// validando data fim no intervalo
		try {

			entidade.setAnoinicial(1990l);
			entidade.setAnofinal(1995l);

			List<LicencaEspecial> lista = new ArrayList<LicencaEspecial>();
			lista.add(existente);

			expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(lista);
			expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
			replay(licencaEspecialDAO);

			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente com data fim no intervalo!!");

		} catch (SRHRuntimeException e) {
			
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarQtdDiasInicial() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setSaldodias(60l);

		expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(new ArrayList<LicencaEspecial>());
		expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
		replay(licencaEspecialDAO);

		// validando ano inicial
		try {

			entidade.setAnoinicial(1934l);
			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou ano inicial de ser superior a 1934!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarQtdDiasFinal() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setSaldodias(60l);
		entidade.setAnoinicial(1990l);

		expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(new ArrayList<LicencaEspecial>());
		expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
		replay(licencaEspecialDAO);

		// validando ano final
		try {

			entidade.setAnofinal(2000l);
			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou ano final deve ser menor que 2000!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarQtdDias() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setSaldodias(60l);

		expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(new ArrayList<LicencaEspecial>());
		expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
		replay(licencaEspecialDAO);

		// validando o ano inicial e final
		try {

			entidade.setAnoinicial(1991l);
			entidade.setAnofinal(1998l);
			licencaEspecialService.salvar(entidade);
			Assert.fail("Nao validou ano final deve ter um intervalo do ano inicial entre 5 a 10 anos!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setPessoal(new Pessoal());
		entidade.getPessoal().setId(1l);
		entidade.setDescricao("teste");
		entidade.setAnoinicial(1992l);
		entidade.setAnofinal(1997l);
		entidade.setQtdedias(60l);
		entidade.setSaldodias(0l);

		expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(new ArrayList<LicencaEspecial>());
		expect(licencaEspecialDAO.salvar(entidade)).andReturn(null);
		replay(licencaEspecialDAO);

		licencaEspecialService.salvar(entidade);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setQtdedias(4l);
		entidade.setSaldodias(4l);

		licencaEspecialDAO.excluir(entidade);
		replay(licencaEspecialDAO);

		try {

			licencaEspecialService.excluir(entidade);

		} catch (SRHRuntimeException e) {
			Assert.fail("Ocorreu algum erro!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.excluir
	 */
	@Test
	public void testExcluirSaldoDiasMenor() {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setQtdedias(4l);
		entidade.setSaldodias(2l);

		licencaEspecialDAO.excluir(entidade);
		replay(licencaEspecialDAO);

		try {

			licencaEspecialService.excluir(entidade);
			Assert.fail("Deixou excluir!!");
			
		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(licencaEspecialDAO.count(1l)).andReturn(1);
		replay(licencaEspecialDAO);

		Assert.assertEquals( licencaEspecialService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(licencaEspecialDAO.search(1l, 0, 10)).andReturn(null);
		replay(licencaEspecialDAO);

		Assert.assertNull( licencaEspecialService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(licencaEspecialDAO.getById(1l)).andReturn(null);
		replay(licencaEspecialDAO);

		Assert.assertNull( licencaEspecialService.getById(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.findByPessoa
	 */
	@Test
	public void testFindByPessoa() {
		expect(licencaEspecialDAO.findByPessoal(1l)).andReturn(null);
		replay(licencaEspecialDAO);
		List<LicencaEspecial> lista = licencaEspecialService.findByPessoal(1l);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.findByPessoalComSaldo
	 */
	@Test
	public void testFindByPessoalComSaldo() {
		expect(licencaEspecialDAO.findByPessoalComSaldo(1l)).andReturn(null);
		replay(licencaEspecialDAO);
		List<LicencaEspecial> lista = licencaEspecialService.findByPessoalComSaldo(1l);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.LicencaEspecialServiceImpl.ajustarSaldo
	 */
	@Test
	public void testAjustarSaldoNegativo() {

		LicencaEspecial entidade = new LicencaEspecial();
		entidade.setQtdedias(4l);
		entidade.setSaldodias(4l);

		expect(licencaEspecialDAO.getById(1l)).andReturn(entidade);
		licencaEspecialDAO.ajustarSaldo(1l, 100l);
		replay(licencaEspecialDAO);

		try {

			licencaEspecialService.ajustarSaldo(1l, "REMOVER", 100);
			Assert.fail("Nao validou com saldo igual ou menor que zero!!!");

		} catch (SRHRuntimeException e) {
			e.printStackTrace();
		}

	}

}
