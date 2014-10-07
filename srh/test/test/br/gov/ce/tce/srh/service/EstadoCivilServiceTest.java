package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.EstadoCivilDAO;
import br.gov.ce.tce.srh.domain.EstadoCivil;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EstadoCivilServiceImpl;

public class EstadoCivilServiceTest {

	private EstadoCivilServiceImpl estadoCivilService;
	private EstadoCivilDAO estadoCivilDAO;

	@Before
	public void beforeTest() {
		estadoCivilDAO = createMock(EstadoCivilDAO.class);
		estadoCivilService = new EstadoCivilServiceImpl();
		estadoCivilService.setDAO(estadoCivilDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CompetenciaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		EstadoCivil entidade = new EstadoCivil();
		entidade.setId(1l);

		EstadoCivil existente = new EstadoCivil();
		existente.setId(2l);

		expect(estadoCivilDAO.getByDescricao(null)).andReturn(existente);
		expect(estadoCivilDAO.salvar(entidade)).andReturn(null);
		replay(estadoCivilDAO);

		try {

			estadoCivilService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EstadoCivilServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		EstadoCivil entidade = new EstadoCivil();

		expect(estadoCivilDAO.getByDescricao(null)).andReturn(null);
		expect(estadoCivilDAO.salvar(entidade)).andReturn(null);
		replay(estadoCivilDAO);

		estadoCivilService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EstadoCivilServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		estadoCivilDAO.excluir(null);
		replay(estadoCivilDAO);
		estadoCivilService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EstadoCivilServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(estadoCivilDAO.count(null)).andReturn(1);
		replay(estadoCivilDAO);

		Assert.assertEquals( estadoCivilService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EstadoCivilServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(estadoCivilDAO.search(null, 0, 10)).andReturn(null);
		replay(estadoCivilDAO);

		Assert.assertNull( estadoCivilService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EstadoCivilServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(estadoCivilDAO.findAll()).andReturn(null);
		replay(estadoCivilDAO);
		List<EstadoCivil> lista = estadoCivilService.findAll();
		Assert.assertNull(lista);		
	}

}
