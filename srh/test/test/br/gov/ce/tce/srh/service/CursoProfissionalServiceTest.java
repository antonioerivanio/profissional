package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CursoProfissionalDAO;
import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl;


public class CursoProfissionalServiceTest {

	private CursoProfissionalServiceImpl cursoProfissionalService;
	private CursoProfissionalDAO cursoProfissionalDAO;

	@Before
	public void beforeTest() {
		cursoProfissionalDAO = createMock(CursoProfissionalDAO.class);
		cursoProfissionalService = new CursoProfissionalServiceImpl();
		cursoProfissionalService.setDAO(cursoProfissionalDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		CursoProfissional entidade = new CursoProfissional();
		entidade.setId(1l);
		entidade.setArea(new AreaProfissional());
		entidade.setInstituicao(new Instituicao());

		CursoProfissional existente = new CursoProfissional();
		existente.setId(2l);

		expect(cursoProfissionalDAO.getByCursoAreaInstituicao(null, null, null)).andReturn(existente);
		expect(cursoProfissionalDAO.salvar(entidade)).andReturn(null);
		replay(cursoProfissionalDAO);

		try {

			cursoProfissionalService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		CursoProfissional entidade = new CursoProfissional();
		entidade.setArea(new AreaProfissional());
		entidade.setInstituicao(new Instituicao());

		expect(cursoProfissionalDAO.getByCursoAreaInstituicao(null, null, null)).andReturn(null);
		expect(cursoProfissionalDAO.salvar(entidade)).andReturn(null);
		replay(cursoProfissionalDAO);

		cursoProfissionalService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		cursoProfissionalDAO.excluir(null);
		replay(cursoProfissionalDAO);
		cursoProfissionalService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(cursoProfissionalDAO.count(null)).andReturn(1);
		replay(cursoProfissionalDAO);

		Assert.assertEquals( cursoProfissionalService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.count
	 */
	@Test
	public void testCountArea() {
		expect(cursoProfissionalDAO.count(1l, null)).andReturn(1);
		replay(cursoProfissionalDAO);

		Assert.assertEquals( cursoProfissionalService.count(1l, null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(cursoProfissionalDAO.search(null, 0, 10)).andReturn(null);
		replay(cursoProfissionalDAO);

		Assert.assertNull( cursoProfissionalService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.search
	 */
	@Test
	public void testSearchArea() {
		expect(cursoProfissionalDAO.search(1l, null, 0, 10)).andReturn(null);
		replay(cursoProfissionalDAO);

		Assert.assertNull( cursoProfissionalService.search(1l, null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(cursoProfissionalDAO.getById(1l)).andReturn(null);
		replay(cursoProfissionalDAO);
		CursoProfissional entidade = cursoProfissionalService.getById(1l);
		Assert.assertNull(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.findByArea
	 */
	@Test
	public void testFindByArea() {
		expect(cursoProfissionalDAO.findByArea(1l)).andReturn(null);
		replay(cursoProfissionalDAO);
		Assert.assertNull( cursoProfissionalService.findByArea(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CursoProfissionalServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(cursoProfissionalDAO.findAll()).andReturn(null);
		replay(cursoProfissionalDAO);
		Assert.assertNull( cursoProfissionalService.findAll() );
	}

}
