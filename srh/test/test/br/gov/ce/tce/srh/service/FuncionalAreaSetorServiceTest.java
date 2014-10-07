package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAO;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetorPk;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl;

public class FuncionalAreaSetorServiceTest {

	private FuncionalAreaSetorServiceImpl funcionalAreaSetorService;
	private FuncionalAreaSetorDAO funcionalAreaSetorDAO;

	@Before
	public void beforeTest() {
		funcionalAreaSetorDAO = createMock(FuncionalAreaSetorDAO.class);
		funcionalAreaSetorService = new FuncionalAreaSetorServiceImpl();
		funcionalAreaSetorService.setDAO(funcionalAreaSetorDAO);

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvarValidarDados() throws SRHRuntimeException {

		FuncionalAreaSetor entidade = new FuncionalAreaSetor();
		entidade.setPk( new FuncionalAreaSetorPk() );

		// validando o funcional NULO
		try {

			funcionalAreaSetorService.salvar( entidade );
			Assert.fail("Nao validou a funcional NULA!!!");

		} catch (SRHRuntimeException e) {}


		// validando a funcional VAZIO
		try {

			entidade.getPk().setFuncional(0l);
			funcionalAreaSetorService.salvar( entidade );
			Assert.fail("Nao validou o funcional VAZIA!!!");

		} catch (SRHRuntimeException e) {
			entidade.getPk().setFuncional(1l);
		}


		// validando a area setor NULA
		try {

			funcionalAreaSetorService.salvar( entidade );
			Assert.fail("Nao validou a area setor NULO!!!");

		} catch (SRHRuntimeException e) {}


		// validando a area setor ZERO
		try {

			entidade.getPk().setAreaSetor(0l);
			funcionalAreaSetorService.salvar( entidade );
			Assert.fail("Nao validou a area setor ZERO!!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException  
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		FuncionalAreaSetor entidade = new FuncionalAreaSetor();
		entidade.setPk( new FuncionalAreaSetorPk() );

		entidade.getPk().setAreaSetor(1l);
		entidade.getPk().setFuncional(1l);

		expect(funcionalAreaSetorDAO.salvar(entidade)).andReturn(null);
		replay(funcionalAreaSetorDAO);
		funcionalAreaSetorService.salvar( entidade );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {

		FuncionalAreaSetor entidade = new FuncionalAreaSetor();
		entidade.setPk( new FuncionalAreaSetorPk() );

		entidade.getPk().setAreaSetor(1l);
		entidade.getPk().setFuncional(1l);

		funcionalAreaSetorDAO.excluir(entidade);
		replay(funcionalAreaSetorDAO);
		funcionalAreaSetorService.excluir( entidade );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(funcionalAreaSetorDAO.count(1l)).andReturn(1);
		replay(funcionalAreaSetorDAO);

		Assert.assertEquals( funcionalAreaSetorService.count(1l), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(funcionalAreaSetorDAO.search(1l, 0, 10)).andReturn(null);
		replay(funcionalAreaSetorDAO);

		Assert.assertNull( funcionalAreaSetorService.search(1l, 0, 10) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.findyByAreaSetor
	 */
	@Test
	public void testFindyByAreaSetor() {
		expect(funcionalAreaSetorDAO.findyByAreaSetor(1l)).andReturn(null);
		replay(funcionalAreaSetorDAO);
		Assert.assertNull( funcionalAreaSetorService.findyByAreaSetor(1l) );
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.FuncionalAreaSetorServiceImpl.findByFuncional
	 */
	@Test
	public void testFindByPessoa() {
		expect(funcionalAreaSetorDAO.findByFuncional(1l)).andReturn(null);
		replay(funcionalAreaSetorDAO);
		Assert.assertNull( funcionalAreaSetorService.findByFuncional(1l) );
	}

}
