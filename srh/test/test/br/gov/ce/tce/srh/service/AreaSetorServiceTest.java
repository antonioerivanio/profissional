package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.AreaSetorDAO;
import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaSetorServiceImpl;

public class AreaSetorServiceTest {

	private AreaSetorServiceImpl areaSetorService;
	private AreaSetorDAO areaSetorDAO;

	@Before
	public void beforeTest() {
		areaSetorDAO = createMock(AreaSetorDAO.class);
		areaSetorService = new AreaSetorServiceImpl();
		areaSetorService.setDAO(areaSetorDAO);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		AreaSetor entidade = new AreaSetor();

		// validando a setor NULO
		try {

			areaSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com setor nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setSetor( new Setor() );
		}



		// validando a descricao NULA
		try {

			areaSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao nula!!!");

		} catch (SRHRuntimeException e) {
			entidade.setDescricao("");
		}


		// validando a descricao VAZIA
		try {

			areaSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade com descricao vazia!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		AreaSetor entidade = new AreaSetor();
		entidade.setId(1l);
		entidade.setSetor(new Setor());
		entidade.getSetor().setId(1l);
		entidade.setDescricao("teste");

		AreaSetor existente = new AreaSetor();
		existente.setId(2l);
		entidade.setSetor(new Setor());
		entidade.getSetor().setId(1l);
		entidade.setDescricao("teste");

		expect(areaSetorDAO.getBySetorDescricao(1l, "teste")).andReturn(existente);
		expect(areaSetorDAO.salvar(entidade)).andReturn(null);
		replay(areaSetorDAO);

		try {

			areaSetorService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		AreaSetor entidade = new AreaSetor();
		entidade.setDescricao("teste");
		entidade.setSetor(new Setor());
		entidade.getSetor().setId(1l);

		expect(areaSetorDAO.getBySetorDescricao(1l, "teste")).andReturn(null);
		expect(areaSetorDAO.salvar(entidade)).andReturn(null);
		replay(areaSetorDAO);

		areaSetorService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		areaSetorDAO.excluir(null);
		replay(areaSetorDAO);
		areaSetorService.excluir(null);		
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(areaSetorDAO.count(null, null)).andReturn(1);
		replay(areaSetorDAO);

		Assert.assertEquals( areaSetorService.count(null, null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(areaSetorDAO.search(null, null, 0, 10)).andReturn(null);
		replay(areaSetorDAO);

		Assert.assertNull( areaSetorService.search(null, null, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.AreaSetorServiceImpl.findBySetor
	 */
	@Test
	public void testFindBySetor() {
		expect(areaSetorDAO.findBySetor(1l)).andReturn(null);
		replay(areaSetorDAO);
		List<AreaSetor> lista = areaSetorService.findBySetor(1l);
		Assert.assertNull(lista);
	}

}
