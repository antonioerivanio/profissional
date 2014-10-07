package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoLicencaDAO;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoLicencaServiceImpl;

public class TipoLicencaServiceTest {

	private TipoLicencaServiceImpl tipoLicencaService;
	private TipoLicencaDAO tipoLicencaDAO;

	@Before
	public void beforeTest() {
		tipoLicencaDAO = createMock(TipoLicencaDAO.class);
		tipoLicencaService = new TipoLicencaServiceImpl();
		tipoLicencaService.setDAO(tipoLicencaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		TipoLicenca entidade = new TipoLicenca();
		entidade.setId(1l);

		TipoLicenca existente = new TipoLicenca();
		existente.setId(2l);

		expect(tipoLicencaDAO.getByDescricao(null)).andReturn(existente);
		expect(tipoLicencaDAO.salvar(entidade)).andReturn(null);
		replay(tipoLicencaDAO);

		try {

			tipoLicencaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		TipoLicenca entidade = new TipoLicenca();
		expect(tipoLicencaDAO.getByDescricao(null)).andReturn(null);
		expect(tipoLicencaDAO.salvar(entidade)).andReturn(null);
		replay(tipoLicencaDAO);
		tipoLicencaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		tipoLicencaDAO.excluir(null);
		replay(tipoLicencaDAO);
		tipoLicencaService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(tipoLicencaDAO.count(null)).andReturn(1);
		replay(tipoLicencaDAO);

		Assert.assertEquals( tipoLicencaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(tipoLicencaDAO.search(null, 0, 10)).andReturn(null);
		replay(tipoLicencaDAO);

		Assert.assertNull( tipoLicencaService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(tipoLicencaDAO.getById(1l)).andReturn(null);
		replay(tipoLicencaDAO);
		TipoLicenca entidade = tipoLicencaService.getById(1l);
		Assert.assertNull(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoLicencaServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(tipoLicencaDAO.findAll()).andReturn(null);
		replay(tipoLicencaDAO);
		List<TipoLicenca> lista = tipoLicencaService.findAll();
		Assert.assertNull(lista);		
	}

}
