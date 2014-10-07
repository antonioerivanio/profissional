package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.ParametroDAO;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.service.ParametroServiceImpl;

public class ParametroServiceTest {

	private ParametroServiceImpl parametroService;
	private ParametroDAO parametroDAO;

	@Before
	public void beforeTest(){
		parametroDAO = createMock(ParametroDAO.class);
		parametroService = new ParametroServiceImpl();
		parametroService.setDAO(parametroDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.sapjava.ParametroServiceImpl.findAll
	 */
	@Test
	public void testFindAll(){
		expect(parametroDAO.getByNome(null)).andReturn(null);
		replay(parametroDAO);
		Parametro parametro = parametroService.getByNome(null);
		Assert.assertNull(parametro);		
	}

}
