package test.br.gov.ce.tce.srh.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.tce.srh.dao.SimboloDAO;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.SimboloServiceImpl;


public class SimboloServiceTest {

	private SimboloServiceImpl simboloService;
	private SimboloDAO simboloDAO;

	@Before
	public void beforeTest() {
		simboloDAO = createMock(SimboloDAO.class);
		simboloService = new SimboloServiceImpl();
		simboloService.setDAO(simboloDAO);
	}



	/**
	 * Test of br.gov.ce.tce.srh.service.SimboloServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvarExistente() throws SRHRuntimeException {

		Simbolo entidade = new Simbolo();
		entidade.setId(1l);
		entidade.setOcupacao( new Ocupacao() );
		entidade.getOcupacao().setId(1l);
		entidade.setSimbolo("RE");

		Simbolo existente = new Simbolo();
		existente.setId(2l);

		expect(simboloDAO.getByOcupacaoSimbolo(1l, "RE")).andReturn(existente);
		expect(simboloDAO.salvar(entidade)).andReturn(null);
		replay(simboloDAO);

		try {

			simboloService.salvar(entidade);
			Assert.fail("Nao validou a entidade existente!!");

		} catch (SRHRuntimeException e) {}

	}



	/**
	 * Test of br.gov.ce.tce.srh.service.SimboloServiceImpl.salvar
	 * 
	 * @throws SRHRuntimeException 
	 */
	@Test
	public void testSalvar() throws SRHRuntimeException {

		Simbolo entidade = new Simbolo();
		entidade.setOcupacao( new Ocupacao() );
		entidade.getOcupacao().setId( 1l );
		entidade.setSimbolo("RE");

		expect(simboloDAO.getByOcupacaoSimbolo(1l, "RE")).andReturn(null);
		expect(simboloDAO.salvar(entidade)).andReturn(null);
		replay(simboloDAO);

		simboloService.salvar(entidade);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.SimboloServiceImpl.excluir
	 */
	@Test
	public void testExcluir() {
		simboloDAO.excluir(null);
		replay(simboloDAO);
		simboloService.excluir(null);
	}


	/**
	 * Test of br.gov.ce.tce.srh.service.SimboloServiceImpl.findByOcupacao
	 */
	@Test
	public void testFindByOcupacao() {
		expect(simboloDAO.findByOcupacao(1l)).andReturn(null);
		replay(simboloDAO);
		List<Simbolo> lista = simboloService.findByOcupacao(1l);
		Assert.assertNull(lista);
	}

}
