package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoBeneficioDAO;
import br.gov.ce.tce.srh.domain.TipoBeneficio;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoBeneficioServiceImpl;

public class TipoBeneficioServiceTest {

	private TipoBeneficioServiceImpl tipoBeneficioService;
	private TipoBeneficioDAO tipoBeneficioDAO;

	@Before
	public void beforeTest() {
		tipoBeneficioDAO = createMock(TipoBeneficioDAO.class);
		tipoBeneficioService = new TipoBeneficioServiceImpl();
		tipoBeneficioService.setDAO(tipoBeneficioDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoBeneficioServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		TipoBeneficio entidade = new TipoBeneficio();
		entidade.setId(1l);

		TipoBeneficio existente = new TipoBeneficio();
		existente.setId(2l);

		expect(tipoBeneficioDAO.getByDescricao(null)).andReturn(existente);
		expect(tipoBeneficioDAO.salvar(entidade)).andReturn(null);
		replay(tipoBeneficioDAO);

		try {

			tipoBeneficioService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoBeneficioServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		TipoBeneficio entidade = new TipoBeneficio();
		expect(tipoBeneficioDAO.getByDescricao(null)).andReturn(null);
		expect(tipoBeneficioDAO.salvar(entidade)).andReturn(null);
		replay(tipoBeneficioDAO);		
		tipoBeneficioService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoBeneficioServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		tipoBeneficioDAO.excluir(null);
		replay(tipoBeneficioDAO);
		tipoBeneficioService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoBeneficioServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(tipoBeneficioDAO.count(null)).andReturn(1);
		replay(tipoBeneficioDAO);

		Assert.assertEquals( tipoBeneficioService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoBeneficioServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(tipoBeneficioDAO.search(null, 0, 10)).andReturn(null);
		replay(tipoBeneficioDAO);

		Assert.assertNull( tipoBeneficioService.search(null, 0, 10) );
	}

}
