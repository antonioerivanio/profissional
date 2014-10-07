package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.UfDAO;
import br.gov.ce.tce.srh.domain.Uf;
import br.gov.ce.tce.srh.service.UfServiceImpl;

public class UfServiceTest {

	private UfServiceImpl ufService;
	private UfDAO ufDAO;

	@Before
	public void beforeTest() {
		ufDAO = createMock(UfDAO.class);
		ufService = new UfServiceImpl();
		ufService.setDAO(ufDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.UfServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(ufDAO.findAll()).andReturn(null);
		replay(ufDAO);
		List<Uf> lista = ufService.findAll();
		Assert.assertNull(lista);
	}

}
