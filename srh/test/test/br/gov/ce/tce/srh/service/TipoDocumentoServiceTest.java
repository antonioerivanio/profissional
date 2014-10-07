package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoDocumentoDAO;
import br.gov.ce.tce.srh.domain.TipoDocumento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl;

public class TipoDocumentoServiceTest {

	private TipoDocumentoServiceImpl tipoDocumentoService;
	private TipoDocumentoDAO tipoDocumentoDAO;

	@Before
	public void beforeTest() {
		tipoDocumentoDAO = createMock(TipoDocumentoDAO.class);
		tipoDocumentoService = new TipoDocumentoServiceImpl();
		tipoDocumentoService.setDAO(tipoDocumentoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		TipoDocumento entidade = new TipoDocumento();
		entidade.setId(1l);

		TipoDocumento existente = new TipoDocumento();
		existente.setId(2l);

		expect(tipoDocumentoDAO.getByDescricao(null)).andReturn(existente);
		expect(tipoDocumentoDAO.salvar(entidade)).andReturn(null);
		replay(tipoDocumentoDAO);

		try {

			tipoDocumentoService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		TipoDocumento entidade = new TipoDocumento();

		expect(tipoDocumentoDAO.getByDescricao(null)).andReturn(null);
		expect(tipoDocumentoDAO.salvar(entidade)).andReturn(null);
		replay(tipoDocumentoDAO);

		tipoDocumentoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		tipoDocumentoDAO.excluir(null);
		replay(tipoDocumentoDAO);
		tipoDocumentoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(tipoDocumentoDAO.count(null)).andReturn(1);
		replay(tipoDocumentoDAO);

		Assert.assertEquals( tipoDocumentoService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(tipoDocumentoDAO.search(null, 0, 10)).andReturn(null);
		replay(tipoDocumentoDAO);

		Assert.assertNull( tipoDocumentoService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.findByEsfera
	 */
	@Test
	public void testFindByEsfera() {
		expect(tipoDocumentoDAO.findByEsfera(1l)).andReturn(null);
		replay(tipoDocumentoDAO);
		Assert.assertNull( tipoDocumentoService.findByEsfera(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.findByDocfuncional
	 */
	@Test
	public void testFindByDocfuncional() {
		expect(tipoDocumentoDAO.findByDocFuncional(true)).andReturn(null);
		replay(tipoDocumentoDAO);
		Assert.assertNull( tipoDocumentoService.findByDocfuncional(true) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoDocumentoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(tipoDocumentoDAO.findAll()).andReturn(null);
		replay(tipoDocumentoDAO);
		List<TipoDocumento> lista = tipoDocumentoService.findAll();
		Assert.assertNull(lista);		
	}

}
