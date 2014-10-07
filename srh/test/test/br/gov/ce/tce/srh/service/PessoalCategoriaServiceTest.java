package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.PessoalCategoriaDAO;
import br.gov.ce.tce.srh.domain.PessoalCategoria;
import br.gov.ce.tce.srh.service.PessoalCategoriaServiceImpl;

public class PessoalCategoriaServiceTest {

	private PessoalCategoriaServiceImpl pessoalCategoriaService;
	private PessoalCategoriaDAO pessoalCategoriaDAO;

	@Before
	public void beforeTest() {
		pessoalCategoriaDAO = createMock(PessoalCategoriaDAO.class);
		pessoalCategoriaService = new PessoalCategoriaServiceImpl();
		pessoalCategoriaService.setDAO(pessoalCategoriaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.PessoalCategoriaServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(pessoalCategoriaDAO.findAll()).andReturn(null);
		replay(pessoalCategoriaDAO);
		List<PessoalCategoria> lista = pessoalCategoriaService.findAll();
		Assert.assertNull(lista);		
	}

}
