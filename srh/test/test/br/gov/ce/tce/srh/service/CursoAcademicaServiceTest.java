package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CursoAcademicaDAO;
import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl;


public class CursoAcademicaServiceTest {

	private CursoAcademicaServiceImpl cursoAcademicaService;
	private CursoAcademicaDAO cursoAcademicaDAO;

	@Before
	public void beforeTest() {
		cursoAcademicaDAO = createMock(CursoAcademicaDAO.class);
		cursoAcademicaService = new CursoAcademicaServiceImpl();
		cursoAcademicaService.setDAO(cursoAcademicaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		CursoAcademica entidade = new CursoAcademica();
		entidade.setId(1l);
		entidade.setArea(new AreaAcademica());

		CursoAcademica existente = new CursoAcademica();
		existente.setId(2l);

		expect(cursoAcademicaDAO.getByAreaDescricao(null, null)).andReturn(existente);
		expect(cursoAcademicaDAO.salvar(entidade)).andReturn(null);
		replay(cursoAcademicaDAO);

		try {

			cursoAcademicaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		CursoAcademica entidade = new CursoAcademica();
		entidade.setArea(new AreaAcademica());

		expect(cursoAcademicaDAO.getByAreaDescricao(null, null)).andReturn(null);
		expect(cursoAcademicaDAO.salvar(entidade)).andReturn(null);
		replay(cursoAcademicaDAO);		

		cursoAcademicaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		cursoAcademicaDAO.excluir(null);
		replay(cursoAcademicaDAO);
		cursoAcademicaService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(cursoAcademicaDAO.count(null)).andReturn(1);
		replay(cursoAcademicaDAO);

		Assert.assertEquals( cursoAcademicaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.count
	 */
	@Test
	public void testCountArea() {
		expect(cursoAcademicaDAO.count(1l, null)).andReturn(1);
		replay(cursoAcademicaDAO);

		Assert.assertEquals( cursoAcademicaService.count(1l, null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(cursoAcademicaDAO.search(null, 0, 10)).andReturn(null);
		replay(cursoAcademicaDAO);

		Assert.assertNull( cursoAcademicaService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.search
	 */
	@Test
	public void testSearchArea() {
		expect(cursoAcademicaDAO.search(1l, null, 0, 10)).andReturn(null);
		replay(cursoAcademicaDAO);

		Assert.assertNull( cursoAcademicaService.search(1l, null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoAcademicaServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(cursoAcademicaDAO.findAll()).andReturn(null);
		replay(cursoAcademicaDAO);

		Assert.assertNull( cursoAcademicaService.findAll() );
	}

}
