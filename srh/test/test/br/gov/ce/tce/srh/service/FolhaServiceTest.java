package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.FolhaDAO;
import br.gov.ce.tce.srh.domain.Folha;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FolhaServiceImpl;

public class FolhaServiceTest {

	private FolhaServiceImpl folhaService;
	private FolhaDAO folhaDAO;

	@Before
	public void beforeTest() {
		folhaDAO = createMock(FolhaDAO.class);
		folhaService = new FolhaServiceImpl();
		folhaService.setDAO(folhaDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteCodigo() throws SRHRuntimeException {

		Folha entidade = new Folha();
		entidade.setId(1l);

		Folha existente = new Folha();
		existente.setId(2l);

		expect(folhaDAO.getByCodigo(null)).andReturn(existente);
		expect(folhaDAO.salvar(entidade)).andReturn(null);
		replay(folhaDAO);

		try {

			folhaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDescricao() throws SRHRuntimeException {

		Folha entidade = new Folha();
		entidade.setId(1l);

		Folha existente = new Folha();
		existente.setId(2l);

		expect(folhaDAO.getByCodigo(null)).andReturn(null);
		expect(folhaDAO.getByDescricao(null)).andReturn(existente);
		expect(folhaDAO.salvar(entidade)).andReturn(null);
		replay(folhaDAO);

		try {

			folhaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Folha entidade = new Folha();

		expect(folhaDAO.getByCodigo(null)).andReturn(null);
		expect(folhaDAO.getByDescricao(null)).andReturn(null);
		expect(folhaDAO.salvar(entidade)).andReturn(null);
		replay(folhaDAO);

		folhaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		folhaDAO.excluir(null);
		replay(folhaDAO);
		folhaService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(folhaDAO.count(null)).andReturn(1);
		replay(folhaDAO);

		Assert.assertEquals( folhaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(folhaDAO.search(null, 0, 10)).andReturn(null);
		replay(folhaDAO);

		Assert.assertNull( folhaService.search(null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FolhaServiceImpl.findByAtivo
	 */
	@Test
	public void testFindByAtivo() {
		expect(folhaDAO.findByAtivo(true)).andReturn(null);
		replay(folhaDAO);
		List<Folha> lista = folhaService.findByAtivo(true);
		Assert.assertNull(lista);
	}

}
