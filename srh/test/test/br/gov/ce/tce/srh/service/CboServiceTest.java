package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.CboDAO;
import br.gov.ce.tce.srh.domain.Cbo;
import br.gov.ce.tce.srh.service.CboServiceImpl;

public class CboServiceTest {

	private CboServiceImpl cboService;
	private CboDAO cboDAO;

	@Before
	public void beforeTest() {
		cboDAO = createMock(CboDAO.class);
		cboService = new CboServiceImpl();
		cboService.setDAO(cboDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CboServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(cboDAO.getById(1l)).andReturn(null);
		replay(cboDAO);
		Cbo cbo = cboService.getById(1l);
		Assert.assertNull(cbo);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CboServiceImpl.getByCodigo
	 */
	@Test
	public void testGetByCodigo() {
		expect(cboDAO.getByCodigo(null)).andReturn(null);
		replay(cboDAO);
		Assert.assertNull( cboService.getByCodigo(null) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CboServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(cboDAO.findAll()).andReturn(null);
		replay(cboDAO);
		Assert.assertNull( cboService.findAll() );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CboServiceImpl.findByNivel
	 */
	@Test
	public void testFindByNivel() {
		expect(cboDAO.findByNivel(1l)).andReturn(null);
		replay(cboDAO);
		Assert.assertNull( cboService.findByNivel(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.CboServiceImpl.findByNivelCodigo
	 */
	@Test
	public void testFindByNivelCodigo() {
		expect(cboDAO.findByNivelCodigo(1l, null)).andReturn(null);
		replay(cboDAO);
		Assert.assertNull( cboService.findByNivelCodigo(1l, null) );
	}

}
