package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.RepresentacaoCargoDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoCargoServiceImpl;

public class RepresentacaoCargoServiceTest {

	private RepresentacaoCargoServiceImpl representacaoCargoService;
	private RepresentacaoCargoDAO representacaoCargoDAO;

	@Before
	public void beforeTest() {
		representacaoCargoDAO = createMock(RepresentacaoCargoDAO.class);
		representacaoCargoService = new RepresentacaoCargoServiceImpl();
		representacaoCargoService.setDAO(representacaoCargoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		RepresentacaoCargo entidade = new RepresentacaoCargo();
		entidade.setId(1l);

		RepresentacaoCargo existente = new RepresentacaoCargo();
		existente.setId(2l);

		expect(representacaoCargoDAO.getByNomenclaturaSimbolo(null, null)).andReturn(existente);
		expect(representacaoCargoDAO.salvar(entidade)).andReturn(null);
		replay(representacaoCargoDAO);

		try {

			representacaoCargoService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoCargoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		RepresentacaoCargo entidade = new RepresentacaoCargo();
		expect(representacaoCargoDAO.getByNomenclaturaSimbolo(null, null)).andReturn(null);
		expect(representacaoCargoDAO.salvar(entidade)).andReturn(null);
		replay(representacaoCargoDAO);		
		representacaoCargoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoCargoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		representacaoCargoDAO.excluir(null);
		replay(representacaoCargoDAO);
		representacaoCargoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoCargoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(representacaoCargoDAO.count(null)).andReturn(1);
		replay(representacaoCargoDAO);

		Assert.assertEquals( representacaoCargoService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoCargoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(representacaoCargoDAO.search(null, 0, 10)).andReturn(null);
		replay(representacaoCargoDAO);

		Assert.assertNull( representacaoCargoService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RepresentacaoCargoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(representacaoCargoDAO.findAll()).andReturn(null);
		replay(representacaoCargoDAO);
		List<RepresentacaoCargo> lista = representacaoCargoService.findAll();
		Assert.assertNull(lista);		
	}

}
