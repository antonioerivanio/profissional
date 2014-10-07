package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.InstituicaoDAO;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.InstituicaoServiceImpl;

public class InstituicaoServiceTest {

	private InstituicaoServiceImpl instituicaoService;
	private InstituicaoDAO instituicaoDAO;

	@Before
	public void beforeTest() {
		instituicaoDAO = createMock(InstituicaoDAO.class);
		instituicaoService = new InstituicaoServiceImpl();
		instituicaoService.setDAO(instituicaoDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.InstituicaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Instituicao entidade = new Instituicao();
		entidade.setId(1l);

		Instituicao existente = new Instituicao();
		existente.setId(2l);

		expect(instituicaoDAO.getByDescricao(null)).andReturn(existente);
		expect(instituicaoDAO.salvar(entidade)).andReturn(null);
		replay(instituicaoDAO);

		try {

			instituicaoService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.InstituicaoServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Instituicao entidade = new Instituicao();

		expect(instituicaoDAO.getByDescricao(null)).andReturn(null);
		expect(instituicaoDAO.salvar(entidade)).andReturn(null);
		replay(instituicaoDAO);

		instituicaoService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.InstituicaoServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		instituicaoDAO.excluir(null);
		replay(instituicaoDAO);
		instituicaoService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.InstituicaoServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(instituicaoDAO.count(null)).andReturn(1);
		replay(instituicaoDAO);

		Assert.assertEquals( instituicaoService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.InstituicaoServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(instituicaoDAO.search(null, 0, 10)).andReturn(null);
		replay(instituicaoDAO);

		Assert.assertNull( instituicaoService.search(null, 0, 10) );
	}

	
	/**
	 * Test of br.gov.ce.tce.srh.service.InstituicaoServiceImpl.findAll
	 */
	@Test
	public void testFindAll() {
		expect(instituicaoDAO.findAll()).andReturn(null);
		replay(instituicaoDAO);
		List<Instituicao> lista = instituicaoService.findAll();
		Assert.assertNull(lista);
	}

}
