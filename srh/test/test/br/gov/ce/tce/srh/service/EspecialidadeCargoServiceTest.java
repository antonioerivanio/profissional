package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.EspecialidadeCargoDAO;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EspecialidadeCargoServiceImpl;

public class EspecialidadeCargoServiceTest {

	private EspecialidadeCargoServiceImpl especialidadeCargoService;
	private EspecialidadeCargoDAO especialidadeCargoDAO;

	@Before
	public void beforeTest() {
		especialidadeCargoDAO = createMock(EspecialidadeCargoDAO.class);
		especialidadeCargoService = new EspecialidadeCargoServiceImpl();
		especialidadeCargoService.setDAO(especialidadeCargoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeCargoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		EspecialidadeCargo entidade = new EspecialidadeCargo();

		expect(especialidadeCargoDAO.salvar(entidade)).andReturn(null);
		replay(especialidadeCargoDAO);

		especialidadeCargoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeCargoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		EspecialidadeCargo entidade = new EspecialidadeCargo();

		especialidadeCargoDAO.excluir(entidade);
		replay(especialidadeCargoDAO);

		try {

			especialidadeCargoService.excluir(entidade);

		} catch (SRHRuntimeException e) {
			Assert.fail("Ocorreu algum erro!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidadeCargoServiceImpl.excluirAll
	 */
	@Test
	public void testExcluirAll() {

		especialidadeCargoDAO.excluirAll(1l);
		replay(especialidadeCargoDAO);

		try {

			especialidadeCargoService.excluirAll(1l);

		} catch (SRHRuntimeException e) {
			Assert.fail("Ocorreu algum erro!!!");
			e.printStackTrace();
		}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EspecialidaddeCargoServiceImpl.findByOcupacao
	 */
	@Test
	public void testFindByOcupacao() {
		expect(especialidadeCargoDAO.findByOcupacao(1l)).andReturn(null);
		replay(especialidadeCargoDAO);
		List<EspecialidadeCargo> lista = especialidadeCargoService.findByOcupacao(1l);
		Assert.assertNull(lista);
	}

}
