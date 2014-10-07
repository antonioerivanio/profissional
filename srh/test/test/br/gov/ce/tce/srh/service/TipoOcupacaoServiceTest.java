package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.TipoOcupacaoDAO;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.service.TipoOcupacaoServiceImpl;

public class TipoOcupacaoServiceTest {

	private TipoOcupacaoServiceImpl tipoOcupacaoService;
	private TipoOcupacaoDAO tipoOcupacaoDAO;

	@Before
	public void beforeTest(){
		tipoOcupacaoDAO = createMock(TipoOcupacaoDAO.class);
		tipoOcupacaoService = new TipoOcupacaoServiceImpl();
		tipoOcupacaoService.setDAO(tipoOcupacaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoOcupacaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(tipoOcupacaoDAO.findAll()).andReturn(null);
		replay(tipoOcupacaoDAO);
		List<TipoOcupacao> lista = tipoOcupacaoService.findAll();
		Assert.assertNull(lista);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.TipoOcupacaoServiceImpl.getById
	 */
	@Test
	public void testGetById() {
		expect(tipoOcupacaoDAO.getById(1l)).andReturn(null);
		replay(tipoOcupacaoDAO);
		TipoOcupacao tipo = tipoOcupacaoService.getById(1l);
		Assert.assertNull(tipo);
	}

}
