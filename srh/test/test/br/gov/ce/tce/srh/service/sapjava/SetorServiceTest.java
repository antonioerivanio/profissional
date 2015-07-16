package test.br.gov.ce.tce.srh.service.sapjava;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.sapjava.dao.SetorDAO;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorServiceImpl;

public class SetorServiceTest {

	private SetorServiceImpl setorService;
	private SetorDAO setorDAO;

	@Before
	public void beforeTest(){
		setorDAO = createMock(SetorDAO.class);
		setorService = new SetorServiceImpl();
		setorService.setSetorDAO(setorDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.sapjava.service.SetorServiceImpl.findAll
	 */
	@Test
	public void testFindAll(){
		expect(setorDAO.findAll()).andReturn(null);
		replay(setorDAO);
		List<Setor> lista = setorService.findAll();
		Assert.assertNull(lista);		
	}

}
