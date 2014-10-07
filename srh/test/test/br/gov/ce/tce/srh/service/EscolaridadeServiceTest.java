package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.EscolaridadeDAO;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.EscolaridadeServiceImpl;

public class EscolaridadeServiceTest {

	private EscolaridadeServiceImpl escolaridadeService;
	private EscolaridadeDAO escolaridadeDAO;

	@Before
	public void beforeTest() {
		escolaridadeDAO = createMock(EscolaridadeDAO.class);
		escolaridadeService = new EscolaridadeServiceImpl();
		escolaridadeService.setDAO(escolaridadeDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDescricao() throws SRHRuntimeException {

		Escolaridade entidade = new Escolaridade();
		entidade.setId(1l);

		Escolaridade existente = new Escolaridade();
		existente.setId(2l);

		expect(escolaridadeDAO.getByDescricao(null)).andReturn(existente);
		expect(escolaridadeDAO.salvar(entidade)).andReturn(null);
		replay(escolaridadeDAO);

		try {

			escolaridadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteOrdem() throws SRHRuntimeException {

		Escolaridade entidade = new Escolaridade();
		entidade.setId(1l);

		Escolaridade existente = new Escolaridade();
		existente.setId(2l);

		expect(escolaridadeDAO.getByDescricao(null)).andReturn(null);
		expect(escolaridadeDAO.getByOrdem(null)).andReturn(existente);
		expect(escolaridadeDAO.salvar(entidade)).andReturn(null);
		replay(escolaridadeDAO);

		try {

			escolaridadeService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {
		Escolaridade entidade = new Escolaridade();
		expect(escolaridadeDAO.getByDescricao(null)).andReturn(null);
		expect(escolaridadeDAO.getByOrdem(null)).andReturn(null);
		expect(escolaridadeDAO.salvar(entidade)).andReturn(null);
		replay(escolaridadeDAO);		
		escolaridadeService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		escolaridadeDAO.excluir(null);
		replay(escolaridadeDAO);
		escolaridadeService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(escolaridadeDAO.count(null)).andReturn(1);
		replay(escolaridadeDAO);

		Assert.assertEquals( escolaridadeService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(escolaridadeDAO.search(null, 0, 10)).andReturn(null);
		replay(escolaridadeDAO);

		Assert.assertNull( escolaridadeService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.EscolaridadeServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(escolaridadeDAO.findAll()).andReturn(null);
		replay(escolaridadeDAO);
		List<Escolaridade> lista = escolaridadeService.findAll();
		Assert.assertNull(lista);		
	}

}
