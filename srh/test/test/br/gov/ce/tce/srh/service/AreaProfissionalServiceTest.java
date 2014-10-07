package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.AreaProfissionalDAO;
import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaProfissionalServiceImpl;

public class AreaProfissionalServiceTest {

	private AreaProfissionalServiceImpl areaProfissionalService;
	private AreaProfissionalDAO areaProfissionalDAO;

	@Before
	public void beforeTest() {
		areaProfissionalDAO = createMock(AreaProfissionalDAO.class);
		areaProfissionalService = new AreaProfissionalServiceImpl();
		areaProfissionalService.setDAO(areaProfissionalDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		AreaProfissional entidade = new AreaProfissional();
		entidade.setId(1l);

		AreaProfissional existente = new AreaProfissional();
		existente.setId(2l);

		expect(areaProfissionalDAO.getByDescricao(null)).andReturn(existente);
		expect(areaProfissionalDAO.salvar(entidade)).andReturn(null);
		replay(areaProfissionalDAO);

		try {

			areaProfissionalService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		AreaProfissional entidade = new AreaProfissional();
		expect(areaProfissionalDAO.getByDescricao(null)).andReturn(null);
		expect(areaProfissionalDAO.salvar(entidade)).andReturn(null);
		replay(areaProfissionalDAO);		
		areaProfissionalService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		areaProfissionalDAO.excluir(null);
		replay(areaProfissionalDAO);
		areaProfissionalService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(areaProfissionalDAO.count(null)).andReturn(1);
		replay(areaProfissionalDAO);

		Assert.assertEquals( areaProfissionalService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(areaProfissionalDAO.search(null, 0, 10)).andReturn(null);
		replay(areaProfissionalDAO);

		Assert.assertNull( areaProfissionalService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(areaProfissionalDAO.findAll()).andReturn(null);
		replay(areaProfissionalDAO);
		List<AreaProfissional> lista = areaProfissionalService.findAll();
		Assert.assertNull(lista);		
	}

}
