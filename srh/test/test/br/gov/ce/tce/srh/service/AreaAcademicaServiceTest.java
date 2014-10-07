package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.AreaAcademicaDAO;
import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaAcademicaServiceImpl;

public class AreaAcademicaServiceTest {

	private AreaAcademicaServiceImpl areaFormacaoService;
	private AreaAcademicaDAO areaFormacaoDAO;

	@Before
	public void beforeTest() {
		areaFormacaoDAO = createMock(AreaAcademicaDAO.class);
		areaFormacaoService = new AreaAcademicaServiceImpl();
		areaFormacaoService.setDAO(areaFormacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		AreaAcademica entidade = new AreaAcademica();
		entidade.setId(1l);

		AreaAcademica existente = new AreaAcademica();
		existente.setId(2l);

		expect(areaFormacaoDAO.getByDescricao(null)).andReturn(existente);
		expect(areaFormacaoDAO.salvar(entidade)).andReturn(null);
		replay(areaFormacaoDAO);

		try {

			areaFormacaoService.salvar(entidade);
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
		AreaAcademica entidade = new AreaAcademica();
		expect(areaFormacaoDAO.getByDescricao(null)).andReturn(null);
		expect(areaFormacaoDAO.salvar(entidade)).andReturn(null);
		replay(areaFormacaoDAO);		
		areaFormacaoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		areaFormacaoDAO.excluir(null);
		replay(areaFormacaoDAO);
		areaFormacaoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(areaFormacaoDAO.count(null)).andReturn(1);
		replay(areaFormacaoDAO);

		Assert.assertEquals( areaFormacaoService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(areaFormacaoDAO.search(null, 0, 10)).andReturn(null);
		replay(areaFormacaoDAO);

		Assert.assertNull( areaFormacaoService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaFormacaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(areaFormacaoDAO.findAll()).andReturn(null);
		replay(areaFormacaoDAO);
		List<AreaAcademica> lista = areaFormacaoService.findAll();
		Assert.assertNull(lista);		
	}

}
