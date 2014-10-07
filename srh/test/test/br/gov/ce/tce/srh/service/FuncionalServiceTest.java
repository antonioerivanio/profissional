package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.FuncionalDAO;
import br.gov.ce.tce.srh.service.FuncionalServiceImpl;

public class FuncionalServiceTest {

	private FuncionalServiceImpl funcionalService;
	private FuncionalDAO funcionalDAO;

	@Before
	public void beforeTest() {
		funcionalDAO = createMock(FuncionalDAO.class);
		funcionalService = new FuncionalServiceImpl();
		funcionalService.setDAO(funcionalDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalServiceImpl.findByNome
	 */
	@Test
	public void testFindByNome() {
		expect(funcionalDAO.findByNome("teste")).andReturn(null);
		replay(funcionalDAO);
		List<String> lista = funcionalService.findByNome("teste");
		Assert.assertNull(lista);
	}

}
