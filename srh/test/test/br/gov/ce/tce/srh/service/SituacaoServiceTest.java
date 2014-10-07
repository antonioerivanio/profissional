package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.SituacaoDAO;
import br.gov.ce.tce.srh.domain.Situacao;
import br.gov.ce.tce.srh.service.SituacaoServiceImpl;

public class SituacaoServiceTest {

	private SituacaoServiceImpl situacaoService;
	private SituacaoDAO situacaoDAO;

	@Before
	public void beforeTest(){
		situacaoDAO = createMock(SituacaoDAO.class);
		situacaoService = new SituacaoServiceImpl();
		situacaoService.setDAO(situacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.SituacaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll(){
		expect(situacaoDAO.findAll()).andReturn(null);
		replay(situacaoDAO);
		List<Situacao> lista = situacaoService.findAll();
		Assert.assertNull(lista);		
	}

}
