package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.PessoalDAO;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.PessoalServiceImpl;

public class PessoalServiceTest {

	private PessoalServiceImpl pessoalService;
	private PessoalDAO pessoalDAO;

	@Before
	public void beforeTest() {
		pessoalDAO = createMock(PessoalDAO.class);
		pessoalService = new PessoalServiceImpl();
		pessoalService.setDAO(pessoalDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Pessoal entidade = new Pessoal();

		// validando o nome completo NULO
		try {

			pessoalService.salvar(entidade);
			Assert.fail("Nao validou a entidade com o nome completo NULO!!!");

		} catch (SRHRuntimeException e) {
			entidade.setNomeCompleto("");
		}


		// validando o nome completo VAZIO
		try {

			pessoalService.salvar(entidade);
			Assert.fail("Nao validou a entidade com o nome completo VAZIO!!!");

		} catch (SRHRuntimeException e) {
			entidade.setNomeCompleto("Robstown");
		}


		// validando o sexo NULO
		try {

			pessoalService.salvar(entidade);
			Assert.fail("Nao validou a entidade com sexo NULO!!!");

		} catch (SRHRuntimeException e) {
			entidade.setSexo("");
		}


		// validando o sexo VAZIO
		try {

			pessoalService.salvar(entidade);
			Assert.fail("Nao validou a entidade com sexo NULO!!!");

		} catch (SRHRuntimeException e) {
			entidade.setSexo("M");
		}
		

		// validando o CPF
		try {

			entidade.setCpf("665.269.063-99");
			pessoalService.salvar(entidade);
			Assert.fail("Nao validou o cpf!!!");

		} catch (SRHRuntimeException e) {
		}


		// validando a agencia bancaria
		try {

			entidade.setAgenciaBbd("2131231");
			pessoalService.salvar(entidade);
			Assert.fail("Nao validou a agencia bancaria!!!");

		} catch (SRHRuntimeException e) {
			entidade.setCpf(null);
			entidade.setAgenciaBbd("");
		}


		// validando a conta bancaria
		try {

			entidade.setContaBbd("12313213123");
			pessoalService.salvar(entidade);
			Assert.fail("Nao validou a conta bancaria!!!");

		} catch (SRHRuntimeException e) {
			entidade.setContaBbd("");
			entidade.setAgenciaBbd(null);
		}


		// validando o PIS/PASEP
		try {

			entidade.setPasep("23123123");
			pessoalService.salvar(entidade);
			Assert.fail("Nao validou o PIS/PASEP!!!");

		} catch (SRHRuntimeException e) {
			entidade.setPasep("");
			entidade.setContaBbd(null);
		}

	}

	
	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarPASEPExiste() throws SRHRuntimeException {

		Pessoal entidade = new Pessoal();
		entidade.setId(1l);
		entidade.setNomeCompleto("Robstown");
		entidade.setSexo("M");

		Pessoal existente = new Pessoal();
		existente.setId(2l);
	
		// validando o PIS/PASEP
		try {

			entidade.setPasep("23123123");

			expect(pessoalDAO.getByPasep("23123123")).andReturn(existente);
			expect(pessoalDAO.salvar(entidade)).andReturn(null);
			replay(pessoalDAO);
			
			pessoalService.salvar(entidade);
			Assert.fail("Nao validou o PIS/PASEP existente!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Pessoal entidade = new Pessoal();
		entidade.setNome("teste");
		entidade.setNomeCompleto("teste");
		entidade.setSexo("M");
		entidade.setCpf("89073819334");

		expect(pessoalDAO.getByCPf("89073819334")).andReturn(null);
		expect(pessoalDAO.salvar(entidade)).andReturn(null);
		replay(pessoalDAO);

		pessoalService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		pessoalDAO.excluir(null);
		replay(pessoalDAO);
		pessoalService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(pessoalDAO.count(null, null)).andReturn(1);
		replay(pessoalDAO);
		Assert.assertEquals( pessoalService.count(null, null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(pessoalDAO.search(null, null, 0, 10)).andReturn(null);
		replay(pessoalDAO);
		Assert.assertNull( pessoalService.search(null, null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(pessoalDAO.getById(1l)).andReturn(null);
		replay(pessoalDAO);
		Assert.assertNull( pessoalService.getById(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.getByCpf
	 */
	@Test
	public void testGetByCpf() {
		expect(pessoalDAO.getByCPf("000")).andReturn(null);
		replay(pessoalDAO);
		Assert.assertNull( pessoalService.getByCpf("000") );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalServiceImpl.findByNome
	 */
	@Test
	public void testFindByNome() {
		expect(pessoalDAO.findByNome(null)).andReturn(null);
		replay(pessoalDAO);
		Assert.assertNull( pessoalService.findByNome(null) );
	}

}
