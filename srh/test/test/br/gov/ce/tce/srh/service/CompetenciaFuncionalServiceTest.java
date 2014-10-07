package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CompetenciaDAO;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaServiceImpl;

public class CompetenciaFuncionalServiceTest {

	private CompetenciaServiceImpl competenciaService;
	private CompetenciaDAO competenciaDAO;

	@Before
	public void beforeTest() {
		competenciaDAO = createMock(CompetenciaDAO.class);
		competenciaService = new CompetenciaServiceImpl();
		competenciaService.setDAO(competenciaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		Competencia entidade = new Competencia();

		// validando a descricao NULA
		try {

			competenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao nula!!!");

		} catch (SRHRuntimeException e) {}


		// validando a descricao VAZIA
		entidade.setDescricao(new String());

		try {

			competenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao vazia!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Competencia entidade = new Competencia();
		entidade.setId(1l);
		entidade.setDescricao("teste");

		Competencia existente = new Competencia();
		existente.setId(2l);

		expect(competenciaDAO.getByDescricao("teste")).andReturn(existente);
		expect(competenciaDAO.salvar(entidade)).andReturn(null);
		replay(competenciaDAO);

		try {

			competenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Competencia entidade = new Competencia();
		entidade.setDescricao("teste");

		expect(competenciaDAO.getByDescricao("teste")).andReturn(null);
		expect(competenciaDAO.salvar(entidade)).andReturn(null);
		replay(competenciaDAO);		

		competenciaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testAlterar() throws SRHRuntimeException {

		Competencia entidade = new Competencia();
		entidade.setDescricao("teste");
		entidade.setId(1l);

		Competencia existente = new Competencia();
		existente.setId(1l);

		expect(competenciaDAO.getByDescricao("teste")).andReturn(existente);
		expect(competenciaDAO.salvar(entidade)).andReturn(null);
		replay(competenciaDAO);		

		competenciaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.excluir
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testExcluir() {
		competenciaDAO.excluir(null);
		replay(competenciaDAO);
		competenciaService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(competenciaDAO.count(null)).andReturn(1);
		replay(competenciaDAO);

		Assert.assertEquals( competenciaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(competenciaDAO.search(null, 0, 10)).andReturn(null);
		replay(competenciaDAO);

		Assert.assertNull( competenciaService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(competenciaDAO.getById(1l)).andReturn(null);
		replay(competenciaDAO);
		Competencia entidade = competenciaService.getById(1l);
		Assert.assertNull(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(competenciaDAO.findAll()).andReturn(null);
		replay(competenciaDAO);
		List<Competencia> lista = competenciaService.findAll();
		Assert.assertNull(lista);		
	}

}
