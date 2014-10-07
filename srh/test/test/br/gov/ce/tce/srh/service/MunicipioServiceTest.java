package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.MunicipioDAO;
import br.gov.ce.tce.srh.service.MunicipioServiceImpl;

public class MunicipioServiceTest {

	private MunicipioServiceImpl municipioService;
	private MunicipioDAO municipioDAO;

	@Before
	public void beforeTest(){
		municipioDAO = createMock(MunicipioDAO.class);
		municipioService = new MunicipioServiceImpl();
		municipioService.setDAO(municipioDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.MunicipioServiceImpl.findByUF
	 */
	@Test
	public void testFindByUF(){
		expect(municipioDAO.findByUF("CE")).andReturn(null);
		replay(municipioDAO);
		Assert.assertNull(municipioService.findByUF("CE"));		
	}

}
