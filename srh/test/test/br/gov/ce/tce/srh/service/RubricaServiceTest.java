package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.RubricaDAO;
import br.gov.ce.tce.srh.domain.Rubrica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RubricaServiceImpl;

public class RubricaServiceTest {

	private RubricaServiceImpl rubricaService;
	private RubricaDAO rubricaDAO;

	@Before
	public void beforeTest() {
		rubricaDAO = createMock(RubricaDAO.class);
		rubricaService = new RubricaServiceImpl();
		rubricaService.setDAO(rubricaDAO);
	}



	/**
	 * Test of br.gov.ce.tce.srh.service.RubricaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteCodigo() throws SRHRuntimeException {

		Rubrica entidade = new Rubrica();
		entidade.setId(1l);

		Rubrica existente = new Rubrica();
		existente.setId(2l);

		expect(rubricaDAO.getByCodigo(null)).andReturn(existente);
		expect(rubricaDAO.salvar(entidade)).andReturn(null);
		replay(rubricaDAO);

		try {

			rubricaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}





	/**
	 * Test of br.gov.ce.tce.srh.service.RubricaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistenteDescricao() throws SRHRuntimeException {

		Rubrica entidade = new Rubrica();
		entidade.setId(1l);

		Rubrica existente = new Rubrica();
		existente.setId(2l);

		expect(rubricaDAO.getByCodigo(null)).andReturn(null);
		expect(rubricaDAO.getByDescricao(null)).andReturn(existente);
		expect(rubricaDAO.salvar(entidade)).andReturn(null);
		replay(rubricaDAO);

		try {

			rubricaService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RubricaServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Rubrica entidade = new Rubrica();

		expect(rubricaDAO.getByCodigo(null)).andReturn(null);
		expect(rubricaDAO.getByDescricao(null)).andReturn(null);
		expect(rubricaDAO.salvar(entidade)).andReturn(null);
		replay(rubricaDAO);

		rubricaService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RubricaServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		rubricaDAO.excluir(null);
		replay(rubricaDAO);
		rubricaService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RubricaServiceImpl.count
	 */
	@Test
	public void testCount() {
		expect(rubricaDAO.count(null)).andReturn(1);
		replay(rubricaDAO);

		Assert.assertEquals( rubricaService.count(null), 1);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.RubricaServiceImpl.search
	 */
	@Test
	public void testSearch() {
		expect(rubricaDAO.search(null, 0, 10)).andReturn(null);
		replay(rubricaDAO);

		Assert.assertNull( rubricaService.search(null, 0, 10) );
	}

}
