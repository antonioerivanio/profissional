package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoMovimentoDAO;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl;

public class TipoMovimentoServiceTest {

	private TipoMovimentoServiceImpl tipoMovimentoService;
	private TipoMovimentoDAO tipoMovimentoDAO;

	@Before
	public void beforeTest(){
		tipoMovimentoDAO = createMock(TipoMovimentoDAO.class);
		tipoMovimentoService = new TipoMovimentoServiceImpl();
		tipoMovimentoService.setDAO(tipoMovimentoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		TipoMovimento entidade = new TipoMovimento();
		entidade.setId(1l);

		TipoMovimento existente = new TipoMovimento();
		existente.setId(2l);

		expect(tipoMovimentoDAO.getByDescricao(null)).andReturn(existente);
		expect(tipoMovimentoDAO.salvar(entidade)).andReturn(null);
		replay(tipoMovimentoDAO);

		try {

			tipoMovimentoService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		TipoMovimento entidade = new TipoMovimento();
		expect(tipoMovimentoDAO.getByDescricao(null)).andReturn(null);
		expect(tipoMovimentoDAO.salvar(entidade)).andReturn(null);
		replay(tipoMovimentoDAO);		
		tipoMovimentoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		tipoMovimentoDAO.excluir(null);
		replay(tipoMovimentoDAO);
		tipoMovimentoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(tipoMovimentoDAO.count(null)).andReturn(1);
		replay(tipoMovimentoDAO);

		Assert.assertEquals( tipoMovimentoService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(tipoMovimentoDAO.search(null, 0, 10)).andReturn(null);
		replay(tipoMovimentoDAO);

		Assert.assertNull( tipoMovimentoService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(tipoMovimentoDAO.getById(1l)).andReturn(null);
		replay(tipoMovimentoDAO);
		TipoMovimento entidade = tipoMovimentoService.getById(1l);
		Assert.assertNull(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.findByTipo
	 */
	@Test
	public void testFindByTipo() {
		expect(tipoMovimentoDAO.findByTipo(1l)).andReturn(null);
		replay(tipoMovimentoDAO);
		List<TipoMovimento> lista = tipoMovimentoService.findByTipo(1l);
		Assert.assertNull(lista);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoMovimentoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(tipoMovimentoDAO.findAll()).andReturn(null);
		replay(tipoMovimentoDAO);
		List<TipoMovimento> lista = tipoMovimentoService.findAll();
		Assert.assertNull(lista);		
	}

}
