package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.EspecialidadeDAO;
import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EspecialidadeServiceImpl;

public class EspecialidadeServiceTest {

	private EspecialidadeServiceImpl especialidadeService;
	private EspecialidadeDAO especialidadeDAO;

	@Before
	public void beforeTest() {
		especialidadeDAO = createMock(EspecialidadeDAO.class);
		especialidadeService = new EspecialidadeServiceImpl();
		especialidadeService.setDAO(especialidadeDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Especialidade entidade = new Especialidade();

		// validando a descricao NULA
		try {

			especialidadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao nula!!!");

		} catch (SRHRuntimeException e) {}


		// validando a descricao VAZIA
		entidade.setDescricao("");

		try {

			especialidadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao vazio!!!");

		} catch (SRHRuntimeException e) {}


		// validando a area NULA
		entidade.setDescricao("teste");

		try {

			especialidadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade com area nula!!!");

		} catch (SRHRuntimeException e) {}


		// validando a area VAZIA
		entidade.setArea("");

		try {

			especialidadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade com area vazia!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Especialidade entidade = new Especialidade();
		entidade.setId(1l);
		entidade.setDescricao("teste");
		entidade.setArea("controle");

		Especialidade existente = new Especialidade();
		existente.setId(2l);

		expect(especialidadeDAO.getByDescricao("teste")).andReturn(existente);
		expect(especialidadeDAO.salvar(entidade)).andReturn(null);
		replay(especialidadeDAO);

		try {

			especialidadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Especialidade entidade = new Especialidade();
		entidade.setId(1l);
		entidade.setDescricao("teste");
		entidade.setArea("controle");

		expect(especialidadeDAO.getByDescricao("teste")).andReturn(null);
		expect(especialidadeDAO.salvar(entidade)).andReturn(null);
		replay(especialidadeDAO);		

		especialidadeService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		especialidadeDAO.excluir(null);
		replay(especialidadeDAO);
		especialidadeService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(especialidadeDAO.count(null)).andReturn(1);
		replay(especialidadeDAO);

		Assert.assertEquals( especialidadeService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(especialidadeDAO.search(null, 0, 10)).andReturn(null);
		replay(especialidadeDAO);

		Assert.assertNull( especialidadeService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(especialidadeDAO.getById(1l)).andReturn(null);
		replay(especialidadeDAO);
		Especialidade entidade = especialidadeService.getById(1l);
		Assert.assertNull(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.findByDescricao
	 */
	@Test
	public void testFindByDescricao() {
		expect(especialidadeDAO.findByDescricao("teste")).andReturn(null);
		replay(especialidadeDAO);
		List<Especialidade> lista = especialidadeService.findByDescricao("teste");
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(especialidadeDAO.findAll()).andReturn(null);
		replay(especialidadeDAO);
		List<Especialidade> lista = especialidadeService.findAll();
		Assert.assertNull(lista);		
	}

}
