package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoDependenciaDAO;
import br.gov.ce.tce.srh.domain.TipoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoDependenciaServiceImpl;

public class TipoDependenciaServiceTest {

	private TipoDependenciaServiceImpl tipoDependenciaService;
	private TipoDependenciaDAO tipoDependenciaDAO;

	@Before
	public void beforeTest() {
		tipoDependenciaDAO = createMock(TipoDependenciaDAO.class);
		tipoDependenciaService = new TipoDependenciaServiceImpl();
		tipoDependenciaService.setDAO(tipoDependenciaDAO);
	}



	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDependenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		TipoDependencia entidade = new TipoDependencia();
		entidade.setId(1l);

		TipoDependencia existente = new TipoDependencia();
		existente.setId(2l);

		expect(tipoDependenciaDAO.getByDescricao(null)).andReturn(existente);
		expect(tipoDependenciaDAO.salvar(entidade)).andReturn(null);
		replay(tipoDependenciaDAO);

		try {

			tipoDependenciaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDependenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		TipoDependencia entidade = new TipoDependencia();
		expect(tipoDependenciaDAO.getByDescricao(null)).andReturn(null);
		expect(tipoDependenciaDAO.salvar(entidade)).andReturn(null);
		replay(tipoDependenciaDAO);		
		tipoDependenciaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDependenciaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		tipoDependenciaDAO.excluir(null);
		replay(tipoDependenciaDAO);
		tipoDependenciaService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDependenciaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(tipoDependenciaDAO.count(null)).andReturn(1);
		replay(tipoDependenciaDAO);

		Assert.assertEquals( tipoDependenciaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDependenciaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(tipoDependenciaDAO.search(null, 0, 10)).andReturn(null);
		replay(tipoDependenciaDAO);

		Assert.assertNull( tipoDependenciaService.search(null, 0, 10) );
	}

}
