package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoFeriasDAO;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.service.TipoFeriasServiceImpl;

public class TipoFeriasServiceTest {

	private TipoFeriasServiceImpl tipoFeriasService;
	private TipoFeriasDAO tipoFeriasDAO;

	@Before
	public void beforeTest(){
		tipoFeriasDAO = createMock(TipoFeriasDAO.class);
		tipoFeriasService = new TipoFeriasServiceImpl();
		tipoFeriasService.setDAO(tipoFeriasDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoFeriasServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(tipoFeriasDAO.findAll()).andReturn(null);
		replay(tipoFeriasDAO);
		List<TipoFerias> lista = tipoFeriasService.findAll();
		Assert.assertNull(lista);		
	}

}
