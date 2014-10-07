package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.MotivoDependenciaDAO;
import br.gov.ce.tce.srh.domain.MotivoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.MotivoDependenciaServiceImpl;

public class MotivoDependenciaServiceTest {

	private MotivoDependenciaServiceImpl motivoDependenciaService;
	private MotivoDependenciaDAO motivoDependenciaDAO;

	@Before
	public void beforeTest() {
		motivoDependenciaDAO = createMock(MotivoDependenciaDAO.class);
		motivoDependenciaService = new MotivoDependenciaServiceImpl();
		motivoDependenciaService.setDAO(motivoDependenciaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		MotivoDependencia entidade = new MotivoDependencia();
		entidade.setId(1l);

		MotivoDependencia existente = new MotivoDependencia();
		existente.setId(2l);

		expect(motivoDependenciaDAO.getByDescricao(null)).andReturn(existente);
		expect(motivoDependenciaDAO.salvar(entidade)).andReturn(null);
		replay(motivoDependenciaDAO);

		try {

			motivoDependenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.MotivoDependenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		MotivoDependencia entidade = new MotivoDependencia();
		expect(motivoDependenciaDAO.getByDescricao(null)).andReturn(null);
		expect(motivoDependenciaDAO.salvar(entidade)).andReturn(null);
		replay(motivoDependenciaDAO);		
		motivoDependenciaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.MotivoDependenciaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		motivoDependenciaDAO.excluir(null);
		replay(motivoDependenciaDAO);
		motivoDependenciaService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.MotivoDependenciaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(motivoDependenciaDAO.count(null)).andReturn(1);
		replay(motivoDependenciaDAO);

		Assert.assertEquals( motivoDependenciaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.MotivoDependenciaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(motivoDependenciaDAO.search(null, 0, 10)).andReturn(null);
		replay(motivoDependenciaDAO);

		Assert.assertNull( motivoDependenciaService.search(null, 0, 10) );
	}

}
