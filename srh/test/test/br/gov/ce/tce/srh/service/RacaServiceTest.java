package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.RacaDAO;
import br.gov.ce.tce.srh.domain.Raca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RacaServiceImpl;

public class RacaServiceTest {

	private RacaServiceImpl racaService;
	private RacaDAO racaDAO;

	@Before
	public void beforeTest() {
		racaDAO = createMock(RacaDAO.class);
		racaService = new RacaServiceImpl();
		racaService.setDAO(racaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RacaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Raca entidade = new Raca();
		entidade.setId(1l);

		Raca existente = new Raca();
		existente.setId(2l);

		expect(racaDAO.getByDescricao(null)).andReturn(existente);
		expect(racaDAO.salvar(entidade)).andReturn(null);
		replay(racaDAO);

		try {

			racaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RacaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Raca entidade = new Raca();

		expect(racaDAO.getByDescricao(null)).andReturn(null);
		expect(racaDAO.salvar(entidade)).andReturn(null);
		replay(racaDAO);

		racaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RacaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		racaDAO.excluir(null);
		replay(racaDAO);
		racaService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RacaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(racaDAO.count(null)).andReturn(1);
		replay(racaDAO);

		Assert.assertEquals( racaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RacaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(racaDAO.search(null, 0, 10)).andReturn(null);
		replay(racaDAO);

		Assert.assertNull( racaService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RacaServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(racaDAO.findAll()).andReturn(null);
		replay(racaDAO);
		List<Raca> lista = racaService.findAll();
		Assert.assertNull(lista);		
	}

}
