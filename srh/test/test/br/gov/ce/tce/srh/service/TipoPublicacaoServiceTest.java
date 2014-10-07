
package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoPublicacaoDAO;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.service.TipoPublicacaoServiceImpl;

public class TipoPublicacaoServiceTest {

	private TipoPublicacaoServiceImpl tipoPublicacaoService;
	private TipoPublicacaoDAO tipoPublicacaoDAO;

	@Before
	public void beforeTest(){
		tipoPublicacaoDAO = createMock(TipoPublicacaoDAO.class);
		tipoPublicacaoService = new TipoPublicacaoServiceImpl();
		tipoPublicacaoService.setDAO(tipoPublicacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoPublicacaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll(){
		expect(tipoPublicacaoDAO.findAll()).andReturn(null);
		replay(tipoPublicacaoDAO);
		List<TipoPublicacao> lista = tipoPublicacaoService.findAll();
		Assert.assertNull(lista);		
	}

}
