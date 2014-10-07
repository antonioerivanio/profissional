package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.VinculoDAO;
import br.gov.ce.tce.srh.domain.Vinculo;
import br.gov.ce.tce.srh.service.VinculoServiceImpl;

public class VinculoServiceTest {

	private VinculoServiceImpl vinculoService;
	private VinculoDAO vinculoDAO;

	@Before
	public void beforeTest(){
		vinculoDAO = createMock(VinculoDAO.class);
		vinculoService = new VinculoServiceImpl();
		vinculoService.setDao(vinculoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.VinculoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(vinculoDAO.findAll()).andReturn(null);
		replay(vinculoDAO);
		List<Vinculo> lista = vinculoService.findAll();
		Assert.assertNull(lista);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.VinculoServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(vinculoDAO.getById(1l)).andReturn(null);
		replay(vinculoDAO);
		Vinculo vinculo = vinculoService.getById(1l);
		Assert.assertNull(vinculo);
	}

}
